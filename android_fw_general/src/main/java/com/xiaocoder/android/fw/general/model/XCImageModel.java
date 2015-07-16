package com.xiaocoder.android.fw.general.model;

import java.io.Serializable;

//本地图片对象
public class XCImageModel implements Serializable {
	private static final long serialVersionUID = -6847612368978583756L;

	public String displayname;
	public long length;
	public String url;

	public int index = 0;

	public int _ID;
	public String MIME_TYPE;
	public String MINI_THUMB_MAGIC;
	public String thumbnail;

	@Override
	public String toString() {
		return "ImageMessage [displayname=" + displayname + ", length="
				+ length + ", url=" + url + ", index=" + index + ", _ID=" + _ID
				+ ", MIME_TYPE=" + MIME_TYPE + ", MINI_THUMB_MAGIC="
				+ MINI_THUMB_MAGIC + ", thumbnail=" + thumbnail + "]";
	}

}
