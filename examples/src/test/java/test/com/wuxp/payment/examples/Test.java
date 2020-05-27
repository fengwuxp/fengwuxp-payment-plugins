package test.com.wuxp.payment.examples;

import com.wuxp.payment.AliPayPaymentConfig;
import com.wuxp.payment.PaymentConfiguration;

public class Test {

    public static void main(String[] args) {
        System.out.println(PaymentConfiguration.class.isAssignableFrom(AliPayPaymentConfig.class));
    }
}
