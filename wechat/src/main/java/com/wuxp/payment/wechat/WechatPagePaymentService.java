package com.wuxp.payment.wechat;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.WxTradeType;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.wechat.config.WechatPaymentConfig;
import com.wuxp.payment.wechat.model.WechatPageTradePayResult;
import com.wuxp.payment.wechat.req.PagePreOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: zhuox
 * @create: 2020-02-05
 * @description: 微信网页支付
 **/
@Slf4j
public class WechatPagePaymentService extends AbstractWechatPaymentService implements WechatPagePaymentPlugin {

    public WechatPagePaymentService () {
        this(null);
    }

    public WechatPagePaymentService(WechatPaymentConfig paymentConfig) {
        super(PaymentMethod.PC_BROWSER, paymentConfig);
    }

    @Override
    public PreOrderResponse<WechatPageTradePayResult> payment(PagePreOrderRequest request) {
        PreOrderResponse<WechatPageTradePayResult> preOrderResponse = new PreOrderResponse<>(request);
        try {
            WxPayUnifiedOrderRequest unifiedOrderRequest = new WxPayUnifiedOrderRequest();
            unifiedOrderRequest.setNotifyUrl(request.getNotifyUrl());
            unifiedOrderRequest.setTradeType(WxTradeType.MWEB.name());
            unifiedOrderRequest.setBody(StringUtils.abbreviate(request.getDescription(), 128));
            unifiedOrderRequest.setOutTradeNo(request.getTradeNo());
            unifiedOrderRequest.setTotalFee(request.getOrderAmount());
            unifiedOrderRequest.setSpbillCreateIp(request.getRemoteIp());
            unifiedOrderRequest.setProductId(request.getTradeNo());
            unifiedOrderRequest.setTimeExpire(this.getTimeExpire(request.getTimeExpire()));
            unifiedOrderRequest.setSceneInfo(request.getSceneInfo());
            WxPayUnifiedOrderResult orderResult = this.wxPayService.createOrder(unifiedOrderRequest);
            if (log.isDebugEnabled()) {
                log.debug("微信网页支付预下单响应 :{}", orderResult);
            }
            WechatPageTradePayResult tradePayResult = new WechatPageTradePayResult();
            tradePayResult.setUseSandboxEnv(this.isUseSandboxEnv)
                    .setPrepayId(orderResult.getPrepayId())
                    .setMwebUrl(orderResult.getMwebUrl());
            preOrderResponse.setResult(tradePayResult)
                    .setUseSandboxEnv(this.isUseSandboxEnv)
                    .setRawResponse(orderResult)
                    .setSuccess(true);
        } catch (WxPayException e) {
            log.error("微信网页支付预下单失败：{}", e);
            preOrderResponse.setSuccess(false);
            preOrderResponse.setCode(e.getReturnCode());
            preOrderResponse.setMessage(e.getReturnMsg());
        }
        return preOrderResponse;
    }

}
