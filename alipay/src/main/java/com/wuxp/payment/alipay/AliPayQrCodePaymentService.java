package com.wuxp.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.wuxp.payment.AliPayPaymentConfig;
import com.wuxp.payment.alipay.model.AliPayQrCodeTradePayResult;
import com.wuxp.payment.enums.ExpireDateType;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.utils.PaymentUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 支付宝二维码支付
 */
@Slf4j
public class AliPayQrCodePaymentService extends AbstractAliPayPaymentService {


    public AliPayQrCodePaymentService(AliPayPaymentConfig payPaymentConfig) {
        super(payPaymentConfig);
    }

    @Override
    public PreOrderResponse<AliPayQrCodeTradePayResult> preOrder(PreOrderRequest req) {


        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setBody(StringUtils.abbreviate(req.getDescription().replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
        model.setSubject(req.getSubject());

        String timeExpire = req.getTimeExpire();
        if (StringUtils.isNotEmpty(timeExpire)) {
            model.setTimeoutExpress(timeExpire);
        } else {
            // 默认30分钟过期
            model.setTimeoutExpress(PaymentUtil.getAliRuleDesc(30, ExpireDateType.MINUTE));
        }
        model.setTotalAmount(PaymentUtil.fen2Yun(req.getOrderAmount()).toString());
        model.setOutTradeNo(req.getTradeNo());
        request.setBizModel(model);
        if (log.isDebugEnabled()) {
            log.debug("支付请求参数：{}", model);
        }
        //实例化客户端
        AlipayClient alipayClient = this.alipayClient;
        PreOrderResponse<AliPayQrCodeTradePayResult> preOrderResponse = new PreOrderResponse<>(req);
        try {

            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if (log.isDebugEnabled()) {
                log.debug("支付响应 :{}", response.getBody());
            }

            if (response.isSuccess()) {
                preOrderResponse
                        .setTradeNo(response.getOutTradeNo())
                        .setUseSandboxEnv(this.isUseSandboxEnv())
                        .setOrderAmount(req.getOrderAmount())
                        .setResult(new AliPayQrCodeTradePayResult(response.getQrCode(), response.getOutTradeNo()))
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
