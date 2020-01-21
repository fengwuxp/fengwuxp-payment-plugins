package com.wuxp.payment.resp;

import java.io.Serializable;

/**
 * 支付插件的基础响应
 */
public interface PaymentBaseResponse extends Serializable {


    /**
     * 结果是否成功
     *
     * @return
     */
    boolean isSuccess();

    /**
     * 返回码
     *
     * @return
     */
    String getCode();

    /**
     * 返回的描述信息，例如错误消息描述等
     *
     * @return
     */
    String getMessage();

    /**
     * 获取第三方平台的原始响应
     *
     * @param <T>
     * @return
     */
    <T> T getRawResponse();
}
