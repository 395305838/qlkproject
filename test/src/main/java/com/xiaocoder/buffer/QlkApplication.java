package com.xiaocoder.buffer;

import android.content.Context;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.exception.XLCrashHandler;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.util.UtilSystem;

/**
 * Created by xiaocoder on 2015/7/14.
 * <p/>
 * 各个初始化的顺序不要去改变
 */
public class QlkApplication extends XCApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化一些路径
        initPath();

        printi(UtilSystem.getDeviceId(getApplicationContext()) + "--deviceId , "
                + UtilSystem.getVersionCode(getApplicationContext())
                + "--versionCode , " + UtilSystem.getVersionName(getApplicationContext()) + "--versionName , "
                + UtilScreen.getScreenHeightPx(getApplicationContext()) + "--screenHeightPx , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthPx , "
                + UtilScreen.getDensity(getApplicationContext()) + "--density , "
                + UtilScreen.getScreenHeightDP(getApplicationContext()) + "--screenHeightDP , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthDP");
    }

    private void initPath() {

        // log , 可以打印日志 与 toast
        base_log = new XCLog(getApplicationContext(),
                QlkConfig.IS_DTOAST, QlkConfig.IS_OUTPUT, QlkConfig.IS_PRINTLOG,
                QlkConfig.APP_ROOT, QlkConfig.LOG_FILE, QlkConfig.TEMP_PRINT_FILE, XCConfig.ENCODING_UTF8);

        // 图片缓存路径
        base_imageloader = XCImageLoaderHelper
                .getInitedImageLoader
                        (XCImageLoaderHelper.getImageLoaderConfiguration
                                (getApplicationContext(), base_io.createDirInAndroid(QlkConfig.CACHE_DIRECTORY)));
        // sp保存路径
        base_sp = new XCSP(getApplicationContext(), QlkConfig.SP_SETTING, Context.MODE_APPEND);// Context.MODE_MULTI_PROCESS

        // 图片视频等保存的路径
        base_io.createDirInAndroid(QlkConfig.CHAT_MOIVE_FILE);
        base_io.createDirInAndroid(QlkConfig.CHAT_VIDEO_FILE);
        base_io.createDirInAndroid(QlkConfig.CHAT_PHOTO_FILE);
        base_io.createDirInAndroid(QlkConfig.CRASH_FILE);

        // 是否开启异常日志捕获，以及异常日志的存储路径等
        XLCrashHandler.getInstance().init(QlkConfig.IS_INIT_CRASH_HANDLER,
                getApplicationContext(), QlkConfig.CRASH_FILE, QlkConfig.IS_SHOW_EXCEPTION_ACTIVITY);
    }
}
