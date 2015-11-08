package com.xiaocoder.android.fw.general.exception;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xiaocoder.android.fw.general.db.XCDbHelper;
import com.xiaocoder.android.fw.general.db.XCExceptionDao;

/**
 * Created by xiaocoder on 2015/11/5.
 */
public class XCExceptionDbHelper extends XCDbHelper {
    /**
     * dbname
     */
    public static String DB_NAME_EXCEPTION = "exception.db";
    /**
     * 版本
     */
    public static int DB_VERSION_EXCEPTION = 1;
    /**
     * 表名
     */
    public static String DB_TABLE_EXCEPTION = "exception_1";
    /**
     * sql
     */
    public static String DB_SQL_EXCEPTION =
            "CREATE TABLE " + DB_TABLE_EXCEPTION + "("
                    + XCExceptionDao._ID + " integer primary key autoincrement,"
                    + XCExceptionDao.INFO + " text,"
                    + XCExceptionDao.EXCEPTION_TIME + " text,"
                    + XCExceptionDao.UPLOAD_SUCCESS + " text,"
                    + XCExceptionDao.USER_ID + " text,"
                    + XCExceptionDao.UNIQUE_ID + " text)";

    public XCExceptionDbHelper(Context context, String dbName, int version, String[] sqls) {
        super(context, dbName, version, sqls);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        super.onUpgrade(sqLiteDatabase, i, i2);
    }
}
