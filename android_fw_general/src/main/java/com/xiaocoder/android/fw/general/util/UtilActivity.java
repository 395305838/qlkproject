package com.xiaocoder.android.fw.general.util;

import android.content.Intent;
import android.os.Parcelable;

import com.xiaocoder.android.fw.general.base.XCBaseActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaocoder on 2015/10/16.
 * version: 1.2.0
 * description:
 */
public class UtilActivity {

    /**
     * 在XCBaseActivity里有activity创建和销毁的 动画效果
     *
     * @param requestCode 如果为null ，则startActivity();
     */
    public static void myStartActivity(XCBaseActivity activity, Intent intent, Integer requestCode) {

        if (requestCode != null) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
        activity.activityStartAnimation();
    }

    /**
     * @param activity_class
     * @param requestCode    如果等于null，则startActivity   否则startActivityForResult
     * @param flags          没有flags就填null --注：不要随意填负数，可能有错误
     * @param command_keys   可以传入null
     * @param command_values 可以传入null
     */
    public static void myStartActivity(XCBaseActivity activity, Class<? extends XCBaseActivity> activity_class,
                                       Integer requestCode,
                                       Integer flags,
                                       String[] command_keys,
                                       Object[] command_values) {
        Intent intent = new Intent(activity, activity_class);
        if (flags != null) {
            intent.setFlags(flags);
        }
        if (command_keys.length != command_values.length) {
            throw new RuntimeException("myStartActivity中传入的keys 和 values的size不一致");
        }
        int size = command_keys.length;
        for (int i = 0; i < size; i++) {
            Object obj = command_values[i];
            if (obj instanceof String) {
                intent.putExtra(command_keys[i], (String) obj);
            } else if (obj instanceof Boolean) {
                intent.putExtra(command_keys[i], (Boolean) obj);
            } else if (obj instanceof Integer) {
                intent.putExtra(command_keys[i], (Integer) obj);
            } else if (obj instanceof Long) {
                intent.putExtra(command_keys[i], (Long) obj);
            } else if (obj instanceof Double) {
                intent.putExtra(command_keys[i], (Double) obj);
            } else if (obj instanceof Float) {
                intent.putExtra(command_keys[i], (Float) obj);
            } else if (obj instanceof ArrayList) {
                ArrayList list = (ArrayList) obj;
                if (list.size() > 0) {
                    Object o = list.get(0);
                    if (o instanceof String) {
                        intent.putStringArrayListExtra(command_keys[i], (ArrayList<String>) obj);
                    } else if (o instanceof Integer) {
                        intent.putIntegerArrayListExtra(command_keys[i], (ArrayList<Integer>) obj);
                    }
                }
            } else if (obj instanceof Serializable) {
                intent.putExtra(command_keys[i], (Serializable) obj);
            } else if (obj instanceof Parcelable) {
                intent.putExtra(command_keys[i], (Parcelable) obj);
            } else {
                throw new RuntimeException("myStartActivity()中intent的putExtra参数没有转型");
            }
        }

        myStartActivity(activity, intent, requestCode);

    }

    public static void myStartActivity(XCBaseActivity activity, Class<? extends XCBaseActivity> activity_class) {
        myStartActivity(activity, activity_class, null, null, new String[]{}, new String[]{});
    }

    public static void myStartActivity(XCBaseActivity activity, Class<? extends XCBaseActivity> activity_class, int flags) {
        myStartActivity(activity, activity_class, null, flags, new String[]{}, new String[]{});
    }

}
