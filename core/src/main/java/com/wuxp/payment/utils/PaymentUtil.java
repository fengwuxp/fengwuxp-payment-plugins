package com.wuxp.payment.utils;

import com.wuxp.payment.enums.ExpireDateType;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class PaymentUtil {

    private static final BigDecimal MULTIPLIER = new BigDecimal(100);

    private PaymentUtil() {
    }


    /**
     * 分转换为元
     *
     * @param fen
     * @return
     */
    public static BigDecimal fen2Yun(Integer fen) {

        return new BigDecimal(fen).divide(MULTIPLIER).setScale(2);
    }

    /**
     * 元转为分
     *
     * @param yuan
     * @return
     */
    public static int yuanToFen(String yuan) {

        return yuanToFen(new BigDecimal(yuan));
    }

    /**
     * 分转换为元
     *
     * @param yuan
     * @return
     */
    public static int yuanToFen(BigDecimal yuan) {

        return yuan.multiply(MULTIPLIER).intValue();
    }


    /**
     * 获取ali rule的时间描述
     *
     * @param num
     * @param dateType
     * @return
     */
    public static String getAliRuleDesc(Integer num, ExpireDateType dateType) {

        return MessageFormat.format("{0}{1}", num, dateType.getSymbol());
    }

    /**
     * 获取支付过期时间
     * 1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
     *
     * @param expr
     * @return
     */
    public static Date getTimeExpireByAliRule(String expr) {

        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        Calendar calendar = Calendar.getInstance(zone);

        if (expr.endsWith("m")) {
            String m = expr.replaceAll("m", "");
            calendar.add(Calendar.MINUTE, Integer.valueOf(m));

        } else if (expr.endsWith("h")) {
            String m = expr.replaceAll("h", "");
            calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(m));

        } else if (expr.endsWith("d")) {
            String m = expr.replaceAll("d", "");
            calendar.add(Calendar.DAY_OF_YEAR, Integer.valueOf(m));
        } else if (expr.endsWith("c")) {
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }

        return calendar.getTime();
    }
}
