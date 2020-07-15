package com.wuxp.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wuxp.payment.AbstractPlatformPaymentService;
import com.wuxp.payment.AliPayPaymentConfig;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.enums.TradeStatus;
import com.wuxp.payment.model.PaymentBaseOrder;
import com.wuxp.payment.model.RefundBaseOrder;
import com.wuxp.payment.req.*;
import com.wuxp.payment.resp.OrderRefundResponse;
import com.wuxp.payment.resp.QueryOrderResponse;
import com.wuxp.payment.resp.QueryRefundOrderResponse;
import com.wuxp.payment.utils.PaymentUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付宝支付的抽象实现
 *
 * @author wxup
 */
@Slf4j
public abstract class AbstractAliPayPaymentService extends AbstractPlatformPaymentService<AliPayPaymentConfig> {
    /**
     * 支付结果处理成功返回码
     */
    private final static String PAYMENT_RESULT_HANDLE_SUCCESS_RETURN_CODE = "success";

    /**
     * 支付结果处理失败返回码
     */
    private final static String PAYMENT_RESULT_HANDLE_FAILURE_RETURN_CODE = "fail";


    protected final static String ALI_PAY_DEV = "alipaydev";


    protected AlipayClient alipayClient;


    public AbstractAliPayPaymentService(PaymentMethod paymentMethod, AliPayPaymentConfig paymentConfig) {
        super(PaymentPlatform.ALI_PAY,paymentMethod, paymentConfig);
        this.alipayClient = this.getAliPayClient();
    }


    /**
     * 支付宝交易状态
     * WAIT_BUYER_PAY（交易创建，等待买家付款）、
     * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
     * TRADE_SUCCESS（交易支付成功）
     * TRADE_FINISHED（交易结束，不可退款）
     */
    @AllArgsConstructor
    @Getter
    protected enum AliPayTradeStatus {

        WAIT_BUYER_PAY("交易创建，等待买家付款。"),

        TRADE_FINISHED("交易成功且结束"),

        TRADE_SUCCESS("交易成功"),

        TRADE_PENDING("等待卖家收款"),

        TRADE_CLOSED("交易关闭");

        //说明
        private String memo;


    }

    /**
     * 加密类型
     */
    protected enum EncryptType {

        RSA,

        RSA2;
    }


    /**
     * 支付宝支付异步通知处理  {https://docs.open.alipay.com/194/103296}
     *
     * @param request
     * @param paymentBaseOrder 订单信息
     * @return
     */
    @Override
    public String paymentProcess(PaymentNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder) {

        if (log.isDebugEnabled()) {
            log.debug("支付异步通知：request = {}  ", paymentBaseOrder);
            log.debug("支付异步通知：order = {}  ", paymentBaseOrder);
        }

        // 验证签名
        if (!this.verifyPaymentNotifyRequest(request)) {
            if (log.isDebugEnabled()) {
                log.debug("支付宝支付通知：【{}】签名验证失败", request);
            }
            return this.getNotifyReturnCode(false);
        }

        QueryOrderResponse response = new QueryOrderResponse();
        response.setPaymentPlatform(PaymentPlatform.ALI_PAY);
        response.setPaymentMethod(paymentBaseOrder.getPaymentMethod());
        Map<String, String> notifyParams = request.getNotifyParams();
        response.setOutTradeNo(notifyParams.get("trade_no"));
        response.setTradeNo(notifyParams.get("out_trade_no"));
        response.setOrderAmount(PaymentUtil.yuanToFen(notifyParams.get("total_amount")));
        String tradeStatus = notifyParams.get("trade_status");
        Integer buyerPayAmount;
        String payAmount = notifyParams.get("buyer_pay_amount");
        if (payAmount == null) {
            buyerPayAmount = request.getOrderAmount();
        } else {
            buyerPayAmount = new BigDecimal(payAmount).intValue();
        }
        TradeStatus status = this.transformTradeStatus(tradeStatus, buyerPayAmount);
        response.setTradeStatus(status)
                .setBuyerPayAmount(buyerPayAmount)
                .setUseSandboxEnv(this.isUseSandboxEnv());
        String buyerLogonId = notifyParams.get("buyer_logon_id");
        response.setPayerAccount(buyerLogonId);
        String receiptAmount = notifyParams.get("receipt_amount");
        if (receiptAmount != null) {
            // TODO实收金额验证
            response.setReceiptAmount((new BigDecimal(receiptAmount)).intValue());
        }

        // 支付处理订单通知
        boolean r = this.callbackTemplate.handlePaymentCallback(request.getNotifyMethod(), response, paymentBaseOrder);
        return this.getNotifyReturnCode(r);
    }

