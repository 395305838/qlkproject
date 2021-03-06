package com.xiaocoder.middle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xiaocoder.android.fw.general.db.XCDbHelper;
import com.xiaocoder.android.fw.general.db.XCSearchDao;

/**
 * Created by xiaocoder on 2015/8/30.
 * <p/>
 * 可能需要处理各数据库升级相关的东东
 */

public class MDbHelper extends XCDbHelper {

    /**
     * 搜索记录的数据库名字
     */
    public static String DB_SEARCH_RECODER = MConfig.APP_ROOT + "_search_recoder.db";
    /**
     * 搜索记录db的版本号
     */
    public static int DB_VERSION_SEARCH_RECODER = 1;
    /**
     * 搜索记录db中的表，不同的搜索界面各创建一个表
     */
    public static String DB_TABLE_SEARCH_RECODER_1 = "search_recoder1";
    public static String DB_TABLE_SEARCH_RECODER_2 = "search_recoder2";
    public static String DB_TABLE_SEARCH_RECODER_3 = "search_recoder3";
    /**
     * 搜索记录db的sql
     */
    public static String DB_SQL_SEARCH_RECODER_1 = "CREATE TABLE " + DB_TABLE_SEARCH_RECODER_1 + "(_id integer primary key autoincrement," + XCSearchDao.TIME + " text," + XCSearchDao.KEY_WORD + " text)";
    public static String DB_SQL_SEARCH_RECODER_2 = "CREATE TABLE " + DB_TABLE_SEARCH_RECODER_2 + "(_id integer primary key autoincrement," + XCSearchDao.TIME + " text," + XCSearchDao.KEY_WORD + " text)";
    public static String DB_SQL_SEARCH_RECODER_3 = "CREATE TABLE " + DB_TABLE_SEARCH_RECODER_3 + "(_id integer primary key autoincrement," + XCSearchDao.TIME + " text," + XCSearchDao.KEY_WORD + " text)";


    public MDbHelper(Context context, String dbName, int version, String[] sqls) {
        super(context, dbName, version, sqls);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
