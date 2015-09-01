package com.xiaocoder.buffer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xiaocoder.android.fw.general.db.XCDbHelper;

/**
 * Created by xiaocoder on 2015/8/30.
 * <p/>
 * 可能需要处理各数据库升级相关的东东
 */

public class QlkDbHelper extends XCDbHelper {

    public QlkDbHelper(Context context, String dbName, int version, String[] sqls) {
        super(context, dbName, version, sqls);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // db.execSQL("drop table if exists " + );
        // onCreate(db);
    }

}
