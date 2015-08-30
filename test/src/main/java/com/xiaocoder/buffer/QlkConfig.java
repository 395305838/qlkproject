package com.xiaocoder.buffer;

/*
 * 该类配置开发环境  ， 调试开关  ，域名 ， 路径  ，url等
 */
public class QlkConfig {


    // ----------------------------------以下补充接口url---------------------------------------


    // ----------------------------------以上补充接口url---------------------------------------

    public enum RunEnvironment {
        DEV, TEST, ONLINE
    }

    public enum DebugControl {
        CLOSE, OPEN_DEFAULT, OPEN_DEFINE
    }

    /**
     * 当前的运行环境 , 上线前，改为ONLINE
     */
    public static RunEnvironment CURRENT_RUN_ENVIRONMENT = RunEnvironment.ONLINE;

    /**
     * 是否打开调试开关 , 上线前，改为CLOSE
     */
    public static DebugControl DEBUG_CONTROL = DebugControl.OPEN_DEFINE;

    static {
        if (DEBUG_CONTROL == DebugControl.CLOSE) {

            // 是否打印到控制台
            IS_OUTPUT = false;

            // 调试土司是否开启
            IS_DTOAST = false;

            // 是否初始化crashhandler
            IS_INIT_CRASH_HANDLER = false;

            // 是否打印出异常界面（只有在IS_INIT_CRASH_HANDLER 为true时，该设置才有效）
            IS_SHOW_EXCEPTION_ACTIVITY = false;

            // i()方法中的log是否打印到本地日志
            IS_PRINTLOG = false;

        } else if (DEBUG_CONTROL == DebugControl.OPEN_DEFAULT) {

            // 是否打印到控制台
            IS_OUTPUT = true;

            // 调试土司是否开启
            IS_DTOAST = true;

            // 是否初始化crashhandler
            IS_INIT_CRASH_HANDLER = true;

            // 是否打印出异常界面（只有在IS_INIT_CRASH_HANDLER 为true时，该设置才有效）
            IS_SHOW_EXCEPTION_ACTIVITY = true;

            // i()方法中的log是否打印到本地日志
            IS_PRINTLOG = true;

        } else if (DEBUG_CONTROL == DebugControl.OPEN_DEFINE) {

            // 是否打印到控制台
            IS_OUTPUT = true;

            // 调试土司是否开启
            IS_DTOAST = false;

            // 是否初始化crashhandler
            IS_INIT_CRASH_HANDLER = true;

            // 是否打印出异常界面（只有在IS_INIT_CRASH_HANDLER 为true时，该设置才有效）
            IS_SHOW_EXCEPTION_ACTIVITY = false;

            // i()方法中的log是否打印到本地日志
            IS_PRINTLOG = false;

        } else {
            throw new RuntimeException("QlkConfig的static代码块中没有找到与DEBUG_CONTROL匹配的枚举值");
        }
    }

    /*
     * 域名配置
     */
    public static String ONLINE_HOST = "online.123.cn";
    public static String ONLINE_PORT = "12345";
    public static String ONLINE_ADDR = ONLINE_HOST + ":" + ONLINE_PORT;

    public static String TEST_HOST = "test.123.cn";
    public static String TEST_PORT = "12345";
    public static String TEST_ADDR = TEST_HOST + ":" + TEST_PORT;

    public static String DEV_HOST = "dev.123.cn";
    public static String DEV_PORT = "12345";
    public static String DEV_ADDR = DEV_HOST + ":" + DEV_PORT;


    public static String getUrl(String key) {

        if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.ONLINE) {

            return ONLINE_ADDR + key;

        } else if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.TEST) {

            return TEST_ADDR + key;

        } else if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.DEV) {

            return DEV_ADDR + key;

        } else {

            throw new RuntimeException("QlkConfig中没有找到匹配的url");
        }
    }

    /*
     * app的根目录
     */
    public static String APP_ROOT = "app_test";
    /*
     * 打印到日志文件printe()
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
     * 图片加载缓存目录的路径
     */
    public static String CACHE_DIRECTORY = APP_ROOT + "/cache";
    /*
     * sp文件
     */
    public static String SP_SETTING = APP_ROOT + "_setting";
    /*
     * 打印测试的文件，有时候控制台可能打印json不全，比如json太长的时候， 可以调用tempPrint方法，打印到本地查看
     */
    public static String TEMP_PRINT_FILE = APP_ROOT + "/temp_print_file";
    /*
     * 是否打印日志到控制台
     */
    public static boolean IS_OUTPUT;
    /*
     * 是否弹出调试的土司
     */
    public static boolean IS_DTOAST;
    /*
     * 是否初始化crashHandler,上线前得关
     */
    public static boolean IS_INIT_CRASH_HANDLER;
    /*
     * 是否打印异常的日志到屏幕， 上线前得关
     */
    public static boolean IS_SHOW_EXCEPTION_ACTIVITY;
    /*
     * 是否打印日志到手机文件中,i()中的上线前全部关闭
     */
    public static boolean IS_PRINTLOG;


}