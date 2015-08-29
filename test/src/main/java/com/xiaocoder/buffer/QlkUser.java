package com.xiaocoder.buffer;

import android.content.Context;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;

/**
 * Created by xiaocoder on 2015/7/13.
 */
public class QlkUser {

    public static String USER_NAME = " ";
    public static String USER_ID = " ";
    public static String USER_TOKEN = " ";
    public static String IS_LOGIN = "isLogin";

    public static String getUserId() {

        return XCApplication.spGet(USER_ID, "");

    }

    public static String getUserToken() {

        return XCApplication.spGet(USER_TOKEN, "");

    }

    public static String getUserName() {

        return XCApplication.spGet(USER_NAME, "");

    }

    public static boolean isLogin() {

        return XCApplication.spGet(IS_LOGIN, false);

    }

    public static void putUserId(String userId) {

        XCApplication.spPut(USER_ID, userId);

    }

    public static void putUserToken(String userToken) {

        XCApplication.spPut(USER_TOKEN, userToken);

    }

    public static void putUserName(String userName) {

        XCApplication.spPut(USER_NAME, userName);

    }

    public static void putLogin(boolean isLogin) {

        XCApplication.spPut(IS_LOGIN, isLogin);

    }

    public static void loginOut(Class<? extends XCBaseActivity> classes, Context context) {

    }

    public static void refreshUserInfo(Context context) {

    }

}
