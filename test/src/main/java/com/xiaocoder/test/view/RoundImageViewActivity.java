package com.xiaocoder.test.view;

import android.os.Bundle;
import android.view.View;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.view.XCRoundedImageView;
import com.xiaocoder.test.R;

public class RoundImageViewActivity extends XCBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_round_image_view);
        super.onCreate(savedInstanceState);
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
    }

    // 初始化控件
    @Override
    public void initWidgets() {

        XCRoundedImageView image3 = getViewById(R.id.image3);
        XCApplication.displayImage("http://www.baidu.com/img/bdlogo.png", image3);

    }

    // 设置监听
    @Override
    public void listeners() {

    }


}
