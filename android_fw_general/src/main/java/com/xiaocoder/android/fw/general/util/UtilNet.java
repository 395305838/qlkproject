package com.xiaocoder.android.fw.general.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.xiaocoder.android.fw.general.application.XCApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class UtilNet {

    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否是wifi
     */
    public static boolean isNetWorkWifi(Context context) {
        if (isNetworkAvailable(context)) {
            ConnectivityManager conManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private static String getWifiMacAddress(Context context) {
        // 在wifi未开启状态下，仍然可以获取MAC地址
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            return info.getMacAddress();
        } else {
            return "0";
        }
    }

    /**
     * ip地址
     */
    public static String getIpAddr() {
        String ip = "000.000.000.000";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress().toString();
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return ip;
    }

    /**
     * 获取网络类型
     *
     * @return String 返回网络类型
     */
    public static String getAccessNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int type = cm.getActiveNetworkInfo().getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "wifi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            return "phone";
        }
        return "";
    }

    /**
     * 获得当前网络类型
     *
     * @return TYPE_MOBILE_CMNET:1 TYPE_MOBILE_CMWAP:2 TYPE_WIFI:3 TYPE_NO:0(未知类型)
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获得当前网络信息
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable()) {
            int currentNetWork = ni.getType();
            if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
                if (ni.getExtraInfo() != null && ni.getExtraInfo().equals("cmwap")) {
                    XCApplication.printi("", "当前网络为:cmwap网络");
                    return TYPE_MOBILE_CMWAP;
                } else if (ni.getExtraInfo() != null && ni.getExtraInfo().equals("uniwap")) {
                    XCApplication.printi("", "当前网络为:uniwap网络");
                    return TYPE_MOBILE_CMWAP;
                } else if (ni.getExtraInfo() != null && ni.getExtraInfo().equals("3gwap")) {
                    XCApplication.printi("", "当前网络为:3gwap网络");
                    return TYPE_MOBILE_CMWAP;
                } else if (ni.getExtraInfo() != null && ni.getExtraInfo().contains("ctwap")) {
                    XCApplication.printi("", "当前网络为:" + ni.getExtraInfo() + "网络");
                    return TYPE_MOBILE_CTWAP;
                } else {
                    XCApplication.printi("", "当前网络为:net网络");
                    return TYPE_MOBILE_CMNET;
                }

            } else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
                XCApplication.printi("", "当前网络为:WIFI网络");
                return TYPE_WIFI;
            }
        }
        XCApplication.printi("", "当前网络为:不是我们考虑的网络");
        return TYPE_NO;
    }

    public static int TYPE_NO = 0;
    public static int TYPE_MOBILE_CMNET = 1;
    public static int TYPE_MOBILE_CMWAP = 2;
    public static int TYPE_WIFI = 3;
    public static int TYPE_MOBILE_CTWAP = 4; // 移动梦网代理
}
