package com.xiaocoder.android.fw.general.application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.util.UtilInputMethod;
import com.xiaocoder.android.fw.general.view.XCSwipeBackLayout;
import com.xiaocoder.android_fw_general.R;

import java.lang.reflect.Constructor;
import java.util.List;

public abstract class XCBaseActivity extends FragmentActivity implements OnClickListener, XCIHttpResult {

    public Context base_context;

    public LayoutInflater base_inflater;

    public FragmentManager base_fm;

    public XCSwipeBackLayout back_layout;

    // 整个layout
    public ViewGroup xc_id_model_layout;
    // title
    public ViewGroup xc_id_model_titlebar;
    // bottom
    public ViewGroup xc_id_model_bottombar;
    // content
    public ViewGroup xc_id_model_content;

    // 无网络时显示的界面
    public ViewGroup xc_id_model_no_net;
    public Button xc_id_no_net_button;

    // activity是否销毁
    private boolean isActivityDestroied;

    /**
     * 记录网络失败的请求，待重刷新
     */
    private XCIResponseHandler recoderNetFailHandler;


    @SuppressWarnings("unchecked")
    public <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    // 别的应用调用时传进来的
    protected Uri interceptUri() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri != null) {
                return uri;
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            XCApp.e(this, "回收后重新创建");
        }

        // 添加到stack
        ((XCApp) getApplication()).addActivityToStack(this);
        base_context = this;
        base_inflater = LayoutInflater.from(this);
        base_fm = getSupportFragmentManager();
        isActivityDestroied = false;

        initSlideDestroyActivity();

        // 找到页面的布局控件
        xc_id_model_layout = getViewById(R.id.xc_id_model_layout);
        xc_id_model_titlebar = getViewById(R.id.xc_id_model_titlebar);
        xc_id_model_content = getViewById(R.id.xc_id_model_content);
        xc_id_model_bottombar = getViewById(R.id.xc_id_model_bottombar);
        xc_id_model_no_net = getViewById(R.id.xc_id_model_no_net);
        xc_id_no_net_button = getViewById(R.id.xc_id_no_net_button);

        // 无网络的背景
        if (xc_id_model_no_net != null) {
            xc_id_model_no_net.setOnClickListener(this);
        }
        if (xc_id_no_net_button != null) {
            xc_id_no_net_button.setOnClickListener(this);
        }
        initWidgets();
        listeners();
        showPage();
    }

    /**
     * 手势滑动退出activity的基类布局
     * 想要实现向右滑动删除Activity，只需要继承该activity即可
     */
    protected void initSlideDestroyActivity() {

        back_layout = ((XCSwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.baseactivity_swipe_back, null));

        back_layout.attachToActivity(this);

    }

    public abstract void initWidgets();

    public abstract void listeners();

    // 网络访问失败时, 调用该方法
    @Override
    public void onNetFail(XCIResponseHandler resHandler, boolean show_background_when_net_fail) {

        recoderNetFailHandler = resHandler;

        if (show_background_when_net_fail) {
            showNoNetLayout();
        }
    }

    public void showNoNetLayout() {

        if (xc_id_model_content != null) {
            setViewVisible(false, xc_id_model_content);
        }

        if (xc_id_model_no_net != null) {
            setViewVisible(true, xc_id_model_no_net);
        }

    }

    // 网络访问成功时访问该方法
    @Override
    public void onNetSuccess() {
        showContentLayout();
    }

    // 该方法不可以删除 ，防止fragment被保存
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xc_id_no_net_button || id == R.id.xc_id_model_no_net) {
            // 无网络时，点击刷新
            refreshNet();
        }
    }

    public void refreshNet() {
        XCApp.sendHttpRequest(recoderNetFailHandler);
    }

    @Override
    protected void onDestroy() {

        XCApp.resetNetingStatus();
        isActivityDestroied = true;
        recoderNetFailHandler = null;
        super.onDestroy();

        getXCApplication().delActivityFromStack(this);

    }

    public void myFinish() {
        UtilInputMethod.hiddenInputMethod(this);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityEndAnimation();
    }

    public void activityEndAnimation() {
        overridePendingTransition(0, R.anim.baseactivity_slide_right_out);
    }

    public boolean isActivityDestroied() {
        return isActivityDestroied;
    }

    public XCIResponseHandler getRecoderNetFailHandler() {
        return recoderNetFailHandler;
    }

    public XCApp getXCApplication() {
        return (XCApp) getApplication();
    }

    public XCBaseActivity getXCBaseActivity() {
        return this;
    }

    public void addFragment(int layout_id, Fragment fragment, String tag, boolean isToBackStack) {
        FragmentTransaction ft = base_fm.beginTransaction();
        ft.add(layout_id, fragment, tag);
        if (isToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();
        base_fm.executePendingTransactions();
    }

    // 默认不添加到backstack
    public void addFragment(int layout_id, Fragment fragment, String tag) {
        addFragment(layout_id, fragment, tag, false);
    }

    // 默认不添加到backstack
    public void addFragment(int layout_id, Fragment fragment) {
        addFragment(layout_id, fragment, fragment.getClass().getSimpleName(), false);
    }

    public void replaceFragment(int layout_id, Fragment fragment, String tag, boolean isToBackStack) {
        FragmentTransaction ft = base_fm.beginTransaction();
        ft.replace(layout_id, fragment, tag);
        if (isToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();
        base_fm.executePendingTransactions();
    }

    // 默认不添加到backstack
    public void replaceFragment(int layout_id, Fragment fragment, String tag) {
        replaceFragment(layout_id, fragment, tag, false);
    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = base_fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
        base_fm.executePendingTransactions();
    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction ft = base_fm.beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }

    // 之前必须有add
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = base_fm.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    @SuppressWarnings("unchecked")
    public <T extends Fragment> T getFragmentByTag(String tag) {
        return (T) base_fm.findFragmentByTag(tag);
    }

    // add + show
    public Fragment showFragmentByClass(Class<? extends Fragment> fragment_class, int layout_id) {
        // 显示点击的哪个fragment
        Fragment fragment = getFragmentByTag(fragment_class.getSimpleName());
        if (fragment == null) {
            try {
                Constructor<? extends Fragment> cons = fragment_class.getConstructor();
                fragment = cons.newInstance();
                addFragment(layout_id, fragment, fragment.getClass().getSimpleName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showFragment(fragment);
        }
        return fragment;
    }

    // title等别的fragment不隐藏
    public void hideBodyFragment() {
        List<Fragment> fragments = (base_fm.getFragments());
        for (Fragment fragment : fragments) {
            if (((XCBaseFragment) fragment).isBodyFragment()) {
                hideFragment(fragment);
            }
        }
    }

    // 隐藏所有fragment , 含title和bottomfragment
    public void hideAllFragment() {
        List<Fragment> fragments = base_fm.getFragments();
        for (Fragment fragment : fragments) {
            hideFragment(fragment);
        }
    }

    public void showTitleLayout(boolean isVisible) {
        if (xc_id_model_titlebar != null) {
            setViewGone(isVisible, xc_id_model_titlebar);
        }
    }

    public void showBottomLayout(boolean isVisible) {
        if (xc_id_model_bottombar != null) {
            setViewGone(isVisible, xc_id_model_bottombar);
        }
    }

    public void showContentLayout() {
        if (xc_id_model_content != null) {
            setViewVisible(true, xc_id_model_content);
        }

        if (xc_id_model_no_net != null) {
            setViewVisible(false, xc_id_model_no_net);
        }
    }

    public void setViewGone(boolean isGone, View view) {
        if (isGone) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void setViewVisible(boolean isVisible, View view) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    // 这里得重写,否则startforresult时, 无法回调到fragment中的方法 ,
    // 如果fragment中又有嵌套的话,fragmetn中的该方法也得重写
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XCApp.i(this + "---onActivityResult");
        List<Fragment> fragments = base_fm.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                XCApp.i(this + "onActivityResult---" + fragment.toString());
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    // 在onCreate()中调用，默认在onCreate()中 只显示title和content的布局，根据需求是否重写
    public void showPage() {
        showTitleLayout(true);
        showContentLayout();
    }

    public void activityStartAnimation() {
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (version >= 5) {
            // overridePendingTransition(R.anim.xc_anim_right_in, R.anim.xc_anim_left_out);  //此为自定义的动画效果，下面两个为系统的动画效果
            overridePendingTransition(R.anim.baseactivity_slide_right_in, R.anim.baseactivity_slide_remain);
            //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            //overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
    }

}
