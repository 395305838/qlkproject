package com.xiaocoder.android.fw.general.model;

import java.io.Serializable;

/**
 * @author xiaocoder
 *  搜索记录的Model
 *  2014-12-9 下午7:13:53
 */
public class XCSearchRecordModel implements Serializable {

	private static final long serialVersionUID = 7806487539561624886L;
	
	String key_word;
	String time;

	@Override
	public String toString() {
		return "SearchRecordBean{" + "key_word='" + key_word + '\''
				+ ", time='" + time + '\'' + '}';
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public XCSearchRecordModel(String key_word, String time) {
		this.key_word = key_word;
		this.time = time;
	}

	public String getKey_word() {
		return key_word;
	}

	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}

}
