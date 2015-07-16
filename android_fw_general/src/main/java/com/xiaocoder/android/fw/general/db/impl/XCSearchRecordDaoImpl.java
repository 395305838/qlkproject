package com.xiaocoder.android.fw.general.db.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseConfig;
import com.xiaocoder.android.fw.general.db.XCIDao;
import com.xiaocoder.android.fw.general.db.helper.XCSearchRecordDBHelper;
import com.xiaocoder.android.fw.general.model.XCSearchRecordModel;
import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.ArrayList;
import java.util.List;

public class XCSearchRecordDaoImpl implements XCIDao<XCSearchRecordModel> {
	private XCSearchRecordDBHelper helper;
	public SQLiteDatabase db;
    public String tabName;

	public XCSearchRecordDaoImpl(Context context) {
		helper = XCSearchRecordDBHelper.getInstance(context);
		db = helper.getWritableDatabase();
        tabName = XCSearchRecordDBHelper.search_record_info;
	}
    public XCSearchRecordDaoImpl(Context context,String tabName) {
        helper = XCSearchRecordDBHelper.getInstance(context);
        db = helper.getWritableDatabase();
        if(UtilString.isBlank(tabName)){
            this.tabName = XCSearchRecordDBHelper.search_record_info;
        }else{
            this.tabName = tabName;
        }
    }

	public void insert(XCSearchRecordModel bean) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("keyword", bean.getKey_word());
		values.put("time", bean.getTime());
		long id = db.insert(tabName, "_id", values);
		XCApplication.printi("插入的记录的id是: " + id);
		db.close();
	}

	public int delete_unique(String time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rows = db.delete(tabName, "time=?",
				new String[] { time + "" });
		XCApplication.printi(rows + "行");
		db.close();
		return rows;
	}

	public int update(XCSearchRecordModel bean, String time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("keyword", bean.getKey_word());
		values.put("time", bean.getTime());
		int rows = db.update(tabName, values, "time=?",
				new String[] { bean.getTime() + "" });
		XCApplication.printi("更新了" + rows + "行");
		db.close();
		return rows;
	}

	public List<XCSearchRecordModel> queryAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(tabName, null, null, null, null, null,
				"_id DESC"); // 条件为null可以查询所有,见api;ORDER
		List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
		while (c.moveToNext()) {
			XCSearchRecordModel bean = new XCSearchRecordModel(c.getString(c
					.getColumnIndex("keyword")), c.getString(c
					.getColumnIndex("time")));
			beans.add(bean);
		}
		c.close();
		db.close();
		return beans;
	}

	public int queryCount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(tabName, new String[] { "COUNT(*)" },
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
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(tabName, null, null, null, null, null,
				null, offset + "," + len);
		List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
		while (c.moveToNext()) {
			XCSearchRecordModel bean = new XCSearchRecordModel(c.getString(c
					.getColumnIndex("keyword")), c.getString(c
					.getColumnIndex("time")));
			beans.add(bean);
		}
		c.close();
		db.close();
		return beans;
	}

	//
	// // 事务这里还是用execSQL方便点
	// public void remit(int from, int to, int amount) {
	// SQLiteDatabase qlk_db = helper.getWritableDatabase();
	// try {
	// qlk_db.beginTransaction(); // 开始事务
	// qlk_db.execSQL("UPDATE person SET balance=balance-? WHERE id=?", new
	// Object[] { amount, from });
	// qlk_db.execSQL("UPDATE person SET balance=balance+? WHERE id=?", new
	// Object[] { amount, to });
	// qlk_db.setTransactionSuccessful(); // 设置成功点, 在事务结束时, 成功点之前的操作会被提交
	// } finally {
	// qlk_db.endTransaction(); // 结束事务, 将成功点之前的操作提交
	// qlk_db.close();
	// }
	// }

	@Override
	public int deleteAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		int raw = db.delete(tabName, null, null);
		db.close();
		return raw;
	}

	@Override
	public int delete(String keyword) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rows = db.delete(tabName, "keyword=?",
				new String[] { keyword + "" });
		XCApplication.printi("delete-->" + rows
				+ "  row");
		db.close();
		return rows;
	}

	@Override
	public XCSearchRecordModel query_unique(String time) {
		return null;
	}

	public List<XCSearchRecordModel> query(String keyword) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(tabName, null, "keyword=?",
				new String[] { keyword + "" }, null, null, "_id ASC"); // 条件为null可以查询所有,见api;ORDER
		List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
		while (c.moveToNext()) {
			XCSearchRecordModel bean = new XCSearchRecordModel(c.getString(c
					.getColumnIndex("keyword")), c.getString(c
					.getColumnIndex("time")));
			beans.add(bean);
		}
		c.close();
		db.close();
		return beans;
	}

}
