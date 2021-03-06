package com.xiaocoder.middle;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.exception.XCCrashHandler;
import com.xiaocoder.android.fw.general.db.XCExceptionDao;
import com.xiaocoder.android.fw.general.exception.XCIException2Server;
import com.xiaocoder.android.fw.general.function.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.imageloader.JSImageLoader;
import com.xiaocoder.android.fw.general.imageloader.XCAsynLoader;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoader;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.model.XCExceptionModel;
import com.xiaocoder.test.R;

/**
 * Created by xiaocoder on 2015/7/14.
 * <p/>
 * 初始化的顺序不要去改动
 */
public class MApp extends XCApp {

    @Override
    public void onCreate() {
        super.onCreate();

        createDir();

        initNumThreadPool();

        initLog();

        initSp();

        initImageLoader();

        initCrash();

        simpleDeviceInfo();

    }

    private void initSp() {
        // sp保存文件名 与 模式
        base_sp = new XCSP(getApplicationContext(), MConfig.SP_FILE, Context.MODE_APPEND);// Context.MODE_MULTI_PROCESS
    }

    private void initLog() {
        // log(可以打印日志到控制台和文件中) 与 toast
        base_log = new XCLog(getApplicationContext(),
                MConfig.IS_DTOAST, MConfig.IS_OUTPUT, MConfig.IS_PRINTLOG,
                MConfig.APP_ROOT, MConfig.LOG_FILE, MConfig.TEMP_PRINT_FILE, XCConfig.ENCODING_UTF8);
    }

    private void initNumThreadPool() {
        // http解析时用到该固定线程池,基类中还有一个cache线程池
        base_fix_threadpool = XCExecutorHelper.getInstance().getFix(MConfig.FIX_THREAD_NUM);
        base_scheduled_threadpool = XCExecutorHelper.getInstance().getScheduledFix(MConfig.SCHEDULE_THREAD_NUM);
    }

    private void createDir() {
        // 应用存储日志 缓存等信息的顶层文件夹
        XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.APP_ROOT);
        // 图片视频等缓存的文件夹
        XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.CHAT_MOIVE_DIR);
        XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.CHAT_VIDEO_DIR);
        XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.CHAT_PHOTO_DIR);
        // crash文件夹
        XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.CRASH_DIR);
        // cache文件夹
        XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.CACHE_DIR);
    }

    private void initImageLoader() {

        setBase_imageloader(new XCAsynLoader(MConfig.getImageloader(getApplicationContext()),
                MConfig.default_image_options
        ));
    }

    private void initImageLoader2() {
        setBase_imageloader(new XCImageLoader(getApplicationContext(),
                XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.CACHE_DIR),
                R.drawable.image_a));
    }

    private void initImageLoader3() {
        setBase_imageloader(new JSImageLoader(R.drawable.image_a));
    }

    private void initCrash() {

        base_crashHandler = XCCrashHandler.getInstance().init(MConfig.IS_INIT_CRASH_HANDLER,
                getApplicationContext(), MConfig.CRASH_DIR, MConfig.IS_SHOW_EXCEPTION_ACTIVITY);

        base_crashHandler.setUploadServer(new XCIException2Server() {
            @Override
            public void uploadException2Server(String info, Throwable ex, Thread thread,
                                               XCExceptionModel model, XCExceptionDao dao) {
                // 将未try catch的异常信息 上传到友盟
                MobclickAgent.reportError(getApplicationContext(), info);
                // 如果MConfig.IS_INIT_CRASH_HANDLER（枚举值中可设置）为false，则dao为空
                if (dao != null) {
                    model.setUserId(MUser.getUserId());
                    dao.update(model);
                    // TODO 将异常信息在下次重启应用时，上传到服务器,如果是成功上传，则更新uploadSuccess字段为“1”
                }
            }
        });
    }

    public void test(XCExceptionModel model, XCExceptionDao dao) {
        XCApp.itemp(dao.queryCount());
        XCApp.itemp(dao.queryUploadFail(XCExceptionDao.SORT_ASC));
        XCApp.itemp(dao.queryUploadFail(XCExceptionDao.SORT_DESC));
        XCApp.itemp(dao.queryUploadSuccess(XCExceptionDao.SORT_DESC));
        XCApp.itemp(dao.queryAll(XCExceptionDao.SORT_DESC));
        XCApp.itemp(dao.queryUnique(model.getUniqueId()));
    }

    public static void exitApp() {
        // 友盟统计
        MobclickAgent.onKillProcess(base_applicationContext);
        appExit();
    }
}
