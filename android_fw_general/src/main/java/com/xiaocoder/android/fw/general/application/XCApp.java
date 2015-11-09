package com.xiaocoder.android.fw.general.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.exception.XCCrashHandler;
import com.xiaocoder.android.fw.general.function.helper.XCActivityHelper;
import com.xiaocoder.android.fw.general.function.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.http.XCHttpSend;
import com.xiaocoder.android.fw.general.imageloader.XCIImageLoader;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.util.UtilSystem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 1 存储activity ， 回到首页activity， 弹出指定activity等
 * 2 线程池    handler  图片加载  log等
 */
public class XCApp extends Application {
    protected static XCActivityHelper base_ActivityHelper = XCActivityHelper.getInstance();
    protected static ExecutorService base_cache_threadpool = XCExecutorHelper.getInstance().getCache();
    protected static Handler base_handler = new Handler();
    protected static XCHttpSend base_xcHttpSend = new XCHttpSend();
    protected static Context base_applicationContext;
    /**
     * 以下的涉及到路径和文件名的，在子类中初始化
     */
    protected static ExecutorService base_fix_threadpool;
    protected static ScheduledExecutorService base_scheduled_threadpool;
    protected static XCLog base_log;
    protected static XCSP base_sp;
    protected static XCCrashHandler base_crashHandler;
    /**
     * 加个接口，以后可能会改别的图片加载库，子类中传入
     */
    protected static XCIImageLoader base_imageloader;

    @Override
    public void onCreate() {
        super.onCreate();
        base_applicationContext = getApplicationContext();
    }

    /**
     * 打印设备的基本信息
     */
    public void simpleDeviceInfo() {
        i(UtilSystem.getDeviceId(getApplicationContext()) + "--deviceId , "
                + UtilSystem.getVersionCode(getApplicationContext())
                + "--versionCode , " + UtilSystem.getVersionName(getApplicationContext()) + "--versionName , "
                + UtilScreen.getScreenHeightPx(getApplicationContext()) + "--screenHeightPx , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthPx , "
                + UtilScreen.getDensity(getApplicationContext()) + "--density , "
                + UtilScreen.getScreenHeightDP(getApplicationContext()) + "--screenHeightDP , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthDP");
    }

    public static XCLog getBase_log() {
        return base_log;
    }

    public static XCSP getBase_sp() {
        return base_sp;
    }

    public static Handler getBase_handler() {
        return base_handler;
    }

    public static ExecutorService getBase_cache_threadpool() {
        return base_cache_threadpool;
    }

    public static ExecutorService getBase_fix_threadpool() {
        return base_fix_threadpool;
    }

    public static ScheduledExecutorService getBase_scheduled_threadpool() {
        return base_scheduled_threadpool;
    }

    public static XCActivityHelper getBase_ActivityHelper() {
        return base_ActivityHelper;
    }

    public static void i(Object msg) {
        base_log.i(msg);
    }

    public static void i(String tag, Object msg) {
        base_log.i(tag, msg);
    }

    public static void dShortToast(Object msg) {
        base_log.debugShortToast(msg);
    }

    public static void dLongToast(Object msg) {
        base_log.debugLongToast(msg);
    }

    public static void tempPrint(String msg) {
        base_log.tempPrint(msg);
    }

    public static void shortToast(Object msg) {
        base_log.shortToast(msg);
    }

    public static void longToast(Object msg) {
        base_log.longToast(msg);
    }

    public static void e(String hint, Exception e) {
        base_log.e(hint, e);
    }

    public static void e(String hint) {
        base_log.e(hint);
    }

    public static void e(Context context, String hint, Exception e) {
        base_log.e(context, hint, e);
    }

    public static void e(Context context, String hint) {
        base_log.e(context, hint);
    }

    public static void itemp(Object msg) {
        base_log.i(XCConfig.TAG_TEMP, msg);
    }

    public static void itest(Object msg) {
        base_log.i(XCConfig.TAG_TEST, msg);
    }

    public static void clearLog() {
        base_log.clearLog();
    }

    public static void spPut(String key, boolean value) {
        base_sp.putBoolean(key, value);
    }

    public static void spPut(String key, int value) {
        base_sp.putInt(key, value);
    }

