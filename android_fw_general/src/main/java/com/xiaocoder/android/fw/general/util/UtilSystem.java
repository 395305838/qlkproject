package com.xiaocoder.android.fw.general.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class UtilSystem {

    public static int getPid() {
        return android.os.Process.myPid();
    }

    public static long getThreadId() {
        return Thread.currentThread().getId();
    }


    public void install(Context context, File file) {

        // 安装应用软件的模块在系统已经存在,所以只要激活就可以了
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive"); // 不要分开设置
        // tomcat的conf的web.xml中有所有的文件类型
        context.startActivity(intent);

    }

    public void uninstall(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:com.enen.hehe"));
        context.startActivity(intent);
    }

    /**
     * 获取top的Activity的ComponentName
     */
    public static ComponentName getTopActivityCompomentName(Context paramContext) {
        List<ActivityManager.RunningTaskInfo> localList = null;
        if (paramContext != null) {
            ActivityManager localActivityManager = (ActivityManager) paramContext
                    .getSystemService("activity");
            if (localActivityManager != null) {
                localList = localActivityManager.getRunningTasks(1);

                if ((localList == null) || (localList.size() <= 0)) {
                    return null;
                }
            }
        }
        ComponentName localComponentName = localList.get(0).topActivity;
        return localComponentName;
    }

    /**
     * 查看是否后台
     *
     * @param paramContext
     * @return
     */
    public static boolean isAppRunningBackground(Context paramContext) {
        String pkgName = null;
        List<RunningAppProcessInfo> localList = null;
        if (paramContext != null) {
            pkgName = paramContext.getPackageName();
            ActivityManager localActivityManager = (ActivityManager) paramContext
                    .getSystemService("activity");
            if (localActivityManager != null) {
                localList = localActivityManager.getRunningAppProcesses();
                if ((localList == null) || (localList.size() <= 0)) {
                    return false;
                }
            }
        }

        for (Iterator<RunningAppProcessInfo> localIterator = localList
                .iterator(); localIterator.hasNext(); ) {
            ActivityManager.RunningAppProcessInfo info = localIterator.next();
            if (info.processName.equals(pkgName) && info.importance != 100) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测手机是否已插入SIM卡
     *
     * @param context
     * @return
     */
    public static boolean isCheckSimCardAvailable(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() != TelephonyManager.SIM_STATE_READY) {
            return false;
        }
        return true;
    }

    public static String getPhoneIMEI(Activity aty) {
        TelephonyManager tm = (TelephonyManager) aty
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取android系统版本号
     *
     * @return
     */
    public static String getOSVersion() {
        String release = android.os.Build.VERSION.RELEASE; // android系统版本号
        release = "android" + release;
        return release;
    }

    /**
     * 获取设备系统SDK API号
     *
     * @return
     */
    public static int getOSVersionSDKINT() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        return currentapiVersion;
    }

    private static String getCPUInfos() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        StringBuilder resusl = new StringBuilder();
        String resualStr = null;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                resusl.append(str2);
                String cup = str2;
            }
            if (resusl != null) {
                resualStr = resusl.toString();
                return resualStr;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resualStr;
    }

    // 检查某个应用是否安装
    public static boolean checkAPP(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 首先取imei，如果没有取macAddress，再没有就自定义一个变量 规则：id开头的一个32位字符串，根据java.util.UUID类生成，
     * 防止重复 如果到java.util.uuid生成的时候，数据被清掉后会被清除。 但对于这样的设备，比较罕见，模拟器是一类
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        // 没有设备id，则生成保存
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        // 防止刷机和山寨机 http://www.lexun.cn/thread-22891880-1-1.html
        if (deviceId != null) {
            char[] chs = deviceId.toCharArray();
            int size = chs.length;
            boolean all0 = true;
            for (int i = 0; i < size; i++) {
                if (chs[i] != '0') {
                    all0 = false;
                    break;
                }
            }
            if (all0)
                deviceId = null;
        }
        // 获取不到时
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getMacAddress(context);
        }
        return deviceId;
    }

    /*
     * 获取mac地址，获取不到则生成一个
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (info != null) {
            String macAddress = info.getMacAddress();
            if (macAddress == null) {
                UUID u = UUID.randomUUID();
                macAddress = "id"
                        + u.toString().replaceAll("-", "").substring(2);
            }
            macAddress = macAddress.toLowerCase();
            return macAddress.replaceAll(":", "");
        } else {
            UUID u = UUID.randomUUID();
            String uuid = "id" + u.toString().replaceAll("-", "").substring(2);
            return uuid;
        }
    }

}