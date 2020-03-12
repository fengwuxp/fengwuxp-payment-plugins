package com.wuxp.payment.wechat;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.collect.Lists;
import com.wuxp.payment.AbstractPlatformPaymentService;
import com.wuxp.payment.enums.ExpireDateType;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.enums.TradeStatus;
import com.wuxp.payment.model.PaymentBaseOrder;
import com.wuxp.payment.req.*;
import com.wuxp.payment.resp.OrderRefundResponse;
import com.wuxp.payment.resp.QueryOrderResponse;
import com.wuxp.payment.resp.QueryRefundOrderResponse;
import com.wuxp.payment.utils.PaymentUtil;
import com.wuxp.payment.wechat.config.WechatPaymentConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: zhuox
 * @create: 2020-02-04
 * @description: 微信支付抽象服务
 **/
@Slf4j
public abstract class AbstractWechatPaymentService extends AbstractPlatformPaymentService<WechatPaymentConfig> {

    protected WxPayService wxPayService;

    private String singType = WxPayConstants.SignType.MD5;

    protected boolean isUseSandboxEnv = false;

    public AbstractWechatPaymentService(PaymentMethod paymentMethod, WechatPaymentConfig paymentConfig) {
        super(paymentMethod, paymentConfig);
        this.paymentPlatform = PaymentPlatform.WE_CHAT;
    }

    @Override
    public void setPaymentConfig(WechatPaymentConfig paymentConfig) {
        this.paymentConfig = paymentConfig;
        this.wxPayService = this.getWxService();
    }

