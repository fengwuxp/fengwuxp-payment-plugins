package com.wuxp.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.wuxp.payment.AliPayPaymentConfig;
import com.wuxp.payment.alipay.model.AliPayPageTradePayResult;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.utils.PaymentUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 支付宝页面支付
 */
@Slf4j
public class AliPayPagePaymentService extends AbstractAliPayPaymentService {


    public AliPayPagePaymentService() {
        this(null);
    }

    public AliPayPagePaymentService(AliPayPaymentConfig paymentConfig) {
        super(PaymentMethod.PC_BROWSER, paymentConfig);
    }

    @Override
    public PreOrderResponse<AliPayPageTradePayResult> preOrder(PreOrderRequest req) {


        //实例化客户端
        AlipayClient alipayClient = this.alipayClient;
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setBody(StringUtils.abbreviate(req.getDescription().replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
        model.setSubject(req.getSubject());
        String timeExpire = req.getTimeExpire();
        if (org.springframework.util.StringUtils.hasText(timeExpire)) {
            model.setTimeoutExpress(timeExpire);
        } else {
            model.setTimeoutExpress("30m");
        }
        model.setTotalAmount(PaymentUtil.fen2Yun(req.getOrderAmount()).toString());
        model.setOutTradeNo(req.getTradeNo());
        request.setBizModel(model);
        if (log.isDebugEnabled()) {
            log.debug("支付请求参数：{}", model);
        }
        PreOrderResponse<AliPayPageTradePayResult> preOrderResponse = new PreOrderResponse<>(req);
        try {

            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (log.isDebugEnabled()) {
                log.debug("支付响应 :{}", response.getBody());
            }
            if (response.isSuccess()) {
                AliPayPageTradePayResult result = new AliPayPageTradePayResult();
                result.setOrderInfo(response.getBody())
                        .setMerchantOrderNo(response.getMerchantOrderNo())
                        .setOutTradeNo(response.getOutTradeNo())
                        .setSellerId(response.getSellerId())
                        .setTotalAmount(response.getTotalAmount())
                        .setTradeNo(response.getTradeNo());
                preOrderResponse
                        .setTradeNo(response.getOutTradeNo())
                        .setOutTradeNo(response.getTradeNo())
                        .setUseSandboxEnv(this.isUseSandboxEnv())
                        .setOrderAmount(PaymentUtil.yuanToFen(response.getTotalAmount()))
                        .setResult(result)
                        .setRawResponse(response)
                        .setSuccess(true);
            } else {
                preOrderResponse.setSuccess(false)
                        .setCode(response.getCode())
                        .setMessage(response.getMsg());
            }
            preOrderResponse.setRawResponse(response);
        } catch (AlipayApiException e) {
            log.error("支付宝请求失败", e);
            preOrderResponse.setSuccess(false)
                    .setCode(e.getErrCode())
                    .setMessage(e.getErrMsg());
        }

        return preOrderResponse;
    }
}