    /**
     * 支付宝退款异步通知
     *
     * @param request
     * @param refundBaseOrder 退款订单信息
     * @return
     */
    @Override
    public String refundProcess(RefundNotifyProcessRequest request, RefundBaseOrder refundBaseOrder) {
        // 验证签名
        if (!this.verifyRefundNotifyRequest(request)) {
            if (log.isDebugEnabled()) {
                log.debug("支付宝支付通知：【{}】签名验证失败", request);
            }
            return this.getNotifyReturnCode(false);
        }
        // 退款处理订单通知
        QueryRefundOrderResponse response = new QueryRefundOrderResponse();
        response.setPaymentPlatform(PaymentPlatform.ALI_PAY);
        response.setPaymentMethod(refundBaseOrder.getPaymentMethod());
        Map<String, String> notifyParams = request.getNotifyParams();
        response.setTradeRefundNo(request.getRefundTradeNo());
        response.setOutTradeRefundNo(notifyParams.get("out_biz_no").toString());
        response.setOrderAmount(PaymentUtil.yuanToFen(notifyParams.get("total_amount").toString()));
        response.setRefundAmount(PaymentUtil.yuanToFen(notifyParams.get("refund_fee").toString()));
        boolean r = this.callbackTemplate.handleRefundCallback(request.getNotifyMethod(), response, refundBaseOrder);
        return this.getNotifyReturnCode(r);
    }


    @Override
    public QueryOrderResponse queryOrder(QueryOrderRequest req) {


        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setTradeNo(req.getOutTradeNo());
        model.setOutTradeNo(req.getTradeNo());
        request.setBizModel(model);
        QueryOrderResponse queryOrderResponse = new QueryOrderResponse(req);
        AlipayClient alipayClient = this.alipayClient;
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (log.isDebugEnabled()) {
                log.debug("查询支付结果 :{}", response.getBody());
            }
            if (response.isSuccess()) {
                int buyerPayAmount = PaymentUtil.yuanToFen(response.getBuyerPayAmount());
                queryOrderResponse.setOutTradeNo(response.getTradeNo())
                        .setTradeNo(response.getOutTradeNo())
                        .setOrderAmount(PaymentUtil.yuanToFen(response.getTotalAmount()))
                        .setBuyerPayAmount(buyerPayAmount)
                        .setReceiptAmount(PaymentUtil.yuanToFen(response.getReceiptAmount()))
                        .setUseSandboxEnv(this.isUseSandboxEnv())
                        .setTradeStatus(this.transformTradeStatus(response.getTradeStatus(), buyerPayAmount))
                        .setSuccess(true);
            } else {
                queryOrderResponse.setSuccess(false)
                        .setCode(response.getCode())
                        .setMessage(response.getMsg());
            }
            queryOrderResponse.setRawResponse(response);
        } catch (AlipayApiException e) {
            log.error("支付宝退款请求失败", e);
            queryOrderResponse.setSuccess(false)
                    .setCode(e.getErrCode())
                    .setMessage(e.getErrMsg());
        }


