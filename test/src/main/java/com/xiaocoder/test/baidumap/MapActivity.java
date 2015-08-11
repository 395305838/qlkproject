package com.xiaocoder.test.baidumap;

import android.os.Bundle;

import com.xiaocoder.test.R;

import qlk.com.baidumap.XCBaseBaiduMapActivity;

public class MapActivity extends XCBaseBaiduMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    public void initWidgets() {


        setOnReceiverAddressListener(new OnReceiverAddressListener() {
            @Override
            public void onReceiverAddressListener(String addr) {
                shortToast(addr);
            }
        });

    }

    @Override
    public void listeners() {

    }


    @Override
    public void onNetRefresh() {

    }

}