    /**
     * 获取微信支付服务
     *
     * @return
     */
    private WxPayService getWxService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.paymentConfig.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.paymentConfig.getPartner()));
        payConfig.setMchKey(StringUtils.trimToNull(this.paymentConfig.getPartnerSecret()));
        payConfig.setSubAppId(StringUtils.trimToNull(this.paymentConfig.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.paymentConfig.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.paymentConfig.getKeyPath()));
        payConfig.setSignType(this.singType);
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(this.isUseSandboxEnv);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 微信支付异步通知
     *
     * @param request          支付通知参数
     * @param paymentBaseOrder 订单信息
     * @return
     */
    @Override
    public String paymentProcess(PaymentNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder) {
        try {
            //验签
            if (!this.verifyPaymentNotifyRequest(request)) {
                if (log.isDebugEnabled()) {
                    log.debug("微信支付通知：【{}】签名验证失败", request);
                }
                return WxPayNotifyResponse.fail("失败");
            }
            String xmlData = request.getRequestBody();
            final WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(xmlData);
            QueryOrderResponse response = new QueryOrderResponse();
            response.setPaymentPlatform(PaymentPlatform.WE_CHAT);
            response.setPaymentMethod(paymentBaseOrder.getPaymentMethod());
            response.setOutTradeNo(notifyResult.getTransactionId())
                    .setTradeNo(notifyResult.getOutTradeNo())
                    .setOrderAmount(notifyResult.getTotalFee())
                    .setBuyerPayAmount(notifyResult.getSettlementTotalFee())
                    .setUseSandboxEnv(this.isUseSandboxEnv);
            List<String> successStrings = Lists.newArrayList(WxPayConstants.ResultCode.SUCCESS, "");
            if (!successStrings.contains(StringUtils.trimToEmpty(notifyResult.getReturnCode()).toUpperCase())
                    || !successStrings.contains(StringUtils.trimToEmpty(notifyResult.getResultCode()).toUpperCase())) {
                response.setTradeStatus(TradeStatus.FAILURE);
            } else {
                response.setTradeStatus(TradeStatus.SUCCESS);
            }
            response.setRawResponse(notifyResult);
            boolean success = this.callbackTemplate.handlePaymentCallback(request.getNotifyMethod(), response, paymentBaseOrder);
            return this.getNotifyReturn(success);
        } catch (WxPayException e) {
            log.error("微信支付异步通知失败：{}", e);
            return WxPayNotifyResponse.fail("失败");
        }
    }

    /**
     * 微信退款异步通知
     *
     * @param request          退款通知参数
     * @param paymentBaseOrder 订单信息
     * @return
     */
    @Override
    public String refundProcess(RefundNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder) {
        try {
            //验签
            if (!this.verifyRefundNotifyRequest(request)) {
                if (log.isDebugEnabled()) {
                    log.debug("微信退款通知：【{}】签名验证失败", request);
                }
                return WxPayNotifyResponse.fail("失败");
            }
            String xmlData = request.getRequestBody();
            final WxPayRefundNotifyResult notifyResult = this.wxPayService.parseRefundNotifyResult(xmlData);
            QueryRefundOrderResponse response = new QueryRefundOrderResponse();
            response.setPaymentPlatform(PaymentPlatform.WE_CHAT);
            response.setPaymentMethod(paymentBaseOrder.getPaymentMethod());
            response.setTradeRefundNo(notifyResult.getReqInfo().getOutRefundNo());
            response.setOutTradeRefundNo(notifyResult.getReqInfo().getRefundId());
            response.setOrderAmount(notifyResult.getReqInfo().getTotalFee());
            response.setRefundAmount(notifyResult.getReqInfo().getSettlementRefundFee());
            response.setRawResponse(notifyResult);
            List<String> successStrings = Lists.newArrayList(WxPayConstants.ResultCode.SUCCESS, "");
            if (!successStrings.contains(StringUtils.trimToEmpty(notifyResult.getReturnCode()).toUpperCase())
                    || !successStrings.contains(StringUtils.trimToEmpty(notifyResult.getResultCode()).toUpperCase())) {
                response.setTradeStatus(TradeStatus.REFUND_FAILURE);
            } else {
                response.setTradeStatus(response.getOrderAmount().equals(response.getRefundAmount()) ? TradeStatus.REFUNDED : TradeStatus.PARTIAL_REFUND);
            }
            boolean success = this.callbackTemplate.handleRefundCallback(request.getNotifyMethod(), response, paymentBaseOrder);
            return this.getNotifyReturn(success);
        } catch (WxPayException e) {
            log.error("微信支付异步通知失败：{}", e);
            return WxPayNotifyResponse.fail("失败");
        }
    }

    @Override
    public QueryOrderResponse queryOrder(QueryOrderRequest req) {
        QueryOrderResponse queryOrderResponse = new QueryOrderResponse(req);
        queryOrderResponse.setUseSandboxEnv(this.isUseSandboxEnv);
        try {
            WxPayOrderQueryResult orderQueryResult = wxPayService.queryOrder(req.getOutTradeNo(), req.getTradeNo());
            if (log.isDebugEnabled()) {
                log.debug("查询微信支付结果 :【{}】{}", req.getTradeNo(), orderQueryResult);
            }
            queryOrderResponse.setOutTradeNo(orderQueryResult.getTransactionId())
                    .setTradeNo(orderQueryResult.getOutTradeNo())
                    .setOrderAmount(orderQueryResult.getTotalFee())
                    .setBuyerPayAmount(orderQueryResult.getSettlementTotalFee())
                    .setReceiptAmount(orderQueryResult.getSettlementTotalFee())
                    .setTradeStatus(this.transformTradeStatus(orderQueryResult.getTradeState()))
                    .setSuccess(true);
            queryOrderResponse.setPayerAccount(orderQueryResult.getOpenid());
            queryOrderResponse.setRawResponse(orderQueryResult);
        } catch (WxPayException e) {
            log.error("微信查询订单失败：{}", e);
            queryOrderResponse.setCode(e.getReturnCode());
            queryOrderResponse.setMessage(e.getReturnMsg());
            queryOrderResponse.setSuccess(false);
        }
        return queryOrderResponse;
    }

    @Override
    public OrderRefundResponse orderRefund(RefundRequest req) {
        WxPayRefundRequest request = new WxPayRefundRequest();
        request.setRefundFee(req.getRefundAmount());
        request.setTransactionId(req.getOutTradeNo());
        request.setOutTradeNo(req.getTradeNo());
        request.setOutRefundNo(req.getRequestRefundNo());
        request.setTotalFee(req.getOrderAmount());
        request.setNotifyUrl(req.getRefundNotifyUrl());
        request.setRefundDesc(req.getRefundReason());
        OrderRefundResponse refundResponse = new OrderRefundResponse();
        try {
            WxPayRefundResult wxMpPayRefundResult = wxPayService.refund(request);
            if (log.isDebugEnabled()) {
                log.debug("微信退款请求结果 :【{}】{}", req.getTradeNo(), wxMpPayRefundResult);
            }
            refundResponse.setOrderAmount(wxMpPayRefundResult.getTotalFee())
                    .setRefundAmount(wxMpPayRefundResult.getRefundFee())
                    .setTradeRefundNo(wxMpPayRefundResult.getOutRefundNo())
                    .setOutTradeRefundNo(wxMpPayRefundResult.getRefundId())
                    .setTradeStatus(TradeStatus.WAIT_REFUND)
                    .setSuccess(true);
        } catch (WxPayException e) {
            log.error("微信退款失败：【{}】{}", req.getTradeNo(), e);
            refundResponse.setCode(e.getReturnCode());
            refundResponse.setMessage(e.getReturnMsg());
            refundResponse.setSuccess(false);
        }
        return refundResponse;
    }

    /**
     * 单笔退款查询
     *
     * @param req
     * @return
     */
    @Override
    public QueryRefundOrderResponse queryOrderRefund(QueryRefundRequest req) {
        QueryRefundOrderResponse orderResponse = new QueryRefundOrderResponse();
        WxPayRefundQueryRequest queryRequest = new WxPayRefundQueryRequest();
        queryRequest.setTransactionId(req.getOutTradeNo());
        queryRequest.setOutTradeNo(req.getTradeNo());
        queryRequest.setOutRefundNo(req.getRequestRefundSn());
        queryRequest.setRefundId(req.getOutTradeRefundSn());
        try {
            WxPayRefundQueryResult queryResult = wxPayService.refundQuery(queryRequest);
            if (log.isDebugEnabled()) {
                log.debug("微信退款查询结果 :【{}】{}", req.getRequestRefundSn(), queryResult);
            }
            WxPayRefundQueryResult.RefundRecord refundRecord = queryResult.getRefundRecords().get(0);
            orderResponse.setOutTradeRefundNo(req.getOutTradeRefundSn())
                    .setTradeRefundNo(req.getRequestRefundSn())
                    .setRefundAmount(refundRecord.getSettlementRefundFee())
                    .setOrderAmount(queryResult.getTotalFee())
                    .setTradeStatus(this.transformRefundTradeStatus(refundRecord.getRefundStatus()))
                    .setSuccess(true);
        } catch (WxPayException e) {
            log.error("微信退款失败：【{}】{}", req.getTradeNo(), e);
            orderResponse.setCode(e.getReturnCode());
            orderResponse.setMessage(e.getReturnMsg());
            orderResponse.setSuccess(false);
        }
        return orderResponse;
    }

    /**
     * 验证支付通知
     *
     * @param request 支付通知参数
     * @return
     */
    private boolean verifyPaymentNotifyRequest(PaymentNotifyProcessRequest request) {
        WechatPaymentConfig paymentConfig = this.paymentConfig;
        String xmlData = request.getRequestBody();
        try {
            final WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(xmlData);
            //验签
            notifyResult.checkResult(this.wxPayService, this.singType, false);
            boolean verifyResult = paymentConfig.getPartner().equals(notifyResult.getMchId())
                    && notifyResult.getOutTradeNo().equals(request.getTradeNo())
                    && request.getOrderAmount().equals(notifyResult.getTotalFee());
            if (!verifyResult) {
                if (log.isDebugEnabled()) {
                    log.debug("微信支付通知，【{}】参数验证失败，{}", request.getTradeNo(), notifyResult);
                }
                return false;
            } else {
                return true;
            }
        } catch (WxPayException e) {
            log.error("微信支付通知，参数验证失败：{}", e);
            return false;
        }
    }

    /**
     * 退款通知验签
     *
     * @param request
     * @return
     */
    private boolean verifyRefundNotifyRequest(RefundNotifyProcessRequest request) {
        WechatPaymentConfig paymentConfig = this.paymentConfig;
        String xmlData = request.getRequestBody();
        try {
            final WxPayRefundNotifyResult notifyResult = this.wxPayService.parseRefundNotifyResult(xmlData);
            //验签
            notifyResult.checkResult(this.wxPayService, this.singType, false);
            boolean verifyResult = paymentConfig.getPartner().equals(notifyResult.getMchId())
                    && notifyResult.getReqInfo().getOutRefundNo().equals(request.getRefundTradeNo())
                    && request.getRefundAmount().equals(notifyResult.getReqInfo().getRefundFee());
            if (!verifyResult) {
                if (log.isDebugEnabled()) {
                    log.debug("微信退款通知，【{}】参数验证失败，{}", request.getRefundTradeNo(), notifyResult);
                }
                return false;
            } else {
                return true;
            }
        } catch (WxPayException e) {
            log.error("微信支付通知，参数验证失败：{}", e);
            return false;
        }
    }

    /**
     * SUCCESS—支付成功,REFUND—转入退款,NOTPAY—未支付,CLOSED—已关闭,REVOKED—已撤销（刷卡支付）,USERPAYING--用户支付中
     * ,PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    @AllArgsConstructor
    @Getter
    protected enum WechatTradeStatus {

        SUCCESS("支付成功"),

        REFUND("转入退款"),

        NOTPAY("未支付"),

        CLOSED("已关闭"),

        REVOKED("已撤销（刷卡支付）"),

        USERPAYING("用户支付中"),

        PAYERROR("支付失败(其他原因，如银行返回失败)");

        //说明
        private String memo;
    }

    /**
     * @param tradeState
     * @return
     */
    private TradeStatus transformTradeStatus(String tradeState) {
        if (tradeState.equals(WechatTradeStatus.SUCCESS.name())) {
            return TradeStatus.SUCCESS;
        }
        if (tradeState.equals(WechatTradeStatus.REFUND.name())) {
            return TradeStatus.WAIT_REFUND;
        }
        if (tradeState.equals(WechatTradeStatus.NOTPAY.name())) {
            return TradeStatus.NOT_PAY;
        }
        if (tradeState.equals(WechatTradeStatus.CLOSED.name())) {
            return TradeStatus.CLOSED;
        }
        if (tradeState.equals(WechatTradeStatus.REVOKED.name())) {
            return TradeStatus.CLOSED;
        }
        if (tradeState.equals(WechatTradeStatus.USERPAYING.name())) {
            return TradeStatus.WAIT_PAY;
        }
        if (tradeState.equals(WechatTradeStatus.PAYERROR.name())) {
            return TradeStatus.FAILURE;
        }
        return TradeStatus.UNKNOWN;
    }

    /**
     * SUCCESS—退款成功，
     * FAIL—退款失败，
     * PROCESSING—退款处理中，
     * CHANGE—转入代发
     */
    @AllArgsConstructor
    @Getter
    protected enum WechatRefundTradeStatus {

        SUCCESS("退款成功"),

        FAIL("退款失败"),

        PROCESSING("退款处理中"),

        CHANGE("转入代发");

        //说明
        private String memo;
    }

    /**
     * 退款状态
     *
     * @param state
     * @return
     */
    private TradeStatus transformRefundTradeStatus(String state) {
        if (state.equals(WechatRefundTradeStatus.SUCCESS.name())) {
            return TradeStatus.REFUNDED;
        }
        if (state.equals(WechatRefundTradeStatus.FAIL.name())) {
            return TradeStatus.REFUND_FAILURE;
        }
        if (state.equals(WechatRefundTradeStatus.PROCESSING.name())) {
            return TradeStatus.WAIT_REFUND;
        }
        if (state.equals(WechatRefundTradeStatus.CHANGE.name())) {
            return TradeStatus.WAIT_REFUND;
        }
        return TradeStatus.UNKNOWN;
    }

    private String getNotifyReturn(boolean success) {
        return success ? WxPayNotifyResponse.success("成功") : WxPayNotifyResponse.fail("失败");
    }

    protected String getTimeExpire(String timeExpire) {
        if (StringUtils.isNotEmpty(timeExpire)) {
            return this.formatDate(PaymentUtil.getTimeExpireByAliRule(timeExpire));
        }
        return this.formatDate(PaymentUtil.getTimeExpireByAliRule(PaymentUtil.getAliRuleDesc(30, ExpireDateType.MINUTE)));
    }

    protected String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, "yyyyMMddHHmmss");
    }
}
