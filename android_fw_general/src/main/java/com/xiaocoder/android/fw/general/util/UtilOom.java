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
        return compressBitmap(file, bitmap, quailty, scale_width, scale_height, Bitmap.CompressFormat.JPEG);
    }


    // 压缩 缩放 到指定文件中
    public static File compressBitmap(File file, Bitmap bitmap, int quailty, int scale_width, int scale_height, Bitmap.CompressFormat type) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (scale_height != 0 && scale_width != 0) {
                bitmap = createScaledBitmap(bitmap, scale_width, scale_height);
            }
            bitmap.compress(type, quailty, fos);
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


    // 从uri获取到bitmap，适用于大图
    // type = Bitmap.Config.RGB_565
    public static Bitmap getBitmapForLargeByUri(Context context, Uri uri, float pix, Bitmap.Config type) {

        InputStream input = null;

        input = getInputStreamFromUri(context, uri);

        if (input == null) {
            return null;
        }

        int[] sizes = getBitmapHeightAndWidth(input, type);

        if (sizes == null) {
            return null;
        }

        int originalSize = (sizes[1] > sizes[0]) ? sizes[1] : sizes[0];

        double ratio = (originalSize > pix) ? (originalSize / pix) : 1.0; // 如px=200

        input = getInputStreamFromUri(context, uri);

        if (input == null) {
            return null;
        }

        return reallyDecodeStream(type, input, ratio);

    }


    public static Bitmap getBitmapForLargeByRaw(Context context, int drawable_id, float pix, Bitmap.Config type) {

        InputStream input = null;

        input = getInputStreamFromRaw(context, drawable_id);

        if (input == null) {
            return null;
        }

        int[] sizes = getBitmapHeightAndWidth(input, type);

        if (sizes == null) {
            return null;
        }

        int originalSize = (sizes[1] > sizes[0]) ? sizes[1] : sizes[0];

        double ratio = (originalSize > pix) ? (originalSize / pix) : 1.0; // 如px=200

        input = getInputStreamFromRaw(context, drawable_id);

        if (input == null) {
            return null;
        }

        return reallyDecodeStream(type, input, ratio);

    }


    private static Bitmap reallyDecodeStream(Bitmap.Config type, InputStream input, double ratio) {

        try {

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            // bitmapOptions.inSampleSize = (int) Math.round(ratio) <= 1 ? 1 : (int) Math.floor(ratio);

            bitmapOptions.inSampleSize = (int) ratio <= 1 ? 1 : (int) Math.floor(ratio);

            bitmapOptions.inJustDecodeBounds = false;

            bitmapOptions.inPreferredConfig = type;

            bitmapOptions.inPurgeable = true;

            bitmapOptions.inInputShareable = true;

            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            // BitmapFactory.decodeByteArray(data, 0, data.length, options);

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


    /**
     * 返回的第一个是宽， 第二个是高 ， type为 Bitmap.Config
     */
    public static int[] getBitmapHeightAndWidth(InputStream input, Bitmap.Config type) {

        try {

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();

            onlyBoundsOptions.inJustDecodeBounds = true;

            onlyBoundsOptions.inPreferredConfig = type;

            onlyBoundsOptions.inPurgeable = true;

            onlyBoundsOptions.inInputShareable = true;

            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);

            input.close();

            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            return new int[]{onlyBoundsOptions.outWidth, onlyBoundsOptions.outHeight};

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

    public static InputStream getInputStreamFromUri(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getInputStreamFromRaw(Context context, int drawable_id) {
        try {
            return context.getResources().openRawResource(drawable_id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getInputStreamFromAsserts(Context context, String name) {
        try {
            return context.getAssets().open(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
