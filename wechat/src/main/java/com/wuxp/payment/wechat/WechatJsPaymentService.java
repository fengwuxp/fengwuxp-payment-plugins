package com.wuxp.payment.wechat;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.WxTradeType;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.wechat.config.WechatPaymentConfig;
import com.wuxp.payment.wechat.model.WechatJsTradePayResult;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * @author: zhuox
 * @create: 2020-02-05
 * @description: 微信JS API支付
 **/
@Slf4j
@Setter
public class WechatJsPaymentService extends AbstractWechatPaymentService{

    public WechatJsPaymentService () {
        this(null);
    }

    public WechatJsPaymentService(WechatPaymentConfig paymentConfig) {
        super(PaymentMethod.JS_API, paymentConfig);
    }

    @Override
    public PreOrderResponse<WechatJsTradePayResult> preOrder(PreOrderRequest request) {
        PreOrderResponse<WechatJsTradePayResult> preOrderResponse = new PreOrderResponse<>(request);
        try {
            WxPayUnifiedOrderRequest unifiedOrderRequest = new WxPayUnifiedOrderRequest();
            unifiedOrderRequest.setNotifyUrl(request.getNotifyUrl());
            unifiedOrderRequest.setTradeType(WxTradeType.JSAPI.name());
            unifiedOrderRequest.setBody(StringUtils.abbreviate(request.getDescription(), 128));
            unifiedOrderRequest.setOutTradeNo(request.getTradeNo());
            unifiedOrderRequest.setTotalFee(request.getOrderAmount());
            unifiedOrderRequest.setSpbillCreateIp(request.getRemoteIp());
            unifiedOrderRequest.setProductId(request.getTradeNo());
            unifiedOrderRequest.setTimeExpire(this.getTimeExpire(request.getTimeExpire()));
            unifiedOrderRequest.setOpenid(request.getUser());
            WxPayMpOrderResult orderResult = this.wxPayService.createOrder(unifiedOrderRequest);
            if (log.isDebugEnabled()) {
                log.debug("微信JS API支付预下单响应 :{}", orderResult);
            }
            WechatJsTradePayResult tradePayResult = new WechatJsTradePayResult();
            BeanUtils.copyProperties(orderResult, tradePayResult);
            tradePayResult.setUseSandboxEnv(this.isUseSandboxEnv);
            preOrderResponse.setResult(tradePayResult)
                    .setUseSandboxEnv(this.isUseSandboxEnv)
                    .setRawResponse(orderResult)
                    .setSuccess(true);
        } catch (WxPayException e) {
            log.error("微信JS API支付预下单失败：{}", e);
            preOrderResponse.setSuccess(false);
            preOrderResponse.setCode(e.getReturnCode());
            preOrderResponse.setMessage(e.getReturnMsg());
        }
        return preOrderResponse;
    }
}
