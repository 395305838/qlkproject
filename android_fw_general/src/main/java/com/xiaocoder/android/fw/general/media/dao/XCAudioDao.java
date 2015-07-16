package com.xiaocoder.android.fw.general.media.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.xiaocoder.android.fw.general.media.IMediaProvider;
import com.xiaocoder.android.fw.general.model.XCAudioModel;

//获取音乐文件的dao
public class XCAudioDao implements IMediaProvider<XCAudioModel> {
	@Override
	public ArrayList<XCAudioModel> getList(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);
		ArrayList<XCAudioModel> list = new ArrayList<XCAudioModel>();
		XCAudioModel bean = null;
		int index = 1;
		while (cursor.moveToNext()) {
			bean = new XCAudioModel();
			bean.index = index;
			bean._ID = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));
			bean.url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			bean.displayname = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			bean.length = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE));
			bean.MIME_TYPE = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
			bean.DURATION = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));
			list.add(bean);
			index++;
		}
		return list;
	}
}
