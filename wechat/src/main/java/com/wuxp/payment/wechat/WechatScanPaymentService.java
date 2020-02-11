package com.wuxp.payment.wechat;

import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.WxTradeType;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.wechat.config.WechatPaymentConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: zhuox
 * @create: 2020-02-05
 * @description: 微信扫码支付
 **/
@Slf4j
public class WechatScanPaymentService extends AbstractWechatPaymentService {

    public WechatScanPaymentService () {
        this(null);
    }

    public WechatScanPaymentService(WechatPaymentConfig paymentConfig) {
        super(PaymentMethod.SCAN_QR_CODE, paymentConfig);
    }

    /**
     *
     * @param request
     * @return result：二维码链接
     */
    @Override
    public PreOrderResponse<String> preOrder(PreOrderRequest request) {
        PreOrderResponse<String> preOrderResponse = new PreOrderResponse<>(request);
        try {
            WxPayUnifiedOrderRequest unifiedOrderRequest = new WxPayUnifiedOrderRequest();
            unifiedOrderRequest.setNotifyUrl(request.getNotifyUrl());
            unifiedOrderRequest.setTradeType(WxTradeType.NATIVE.name());
            unifiedOrderRequest.setBody(StringUtils.abbreviate(request.getDescription(), 128));
            unifiedOrderRequest.setOutTradeNo(request.getTradeNo());
            unifiedOrderRequest.setTotalFee(request.getOrderAmount());
            unifiedOrderRequest.setSpbillCreateIp(request.getRemoteIp());
            unifiedOrderRequest.setProductId(request.getTradeNo());
            unifiedOrderRequest.setTimeExpire(this.getTimeExpire(request.getTimeExpire()));
            WxPayNativeOrderResult orderResult = this.wxPayService.createOrder(unifiedOrderRequest);
            if (log.isDebugEnabled()) {
                log.debug("微信扫码预下单响应 :{}", orderResult);
            }
            preOrderResponse.setResult(orderResult.getCodeUrl())
                    .setUseSandboxEnv(this.isUseSandboxEnv)
                    .setRawResponse(orderResult)
                    .setSuccess(true);
        } catch (WxPayException e) {
            log.error("微信扫码预下单失败：{}", e);
            preOrderResponse.setSuccess(false);
            preOrderResponse.setCode(e.getReturnCode());
            preOrderResponse.setMessage(e.getReturnMsg());
        }
        return preOrderResponse;
    }

}
