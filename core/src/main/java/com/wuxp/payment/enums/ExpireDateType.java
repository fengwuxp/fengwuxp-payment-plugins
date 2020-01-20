package com.wuxp.payment.enums;

/**
 * 过期时间类型
 */
public enum ExpireDateType {

    MINUTE("m"),

    HOUR_OF_DAY("h"),

    DAY_OF_YEAR("d"),

    CURRENT("c");

    private String symbol;

    ExpireDateType(String symbol) {
        this.symbol = symbol;
    }


    public String getSymbol() {
        return symbol;
    }
}
