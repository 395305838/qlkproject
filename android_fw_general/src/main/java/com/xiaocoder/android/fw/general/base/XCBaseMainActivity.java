package com.xiaocoder.android.fw.general.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android_fw_general.R;

// 首页继承该类 
// 1 该类还有点击两次返回按钮 退出应用的功能
// 2 该类中有一个 base_tab_group引用 ， 未初始化
// 3 该类中有一个记录base_recoder_selected_tab_item的常量,未初始化

// 4 以后该类可以加入 更新 推送 等第三方的初始化功能代码

public abstract class XCBaseMainActivity extends XCBaseActivity implements RadioGroup.OnCheckedChangeListener {

    public RadioGroup base_tab_group;
    public int base_recoder_selected_tab_item;

    // 双击两次返回键退出应用
    long back_quit_time;

    @Override
    public void onBackPressed() {
        long this_quit_time = System.currentTimeMillis();
        if (this_quit_time - back_quit_time <= 1000) {
            getXCApplication().AppExit(base_context);
        } else {
            back_quit_time = this_quit_time;
            shortToast("快速再按一次退出");
        }
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UMUpdate();
    }


    /*
    * 友盟更新
    */
    private void UMUpdate() {
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        String upgrade_mode = MobclickAgent.getConfigParams(this, "upgrade_mode");
        if (TextUtils.isEmpty(upgrade_mode)) {
            return;
        }
        String[] upgrade_mode_array = upgrade_mode.split(";");
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.forceUpdate(this);
        for (String mode : upgrade_mode_array) {
            String versionName = ((XCApplication) getApplication()).getVersionName();
            versionName = versionName + "f";
            if (mode.equals(versionName)) {
                //进入强制更新
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {

                    }
                });
                UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
                    @Override
                    public void onClick(int status) {
                        printi("setDialogListener status:" + status);
                        switch (status) {
                            case UpdateStatus.Update:
                                break;
                            default:
                                //退出应用
                                shortToast(getString(R.string.force_update_toast_string));
                                (getXCApplication()).AppExit(XCBaseMainActivity.this);
                        }
                    }
                });
                break;
            }
        }
    }
}