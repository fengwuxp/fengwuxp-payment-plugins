package com.wuxp.payment.springboot.configuration;

import lombok.Data;

import java.time.Duration;

@Data
public class PaymentPluginProperties {


    /**
     * 缓存的过期时间
     * 默认：1天
     */
    private Duration cacheTimeout = Duration.ofDays(1);

    /**
     * 最大缓存的歌声
     */
    private Integer cacheSize = 16;

}
