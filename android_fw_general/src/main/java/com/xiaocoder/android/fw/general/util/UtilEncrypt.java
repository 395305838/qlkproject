package com.xiaocoder.android.fw.general.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 该类提供MD5加密 , AES加密解密,以及简单的加密解密
 */
public class UtilEncrypt {
	/**
	 * MD5加密32位的加密 (原始的MD5是16位的 ， 这里返回的是32位的)
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String MD5(String data) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data.getBytes());
			byte b[] = md5.digest();

			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	// -----------------------------------------------------------------------------------------------------------
	/**
	 * 简单的加密
	 * 
	 * @param str
	 * @return
	 */
	public static String easyEncrypt(String str) {
		char[] cstr = str.toCharArray();
		StringBuilder hex = new StringBuilder();
		for (char c : cstr) {
			hex.append((char) (c + 5));
		}
		return hex.toString();
	}

	/**
	 * 简单的解密
	 * 
	 * @param str
	 * @return
	 */
	public static String easyDecipher(String str) {
		char[] cstr = str.toCharArray();
		StringBuilder hex = new StringBuilder();
		for (char c : cstr) {
			hex.append((char) (c - 5));
		}
		return hex.toString();
	}

	// -------------------------------------------------------------------------------------------------------------
	// editor.putString("pwd", AESEncryptor.encrypt(SEED, pwd));//对用户密码加密
	// pwdEdt.setText(AESEncryptor.decrypt(SEED, sp.getString("pwd", null)));//解密
	/**
	 * AES加密
	 */
	public static String encrypt(String seed, String content) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, content.getBytes());
		return toHex(result);
	}

	/**
	 * AES解密
	 */
	public static String decrypt(String seed, String encrypted) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
}
