package com.xiaocoder.test.excpetion;

import android.os.Bundle;

import com.xiaocoder.middle.QlkActivity;
import com.xiaocoder.test.R;

public class ExceptionActivity extends QlkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exception);
        super.onCreate(savedInstanceState);

        int a = 1 / 0;

    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
    }

    @Override
    public void initWidgets() {
    }

    @Override
    public void listeners() {

    }

}