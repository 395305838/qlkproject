package com.xiaocoder.android.fw.general.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.xiaocoder.android.fw.general.model.XCAudioModel;
import com.xiaocoder.android.fw.general.model.XCImageModel;
import com.xiaocoder.android.fw.general.model.XCVideoModel;

import java.util.ArrayList;

/**
 * Created by xiaocoder on 2015/9/17.
 * <p/>
 * 查询手机中的多媒体资源 （音频 视频 图片）
 */
public class UtilMedia {

    public static ArrayList<XCAudioModel> getAudioList(Context context) {
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


    public static ArrayList<XCImageModel> getImageList(Context context) {
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

            thumbCursor = resolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Thumbnails.IMAGE_ID + "=?", new String[]{bean._ID + ""}, null);

            if (thumbCursor.moveToNext()) {
                bean.thumbnail = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            }
            thumbCursor.close();
            list.add(bean);
            index++;
        }

        return list;
    }

    public static ArrayList<XCVideoModel> getVideoList(Context context) {
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
                    " video_id=?", new String[]{bean._ID + ""}, null);
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
