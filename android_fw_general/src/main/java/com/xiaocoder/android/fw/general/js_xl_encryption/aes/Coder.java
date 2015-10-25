package com.xiaocoder.android.fw.general.js_xl_encryption.aes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import android.util.Base64;

/**
 * 基础加密组件类
 * @author 徐金山
 * @version 1.0
 */
public abstract class Coder {
	public static final String KEY_MD5 = "MD5";

	/**
	 * BASE64解密
	 * @param data 源信息
	 * @return byte[] 解密后的内容
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static byte[] decryptBASE64(String data) throws UnsupportedEncodingException {
		return Base64.decode(data.getBytes("UTF-8"), Base64.DEFAULT);
	}

	/**
	 * BASE64加密
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] data) throws UnsupportedEncodingException {
		return new String(Base64.encode(data, Base64.DEFAULT), "UTF-8");
	}

	/**
	 * MD5加密
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();
	}
}
