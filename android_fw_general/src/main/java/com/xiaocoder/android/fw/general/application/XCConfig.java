package com.xiaocoder.android.fw.general.application;

public class XCConfig {
    /**
     * 不要用system.out输出，用i(),i方法里默认的是该tag
     */
    public static String TAG_SYSTEM_OUT = "System.out";
    /**
     * 可查看如url 参数 返回的json等
     */
    public static String TAG_HTTP = "http";
    /**
     * 可查看handler里每个方法的调用顺序
     */
    public static String TAG_HTTP_HANDLER = "httpHandler";
    /**
     * DB
     */
    public static String TAG_DB = "db";
    /**
     * 该tag可以查看json返回的字段的数据类型
     */
    public static String TAG_JSON_TYPE = "JsonType";
    /**
     * 列出json返回的字段，并以假bean的形式打印到控制台，复制粘贴后可创建一个假bean类，假bean类里有字符串常量和get方法set方法
     * <p/>
     * 注：假bean类不在用了， 现在用studio的gsonformat插件，自动生成一个真model
     */
    public static String TAG_JSON_BEAN = "JsonBean";
    /**
     * 如异常、重要的日志等用该tag，e()方法里就是用了该tag
     */
    public static String TAG_ALOG = "alog";
    /**
     * 以下两个是临时测试查看的tag
     */
    public static String TAG_TEMP = "temp";
    public static String TAG_TEST = "test";


    public static String RMB = "¥";

    /**
     * 编码格式
     */
    public static String ENCODING_UTF8 = "utf-8";

    /**
     * 编码格式
     */
    public static String ENCODING_GBK = "gbk";

}
