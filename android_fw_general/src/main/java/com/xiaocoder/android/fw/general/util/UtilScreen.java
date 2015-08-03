package com.xiaocoder.android.fw.general.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.WindowManager;

import com.xiaocoder.android.fw.general.util.UtilImage;

/**
 * Created by xiaocoder on 2015/8/3.
 */
public class UtilScreen {

    private static int screenHeightPx = -1;
    private static int screenWidthPx = -1;
    private static float density = -1;
    private static int screenHeightDP = -1;
    private static int screenWidthDP = -1;


    public static float getDensity(Context context) {
        if (density == -1) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int getScreenHeightPx(Context context) {
        if (screenHeightPx == -1) {
            screenHeightPx = getScreenSize(context)[0];
        }

        return screenHeightPx;
    }

    public static int getScreenWidthPx(Context context) {
        if (screenWidthPx == -1) {
            screenWidthPx = getScreenSize(context)[1];
        }

        return screenWidthPx;
    }

    public static int getScreenHeightDP(Context context) {
        if (screenHeightDP == -1) {
            screenHeightDP = px2dip(context, getScreenHeightPx(context));
        }
        return screenHeightDP;
    }

    public static int getScreenWidthDP(Context context) {
        if (screenWidthDP == -1) {
            screenWidthDP = px2dip(context, getScreenWidthPx(context));
        }
        return screenWidthDP;
    }


    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dipValue + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int[] getScreenSize(Context context) {
        // 获取屏幕分辨率
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int[] size = new int[2];
        size[0] = display.getHeight();
        size[1] = display.getWidth();
        return size;
    }

}
