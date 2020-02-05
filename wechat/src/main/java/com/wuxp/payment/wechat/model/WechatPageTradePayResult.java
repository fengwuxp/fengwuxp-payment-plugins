package com.wuxp.payment.wechat.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: zhuox
 * @create: 2020-02-05
 * @description: 微信网页支付返回结果
 **/
@Data
@Accessors(chain = true)
public class WechatPageTradePayResult {

    /**
     * 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepayId;

    /**
     * mweb_url 支付跳转链接
     */
    private String mwebUrl;

    /**
     * 是否沙箱环境
     */
    private boolean useSandboxEnv;
}
