package com.xiaocoder.middle.function;

import android.os.Bundle;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.db.XCExceptionDao;
import com.xiaocoder.android.fw.general.model.XCExceptionModel;
import com.xiaocoder.middle.MActivity;

import java.util.List;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public abstract class MMainActivity extends MActivity {

    // 双击两次返回键退出应用
    long back_quit_time;
    public static final int CLICK_QUICK_GAP = 1000;

    @Override
    public void onBackPressed() {
        long this_quit_time = System.currentTimeMillis();
        if (this_quit_time - back_quit_time <= CLICK_QUICK_GAP) {
            getXCApplication().appExit();
        } else {
            back_quit_time = this_quit_time;
            XCApp.shortToast("快速再按一次退出");
        }
    }

    /**
     * 首页的activity不可滑动销毁
     */
    @Override
    protected void initSlideDestroyActivity() {

    }

    /**
     * 友盟统计的一些初始化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(getApplicationContext());
        AnalyticsConfig.enableEncrypt(true);
        uploadException();
    }

    /**
     * 进入首页时或闪屏页，把未上传的异常信息上传到服务器
     */
    protected void uploadException() {
        List<XCExceptionModel> xcExceptionModels = XCApp.getBase_crashHandler().getDao().queryUploadFail(XCExceptionDao.SORT_DESC);
        // TODO 上传到服务器，每上传成功一条，更新model中的uploadSuccess为“1”
    }


}
