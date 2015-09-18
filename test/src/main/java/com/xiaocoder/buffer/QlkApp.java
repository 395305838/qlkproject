package com.xiaocoder.buffer;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.exception.XLCrashHandler;
import com.xiaocoder.android.fw.general.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.util.UtilSystem;

/**
 * Created by xiaocoder on 2015/7/14.
 */
public class QlkApp extends XCApp {

    @Override
    public void onCreate() {
        super.onCreate();

        // http解析时用到该固定线程池,基类中还有一个cache线程池
        base_fix_threadpool = XCExecutorHelper.getExecutorHelperInstance().getFix(QlkConfig.THREAD_NUM);

        // log(可以打印日志到控制台和文件中) 与 toast
        base_log = new XCLog(getApplicationContext(),
                QlkConfig.IS_DTOAST, QlkConfig.IS_OUTPUT, QlkConfig.IS_PRINTLOG,
                QlkConfig.APP_ROOT, QlkConfig.LOG_FILE, QlkConfig.TEMP_PRINT_FILE, XCConfig.ENCODING_UTF8);

        // sp保存文件名 与 模式
        base_sp = new XCSP(getApplicationContext(), QlkConfig.SP_SETTING, Context.MODE_APPEND);// Context.MODE_MULTI_PROCESS

        // 图片视频等缓存的路径
        XCIOAndroid.createDirInAndroid(getApplicationContext(), QlkConfig.CHAT_MOIVE_DIR);
        XCIOAndroid.createDirInAndroid(getApplicationContext(), QlkConfig.CHAT_VIDEO_DIR);
        XCIOAndroid.createDirInAndroid(getApplicationContext(), QlkConfig.CHAT_PHOTO_DIR);

        // crash文件
        XCIOAndroid.createDirInAndroid(getApplicationContext(), QlkConfig.CRASH_DIR);

        // 图片加载的初始化
        setBase_imageloader(new IXCImageLoader() {
            ImageLoader imageloader = QlkConfig.getImageloader(getApplicationContext());

            @Override
            public void display(String url, ImageView imageview, Object... obj) {
                // 指定配置
                imageloader.displayImage(url, imageview, (DisplayImageOptions) obj[0]);
            }

            @Override
            public void display(String url, ImageView imageview) {
                // 默认配置
                imageloader.displayImage(url, imageview, QlkConfig.display_image_options);
            }
        });

        // 是否开启异常日志捕获，以及异常日志的存储路径等
        XLCrashHandler.getInstance().init(QlkConfig.IS_INIT_CRASH_HANDLER,
                getApplicationContext(), QlkConfig.CRASH_DIR, QlkConfig.IS_SHOW_EXCEPTION_ACTIVITY);

        // 打印一些简单的设备信息
        simpleDeviceInfo();

    }

    private void simpleDeviceInfo() {
        i(UtilSystem.getDeviceId(getApplicationContext()) + "--deviceId , "
                + UtilSystem.getVersionCode(getApplicationContext())
                + "--versionCode , " + UtilSystem.getVersionName(getApplicationContext()) + "--versionName , "
                + UtilScreen.getScreenHeightPx(getApplicationContext()) + "--screenHeightPx , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthPx , "
                + UtilScreen.getDensity(getApplicationContext()) + "--density , "
                + UtilScreen.getScreenHeightDP(getApplicationContext()) + "--screenHeightDP , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthDP");
    }

    @Override
    public void AppExit(Context context) {
        MobclickAgent.onKillProcess(getApplicationContext());
        super.AppExit(context);
    }
}
