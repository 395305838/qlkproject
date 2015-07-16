package com.xiaocoder.android.fw.general.media;

import java.util.ArrayList;

import android.content.Context;

// 获取Media文件
public interface IMediaProvider<T> {

	public abstract ArrayList<T> getList(Context context);

}