package com.xiaocoder.android.fw.general.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;

/**
 * 1 存储activity ， 回到首页activity， 弹出指定activity等
 * 2 线程池    handler  图片加载  log等
 */
public class XCApp extends Application {

    private Stack<Activity> stack = new Stack<Activity>();
    protected static ExecutorService base_cache_threadpool = XCExecutorHelper.getExecutorHelperInstance().getCache();
    protected static Handler base_handler = new Handler();
    /**
     * 以下的涉及到路径和文件名的，在子类中初始化
     */
    protected static ExecutorService base_fix_threadpool;
    protected static XCLog base_log;
    protected static XCSP base_sp;
    /**
     * 加个接口，以后可能会改别的图片加载库，子类中传入
     */
    protected static IXCImageLoader base_imageloader;

    public interface IXCImageLoader {

        void display(String url, ImageView imageview, Object... obj);

        void display(String url, ImageView imageview);

    }

    public Stack<Activity> getStack() {
        return stack;
    }

    /**
     * 添加Activity到栈中
     */
    public void addActivityToStack(Activity activity) {
        stack.add(activity);
    }

    /**
     * 把Activity移出栈
     */
    public void delActivityFromStack(Activity activity) {
        stack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        return stack.lastElement();
    }

    /**
     * 判断某个acivity实例是否存在
     */
    public boolean isActivityExist(Class<?> cls) {
        for (Activity activity : stack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某个activity（activity不删除）
     */
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
     * main_activity_class 首页的activity的字节码
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

    public static Handler getBase_handler() {
        return base_handler;
    }

    public static ExecutorService getBase_cache_threadpool() {
        return base_cache_threadpool;
    }

    public static ExecutorService getBase_fix_threadpool() {
        return base_fix_threadpool;
    }

    public static void i(Object msg) {
        base_log.i(msg);
    }

    public static void i(String tag, Object msg) {
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

    public static IXCImageLoader getBase_imageloader() {
        return base_imageloader;
    }

    public static void setBase_imageloader(IXCImageLoader imageloader) {
        base_imageloader = imageloader;
    }

    public static void displayImage(String uri, ImageView imageView, Object... options) {
        base_imageloader.display(uri, imageView, options);
    }

    public static void displayImage(String uri, ImageView imageView) {
        base_imageloader.display(uri, imageView);
    }
}
