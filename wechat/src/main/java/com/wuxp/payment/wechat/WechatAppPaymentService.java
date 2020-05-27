package com.wuxp.payment.wechat;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.WxTradeType;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.wechat.config.WechatPaymentConfig;
import com.wuxp.payment.wechat.model.WechatAppTradePayResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * @author: zhuox
 * @create: 2020-02-04
 * @description: 微信APP支付
 **/
@Slf4j
public class WechatAppPaymentService extends AbstractWechatPaymentService {


    public WechatAppPaymentService(WechatPaymentConfig paymentConfig) {
        super(PaymentMethod.APP, paymentConfig);
    }

    @Override
    public PreOrderResponse<WechatAppTradePayResult> preOrder(PreOrderRequest request) {
        PreOrderResponse<WechatAppTradePayResult> preOrderResponse = new PreOrderResponse<>(request);
        try {
            WxPayUnifiedOrderRequest unifiedOrderRequest = new WxPayUnifiedOrderRequest();
            unifiedOrderRequest.setNotifyUrl(request.getNotifyUrl());
            unifiedOrderRequest.setTradeType(WxTradeType.APP.name());
            unifiedOrderRequest.setBody(StringUtils.abbreviate(request.getDescription(), 128));
            unifiedOrderRequest.setOutTradeNo(request.getTradeNo());
            unifiedOrderRequest.setTotalFee(request.getOrderAmount());
            unifiedOrderRequest.setSpbillCreateIp(request.getRemoteIp());
            unifiedOrderRequest.setProductId(request.getTradeNo());
            unifiedOrderRequest.setTimeExpire(this.getTimeExpire(request.getTimeExpire()));
            WxPayAppOrderResult orderResult = this.wxPayService.createOrder(unifiedOrderRequest);
            if (log.isDebugEnabled()) {
                log.debug("微信APP预下单响应 :{}", orderResult);
            }
            WechatAppTradePayResult appTradePayResult = new WechatAppTradePayResult();
            BeanUtils.copyProperties(orderResult, appTradePayResult);
            appTradePayResult.setUseSandboxEnv(this.isUseSandboxEnv);
            preOrderResponse.setResult(appTradePayResult)
                    .setUseSandboxEnv(this.isUseSandboxEnv)
                    .setRawResponse(orderResult)
                    .setSuccess(true);
        } catch (WxPayException e) {
            log.error("微信APP支付预下单失败：{}", e);
            preOrderResponse.setSuccess(false);
            preOrderResponse.setCode(e.getErrCode());
            preOrderResponse.setMessage(e.getErrCodeDes());
        }
        return preOrderResponse;
    }

}
