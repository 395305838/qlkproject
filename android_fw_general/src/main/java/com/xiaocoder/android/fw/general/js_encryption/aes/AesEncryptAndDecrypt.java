package com.xiaocoder.android.fw.general.js_encryption.aes;

/**
 * AES加密解密类
 * @author 徐金山
 * @version 1.0
 */
public class AesEncryptAndDecrypt {
	/** AES密钥 */
	public static final String SHARE_KEY = "6tXLbEHPpavPfD+FKhiZIA==";
	
	/**
	 * 加密处理
	 * @param requestStr 明文请求报文（明文字符串）
	 * @return String 密文请求报文（密文字符串）
	 */
	public static String encodeRequestStr(String requestStr) {
		String encryptedStr = "";
		
		try {
			// 请求报文（明文字符串）
			encryptedStr = requestStr.trim();
			// AES加密
			byte[] encrypt = AESCoder.encrypt(encryptedStr.getBytes(), AesEncryptAndDecrypt.SHARE_KEY);
			// base64加密得到明文串对应的密文字符串
			encryptedStr = Coder.encryptBASE64(encrypt);
		}catch(Exception e) {
			e.printStackTrace();
			encryptedStr = "";
		}
		
		return encryptedStr;
	}

	
	/**
	 * 解密处理
	 * @param responseStr 密文应答报文（密文字符串）
	 * @return String 明文应答报文（明文字符串）
	 */
	public static String decodeResponseStr(String responseStr) {
		String decipheredStr = "";
		
		try {
			// 应答报文（密文字符串）
			decipheredStr = responseStr.trim();
			// base64解密得到密文串对应的字节数组
			byte[] temp_bytearray_response = Coder.decryptBASE64(decipheredStr);
			// AES解密
			byte[] decrypt = AESCoder.decrypt(temp_bytearray_response, AesEncryptAndDecrypt.SHARE_KEY);
			decipheredStr = new String(decrypt);
		}catch(Exception e) {
			e.printStackTrace();
			decipheredStr = "";
		}
		
		return decipheredStr;
	}

}
