package com.xiaocoder.android.fw.general.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by xiaocoder on 2015/8/3.
 */
public class UtilScreen {

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenHeightPx(Context context) {
        return getScreenSize(context)[0];

    }

    public static int getScreenWidthPx(Context context) {
        return getScreenSize(context)[1];
    }

    public static int getScreenHeightDP(Context context) {
        return px2dip(context, getScreenHeightPx(context));
    }

    public static int getScreenWidthDP(Context context) {
        return px2dip(context, getScreenWidthPx(context));
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dipValue + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
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
