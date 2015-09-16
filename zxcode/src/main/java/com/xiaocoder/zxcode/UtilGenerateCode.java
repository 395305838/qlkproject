package com.xiaocoder.zxcode;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.xiaocoder.zxcode.encoding.EncodingHandler;

/**
 * Created by xiaocoder on 2015/6/21.
 * <p/>
 * 生成二维码图片
 */
public class UtilGenerateCode {

    public static Bitmap generateCode(String url) {
        try {
            return EncodingHandler.createQRCode(url, 350);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap generateCode(String url, int widthAndHeight) {
        try {
            return EncodingHandler.createQRCode(url, widthAndHeight);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
