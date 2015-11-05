package com.xiaocoder.android.fw.general.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.model.XCSearchRecordModel;
import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个dao的sample，这个dao有保存搜索记录的字段
 */
public class XCSearchDao {

    private XCDbHelper mHelper;
    private String mTabName;

    public static String KEY_WORD = "keyword";
    public static String TIME = "time";
    public static String _ID = "_id";

    public static String SORT_DESC = " DESC";
    public static String SORT_ASC = " ASC";


    public XCSearchDao(Context context, String tabName, Class<? extends XCDbHelper> dbHelper, String dbName,
                       int version, String[] sqls) {

        if (UtilString.isBlank(tabName)) {
            throw new RuntimeException(this + "--new dao时，数据库表名不能为空");
        } else {
            mTabName = tabName;
        }

        mHelper = XCDbHelper.instanceHelper(context, dbHelper, dbName, version, sqls);

    }

    @NonNull
    private ContentValues createContentValue(XCSearchRecordModel bean) {
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, bean.getKey_word());
        values.put(TIME, bean.getTime());
        return values;
    }

    @NonNull
    private XCSearchRecordModel createModel(Cursor c) {
        return new XCSearchRecordModel(
                c.getString(c.getColumnIndex(KEY_WORD)),
                c.getString(c.getColumnIndex(TIME)));
    }

    public void insert(XCSearchRecordModel bean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = createContentValue(bean);
        long id = db.insert(mTabName, _ID, values);
        XCApp.i(XCConfig.TAG_DB, "插入的记录的id是: " + id);
        db.close();
    }

    public int delete_unique(String time) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.delete(mTabName, TIME + "=?",
                new String[]{time + ""});
        XCApp.i(XCConfig.TAG_DB, "delete_unique-->" + rows + "行");
        db.close();
        return rows;
    }

    public int delete(String keyword) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.delete(mTabName, KEY_WORD + "=?",
                new String[]{keyword + ""});
        XCApp.i(XCConfig.TAG_DB, "delete-->" + rows + "行");
        db.close();
        return rows;
    }

    public int deleteAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int raw = db.delete(mTabName, null, null);
        db.close();
        return raw;
    }


    public int update(XCSearchRecordModel bean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = createContentValue(bean);
        int rows = db.update(mTabName, values, TIME + "=?",
                new String[]{bean.getTime() + ""});
        XCApp.i(XCConfig.TAG_DB, "更新了" + rows + "行");
        db.close();
        return rows;
    }

    public List<XCSearchRecordModel> queryAll(String sort) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, null, null, null, null,
                _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    public List<XCSearchRecordModel> query(String keyword, String sort) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, KEY_WORD + "=?",
                new String[]{keyword + ""}, null, null, _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    public int queryCount() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, new String[]{"COUNT(*)"},
                null, null, null, null, null);
        c.moveToNext();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count;
    }

    public List<XCSearchRecordModel> queryPage(int pageNum, int capacity) {
        String offset = (pageNum - 1) * capacity + ""; // 偏移量
        String len = capacity + ""; // 个数
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, null, null, null, null,
                null, offset + "," + len);
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    // public void remit(int from, int to, int amount) {
    // SQLiteDatabase qlk_db = helper.getWritableDatabase();
    // try {
    // qlk_db.beginTransaction(); // 开始事务
    // qlk_db.execSQL("UPDATE person SET balance=balance-? WHERE id=?", new
    // Object[] { amount, from });
    // qlk_db.execSQL("UPDATE person SET balance=balance+? WHERE id=?", new
    // Object[] { amount, to });
    // qlk_db.setTransactionSuccessful(); // 事务结束时, 成功点之前的操作会被提交
    // } finally {
    // qlk_db.endTransaction(); // 结束事务, 将成功点之前的操作提交
    // qlk_db.close();
    // }
    // }


}
