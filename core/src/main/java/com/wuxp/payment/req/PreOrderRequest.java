package com.wuxp.payment.req;


import com.wuxp.payment.PreOrderRequestFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 预下单请求
 * 如果有更复杂的支付参数，可以通过继承该类做扩展
 *
 * @author wxup
 * @see PreOrderRequestFactory
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PreOrderRequest extends AbstractPaymentRequest {

    private static final long serialVersionUID = -282363501463676553L;

    /**
     * 应用内的交易流水号
     */
    private String tradeNo;

    /**
     * 支付用户标识
     */
    @NotBlank
    private String user;

    /**
     * 支付金额，单位分
     */
    @NotNull
    @Min(value = 1)
    private Integer orderAmount;

    /**
     * 支付请求发起ip
     */
    @NotBlank
    private String remoteIp;


    /**
     * 同步回调url
     */
    @NotBlank
    private String returnUrl;

    /**
     * 异步回调url
     */
    @NotBlank
    private String notifyUrl;


    /**
     * 支付说明
     */
    private String description;


    /**
     * 交易结束时间
     * 使用阿里规则
     * 1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
     * <p>
     * 默认：30m，  30分钟过期
     */
    private String timeExpire;


    /**
     * 商品显示url
     */
    private String showUrl;

    /**
     * 主题
     */
    private String subject;


    /**
     * 其他参数
     */
//    private Object otherParams;
}
