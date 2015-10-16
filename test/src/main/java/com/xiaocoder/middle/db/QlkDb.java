package com.xiaocoder.middle.db;

import com.xiaocoder.middle.QlkConfig;

/**
 * Created by xiaocoder on 2015/8/30.
 */
public class QlkDb {

    // 搜索记录的数据库
    public static String DB_SEARCH_RECODER = QlkConfig.APP_ROOT + "_search_recoder.db";
    // 搜索记录数据库的版本号
    public static int DB_VERSION_SEARCH_RECODER = 1;
    // 搜索记录数据中的表，不同的搜索界面各创建一个表
    public static String DB_TABLE_SEARCH_RECODER_1 = "search_recoder1";
    public static String DB_TABLE_SEARCH_RECODER_2 = "search_recoder2";
    public static String DB_TABLE_SEARCH_RECODER_3 = "search_recoder3";
    // 搜索记录数据库的sql
    public static String DB_SQL_SEARCH_RECODER_1 = "CREATE TABLE " + DB_TABLE_SEARCH_RECODER_1 + "(_id integer primary key autoincrement," + "time text," + "keyword text)";
    public static String DB_SQL_SEARCH_RECODER_2 = "CREATE TABLE " + DB_TABLE_SEARCH_RECODER_2 + "(_id integer primary key autoincrement," + "time text," + "keyword text)";
    public static String DB_SQL_SEARCH_RECODER_3 = "CREATE TABLE " + DB_TABLE_SEARCH_RECODER_3 + "(_id integer primary key autoincrement," + "time text," + "keyword text)";

}
