package com.xiaocoder.android.fw.general.base.function;

import android.widget.RadioGroup;

import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.helper.XCExecutorHelper;

// 首页继承该类 
// 1 该类还有点击两次返回按钮 退出应用的功能
// 2 该类中有一个 base_tab_group引用 ， 未初始化
// 3 该类中有一个记录base_recoder_selected_tab_item的常量,未初始化

// 4 以后该类的子类可以加入 更新 推送 等第三方的初始化功能代码

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
}