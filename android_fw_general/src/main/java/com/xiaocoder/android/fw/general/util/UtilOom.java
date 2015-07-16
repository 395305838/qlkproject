package com.xiaocoder.android.fw.general.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class UtilOom {

    // 从uri获取到bitmap，适用于小图
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            // 读取uri所在的图片
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 压缩 缩放 到指定文件中
    public static File compressBitmapJPEG(File file, Bitmap bitmap, int quailty, int scale_width, int scale_height) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (scale_height != 0 && scale_width != 0) {
                bitmap = createScaledBitmap(bitmap, scale_width, scale_height);
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quailty, fos);
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 压缩 缩放 到指定文件中
    public static File compressBitmapPNG(File file, Bitmap bitmap, int quailty, int scale_width, int scale_height) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (scale_height != 0 && scale_width != 0) {
                bitmap = createScaledBitmap(bitmap, scale_width, scale_height);
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, quailty, fos);
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // 缩放图片
    public static Bitmap createScaledBitmap(Bitmap bitmap, int width, int height) {
        // 如 width=700，height=700
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    public static int[] getBitmapHeightAndWidth(Context context, Uri uri) {

        InputStream input = null;

        try {
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            onlyBoundsOptions.inPurgeable = true;
            onlyBoundsOptions.inInputShareable = true;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            return new int[]{onlyBoundsOptions.outHeight, onlyBoundsOptions.outHeight};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 从uri获取到bitmap，适用于大图
    public static Bitmap getBitmapFromUriForLarge(Context context, Uri uri, float pix) {
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            onlyBoundsOptions.inPurgeable = true;
            onlyBoundsOptions.inInputShareable = true;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > pix) ? (originalSize / pix) : 1.0; // 如px=200

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//            bitmapOptions.inSampleSize = (int) Math.round(ratio) <= 1 ? 1 : (int) Math.floor(ratio);
            bitmapOptions.inSampleSize = (int) ratio < 1 ? 1 : (int) Math.floor(ratio);
            bitmapOptions.inJustDecodeBounds = false;
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmapOptions.inPurgeable = true;
            bitmapOptions.inInputShareable = true;
            input = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
