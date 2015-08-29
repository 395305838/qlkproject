package com.xiaocoder.android.fw.general.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.xiaocoder.android.fw.general.application.XCApplication;

/**
 * Created by xiaocoder on 2015/7/28.
 */
public class UtilBroadcast {

    // 动态广播
    public static void myRegisterReceiver(Context context, int priority, String action, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(priority);
        filter.addAction(action);
        context.registerReceiver(receiver, filter);
    }

    // 动态广播
    public static void myUnregisterReceiver(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    /*
     * 发送广播
     */
    public static void mySendBroadcastReceiver(Context context, String action, String command_key, String command_value) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(command_key, command_value);
        context.sendBroadcast(intent);
    }

    public static void mySendBroadcastReceiver(Context context, String action, String[] command_keys, String[] command_values) {

        Intent intent = new Intent();
        intent.setAction(action);
        int size = command_keys.length;
        for (int i = 0; i < size; i++) {
            intent.putExtra(command_keys[i], command_values[i]);
        }
        context.sendBroadcast(intent);

    }
}
