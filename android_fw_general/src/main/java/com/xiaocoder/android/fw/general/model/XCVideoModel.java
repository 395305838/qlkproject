package com.xiaocoder.android.fw.general.model;

import java.io.Serializable;

public class XCVideoModel implements Serializable {

	private static final long serialVersionUID = 1410038234462714175L;

	public String displayname;
	public long length;
	public String url;
	public int index = 0;
	public int _ID;
	public String MIME_TYPE;
	public long DURATION;
	public String thumbnail;

	@Override
	public String toString() {
		return "VideoMessage [displayname=" + displayname + ", length="
				+ length + ", url=" + url + ", index=" + index + ", _ID=" + _ID
				+ ", MIME_TYPE=" + MIME_TYPE + ", DURATION=" + DURATION
				+ ", thumbnail=" + thumbnail + "]";
	}

}
