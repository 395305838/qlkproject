package com.xiaocoder.android.fw.general.media.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.xiaocoder.android.fw.general.model.XCVideoModel;

//获取视频dao
public class XCVideoDao {

	public ArrayList<XCVideoModel> getList(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);
		ArrayList<XCVideoModel> list = new ArrayList<XCVideoModel>();
		XCVideoModel bean = null;
		int index = 1;
		Cursor cursorThubnail;
		while (cursor.moveToNext()) {
			bean = new XCVideoModel();
			bean.index = index;
			bean._ID = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Video.Media._ID));
			bean.url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DATA));
			bean.displayname = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
			bean.length = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Video.Media.SIZE));
			bean.MIME_TYPE = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
			bean.DURATION = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Video.Media.DURATION));
			// 查找对应视频的缩略表
			cursorThubnail = resolver.query(
					MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, null,
					" video_id=?", new String[] { bean._ID + "" }, null);
			if (cursorThubnail.moveToNext()) {
				bean.thumbnail = cursorThubnail.getString(cursorThubnail
						.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
			}
			cursorThubnail.close();
			list.add(bean);
			index++;
		}
		cursor.close();
		return list;
	}
}
