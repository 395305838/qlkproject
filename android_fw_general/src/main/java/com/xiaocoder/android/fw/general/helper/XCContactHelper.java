package com.xiaocoder.android.fw.general.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xiaocoder.android.fw.general.model.XCContactModel;

/**
 * 获取通讯录
 * @author xiaocoder
 */
public class XCContactHelper {
	private Context context;

	public XCContactHelper(Context context) {
		this.context = context;
	}

	public List<XCContactModel> getContacts() {
		// 创建一个保存联系人的集合
		List<XCContactModel> contact_list = new ArrayList<XCContactModel>();
		ContentResolver resolver = context.getContentResolver();
		// raw_contact 表的uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		// data 表的uri
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			if (id != null) {
				Cursor dataCursor = resolver.query(dataUri, new String[] {
						"data1", "mimetype" }, "raw_contact_id=?", //
						new String[] { id }, null);
				XCContactModel contact_model = new XCContactModel();
				while (dataCursor.moveToNext()) {
					String data = dataCursor.getString(dataCursor
							.getColumnIndex("data1"));
					String mimetype = dataCursor.getString(dataCursor
							.getColumnIndex("mimetype"));
					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						contact_model.name = data;
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						contact_model.phone_number = data;
					} else if ("vnd.android.cursor.item/email_v2"
							.equals(mimetype)) {
						contact_model.email = data;
					}
				}
				contact_list.add(contact_model);
				dataCursor.close();
			}
		}
		cursor.close();
		return contact_list;
	}
}
