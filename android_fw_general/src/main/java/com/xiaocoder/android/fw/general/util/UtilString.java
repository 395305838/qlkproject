package com.xiaocoder.android.fw.general.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.io.XCIO;

public class UtilString {

    public static String getStringFromLastIndex(String origin, String symbol) {
        if (isBlank(origin)) {
            return "";
        }
        return origin.substring(origin.lastIndexOf(symbol) + 1, origin.length());
    }

    // 去除最后一个，
    public static String getStringWithoutLast(String origin) {
        if (origin.lastIndexOf(",") > 0) {
            origin = origin.substring(0, origin.lastIndexOf(","));
            return origin;
        }
        return "";
    }

    // 去除最后一个符号后面的
    public static String getStringWithoutLast(String origin, String symbol) {
        int position = origin.lastIndexOf(symbol);
        if (position > 0) {
            origin = origin.substring(0, position);
            return origin;
        }
        return "";
    }

    public static String getHttplastnameWithoutDotAndLine(String http_url) {
        int last_dot_position = http_url.lastIndexOf(".");

        if (last_dot_position > 0) {
            http_url = http_url.substring(0, last_dot_position);
        }

        int last_line_position = http_url.lastIndexOf("/");

        if (last_line_position > 0) {
            http_url = http_url.substring(last_line_position + 1, http_url.length());
        }
        return http_url;
    }

    /**
     * 给String高亮显示
     *
     * @param str
     * @param textview
     */
    public static void setLightString(String str, TextView textview) {
        // 实体对象值显示在控件上
        SpannableString hightlight = new SpannableString(str);
        // 高亮器
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#184DA3"));
        hightlight.setSpan(span, 0, (str).length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        textview.setText(hightlight);
    }

    public static void setLightString(String str, TextView textview, int start, int end, String color) {
        // 实体对象值显示在控件上
        SpannableString hightlight = new SpannableString(str);
        // 高亮器
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
        hightlight.setSpan(span, start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        textview.setText(hightlight);
    }

    public static void setLightAppendString(String str, TextView textview) {
        // 实体对象值显示在控件上
        SpannableString hightlight = new SpannableString(str);
        // 高亮器
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#FDC2B5"));
        hightlight.setSpan(span, str.indexOf("：") + 1, (str).length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.append(hightlight);
        textview.append(XCIO.LINE_SEPARATOR);
    }

    public static void setSizeAppendString(String str, TextView textview) {
        // 实体对象值显示在控件上
        SpannableString sizespan = new SpannableString(str);
        // 字体大小
        AbsoluteSizeSpan size_span = new AbsoluteSizeSpan(20);
        sizespan.setSpan(size_span, 0, (str).length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.append(sizespan);
        textview.append(XCIO.LINE_SEPARATOR);
    }

    /**
     * 在String上添加删除线
     *
     * @param str
     * @param textview
     */
    public static void setDeleteString(String str, TextView textview) {
        // 删除线
        SpannableString deleteLine = new SpannableString(str);
        StrikethroughSpan delete = new StrikethroughSpan();
        deleteLine.setSpan(delete, 0, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        textview.setText(deleteLine);
    }

    public static void setDeleteString(TextView textview) {
        textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void slipString(String origin_string, TextView textview) {
        String[] strs = origin_string.split("\n");
        int i = 0;
        for (String str : strs) {
            if (i == 0) {
                i = 1;
                setSizeAppendString(str, textview);
                continue;
            }
            if (str.indexOf("：") > 0) {
                setLightAppendString(str, textview);
            } else {
                textview.append(str + XCIO.LINE_SEPARATOR);
            }
        }
    }

    /**
     * 获取文件大小的 M/K/B表示方式
     *
     * @param contentLenght
     * @return
     */
    public static String getLengthText(long contentLenght) {
        // b/k/M

        DecimalFormat format = new DecimalFormat("###.00");
        float result = 0.0f;
        String resultText = "";
        if (contentLenght > 1024 * 1024)// 1M
        {
            result = (float) (contentLenght / (1024 * 1024.00));
            resultText = format.format(result) + "M";
        } else if (contentLenght > 1024)// 1K
        {
            result = (float) (contentLenght / (1024.00));
            resultText = format.format(result) + "K";
        } else // 1B
        {
            result = (float) (contentLenght / (1.00));
            resultText = format.format(result) + "B";
        }
        return resultText;
    }

    /**
     * 获取两数相除的百分比
     *
     * @param progressBar
     * @param contentLenght
     * @return
     */
    public static String getPercentText(double progressBar, double contentLenght) {
        DecimalFormat format = new DecimalFormat("###.00");
        double resultSize = (double) (progressBar * 100 / contentLenght);
        String reusltText = format.format(resultSize);
        return reusltText + "%";
    }

    /**
     * 显示下载数量
     *
     * @param number
     * @return
     */
    public static String getDownNumberText(long number) {
        double resultSize;
        String resultStr = "";
        DecimalFormat format = new DecimalFormat("###.00");
        if (number > 10000 * 10000) {// 亿
            resultSize = number / (10000 * 10000.00);
            resultStr = format.format(resultSize) + "亿";
        } else if (number > 10000) {// 万
            resultSize = number / (10000.00);
            resultStr = format.format(resultSize) + "万";
        } else {
            resultStr = number + "";
        }
        return resultStr;
    }

    public static String delHtml(String str) {
        String info = str.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
        info = info.replaceAll("[(/>)<]", "");
        return info;
    }

    public static final String encodeURL(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String decodeURL(String str) {
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断给定字符串是否包含空白符<br>
     * 空白串是指含有空格、制表符、回车符、换行符组成的字符串<br>
     * 若输入字符串为null或空字符串，也返回true
     */
    public static boolean isIncludeBlank(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ' || c == '\t' || c != '\r' || c != '\n') {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);// trim()也可以去除制表符
    }

    // ------------------------------------------------------------------
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    public static boolean isPhoneNum(String num) {
        if (num != null && num.length() == 11) {
            // if (num.matches("1[34568]\\d{9}")) {
            // 访问网络获取验证码
            return true;
            // }
        }
        return false;
    }

    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defValue;
        }
//        return defValue;
    }

    public static int toInt(Object obj) {
        if (obj == null) {
            return 0;
        } else if (obj.toString().toString().trim().replace(" ", "").equals("")) {
            return 0;
        }
        return toInt(obj.toString(), 0);
    }

    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public String discount(String price_orign, String price_sell, int keep_dot) {
        double discount_num = Double.parseDouble(price_sell) / Double.parseDouble(price_orign);
        discount_num = discount_num * Math.pow(10, keep_dot + 2);// 显示如6.22则,但是6.226可以进一位
        String discount_text = discount_num + "";
        char c = discount_text.charAt(keep_dot + 2);
        // 四舍五入
        if (Integer.parseInt(c + "") >= 5) {
            discount_num = (discount_num + 10.0) / Math.pow(10, keep_dot + 1);
        } else {
            discount_num = discount_num / Math.pow(10, keep_dot + 1);
        }
        if ((discount_num + "").length() < 4) {
            discount_text = discount_num + "0";
        } else {
            discount_text = discount_num + "";
        }
        return discount_text.substring(0, keep_dot + 2);
    }

    /**
     * 该方法只判断银行卡是否为16-19位
     *
     * @param card
     * @return
     */
    public static boolean isValidBankCard(String card) {
        if (TextUtils.isEmpty(card)) {
            return false;
        }
        card = card.replace(" ", "");
        int length = card.length();
        if (length >= 16 && length <= 19) {
            return true;
        }
        return false;

    }
}
