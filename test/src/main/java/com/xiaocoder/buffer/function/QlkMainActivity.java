package com.xiaocoder.buffer.function;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.buffer.QlkActivity;

/**
 * Created by xiaocoder on 2015/7/15.
 * 点击两次退出
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
            XCApplication.shortToast("快速再按一次退出");
        }
    }

    /**
     * 首页滑动不销毁activity
     */
    @Override
    protected void slideDestroyActivity() {

    }
}
