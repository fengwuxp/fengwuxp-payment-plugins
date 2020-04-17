package com.wuxp.payment.springboot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = PaymentPluginProperties.PREFIX)
public class PaymentPluginProperties {


    public static final String PREFIX = "payment.plugins";

    /**
     * 缓存的过期时间
     * 默认：1天
     */
    private Duration cacheTimeout = Duration.ofDays(1);

    /**
     * 最大缓存的个数
     */
    private Integer cacheSize = 16;

}