    public static void spPut(String key, long value) {
        base_sp.putLong(key, value);
    }

    public static void spPut(String key, float value) {
        base_sp.putFloat(key, value);
    }

    public static void spPut(String key, String value) {
        base_sp.putString(key, value);
    }

    public static String spGet(String key, String default_value) {
        return base_sp.getString(key, default_value);
    }

    public static int spGet(String key, int default_value) {
        return base_sp.getInt(key, default_value);
    }

    public static long spGet(String key, long default_value) {
        return base_sp.getLong(key, default_value);
    }

    public static boolean spGet(String key, boolean default_value) {
        return base_sp.getBoolean(key, default_value);
    }

    public static float spGet(String key, float default_value) {
        return base_sp.getFloat(key, default_value);
    }

    public static Map<String, ?> spGetAll() {
        return base_sp.getAll();
    }

    public static Context getBase_applicationContext() {
        return base_applicationContext;
    }

    public static XCCrashHandler getBase_crashHandler() {
        return base_crashHandler;
    }

    /**
     * 图片加载
     */
    public static XCIImageLoader getBase_imageloader() {
        return base_imageloader;
    }

    public static void setBase_imageloader(XCIImageLoader imageloader) {
        base_imageloader = imageloader;
    }

    public static void displayImage(String uri, ImageView imageView, Object... options) {
        base_imageloader.display(uri, imageView, options);
    }

    public static void displayImage(String uri, ImageView imageView) {
        base_imageloader.display(uri, imageView);
    }

    /**
     * http请求
     */
    public static XCHttpSend getBase_xcHttpSend() {
        return base_xcHttpSend;
    }

    public static void setBase_xcHttpSend(XCHttpSend base_xcHttpSend) {
        XCApp.base_xcHttpSend = base_xcHttpSend;
    }

    public static void resetNetingStatus() {
        base_xcHttpSend.resetNetingStatus();
    }

    public static void postAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog,
                                String urlString, Map<String, Object> map, XCIResponseHandler res) {
        base_xcHttpSend.postAsyn(needSecret, isFrequentlyClick, isShowDialog, urlString, map, res);
    }

    public static void postAsyn(boolean isShowDialog, String urlString, Map<String, Object> map,
                                XCIResponseHandler res) {
        base_xcHttpSend.postAsyn(true, true, isShowDialog, urlString, map, res);
    }

    public static void getAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog,
                               String urlString, Map<String, Object> map, XCIResponseHandler res) {
        base_xcHttpSend.getAsyn(needSecret, isFrequentlyClick, isShowDialog, urlString, map, res);
    }

    public static void getAsyn(boolean isShowDialog, String urlString, Map<String, Object> map,
                               XCIResponseHandler res) {
        base_xcHttpSend.getAsyn(true, true, isShowDialog, urlString, map, res);
    }

    public static void sendHttpRequest(XCIResponseHandler responseHandler) {
        base_xcHttpSend.sendAsyn(responseHandler);
    }

    /**
     * activity
     */
    public static void addActivityToStack(Activity activity) {
        base_ActivityHelper.addActivityToStack(activity);
    }

    public static void delActivityFromStack(Activity activity) {
        base_ActivityHelper.delActivityFromStack(activity);
    }

    public static Activity getCurrentActivity() {
        return base_ActivityHelper.getCurrentActivity();
    }

    public static boolean isActivityExist(Class<?> cls) {
        return base_ActivityHelper.isActivityExist(cls);
    }

    public static List<Activity> getActivity(Class<?> cls) {
        return base_ActivityHelper.getActivity(cls);
    }

    public static void finishActivity(Activity activity) {
        base_ActivityHelper.finishActivity(activity);
    }

    public static void finishActivity(Class<?> cls) {
        base_ActivityHelper.finishActivity(cls);
    }

    public static void finishCurrentActivity() {
        base_ActivityHelper.finishCurrentActivity();
    }

    public static void finishAllActivity() {
        base_ActivityHelper.finishAllActivity();
    }

    public static Activity toActivity(Class<? extends XCBaseActivity> main_activity_class) {
        return base_ActivityHelper.toActivity(main_activity_class);
    }

    public static void appExit() {
        base_ActivityHelper.appExit();
    }

}
