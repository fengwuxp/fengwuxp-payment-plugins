package com.wuxp.payment.wechat;

import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.wuxp.payment.AuthCodePaymentHandleStrategy;
import com.wuxp.payment.AuthCodePaymentPlugin;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.req.AuthCodePaymentRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.wechat.config.WechatPaymentConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: zhuox
 * @create: 2020-02-04
 * @description: 微信付款码支付
 **/
@Slf4j
@Setter
public class WechatMicropayPaymentService extends AbstractWechatPaymentService implements AuthCodePaymentPlugin {

    private AuthCodePaymentHandleStrategy paymentHandleStrategy;

    public WechatMicropayPaymentService(WechatPaymentConfig paymentConfig) {
        super(PaymentMethod.AUTH_CODE, paymentConfig);
    }

    @Override
    public PreOrderResponse<Void> payment(AuthCodePaymentRequest request) {
        PreOrderResponse<Void> preOrderResponse = new PreOrderResponse<>(request);
        try {
            WxPayMicropayRequest micropayRequest = new WxPayMicropayRequest();
            micropayRequest.setBody(StringUtils.abbreviate(request.getDescription(), 128));
            micropayRequest.setOutTradeNo(request.getTradeNo());
            micropayRequest.setTotalFee(request.getOrderAmount());
            micropayRequest.setAuthCode(request.getAuthCode());
            micropayRequest.setSpbillCreateIp(request.getRemoteIp());
            micropayRequest.setTimeExpire(this.getTimeExpire(request.getTimeExpire()));
            WxPayMicropayResult micropayResult = this.wxPayService.micropay(micropayRequest);
            if (log.isDebugEnabled()) {
                log.debug("微信付款码支付预下单响应 :{}", micropayResult);
            }
            if (WxPayConstants.ResultCode.SUCCESS.equals(micropayResult.getReturnCode())
                    && WxPayConstants.ResultCode.SUCCESS.equals(micropayResult.getResultCode())) {
                preOrderResponse
                        .setTradeNo(micropayResult.getOutTradeNo())
                        .setOutTradeNo(micropayResult.getTransactionId())
                        .setUseSandboxEnv(this.isUseSandboxEnv)
                        .setOrderAmount(micropayResult.getTotalFee())
                        .setSuccess(true)
                        .setCode(micropayResult.getReturnCode())
                        .setMessage(micropayResult.getReturnMsg());
            } else {
                preOrderResponse.setSuccess(false)
                        .setCode(micropayResult.getReturnCode())
                        .setMessage(micropayResult.getReturnMsg());
            }
            preOrderResponse.setRawResponse(micropayResult);
        } catch (WxPayException e) {
            log.error("微信付款码支付预下单失败：{}", e);
            preOrderResponse.setSuccess(false);
            preOrderResponse.setCode(e.getReturnCode());
            preOrderResponse.setMessage(e.getReturnMsg());
        }
        if (this.paymentHandleStrategy != null) {
            this.paymentHandleStrategy.handle(preOrderResponse, request);
        }
        return preOrderResponse;
    }

}
