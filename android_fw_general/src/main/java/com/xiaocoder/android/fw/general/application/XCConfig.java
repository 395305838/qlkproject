package com.xiaocoder.android.fw.general.application;

public class XCConfig {
    /*
     * TAG
     */
    public static String TAG_SYSTEM_OUT = "System.out";
    public static String TAG_HTTP = "http";
    public static String TAG_JSON_TYPE = "JsonType";
    public static String TAG_JSON_BEAN = "JsonBean";
    public static String TAG_ANDROID_RUNTIME = "AndroidRuntime";

    /*
     * 分别代表开发 测试  线上的环境
     */
    public enum RunEnvironment {
        DEV, TEST, ONLINE
    }


    public static String RMB = "¥";

    /*
    * 编码格式
    */
    public static String ENCODING_UTF8 = "utf-8";

    /*
   * 编码格式
   */
    public static String ENCODING_GBK = "gbk";

}
