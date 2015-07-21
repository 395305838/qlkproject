package com.xiaocoder.test.buffer;

import android.content.Context;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.exception.XLCrashHandler;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.util.UtilSystem;

/**
 * Created by xiaocoder on 2015/7/14.
 */
public class QlkApplication extends XCApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化一些路径的问题

        // 图片缓存路径
        base_imageloader = XCImageLoaderHelper
                .getInitedImageLoader
                        (XCImageLoaderHelper.getImageLoaderConfiguration
                                (getApplicationContext(), base_io.createDirInSDCard(QlkConfig.CACHE_DIRECTORY, getApplicationContext())));
        // sp保存路径
        base_sp = new XCSP(getApplicationContext(), QlkConfig.SP_SETTING, Context.MODE_APPEND);// Context.MODE_MULTI_PROCESS

        // 图片视频等保存的路径
        base_io.createDirInAndroid(QlkConfig.CHAT_MOIVE_FILE, getApplicationContext());
        base_io.createDirInAndroid(QlkConfig.CHAT_VIDEO_FILE, getApplicationContext());
        base_io.createDirInAndroid(QlkConfig.CHAT_PHOTO_FILE, getApplicationContext());

        // log , 可以打印日志 与 toast
        base_log = new XCLog(getApplicationContext(),
                QlkConfig.IS_DTOAST, QlkConfig.IS_OUTPUT, QlkConfig.IS_PRINTLOG,
                QlkConfig.APP_ROOT, QlkConfig.LOG_FILE, QlkConfig.TEMP_PRINT_FILE, QlkConfig.ENCODING_UTF8);

        printi(UtilSystem.getDeviceId(getApplicationContext()) + "--deviceId , " + getVersionCode() + "--versionCode , " + getVersionName() + "--versionName , " + getScreenHeightPx()
                + "--screenHeightPx , " + getScreenWidthPx() + "--screenWidthPx , " + getDensity() + "--density , " + getScreenHeightDP() + "--screenHeightDP , " + getScreenWidthPx() + "--screenWidthDP");

        // 异常日志捕获的存储路径
        base_io.createDirInAndroid(QlkConfig.CRASH_FILE, getApplicationContext());
//        XLCrashHandler crashHandler = XLCrashHandler.getInstance();
//        crashHandler.init(getApplicationContext(), QlkConfig.CRASH_FILE);

    }
}
