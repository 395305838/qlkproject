package com.xiaocoder.middle;

import android.content.Context;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;

/**
 * Created by xiaocoder on 2015/7/13.
 * <p/>
 * 用户的信息，登录状态，用户刷新，注销 等统一在这里存取
 */
public class QlkUser {

    public static String USER_NAME = "userName";
    public static String USER_ID = "userId";
    public static String USER_TOKEN = "userToken";
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

    /**
     * 获取登录状态
     *
     * @return
     */
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

    /**
     * 保存登录状态
     *
     * @param isLogin
     */
    public static void putLogin(boolean isLogin) {

        XCApp.spPut(IS_LOGIN, isLogin);

    }

    /**
     * 注销
     *
     * @param classes
     * @param context
     */
    public static void loginOut(Class<? extends XCBaseActivity> classes, Context context) {

    }

    /**
     * 刷新用户信息
     *
     * @param context
     */
    public static void refreshUserInfo(Context context) {

    }

}
