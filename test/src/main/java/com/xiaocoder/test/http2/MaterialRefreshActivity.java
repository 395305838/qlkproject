package com.xiaocoder.test.http2;


import android.os.Bundle;

import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;


public class MaterialRefreshActivity extends QlkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_material_refresh);
        super.onCreate(savedInstanceState);
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
