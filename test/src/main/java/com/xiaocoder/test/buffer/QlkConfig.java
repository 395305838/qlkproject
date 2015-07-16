package com.xiaocoder.test.buffer;

public class QlkConfig {
    /*
     * url
     */
    public static final String DOMAIN = "api.123.cn";
    public static final String PORT = "";
    public static final String VERSION = "/app/v1.0";
    public static final String SERVER_IP = "http://" + DOMAIN + VERSION;
    /*
     * 编码格式
     */
    public static String ENCODING = "utf-8";
    /*
     * 是否打印日志到控制台
     */
    public static boolean IS_OUTPUT = true;
    /*
     * 是否弹出调试的土司
     */
    public static boolean IS_DTOAST = false;
    /*
     * 是否打印日志到手机文件中
     */
    public static boolean IS_PRINTLOG = false;
    /*
     * app的名字与根目录
     */
    public static String APP_ROOT = "app_qlk_test";
    /*
     * 打印到日志文件
     */
    public static String LOG_FILE = APP_ROOT + "/log";
    /*
     * crash日志文件
     */
    public static String CRASH_FILE = APP_ROOT + "/crash";
    /*
     * chat
     */
    public static String CHAT_FILE = APP_ROOT + "/chat";

    // chat_photo
    public static String CHAT_PHOTO_FILE = CHAT_FILE + "/photo";
    // chat_video
    public static String CHAT_VIDEO_FILE = CHAT_FILE + "/voice";
    // chat_moive
    public static String CHAT_MOIVE_FILE = CHAT_FILE + "/moive";

    /*
     * 缓存目录的路径
     */
    public static String CACHE_DIRECTORY = APP_ROOT + "/cache";
    /*
     * sp文件 , 仅文件名
     */
    public static String SP_SETTING = APP_ROOT + "_setting";
    /*
     * 打印测试的文件
     */
    public static String TEMP_PRINT_FILE = APP_ROOT + "/temp_print_file";


}
