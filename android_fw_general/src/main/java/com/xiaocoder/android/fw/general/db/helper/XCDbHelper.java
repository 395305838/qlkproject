package com.xiaocoder.android.fw.general.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaocoder.android.fw.general.util.UtilString;

// 搜索记录的数据库helper文件
public class XCDbHelper extends SQLiteOpenHelper {

    public String[] mSqls;

    // 如 dbName = "qlk_wyd_.db" ,  version = 1 ， 该方法传入的值，在中间层的Config中找
    public XCDbHelper(Context context, String dbName, int version, String[] sqls) {
        super(context, dbName, null, version);
        if (UtilString.isBlank(dbName)) {
            throw new RuntimeException("数据库名不能为空");
        }

        if (sqls == null || sqls.length < 1) {
            throw new RuntimeException("sql不能为空");
        }
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
