package com.xiaocoder.android.fw.general.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class UtilSystem {

    public static int getPid() {

        return android.os.Process.myPid();

    }

    public static long getThreadId() {

        return Thread.currentThread().getId();

    }

    public static String getThreadName() {

        return Thread.currentThread().getName();

    }


    /**
     * 安装应用
     */
    public void install(Context context, File file) {

        // 安装应用软件的模块在系统已经存在,所以只要激活就可以了
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive"); // 不要分开设置
        // tomcat的conf的web.xml中有所有的文件类型
        context.startActivity(intent);

    }

    /**
     * 卸载应用
     */
    public void uninstall(Context context , String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        //intent.setData(Uri.parse("package:com.enen.hehe"));
        intent.setData(Uri.parse("package:"+packageName));
        context.startActivity(intent);
    }

    /**
     * 获取top的Activity的ComponentName
     */
    public static ComponentName getTopActivityCompomentName(Context paramContext) {
        List<ActivityManager.RunningTaskInfo> localList = null;
        if (paramContext != null) {
            ActivityManager localActivityManager = (ActivityManager) paramContext
                    .getSystemService(Context.ACTIVITY_SERVICE);
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
     * 判断app是否在后台，该方法在广播中可以判断
     */
    public static boolean isApplicationToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * app是否启动
     */
    public static boolean isAppRunning(Context context, String pack_name) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(pack_name) && info.baseActivity.getPackageName().equals(pack_name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测手机是否已插入SIM卡
     */
    public static boolean isCheckSimCardAvailable(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() != TelephonyManager.SIM_STATE_READY) {
            return false;
        }
        return true;
    }

    /**
     * 包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    /**
     * 获取版本名字
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "-1";
        }
    }

    /**
     * 获取android系统版本号
     */
    public static String getOSVersion() {
        String release = android.os.Build.VERSION.RELEASE; // android系统版本号
        release = "android" + release;
        return release;
    }

    /**
     * 获取设备系统SDK API号
     */
    public static int getOSVersionSDKINT() {

        return android.os.Build.VERSION.SDK_INT;

    }

    /**
     * 获取手机的一些信息
     */
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

    /**
     * 检查某个应用是否安装
     */
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
     * 检测某类应用是否安装
     */
    public static boolean checkApp2(Context context, String packageContent) {
        PackageManager pageManage = context.getPackageManager();
        List<PackageInfo> packages = pageManage.getInstalledPackages(0);
        String pagName = "";
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            pagName = packageInfo.packageName;
            //判断是否为QQ包名
            if (pagName.contains(packageContent)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 首先取imei，如果没有取macAddress，再没有就自定义一个变量 规则：id开头的一个32位字符串，根据java.util.UUID类生成，
     * 防止重复 如果到java.util.uuid生成的时候，数据被清掉后会被清除。 但对于这样的设备，比较罕见，模拟器是一类
     *
     * 标准的imei是15位，但是有的是14位
     */
    public static String getDeviceId(Context context) {
        // 没有设备id，则生成保存
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId(); // 这里的deviceId为IMEI
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

    /**
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

    /**
     * 获得设备id
     */
    private String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 获取当前操作系统的语言
     */
    public static String getSysLanguage() {

        return Locale.getDefault().getLanguage();

    }

    /**
     * 获取手机型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }


    /**
     * 读取sim卡序列号
     */
    public static String getSimSerialNum(Context context) {
        if (context == null) {
            return "";
        }
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getSimSerialNumber();
    }

    /**
     * 获取运营商信息
     * @param con 上下文
     * @return String 运营商信息
     */
    public static String getCarrier(Context con) {
        TelephonyManager telManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();
        if (imsi != null && !"".equals(imsi)) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                return "中国移动";
            } else if (imsi.startsWith("46001")) {
                return "中国联通";
            } else if (imsi.startsWith("46003")) {
                return "中国电信";
            }
        }
        return "";
    }

}