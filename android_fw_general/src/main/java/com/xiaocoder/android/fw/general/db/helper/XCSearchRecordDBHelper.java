package com.xiaocoder.android.fw.general.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 搜索记录的数据库helper文件
public class XCSearchRecordDBHelper extends SQLiteOpenHelper {
	private static XCSearchRecordDBHelper helper;
    public static String search_record_info = "search_record_info";
    public static String search_record_info1 = "search_record_info1";
    public static String search_record_info2 = "search_record_info2";
    public static String search_record_info3 = "search_record_info3";

	private XCSearchRecordDBHelper(Context context) {
		super(context, "qlk_wyd_search_record.db", null, 1);

	}

	public static XCSearchRecordDBHelper getInstance(Context context) {
		if (helper == null) {
			synchronized (XCSearchRecordDBHelper.class) {
				if (helper == null) {
					helper = new XCSearchRecordDBHelper(context);
				}
			}
		}
		return helper;
	}

	// price integer
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE search_record_info(_id integer primary key autoincrement," + "time text," + "keyword text)");
		db.execSQL("CREATE TABLE search_record_info1(_id integer primary key autoincrement," + "time text," + "keyword text)");
		db.execSQL("CREATE TABLE search_record_info2(_id integer primary key autoincrement," + "time text," + "keyword text)");
		db.execSQL("CREATE TABLE search_record_info3(_id integer primary key autoincrement," + "time text," + "keyword text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
