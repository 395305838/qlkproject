package com.xiaocoder.android.fw.general.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字格式化工具类
 */
public class UtilNumberFormat {
    /**
     * 只保留整数部分，且不显示千分符。不四舍五入，以截断方式处理。
     *
     * @param doubleNumber 源数据
     * @return String 被格式化后的数据，以字符串方式返回
     */
    public static String decimalFormatForIntNoCommas(double doubleNumber) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("##0");
        doubleNumber = ((int) (doubleNumber * 100)) / 100.0;
        String str_earning = df.format(doubleNumber);

        return str_earning;
    }

    /**
     * 只保留整数部分，且不显示千分符。不四舍五入，以截断方式处理。
     *
     * @param doubleNumber 源数据
     * @return String 被格式化后的数据，以字符串方式返回
     */
    public static String decimalFormatForIntNoCommas(String doubleNumber) {
        return decimalFormatForIntNoCommas(Double.valueOf(doubleNumber));
    }

    /**
     * 只保留整数部分。不四舍五入，以截断方式处理。
     *
     * @param doubleNumber 源数据
     * @return String 被格式化后的数据，以字符串方式返回
     */
    public static String decimalFormatForInt(double doubleNumber) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("#,##0");
        doubleNumber = ((int) (doubleNumber * 100)) / 100.0;
        String str_earning = df.format(doubleNumber);

        return str_earning;
    }

    /**
     * 只保留整数部分。不四舍五入，以截断方式处理。
     *
     * @param doubleNumber 源数据
     * @return String 被格式化后的数据，以字符串方式返回
     */
    public static String decimalFormatForInt(String doubleNumber) {
        return decimalFormatForInt(Double.valueOf(doubleNumber));
    }


    /**
     * 保留小数点后两位，当小数部分不足两位时，进行补零。不四舍五入，以截断方式处理。
     *
     * @param doubleNumber 源数据
     * @return String 被格式化后的数据，以字符串方式返回
     */
    public static String decimalFormat(double doubleNumber) {
        return decimalFormat(String.valueOf(doubleNumber));
    }

    /**
     * 保留小数点后两位，当小数部分不足两位时，进行补零。不四舍五入，以截断方式处理。
     *
     * @param stringNumber 源数据
     * @return String 被格式化后的数据，以字符串方式返回
     */
    public static String decimalFormat(String stringNumber) {
        double doubleNumber = 0.0d;

        try {
            doubleNumber = Double.valueOf(stringNumber);
        } catch (Exception e) {
            doubleNumber = 0.0d;
        }

        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("#,##0.00");

        BigDecimal bigDec_doubleNumber = new BigDecimal(Double.toString(doubleNumber));
        BigDecimal bigDec_100d = new BigDecimal(Double.toString(100.0d));
        BigDecimal result1 = bigDec_doubleNumber.multiply(bigDec_100d);

        doubleNumber = result1.doubleValue() / 100.0d;
        String str_earning = df.format(doubleNumber);

        return str_earning;
    }

    /**
     * 限制EditText输入的小数点位数
     *
     * @param count 限制到小数点后几位
     * @param msg   要约束的字符串(若不满足约束,直接返回传入的字符串)
     */
    public static String LimitNumber(int count, String msg) {
        String[] splits = msg.split("\\.");
        if (2 == splits.length && count > 0 && count < splits.length) {
            if (2 < splits[1].length()) {
                String substring = splits[1].substring(0, count);
                return (splits[0] + "." + substring);
            }
        }
        return msg;
    }
}
