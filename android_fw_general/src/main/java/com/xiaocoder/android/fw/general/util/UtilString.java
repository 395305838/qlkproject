package com.xiaocoder.android.fw.general.util;

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

import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.io.XCIO;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString {

    /**
     * 比较两个字符串
     */
    public static boolean equalsStr(String str1, String str2) {

        if (str1 == null && str2 == null) {

            throw new RuntimeException("UtilString.equalsString()--传入了两个null字符串");

        } else if (str1 != null) {

            return str1.equals(str2);

        } else {

            return false;

        }

    }

    /**
     * 判断给定字符串是否包含空白符
     * 空白串是指含有空格、制表符、回车符、换行符组成的字符串
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

    /**
     * 只判断空 和 空格
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);// trim()也可以去除制表符
    }


    public static int toInt(String str, int defValue) {

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defValue;
        }

    }

    public static long toLong(String obj, long defValue) {

        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
            return defValue;
        }

    }

    public static double toDouble(String obj, double defValue) {

        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
            return defValue;
        }

    }

    public static boolean toBool(String b, boolean defValue) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 去除最后一个 " , "
     */
    public static String getStringWithoutLast(String origin) {
        return getStringWithoutLast(origin, ",");
    }

    /**
     * 去除最后一个符号后面的
     */
    public static String getStringWithoutLast(String origin, String symbol) {
        int position = origin.lastIndexOf(symbol);
        if (position > 0) {
            origin = origin.substring(0, position);
            return origin;
        }
        return "";
    }

    /**
     * 获取url的文件名
     *
     * @param urlStr
     * @return
     */
    public static String getUrlFileName(String urlStr) {
        urlStr = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
        return urlStr;
    }

    /**
     * 获取一个url的最后的文件名， 不带文件后缀名
     */
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
     * 获取真实存在的文件的后缀名
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            String filename = file.getName();
            if (filename.lastIndexOf(".") < 0) {
                return null;
            }
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 获取文件名(不包括后缀名)
     *
     * @param file
     * @return
     */
    public static String getFileNameNoSuffix(File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            String filename = file.getName();
            if (filename.lastIndexOf(".") < 0) {
                return filename;
            }
            return filename.substring(0, filename.lastIndexOf("."));
        }
        return null;
    }

    /**
     * 获取文件大小,单位字节
     *
     * @param filePath 传入文件的绝对路径
     * @return
     */
    public static long getFileSize(String filePath) {
        long size = 0;
        File file = new File(filePath);
        if (file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 换算文件大小含单位
     *
     * @param size
     * @return
     */
    public static String getFileSizeByUnit(long size) {
        if (size <= 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "未知大小";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 获取两数相除的百分比
     */
    public static String getPercentText(double progress, double totalLenght) {
        DecimalFormat format = new DecimalFormat("###.00");
        double resultSize = (double) (progress * 100 / totalLenght);
        String reusltText = format.format(resultSize);
        return reusltText + "%";
    }

    /**
     * 显示下载数量
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
            return URLEncoder.encode(str, XCConfig.ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String decodeURL(String str) {
        try {
            return URLDecoder.decode(str, XCConfig.ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否是邮件
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        return emailer.matcher(email).matches();
    }

    /**
     * 检测输入的邮箱是否符合要求.
     */
    public static boolean validateEmail(String number) {
        Pattern p = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 是否是电话号码
     */
    public static boolean isPhoneNum(String num) {
        if (num != null && num.length() == 11) {
            // if (num.matches("1[34568]\\d{9}")) {
            // 访问网络获取验证码
            return true;
            // }
        }
        return false;
    }

    /**
     * 判断固定电话
     */
    public static boolean isFixPhoneNumber(String gnumber) {
        Pattern pattern = Pattern
                .compile("0\\d{2,3}(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{8}");
        Matcher matcher = pattern.matcher(gnumber);
        return matcher.matches();
    }

    /**
     * 该方法只判断银行卡是否为16-19位
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

//    public String discount(String price_orign, String price_sell, int keep_dot) {
//
//        double discount_num = Double.parseDouble(price_sell) / Double.parseDouble(price_orign);
//        discount_num = discount_num * Math.pow(10, keep_dot + 2);// 显示如6.22则,但是6.226可以进一位
//        String discount_text = discount_num + "";
//        char c = discount_text.charAt(keep_dot + 2);
//        // 四舍五入
//        if (Integer.parseInt(c + "") >= 5) {
//            discount_num = (discount_num + 10.0) / Math.pow(10, keep_dot + 1);
//        } else {
//            discount_num = discount_num / Math.pow(10, keep_dot + 1);
//        }
//        if ((discount_num + "").length() < 4) {
//            discount_text = discount_num + "0";
//        } else {
//            discount_text = discount_num + "";
//        }
//        return discount_text.substring(0, keep_dot + 2);
//    }


    /**
     * 给String高亮显示 颜色的格式 #184DA3
     */
    public static void setLightString(String str, TextView textview, String color) {

        // 实体对象值显示在控件上
        SpannableString hightlight = new SpannableString(str);
        // 高亮器
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
        hightlight.setSpan(span, 0, (str).length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        textview.setText(hightlight);

    }

    /**
     * 指定 位置和 颜色   ，颜色的格式  #184DA3
     */
    public static void setLightString(String str, TextView textview, int start, int end, String color) {
        // 实体对象值显示在控件上
        SpannableString hightlight = new SpannableString(str);
        // 高亮器
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
        hightlight.setSpan(span, start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        textview.setText(hightlight);
    }

    /**
     * 颜色的格式  "#184DA3"
     */
    public static void setLightAppendString(String str, TextView textview, String color) {
        // 实体对象值显示在控件上
        SpannableString hightlight = new SpannableString(str);
        // 高亮器
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
        hightlight.setSpan(span, str.indexOf("：") + 1, (str).length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.append(hightlight);
        textview.append(XCIO.LINE_SEPARATOR);
    }

    /**
     * 设置textview的文本的尺寸
     */
    public static void setSizeAppendString(String str, TextView textview, int size) {
        // 实体对象值显示在控件上
        SpannableString sizespan = new SpannableString(str);
        // 字体大小
        AbsoluteSizeSpan size_span = new AbsoluteSizeSpan(size);
        sizespan.setSpan(size_span, 0, (str).length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.append(sizespan);
        textview.append(XCIO.LINE_SEPARATOR);
    }

    /**
     * 在String上添加删除线
     */
    public static void setDeleteString(String str, TextView textview) {
        // 删除线
        SpannableString deleteLine = new SpannableString(str);
        StrikethroughSpan delete = new StrikethroughSpan();
        deleteLine.setSpan(delete, 0, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        textview.setText(deleteLine);
    }

    /**
     * 在String上添加删除线
     */
    public static void setDeleteString(TextView textview) {
        textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * 设置第一个字母为大写
     */
    public static String setFirstLetterBig(String origin) {

        if (isBlank(origin)) {
            return origin;
        }

        char[] chars = origin.toCharArray();

        if (chars[0] >= 97) {
            chars[0] = (char) (chars[0] - 32);
        }

        return new String(chars);

    }

//    public static void sizeLightSpanAppend(String origin_string, TextView textview) {
//        String[] strs = origin_string.split("\n");
//        int i = 0;
//        for (String str : strs) {
//            if (i == 0) {
//                i = 1;
//                setSizeAppendString(str, textview, 20);
//                continue;
//            }
//            if (str.indexOf("：") > 0) {
//                setLightAppendString(str, textview, "#184DA3");
//            } else {
//                textview.append(str + XCIO.LINE_SEPARATOR);
//            }
//        }
//    }


    /**
     * 加密手机号码
     *
     * @return 加密处理后的手机号码（当手机号码长度不是11位时，不进行加密处理）
     */
    public static String encrypPhoneNumber(String number) {
        String newnumber = number;
        if (number.trim().length() == 11) {
            newnumber = number.substring(0, 3) + "****" + number.substring(number.length() - 4, number.length());
        } else {
            newnumber = number;
        }
        return newnumber;
    }

    /**
     * 加密用户名
     *
     * @return 加密处理后的用户名（当用户名是一个字符时，不进行加密处理）
     */
    public static String encrypName(String name) {
        String newName = name;
        if (null != name && name.trim().length() > 1) {
            if (name.length() == 2) {
                newName = "*" + name.substring(name.length() - 1, name.length());
            } else {
                newName = "*" + name.substring(name.length() - 2, name.length());
            }
        }
        return newName;
    }

    /**
     * 加密邮箱
     */
    public static String encrypEmail(String email) {

        String newemail = "";
        if (email == null || "".equals(email.trim())) {
            return newemail;
        }
        String[] subemail = email.split("@");
        if (subemail[0].length() <= 3) {
//    		newemail = subemail[0].substring(0)  + "***" + "@" + subemail[1];
            newemail = subemail[0].substring(0, 1) + "***" + "@" + subemail[1];
        } else {
            newemail = subemail[0].substring(0, subemail[0].length() - 3) + "***" + "@" + subemail[1];
        }
        return newemail;
    }

    /**
     * 加密其它证件号
     */
    public static String encrypCard(String cardnum) {
        String newcardnum = "";
        if (cardnum == null || "".equals(cardnum.trim())) {
            return newcardnum;
        }
        if (cardnum.length() >= 12) {
            newcardnum = cardnum.substring(0, cardnum.length() - 11) + "********" + cardnum.substring(cardnum.length() - 3, cardnum.length());
        } else {
            newcardnum = cardnum.substring(0, 1) + "********" + cardnum.substring(cardnum.length() - 3, cardnum.length());
        }
        return newcardnum;
    }

    /**
     * 加密身份证(仅显示后四位)
     */
    public static String encrypAuthenCard(String authencardnum) {
        String newauthencardnum = "";
        if (authencardnum == null || "".equals(authencardnum.trim())) {
            return newauthencardnum;
        }
        if (authencardnum.length() == 18) {
            newauthencardnum = "******" + "********" + authencardnum.substring(authencardnum.length() - 4, authencardnum.length());
        } else if (authencardnum.length() == 15) {
            newauthencardnum = "******" + "*****" + authencardnum.substring(authencardnum.length() - 4, authencardnum.length());
        }
        return newauthencardnum;
    }

    /**
     * 加密身份证
     */
    public static String encrypAuthenCardV2(String authencardnum) {
        String newauthencardnum = "";
        if (authencardnum == null || "".equals(authencardnum.trim())) {
            return newauthencardnum;
        }
        if (authencardnum.length() == 18) {
            newauthencardnum = authencardnum.substring(0, 6) + "**********" + authencardnum.substring(authencardnum.length() - 2, authencardnum.length());
        } else if (authencardnum.length() == 15) {
            newauthencardnum = authencardnum.substring(0, 6) + "********" + authencardnum.substring(authencardnum.length() - 1, authencardnum.length());
        }
        return newauthencardnum;
    }

}
