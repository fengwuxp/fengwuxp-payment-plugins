package com.wuxp.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.wuxp.payment.AliPayPaymentConfig;
import com.wuxp.payment.AuthCodePaymentHandleStrategy;
import com.wuxp.payment.AuthCodePaymentPlugin;
import com.wuxp.payment.enums.ExpireDateType;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.req.AuthCodePaymentRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.utils.PaymentUtil;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 支付宝授权码支付
 */
@Slf4j
@Setter
public class AliPayAuthCodePaymentService extends AbstractAliPayPaymentService implements AuthCodePaymentPlugin {


    private AuthCodePaymentHandleStrategy paymentHandleStrategy;

    public AliPayAuthCodePaymentService() {
        this(null);
    }

    public AliPayAuthCodePaymentService(AliPayPaymentConfig paymentConfig) {
        super(PaymentMethod.AUTH_CODE, paymentConfig);
    }


    @Override
    public PreOrderResponse<Void> payment(AuthCodePaymentRequest req) {


        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
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
        // 支付场景（条码支付取值：bar_code ,声波支付取值：wave_code）
        model.setScene("bar_code");
        model.setAuthCode(req.getAuthCode());
        request.setBizModel(model);
        if (log.isDebugEnabled()) {
            log.debug("支付请求参数：{}", model);
        }
        //实例化客户端
        AlipayClient alipayClient = this.alipayClient;
        PreOrderResponse<Void> preOrderResponse = new PreOrderResponse<>(req);
        try {
            AlipayTradePayResponse response = alipayClient.execute(request);
            if (log.isDebugEnabled()) {
                log.debug("支付响应 :{}", response.getBody());
            }

            if (response.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("支付响应 :{}", response.getBody());
                }
                preOrderResponse
                        .setTradeNo(response.getOutTradeNo())
                        .setOutTradeNo(response.getTradeNo())
                        .setUseSandboxEnv(this.isUseSandboxEnv())
                        .setOrderAmount(PaymentUtil.yuanToFen(response.getTotalAmount()))
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

        if (this.paymentHandleStrategy != null) {
            this.paymentHandleStrategy.handle(preOrderResponse, req);
        }

        return preOrderResponse;
    }


}
