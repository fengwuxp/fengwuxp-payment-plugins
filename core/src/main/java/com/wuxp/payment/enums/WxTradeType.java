package com.wuxp.payment.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WxTradeType {


    JSAPI("公众号支付"),

    NATIVE("扫码支付"),


    APP("APP支付"),

    MWEB("网页支付");

    private String desc;


}
