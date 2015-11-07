package com.xiaocoder.android.fw.general.function.helper;

import android.app.Activity;

import com.xiaocoder.android.fw.general.application.XCApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by xiaocoder on 2015/11/4.
 * version: 1.2.0
 * description: 管理activity
 */
public class XCActivityHelper {

    private Stack<Activity> stack = new Stack<Activity>();

    public Stack<Activity> getStack() {
        return stack;
    }

    private XCActivityHelper() {
    }

    private static XCActivityHelper activityHelper = new XCActivityHelper();

    public static XCActivityHelper getActivityHelperInstance() {
        return activityHelper;
    }

    /**
     * 添加Activity到栈中
     */
    public void addActivityToStack(Activity activity) {
        stack.push(activity);
    }

    /**
     * 把Activity移出栈
     */
    public void delActivityFromStack(Activity activity) {
        stack.remove(activity);
    }

    /**
     * 获取顶层Activity（activity不删除）
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
     * 获取某(几)个activity（activity不删除）
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
     * 结束指定的一个Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            stack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 通过class ， 结束指定类名的（几个或一个）Activity
     */
    public void finishActivity(Class<?> cls) {

        for (Iterator<Activity> it = stack.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity.getClass().equals(cls)) {
                // finishActivity(activity);// 并发修改异常
                it.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束当前Activity
     */
    public void finishCurrentActivity() {

        finishActivity(getCurrentActivity());
    }

    /**
     * 关闭所有的activity
     */
    public void finishAllActivity() {
        for (Activity activity : stack) {
            // finishActivity(activity);//并发修改异常
            if (activity != null) {
                activity.finish();
            }
        }
        stack.clear();
    }

    /**
     * 去到指定的activity，该activity之上的activitys将销毁
     * <p/>
     * 如果该activity不存在，则一个也不删除
     */
    public Activity toActivity(Class<? extends Activity> activity_class) {

        if (isActivityExist(activity_class)) {
            // activity存在
            Activity toActivity = null;

            while (true) {

                if (stack.isEmpty()) {
                    XCApp.e(this + "---toActivity()的stack为null");
                    return null;
                }

                // 获取顶层Activity（activity暂不删除）
                toActivity = getCurrentActivity();

                if (toActivity == null) {
                    return null;
                }

                if (toActivity.getClass().getName().equals(activity_class.getName())) {
                    // 这个activity是返回的，不可以删
                    break;
                } else {
                    // 删除activity
                    finishActivity(toActivity);
                }

            }
            return toActivity;
        } else {
            // activity不存在
            return null;
        }
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        finishAllActivity();
        System.exit(0);
    }

}
