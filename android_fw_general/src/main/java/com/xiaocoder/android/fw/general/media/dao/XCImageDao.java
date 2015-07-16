package com.xiaocoder.android.fw.general.media.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.xiaocoder.android.fw.general.media.IMediaProvider;
import com.xiaocoder.android.fw.general.model.XCImageModel;

//获取本地图片的dao
public class XCImageDao implements IMediaProvider<XCImageModel> {

	@Override
	public ArrayList<XCImageModel> getList(Context context) {
		ContentResolver resolver = context.getContentResolver();

		Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		ArrayList<XCImageModel> list = new ArrayList<XCImageModel>();

		XCImageModel bean = null;
		int index = 1;
		Cursor thumbCursor;
		while (cursor.moveToNext()) {
			bean = new XCImageModel();
			bean.index = index;
			bean._ID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
			bean.url = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

			bean.displayname = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
			bean.length = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));

			bean.MIME_TYPE = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
			bean.MINI_THUMB_MAGIC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC));

			thumbCursor = resolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Thumbnails.IMAGE_ID + "=?", new String[] { bean._ID + "" }, null);

			if (thumbCursor.moveToNext()) {
				bean.thumbnail = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
			}
			thumbCursor.close();
			list.add(bean);
			index++;
		}

		return list;
	}

}