        return queryOrderResponse;
    }

    @Override
    public OrderRefundResponse orderRefund(RefundRequest req) {

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(req.getTradeNo());
        model.setTradeNo(req.getOutTradeNo());
        model.setOutRequestNo(req.getRequestRefundNo());
        model.setRefundAmount(PaymentUtil.fen2Yun(req.getRefundAmount()).toString());
        model.setRefundReason(req.getRefundReason());

        if (log.isDebugEnabled()) {
            log.debug("退款请求参数 {}", model);
        }
        request.setBizModel(model);
        if (StringUtils.hasText(req.getRefundNotifyUrl())) {
            request.setNotifyUrl(req.getRefundNotifyUrl());
        } else {
            throw new RuntimeException("refund notify url is null");
        }
        if (log.isDebugEnabled()) {
            log.debug("支付宝退款回调URL->[{}]", request.getNotifyUrl());
        }
        AlipayClient alipayClient = this.alipayClient;
        OrderRefundResponse refundResponse = new OrderRefundResponse(req);
        try {

            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (log.isDebugEnabled()) {
                log.debug("支付宝退款响应, {}  ,{}", response.getCode(), response.getMsg());
            }
            if (response.isSuccess()) {
                refundResponse.setOrderAmount(PaymentUtil.yuanToFen(response.getRefundFee()))
                        .setOrderAmount(req.getOrderAmount())
                        .setTradeRefundNo(req.getRequestRefundNo())
                        .setTradeStatus(TradeStatus.WAIT_REFUND)
                        .setSuccess(true);
            } else {
                refundResponse.setSuccess(false)
                        .setCode(response.getCode())
                        .setMessage(response.getMsg());
            }
            refundResponse.setRawResponse(response);
        } catch (AlipayApiException e) {
            log.error("支付宝退款请求失败", e);
            refundResponse.setSuccess(false)
                    .setCode(e.getErrCode())
                    .setMessage(e.getErrMsg());
        }


        return refundResponse;
    }

    @Override
    public QueryRefundOrderResponse queryOrderRefund(QueryRefundRequest req) {

        QueryRefundOrderResponse refundOrderResponse = new QueryRefundOrderResponse();

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setTradeNo(req.getTradeNo());
        model.setOutTradeNo(req.getOutTradeNo());
        model.setOutRequestNo(req.getRequestRefundSn());
        AlipayClient alipayClient = this.alipayClient;
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                int refundAmount = PaymentUtil.yuanToFen(response.getRefundAmount());
                int orderAmount = PaymentUtil.yuanToFen(response.getTotalAmount());
                refundOrderResponse.setOutTradeRefundNo(response.getOutRequestNo())
                        .setTradeRefundNo(response.getOutTradeNo())
                        .setRefundAmount(refundAmount)
                        .setOrderAmount(orderAmount)
                        .setTradeStatus(refundAmount == orderAmount ? TradeStatus.REFUNDED : TradeStatus.PARTIAL_REFUND)
                        .setSuccess(true);
            } else {
                refundOrderResponse
                        .setCode(response.getErrorCode())
                        .setMessage(response.getMsg());
            }
            refundOrderResponse.setRawResponse(response);
        } catch (AlipayApiException e) {
            log.error("支付宝退款请求失败", e);
            refundOrderResponse.setSuccess(false)
                    .setCode(e.getErrCode())
                    .setMessage(e.getErrMsg());
        }

        return refundOrderResponse;
    }

    @Override
    public String getNotifyReturnCode(boolean success) {
        return success ? PAYMENT_RESULT_HANDLE_SUCCESS_RETURN_CODE : PAYMENT_RESULT_HANDLE_FAILURE_RETURN_CODE;
    }


    /**
     * 获取alipay客户端
     *
     * @return
     */
    protected AlipayClient getAliPayClient() {
        AliPayPaymentConfig paymentConfig = this.paymentConfig;
        return new DefaultAlipayClient(
                paymentConfig.getServiceUrl(),
                paymentConfig.getAppId(),
                paymentConfig.getAppPrivateKey(),
                "json",
                paymentConfig.getCharset(),
                paymentConfig.getAliPayPublicKey(),
                EncryptType.RSA2.name());
    }


    /**
     * @param status
     * @param buyerPaidAmount 买家实付金额
     * @return
     */
    protected TradeStatus transformTradeStatus(String status, int buyerPaidAmount) {
        AliPayTradeStatus aliPayTradeStatus = Enum.valueOf(AliPayTradeStatus.class, status);
        if (AliPayTradeStatus.WAIT_BUYER_PAY.equals(aliPayTradeStatus)) {
            return TradeStatus.WAIT_PAY;
        }

        if (AliPayTradeStatus.TRADE_SUCCESS.equals(aliPayTradeStatus)) {
            return TradeStatus.SUCCESS;
        }

        if (AliPayTradeStatus.TRADE_FINISHED.equals(aliPayTradeStatus)) {
            return TradeStatus.SUCCESS;
        }

        if (AliPayTradeStatus.TRADE_CLOSED.equals(aliPayTradeStatus)) {
            if (buyerPaidAmount == 0) {
                // 买家实付金额为0 说明未支付
                return TradeStatus.CLOSED;
            }
            return TradeStatus.UNKNOWN;
        }

        return TradeStatus.UNKNOWN;
    }

    /**
     * 是否使用沙箱环境
     *
     * @return
     */
    protected boolean isUseSandboxEnv() {
        return this.paymentConfig.getServiceUrl().contains(ALI_PAY_DEV);
    }

    /**
     * 验证支付宝支付通知请求
     *
     * @return
     */
    private boolean verifyPaymentNotifyRequest(PaymentNotifyProcessRequest request) {
        AliPayPaymentConfig paymentConfig = this.paymentConfig;

        //参数验证
        String tradeNo = request.getTradeNo();
        Map<String, String> params = request.getNotifyParams();
        BigDecimal amountBigDecimal = PaymentUtil.fen2Yun(request.getOrderAmount());

        String partner = paymentConfig.getPartner();
        if (partner == null) {
            log.error("验证支付宝异步通知失败，商户号不能为null");
            return false;
        }
        boolean paramVerify = partner.equals(params.get("seller_id"))
                && tradeNo.equals(params.get("out_trade_no"))
                && amountBigDecimal.toString().equals(params.get("total_amount"));
        if (!paramVerify) {
            if (log.isDebugEnabled()) {
                log.debug("支付宝支付通知，【{}】参数验证失败，{}", tradeNo, params);
            }
            return false;
        }
        return verifySign(params);

    }

    /**
     * 验证支付宝退款通知请求
     *
     * @return
     */
    private boolean verifyRefundNotifyRequest(RefundNotifyProcessRequest request) {
        AliPayPaymentConfig paymentConfig = this.paymentConfig;
        //参数验证
        Map<String, String> params = request.getNotifyParams();
        String refundSn = request.getRefundTradeNo();
        BigDecimal refundAmount = PaymentUtil.fen2Yun(request.getRefundAmount());

        boolean paramVerify = refundSn != null && paymentConfig.getPartner().equals(params.get("seller_id"))
                && refundSn.equals(params.get("out_trade_no"))
                && refundAmount.toString().equals(params.get("refund_fee"));
        if (!paramVerify) {
            if (log.isDebugEnabled()) {
                log.debug("支付宝退款通知，【{}】参数验证失败，{}", refundSn, params);
            }
            return false;
        }
        return verifySign(params);

    }

    /**
     * 验证签名
     *
     * @param params
     * @return
     */
    private boolean verifySign(Map<String, String> params) {

        //签名验证
        Map<String, String> stringStringHashMap = new HashMap<String, String>();

        for (String key : params.keySet()) {
            Object o = params.get(key);
            if (o != null) {
                stringStringHashMap.put(key, o.toString());
            }
        }

        boolean RSA2 = EncryptType.RSA2.name().equals(params.get("sign_type"));

        AliPayPaymentConfig paymentConfig = this.paymentConfig;
        try {
            // 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            return AlipaySignature.rsaCheckV1(stringStringHashMap, paymentConfig.getAliPayPublicKey(), paymentConfig.getCharset(),
                    RSA2 ? EncryptType.RSA2.name() : EncryptType.RSA.name());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("支付宝支付通知签名验证异常，参数：{}", params, e);
        }

        return false;
    }


}
