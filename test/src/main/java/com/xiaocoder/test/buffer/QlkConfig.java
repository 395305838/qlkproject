package com.xiaocoder.test.buffer;

import com.xiaocoder.android.fw.general.application.XCConfig;

/*
 * 改类配置开发环境  ， 调试开关  ，路径  ，url等
 */
public class QlkConfig extends XCConfig {

    /*
     * 当前的运行环境
     */
    public static RunEnvironment CURRENT_RUN_ENVIRONMENT = RunEnvironment.DEV;
    /*
     * url
     */
    private static String ONLINE_HOST = "online.123.cn";
    private static String ONLINE_PORT = "12345";
    private static String ONLINE_ADDR = ONLINE_HOST + ":" + ONLINE_PORT;

    private static String TEST_HOST = "test.123.cn";
    private static String TEST_PORT = "12345";
    private static String TEST_ADDR = TEST_HOST + ":" + TEST_PORT;

    private static String DEV_HOST = "dev.123.cn";
    private static String DEV_PORT = "12345";
    private static String DEV_ADDR = DEV_HOST + ":" + DEV_PORT;


    public static String getUrl(String key) {

        if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.ONLINE) {

            return ONLINE_ADDR + key;

        } else if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.TEST) {

            return TEST_ADDR + key;

        } else if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.DEV) {

            return DEV_ADDR + key;

        } else {

            throw new RuntimeException("XCConfig中没有找到匹配的url");
        }
    }


    // ----------------------------------补充接口url---------------------------------------


    // ----------------------------------补充接口url---------------------------------------


    /*
     * 是否打印日志到控制台
     */
    public static boolean IS_OUTPUT = true;
    /*
     * 是否弹出调试的土司
     */
    public static boolean IS_DTOAST = true;
    /*
     * 是否打印日志到手机文件中
     */
    public static boolean IS_PRINTLOG = true;
    /*
     * 是否打印异常的日志到屏幕， 上线前得关
     */
    public static boolean IS_SHOW_EXCEPTION_ACTIVITY = true;

    /*
     * app的名字与根目录
     */
    public static String APP_ROOT = "app_qlk_test";

    /*
     * 数据库的名字
     */
    public static String DB_NAME = APP_ROOT + ".db";
    /*
     * 数据库版本号
     */
    public static int DB_VERSION = 1;
    /*
     * 数据库表名,支持创建多个表
     */
    public static String DB_TABLE_NAME_SEARCH_1 = "search_1";
    public static String DB_TABLE_NAME_SEARCH_2 = "search_2";
    public static String DB_TABLE_NAME_SEARCH_3 = "search_3";
    /*
     * SQL
     */
    public static String DB_TABLE_SQL_SEARCH_1 = "CREATE TABLE " + DB_TABLE_NAME_SEARCH_1 + "(_id integer primary key autoincrement," + "time text," + "keyword text)";
    public static String DB_TABLE_SQL_SEARCH_2 = "CREATE TABLE " + DB_TABLE_NAME_SEARCH_2 + "(_id integer primary key autoincrement," + "time text," + "keyword text)";
    public static String DB_TABLE_SQL_SEARCH_3 = "CREATE TABLE " + DB_TABLE_NAME_SEARCH_3 + "(_id integer primary key autoincrement," + "time text," + "keyword text)";
    /*
     * 打印到日志文件printe
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
     * sp文件 , 仅文件名
     */
    public static String SP_SETTING = APP_ROOT + "_setting";
    /*
     * 打印测试的文件
     */
    public static String TEMP_PRINT_FILE = APP_ROOT + "/temp_print_file";


}