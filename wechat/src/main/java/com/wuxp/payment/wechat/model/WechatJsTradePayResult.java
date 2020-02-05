package com.wuxp.payment.wechat.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: zhuox
 * @create: 2020-02-05
 * @description: 微信js api支付返回结果
 **/
@Data
@Accessors(chain = true)
public class WechatJsTradePayResult {

    /**
     * 应用appid
     */
    private String appId;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;
    /**
     * 由于package为java保留关键字，因此改为packageValue. 前端使用时记得要更改为package
     */
    private String packageValue;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;

    /**
     * 是否沙箱环境
     */
    private boolean useSandboxEnv;
}
