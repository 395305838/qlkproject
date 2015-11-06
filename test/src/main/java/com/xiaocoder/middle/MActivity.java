package com.xiaocoder.middle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.test.R;
import com.xiaocoder.views.view.XCSwipeBackLayout;

/**
 * Created by xiaocoder on 2015/7/15.
 * <p/>
 * 无网络的界面样式与重刷新、友盟统计的初始化等
 */
public abstract class MActivity extends XCBaseActivity implements View.OnClickListener, XCIHttpResult {

    /**
     * 向右滑动，销毁activity
     */
    public XCSwipeBackLayout back_layout;
    /**
     * 无网络时显示的界面
     */
    private ViewGroup xc_id_model_no_net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNoNetBg();

        initSlideDestroyActivity();

    }

    /**
     * 无网络背景控件
     */
    protected void initNoNetBg() {
        xc_id_model_no_net = getViewById(R.id.xc_id_model_no_net);

        if (xc_id_model_no_net != null) {
            xc_id_model_no_net.setOnClickListener(this);
        }
    }

    /**
     * 无网络的背景
     */
    public void showNoNetLayout(boolean visiable) {
        if (xc_id_model_no_net != null) {
            setViewVisible(visiable, xc_id_model_no_net);
        }
    }

    /**
     * 网络访问失败时, 回调该方法
     */
    @Override
    public void onNetFail(XCIResponseHandler resHandler, boolean show_background_when_net_fail) {

        setRecoderNetFailHandler(resHandler);

        if (show_background_when_net_fail) {
            // 隐藏内容的背景
            showContentLayout(false);
            // 显示网络失败的背景
            showNoNetLayout(true);
        } else {
            // 不显示网络失败的背景
            showNoNetLayout(false);
            // 显示内容的背景
            showContentLayout(true);
        }
    }

    /**
     * 网络访问成功时，回调该方法
     */
    @Override
    public void onNetSuccess() {
        showNoNetLayout(false);
        showContentLayout(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xc_id_model_no_net) {
            // 点击无网络界面刷新
            refreshNetFailHandler();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPause(this);
    }

    /**
     * 手势滑动退出activity的基类布局
     */
    protected void initSlideDestroyActivity() {

        back_layout = ((XCSwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.baseactivity_swipe_back, null));

        back_layout.attachToActivity(this);

    }
}
