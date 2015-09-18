package com.xiaocoder.buffer.function;

import android.os.Bundle;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.buffer.QlkActivity;

/**
 * Created by xiaocoder on 2015/7/15.
 * 首页activity，点击两次退出
 */
public abstract class QlkMainActivity extends QlkActivity {

    // 双击两次返回键退出应用
    long back_quit_time;

    @Override
    public void onBackPressed() {
        long this_quit_time = System.currentTimeMillis();
        if (this_quit_time - back_quit_time <= 1000) {
            getXCApplication().AppExit(base_context);
        } else {
            back_quit_time = this_quit_time;
            XCApp.shortToast("快速再按一次退出");
        }
    }

    /**
     * 首页的activity不可滑动销毁
     */
    @Override
    protected void slideDestroyActivity() {

    }

    /**
     * 友盟统计的一些初始化
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(getApplicationContext());
        AnalyticsConfig.enableEncrypt(true);
    }


}
