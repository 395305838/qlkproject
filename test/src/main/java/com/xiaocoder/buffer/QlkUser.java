package com.xiaocoder.buffer;

import android.content.Context;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;

/**
 * Created by xiaocoder on 2015/7/13.
 *
 * 用户的信息，统一在这里存取
 */
public class QlkUser {

    public static String USER_NAME = " ";
    public static String USER_ID = " ";
    public static String USER_TOKEN = " ";
    public static String IS_LOGIN = "isLogin";

    public static String getUserId() {

        return XCApp.spGet(USER_ID, "");

    }

    public static String getUserToken() {

        return XCApp.spGet(USER_TOKEN, "");

    }

    public static String getUserName() {

        return XCApp.spGet(USER_NAME, "");

    }

    public static boolean isLogin() {

        return XCApp.spGet(IS_LOGIN, false);

    }

    public static void putUserId(String userId) {

        XCApp.spPut(USER_ID, userId);

    }

    public static void putUserToken(String userToken) {

        XCApp.spPut(USER_TOKEN, userToken);

    }

    public static void putUserName(String userName) {

        XCApp.spPut(USER_NAME, userName);

    }

    public static void putLogin(boolean isLogin) {

        XCApp.spPut(IS_LOGIN, isLogin);

    }

    public static void loginOut(Class<? extends XCBaseActivity> classes, Context context) {

    }

    public static void refreshUserInfo(Context context) {

    }

}
