package com.xiaocoder.android.fw.general.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.util.UtilImage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;

// 1 存储activity ， 回到首页activity， 弹出指定activity等
// 2 线程池    handler  图片加载  log等
public class XCApplication extends Application {

    private Stack<Activity> stack;

    protected static XCLog base_log;
    protected static XCSP base_sp;
    protected static ImageLoader base_imageloader;
    protected static Handler base_handler;
    protected static ExecutorService base_cache_threadpool;
    protected static ExecutorService base_fix_threadpool;
    protected static XCIOAndroid base_io;

    public Stack<Activity> getStack() {
        return stack;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        stack = new Stack<Activity>();

        // 线程池
        base_cache_threadpool = XCExecutorHelper.getExecutorHelperInstance().getCache();
        base_fix_threadpool = XCExecutorHelper.getExecutorHelperInstance().getFix(50);
        base_handler = new Handler();
        base_io = new XCIOAndroid(getApplicationContext());

        // 涉及到路径的初始化，在子类中

    }

    // 添加Activity到栈中
    public void addActivityToStack(Activity activity) {
        stack.add(activity);
    }

    // 把Activity移出栈
    public void delActivityFromStack(Activity activity) {
        stack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        return stack.lastElement();
    }

    // 判断某个acivity实例是否存在
    public boolean isActivityExist(Class<?> cls) {
        for (Activity activity : stack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    // 获取某个activity（activity不删除）
    public List<Activity> getActivity(Class<?> cls) {
        List<Activity> list = new ArrayList<Activity>();
        for (Activity activity : stack) {
            if (activity.getClass().equals(cls)) {
                list.add(activity);
            }
        }
        return list;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            stack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        finishActivity(stack.lastElement());
    }

    /**
     * 通过class ， 结束指定类名的Activity
     */
    public void finishActivities(Class<?> cls) {
        for (Activity activity : stack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭所有的activity,finish()会调用destroy()方法
     */
    public void finishAllActivity() {
        for (Activity activity : stack) {
            if (activity != null) {
                activity.finish();// 销毁
            }
        }
        stack.clear();
    }

    /**
     * 回到首页
     *
     * @param main_activity_class 首页的activity的字节码
     * @return
     */
    public Object toMainActivity(Class<? extends XCBaseActivity> main_activity_class) {
        Object main_activity = null;

        for (Iterator<Activity> it = stack.iterator(); it.hasNext(); ) {
            Activity item = it.next();
            if (item.getClass().getName().equals(main_activity_class.getName())) {
                main_activity = item;
                continue;
            } else {
                item.finish();
                it.remove();
            }
        }
        return main_activity;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static XCLog getBase_log() {
        return base_log;
    }

    public static XCSP getBase_sp() {
        return base_sp;
    }

    public static ImageLoader getBase_imageloader() {
        return base_imageloader;
    }

    public static Handler getBase_handler() {
        return base_handler;
    }

    public static ExecutorService getBase_cache_threadpool() {
        return base_cache_threadpool;
    }

    public static XCIOAndroid getBase_io() {
        return base_io;
    }

    public static ExecutorService getBase_fix_threadpool() {
        return base_fix_threadpool;
    }

    public static void printi(String msg) {
        base_log.i(msg);
    }

    public static void printi(String tag, String msg) {
        base_log.i(tag, msg);
    }

    public static void dShortToast(String msg) {
        base_log.debugShortToast(msg);
    }

    public static void dLongToast(String msg) {
        base_log.debugLongToast(msg);
    }

    public static void tempPrint(String msg) {
        base_log.tempPrint(msg);
    }

    public static void shortToast(String msg) {
        base_log.shortToast(msg);
    }

    public static void longToast(String msg) {
        base_log.longToast(msg);
    }

    public static void printe(String hint, Exception e) {
        base_log.e(hint, e);
    }

    public static void printe(String hint) {
        base_log.e(hint);
    }

    public static void printe(Context context, String hint, Exception e) {
        base_log.e(context, hint, e);
    }

    public static void printe(Context context, String hint) {
        base_log.e(context, hint);
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

    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        base_imageloader.displayImage(uri, imageView, options);
    }

    public static void displayImage(String uri, ImageView imageView) {
        displayImage(uri, imageView, XCImageLoaderHelper.getDisplayImageOptions());
    }

}
