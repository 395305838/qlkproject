package com.xiaocoder.android.fw.general.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.util.UtilString;

import java.lang.reflect.Constructor;

/**
 * onUpgrade方法空实现的
 */
public class XCDbHelper extends SQLiteOpenHelper {

    public String[] mSqls;
    public String mDbName;
    public int mVersion;

    /**
     *
     * @param context
     * @param dbName "**.db"
     * @param version 1
     * @param sqls
     */
    public XCDbHelper(Context context, String dbName, int version, String[] sqls) {
        super(context, dbName, null, version);
        if (UtilString.isBlank(dbName)) {
            throw new RuntimeException("数据库名不能为空");
        }

        if (sqls == null || sqls.length < 1) {
            throw new RuntimeException("sql不能为空");
        }
        mDbName = dbName;
        mVersion = version;
        mSqls = sqls;
    }

    // db.execSQL("CREATE TABLE search_record_info(_id integer primary key autoincrement," + "time text," + "keyword text)");
    @Override
    public void onCreate(SQLiteDatabase db) {

        for (String sql : mSqls) {
            db.execSQL(sql);
        }

    }

    // db.execSQL("drop table if exists " + );
    // onCreate(db);
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //  子类中实现，如数据库升级的情况
    }


    public static XCDbHelper instanceHelper(Context context, Class<? extends XCDbHelper> dbHelper, String dbName,
                                            int version, String[] sqls) {
        try {
            XCApp.i(XCConfig.TAG_DB, "dbHelper----instanceHelper()");
            Constructor constructor = dbHelper.getConstructor(Context.class, String.class, int.class, String[].class);
            Object o = constructor.newInstance(context, dbName, version, sqls);
            XCApp.i(XCConfig.TAG_DB, o);
            return (XCDbHelper) o;
        } catch (Exception e) {
            e.printStackTrace();
            XCApp.e(context, "", e);
            return null;
        }
    }

}
