package com.xiaocoder.android.fw.general.js_encryption.rc4;

import android.text.TextUtils;
import android.util.Base64;

import com.xiaocoder.android.fw.general.application.XCApp;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xilinch on 2015/5/28.
 */
public class UtilRC4 {

    public static String encryByRC4(String keyStr, String data){
        if(TextUtils.isEmpty(keyStr) || TextUtils.isEmpty(data)){
            return "";
        }
        try {
            byte[] keyByte = keyStr.getBytes("UTF-8");
            Key key = new SecretKeySpec(keyByte,"RC4");

            Cipher cipher1 = Cipher.getInstance("RC4");
            cipher1.init(Cipher.ENCRYPT_MODE, key);

            byte[] decryStr = cipher1.doFinal(data.getBytes("UTF-8"));
            String str = Base64.encodeToString(decryStr, Base64.DEFAULT);
            XCApp.i("Myyyy", str);
            return str;

        } catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }
}
