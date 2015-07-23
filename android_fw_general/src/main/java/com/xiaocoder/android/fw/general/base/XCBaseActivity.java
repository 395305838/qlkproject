package com.xiaocoder.android.fw.general.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.http.XCIHttpResult;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;
import com.xiaocoder.android.fw.general.util.UtilInputMethod;
import com.xiaocoder.android_fw_general.R;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class XCBaseActivity extends FragmentActivity implements OnClickListener, XCIHttpResult {

    public Context base_context;
    // 填充布局的对象
    public LayoutInflater base_inflater;

    public FragmentManager base_fm;

    // 整个layout
    public RelativeLayout xc_id_model_layout;
    // title
    public RelativeLayout xc_id_model_titlebar;
    // bottom
    public RelativeLayout xc_id_model_bottombar;
    // content
    public RelativeLayout xc_id_model_content;

    // 无网络时显示的界面
    public RelativeLayout xc_id_model_no_net;
    public Button xc_id_no_net_button;

    private boolean isActivityDestroied;

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
            printi(this + "回收后重新创建");
        }

        // 添加到stack
        ((XCApplication) getApplication()).addActivityToStack(this);
        base_context = this;
        base_inflater = LayoutInflater.from(this);
        base_fm = getSupportFragmentManager();
        isActivityDestroied = false;

        // 找到页面的布局控件
        xc_id_model_layout = getViewById(R.id.xc_id_model_layout);
        xc_id_model_titlebar = getViewById(R.id.xc_id_model_titlebar);
        xc_id_model_content = getViewById(R.id.xc_id_model_content);
        xc_id_model_no_net = getViewById(R.id.xc_id_model_no_net);
        xc_id_model_bottombar = getViewById(R.id.xc_id_model_bottombar);

        // 无网络的背景
        if (xc_id_model_no_net != null) {
            xc_id_model_no_net.setOnClickListener(this);
        }
        xc_id_no_net_button = getViewById(R.id.xc_id_no_net_button);
        if (xc_id_no_net_button != null) {
            xc_id_no_net_button.setOnClickListener(this);
        }
        initWidgets();
        listeners();
        showPage();
    }

    public abstract void initWidgets();

    public abstract void listeners();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    // 网络访问失败时, 调用该方法
    @Override
    public void onNetFail(boolean show_background_when_net_fail) {
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
            onNetRefresh();
        }
    }

    @Override
    protected void onDestroy() {

        XCHttpAsyn.resetNetingStatus();
        isActivityDestroied = true;
        super.onDestroy();
        getXCApplication().delActivityFromStack(this);

    }

    public boolean isActivityDestroied() {
        return isActivityDestroied;
    }

    /*
         * 获取XCApplication
         */
    public XCApplication getXCApplication() {
        return (XCApplication) getApplication();
    }

    /*
     * 获取XCBaseActivity
     */
    public XCBaseActivity getXCBaseActivity() {
        return (XCBaseActivity) this;
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
        if (fragment != null) {
            FragmentTransaction ft = base_fm.beginTransaction();
            ft.hide(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    // 之前必须有add
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = base_fm.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void myFinish() {
        UtilInputMethod.hiddenInputMethod(this);
        finish();
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

    // title和bottom fragment不隐藏
    public void hideBodyFragment() {
        List<Fragment> fragments = base_fm.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof XCBodyFragment) {
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

    // 这里得重写,否则startforresult时, 无法回调到fragment中的方法 , 如果fragment中又有嵌套的话,
    // fragmetn中的该方法也得重写
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        printi("activity---onActivityResult");
        List<Fragment> fragments = base_fm.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                printi("onActivityResult---" + fragment.toString());
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    // 动态广播
    public void myRegisterReceiver(int priority, String action, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(priority);
        filter.addAction(action);
        registerReceiver(receiver, filter);
    }

    // 动态广播
    public void myUnregisterReceiver(BroadcastReceiver receiver) {
        unregisterReceiver(receiver);
    }

    /*
     * 发送广播
     */
    public void mySendBroadcastReceiver(String action, String command_key, String command_value) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(command_key, command_value);
        sendBroadcast(intent);
    }

    public void mySendBroadcastReceiver(String action, String[] command_keys, String[] command_values) {
        Intent intent = new Intent();
        intent.setAction(action);
        int size = command_keys.length;
        for (int i = 0; i < size; i++) {
            intent.putExtra(command_keys[i], command_values[i]);
        }
        sendBroadcast(intent);
    }

    public void setGridViewStyle(GridView view, boolean show_bar, int num) {
        setGridViewStyle(view, show_bar, 0, 0, num);
    }

    public void setGridViewStyle(GridView view, boolean show_bar) {
        setGridViewStyle(view, show_bar, 0, 0, 1);
    }

    public void setGridViewStyle(GridView view, boolean show_bar, int space_h_px, int space_v_px, int num) {
        view.setCacheColorHint(0x00000000);
        view.setSelector(new ColorDrawable(0x00000000));
        view.setVerticalScrollBarEnabled(show_bar);
        view.setHorizontalSpacing(space_h_px);
        view.setVerticalSpacing(space_v_px);
//        view.setHorizontalSpacing(UtilImage.dip2px(this, space_h_px));
//        view.setVerticalSpacing(UtilImage.dip2px(this, space_v_px));
        view.setNumColumns(num);
    }

    public void setExpandListViewStyle(ExpandableListView view, boolean show_bar, int groupIndicate) {
        view.setCacheColorHint(0x00000000);
        view.setSelector(new ColorDrawable(0x00000000));
        view.setVerticalScrollBarEnabled(show_bar);
        if (groupIndicate <= 0) {
            view.setGroupIndicator(null);
        } else {
            view.setGroupIndicator(getResources().getDrawable(groupIndicate));
        }

    }

    public void setListViewStyle(ListView view, Drawable divider_drawable, int height_px, boolean show_bar) {
        view.setCacheColorHint(0x00000000);
        view.setSelector(new ColorDrawable(0x00000000));
        view.setDivider(divider_drawable);
        view.setDividerHeight(height_px);
        view.setVerticalScrollBarEnabled(show_bar);
    }

    public void setListViewStyle(ListView view, boolean show_bar) {
        setListViewStyle(view, null, 0, show_bar);
    }

    // 在onCreate()中调用，默认在onCreate()中 只显示title和content的布局，根据需求是否重写
    public void showPage() {
        showTitleLayout(true);
        showContentLayout();
    }

    public void myStartActivity(Class<? extends XCBaseActivity> activity_class, String[] command_keys, Object[] command_values) {
        Intent intent = new Intent(this, activity_class);
        int size = command_keys.length;
        for (int i = 0; i < size; i++) {
            Object obj = command_values[i];
            if (obj instanceof String) {
                intent.putExtra(command_keys[i], (String) obj);
            } else if (obj instanceof Boolean) {
                intent.putExtra(command_keys[i], (Boolean) obj);
            } else if (obj instanceof Integer) {
                intent.putExtra(command_keys[i], (Integer) obj);
            } else if (obj instanceof Serializable) {
                intent.putExtra(command_keys[i], (Serializable) obj);
            } else if (obj instanceof Parcelable) {
                intent.putExtra(command_keys[i], (Parcelable) obj);
            } else {
                throw new RuntimeException("myStartActivity()中intent的putExtra参数没有转型");
            }
        }
        startActivity(intent);
        activityAnimation();
    }

    public void myStartActivity(Class<? extends XCBaseActivity> activity_class, String key, ArrayList<String> command_values) {
        Intent intent = new Intent(this, activity_class);
        intent.putStringArrayListExtra(key, command_values);
        startActivity(intent);
        activityAnimation();
    }

    public void myStartActivity(Intent intent) {
        startActivity(intent);
        activityAnimation();
    }

    public void myStartActivity(Class<? extends XCBaseActivity> activity_class) {
        myStartActivity(activity_class, new String[]{}, new String[]{});
        activityAnimation();
    }

    private void activityAnimation() {
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (version >= 5) {
            overridePendingTransition(R.anim.xc_anim_right_in, R.anim.xc_anim_left_out);  //此为自定义的动画效果，下面两个为系统的动画效果
            //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            //overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
    }

    public void myStartActivityForResult(Class<? extends XCBaseActivity> activity_class, int requestCode, String[] command_keys, Object[] command_values) {
        Intent intent = new Intent(this, activity_class);
        int size = command_keys.length;
        for (int i = 0; i < size; i++) {
            Object obj = command_values[i];
            if (obj instanceof String) {
                intent.putExtra(command_keys[i], (String) obj);
            } else if (obj instanceof Boolean) {
                intent.putExtra(command_keys[i], (Boolean) obj);
            } else if (obj instanceof Parcelable) {
                intent.putExtra(command_keys[i], (Parcelable) obj);
            } else {
                throw new RuntimeException("myStartActivity()中intent的putExtra参数没有转型");
            }
        }
        startActivityForResult(intent, requestCode);
    }

    public void myStartActivityForResult(Intent intent, Class<? extends XCBaseActivity> activity_class, int requestCode, int flags) {
        intent.setClass(this, activity_class);
        intent.setFlags(flags);
        startActivityForResult(intent, requestCode);
    }

    public void myStartActivityForResult(Class<? extends XCBaseActivity> activity_class, int requestCode) {
        myStartActivityForResult(activity_class, requestCode, new String[]{}, new String[]{});
    }

    // ------------------------------------调试-----------------------------------------------
    // 以下受debug控制的
    public void printi(String msg) {
        XCApplication.printi(msg);
    }

    public void printi(String tag, String msg) {
        XCApplication.printi(tag, msg);
    }

    public void dShortToast(String msg) {
        XCApplication.dShortToast(msg);
    }

    public void dLongToast(String msg) {
        XCApplication.dLongToast(msg);
    }

    public void tempPrint(String msg) {
        XCApplication.tempPrint(msg);
    }

    // 以下不受debug控制的
    public void shortToast(String msg) {
        XCApplication.shortToast(msg);
    }

    public void longToast(String msg) {
        XCApplication.longToast(msg);
    }

    public void printe(Context context, Exception e) {
        XCApplication.printe(context, e);
    }

    public void printe(Context context, String msg) {
        XCApplication.printe(context, msg);
    }

    public void printe(String hint, Exception e) {
        XCApplication.printe(hint, e);
    }

    public void printe(Context context, String hint, Exception e) {
        XCApplication.printe(context, hint, e);
    }

    public void spPut(String key, boolean value) {
        XCApplication.spPut(key, value);
    }

    public void spPut(String key, int value) {
        XCApplication.spPut(key, value);
    }

    public void spPut(String key, long value) {
        XCApplication.spPut(key, value);
    }

    public void spPut(String key, float value) {
        XCApplication.spPut(key, value);
    }

    public void spPut(String key, String value) {
        XCApplication.spPut(key, value);
    }

    public String spGet(String key, String default_value) {
        return XCApplication.spGet(key, default_value);
    }

    public int spGet(String key, int default_value) {
        return XCApplication.spGet(key, default_value);
    }

    public long spGet(String key, long default_value) {
        return XCApplication.spGet(key, default_value);
    }

    public boolean spGet(String key, boolean default_value) {
        return XCApplication.spGet(key, default_value);
    }

    public float spGet(String key, float default_value) {
        return XCApplication.spGet(key, default_value);
    }

    public Map<String, ?> spGetAll() {
        return XCApplication.spGetAll();
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        XCApplication.displayImage(uri, imageView, options);
    }

    public void displayImage(String uri, ImageView imageView) {
        displayImage(uri, imageView, XCImageLoaderHelper.getDisplayImageOptions());
    }


}
