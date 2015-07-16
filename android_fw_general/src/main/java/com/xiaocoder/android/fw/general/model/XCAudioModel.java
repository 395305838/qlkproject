package com.xiaocoder.android.fw.general.model;

import java.io.Serializable;

public class XCAudioModel implements Serializable {

	private static final long serialVersionUID = 8288878050393530019L;
	
	public String displayname;
	public long length;
	public String url;
	public int index = 0;
	public int _ID;
	public String MIME_TYPE;
	public long DURATION;

	@Override
	public String toString() {
		return "SongMessage [displayname=" + displayname + ", length=" + length
				+ ", url=" + url + ", index=" + index + ", isMenuShow="
				+ ", _ID=" + _ID + ", MIME_TYPE=" + MIME_TYPE + ", DURATION="
				+ DURATION + "]";
	}
}
