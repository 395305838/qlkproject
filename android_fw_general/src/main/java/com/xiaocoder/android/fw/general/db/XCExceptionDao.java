package com.xiaocoder.android.fw.general.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.model.XCExceptionModel;
import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存异常信息的dao
 */
public class XCExceptionDao {

    private XCDbHelper mHelper;
    private String mTabName;

    public static String INFO = "info";
    public static String EXCEPTION_TIME = "exceptionTime";
    public static String UPLOAD_SUCCESS = "uploadSuccess";
    public static String USER_ID = "userId";
    public static String UNIQUE_ID = "uniqueId";
    public static String _ID = "_id";

    public static String SORT_DESC = " DESC";// 有个空格
    public static String SORT_ASC = " ASC";// 有个空格

    public XCExceptionDao(Context context, String tabName, Class<? extends XCDbHelper> dbHelper, String dbName,
                          int version, String[] sqls) {

        if (UtilString.isBlank(tabName)) {
            throw new RuntimeException(this + "--new dao时，数据库表名不能为空");
        } else {
            mTabName = tabName;
        }

        mHelper = XCDbHelper.instanceHelper(context, dbHelper, dbName, version, sqls);
    }

    public ContentValues createContentValue(XCExceptionModel bean) {
        ContentValues values = new ContentValues();
        values.put(INFO, bean.getInfo());
        values.put(EXCEPTION_TIME, bean.getExceptionTime());
        values.put(UPLOAD_SUCCESS, bean.getUploadSuccess());
        values.put(USER_ID, bean.getUserId());
        values.put(UNIQUE_ID, bean.getUniqueId());
        return values;
    }

    public void insert(XCExceptionModel bean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = createContentValue(bean);
        long id = db.insert(mTabName, _ID, values);
        XCApp.i(XCConfig.TAG_DB, "插入的记录的id是: " + id);
        db.close();
    }

    /**
     * 删除一条
     */
    public int delete_unique(String unique) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.delete(mTabName, UNIQUE_ID + "=?",
                new String[]{unique + ""});
        XCApp.i(XCConfig.TAG_DB, "delete_unique-->" + rows + "行");
        db.close();
        return rows;
    }

    /**
     * 删除所有上传成功了的异常信息，“1”为上传成功，“0”为未上传或失败
     */
    public int delete_uploadSuccess() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.delete(mTabName, UPLOAD_SUCCESS + "=?",
                new String[]{XCExceptionModel.UPLOAD_YES});
        XCApp.i(XCConfig.TAG_DB, "delete_uploadSuccess-->" + rows + "行");
        db.close();
        return rows;
    }

    /**
     * 删除与该用户有关的所有异常
     */
    public int delete_userid(String userId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.delete(mTabName, USER_ID + "=?",
                new String[]{userId + ""});
        XCApp.i(XCConfig.TAG_DB, "delete_userid-->" + rows + "行");
        db.close();
        return rows;
    }

    public int deleteAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int raw = db.delete(mTabName, null, null);
        db.close();
        return raw;
    }

    public int update(XCExceptionModel bean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = createContentValue(bean);
        int rows = db.update(mTabName, values, UNIQUE_ID + "=?",
                new String[]{bean.getUniqueId() + ""});
        XCApp.i(XCConfig.TAG_DB, "更新了" + rows + "行");
        db.close();
        return rows;
    }

    public List<XCExceptionModel> queryAll(String sort) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, null, null, null, null,
                _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    @NonNull
    private XCExceptionModel createModel(Cursor c) {
        return new XCExceptionModel(
                c.getString(c.getColumnIndex(INFO)),
                c.getString(c.getColumnIndex(EXCEPTION_TIME)),
                c.getString(c.getColumnIndex(UPLOAD_SUCCESS)),
                c.getString(c.getColumnIndex(USER_ID)),
                c.getString(c.getColumnIndex(UNIQUE_ID))
        );
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

    /**
     * 查询所有上传失败了的
     *
     * @param sort
     * @return
     */
    public List<XCExceptionModel> queryUploadSuccess(String sort) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, UPLOAD_SUCCESS + "=?",
                new String[]{XCExceptionModel.UPLOAD_YES}, null, null, _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 查询所有上传失败了的
     *
     * @param sort
     * @return
     */
    public List<XCExceptionModel> queryUploadFail(String sort) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, UPLOAD_SUCCESS + "=?",
                new String[]{XCExceptionModel.UPLOAD_NO}, null, null, _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 查询指定一条数据
     *
     * @param unique
     * @return
     */
    public List<XCExceptionModel> queryUnique(String unique) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, UNIQUE_ID + "=?",
                new String[]{unique}, null, null, null); // 条件为null可以查询所有,见api;ORDER
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }
}
