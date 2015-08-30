package com.xiaocoder.buffer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaocoder.android.fw.general.util.UtilString;

/**
 * Created by xiaocoder on 2015/8/30.
 */

public class QlkDbHelper extends SQLiteOpenHelper {

    public String[] mSqls;
    public String mDbName;
    public int mVersion;

    // 如 dbName = "**.db" ,  version = 1
    public QlkDbHelper(Context context, String dbName, int version, String[] sqls) {
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

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // db.execSQL("drop table if exists " + );
        // onCreate(db);
    }

}
