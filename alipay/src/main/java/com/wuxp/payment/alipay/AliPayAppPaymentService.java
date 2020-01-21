package com.wuxp.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.wuxp.payment.AliPayPaymentConfig;
import com.wuxp.payment.enums.ExpireDateType;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.utils.PaymentUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * 支付宝app支付
 */
@Slf4j
public class AliPayAppPaymentService extends AbstractAliPayPaymentService {

    public AliPayAppPaymentService() {
        this(null);
    }

    public AliPayAppPaymentService(AliPayPaymentConfig paymentConfig) {
        super(PaymentMethod.APP, paymentConfig);
    }


    @Override
    public PreOrderResponse<String> preOrder(PreOrderRequest req) {


        // 实例化客户端
        AlipayClient alipayClient = this.alipayClient;

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(StringUtils.abbreviate(req.getDescription().replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
        model.setSubject(req.getSubject());
        model.setOutTradeNo(req.getTradeNo());
        String timeExpire = req.getTimeExpire();
        if (StringUtils.isNotEmpty(timeExpire)) {
            model.setTimeoutExpress(timeExpire);
        } else {
            // 默认30分钟过期
            model.setTimeoutExpress(PaymentUtil.getAliRuleDesc(30, ExpireDateType.MINUTE));
        }
        model.setTotalAmount(PaymentUtil.fen2Yun(req.getOrderAmount()).toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        if (log.isDebugEnabled()) {
            log.debug("支付请求参数：{}", model);
        }
        PreOrderResponse<String> preOrderResponse = new PreOrderResponse<String>(req);
        try {

            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);

            if (response.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("支付响应 :{}", response.getBody());
                }
                preOrderResponse
                        .setTradeNo(response.getMerchantOrderNo())
                        .setOutTradeNo(response.getTradeNo())
                        .setUseSandboxEnv(this.isUseSandboxEnv())
                        .setOrderAmount(PaymentUtil.yuanToFen(response.getTotalAmount()))
                        .setResult(response.getBody())
                        .setRawResponse(response);

            } else {
                preOrderResponse.setSuccess(false);
                preOrderResponse.setCode(response.getCode());
                preOrderResponse.setMessage(response.getMsg());
            }
            preOrderResponse.setRawResponse(response);
        } catch (AlipayApiException e) {
            log.error("支付宝请求失败", e);
            preOrderResponse.setSuccess(false);
            preOrderResponse.setCode(e.getErrCode());
            preOrderResponse.setMessage(e.getErrMsg());
        }


        return preOrderResponse;
    }
}
