package com.wuxp.payment.wechat.req;

import com.wuxp.payment.req.PreOrderRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: zhuox
 * @create: 2020-02-05
 * @description: H5预下单请求参数
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PagePreOrderRequest extends PreOrderRequest {

    /**
     * <pre>
     * 字段名：场景信息.
     * 变量名：scene_info
     * 是否必填：对H5支付来说是必填
     * 类型：String(256)
     * 示例值：{
     * "store_id": "SZT10000",
     * "store_name":"腾讯大厦腾大餐厅"
     * }
     * 描述：该字段用于统一下单时上报场景信息，目前支持上报实际门店信息。
     * {
     * "store_id": "", //门店唯一标识，选填，String(32)
     * "store_name":"”//门店名称，选填，String(64)
     * }
     * </pre>
     */
    @NotNull
    private String sceneInfo;
}
