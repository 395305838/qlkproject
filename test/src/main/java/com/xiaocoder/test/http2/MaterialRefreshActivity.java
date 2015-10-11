package com.xiaocoder.test.http2;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.xiaocoder.android.fw.general.adapter.XCAdapterTest;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.view.XCRefreshHandler;
import com.xiaocoder.android.fw.general.view.XCRefreshLayout;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MaterialRefreshActivity extends QlkActivity {

    XCAdapterTest adapter;
    XCRefreshLayout xcRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_material_refresh);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    private ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<String>();
        String[] strs = {"0", "1", "2", "3", "4", "5", "6", "7", "00", "11", "22", "33", "44", "55", "66", "77", "0", "1", "2", "3", "4", "5", "6", "7", "000", "111", "222", "333", "444", "555",
                "666", "777"};
        for (String str : strs) {
            list.add(str);
        }
        return list;
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
    }

    @Override
    public void initWidgets() {

        adapter = new XCAdapterTest(MaterialRefreshActivity.this, null);

        xcRefreshLayout = getViewById(R.id.xc_id_refreshlayout);
        xcRefreshLayout.getListView().setAdapter(adapter);

        // http请求中获取，这里为模拟数据
        xcRefreshLayout.setTotalPage("3");

    }

    @Override
    public void listeners() {
        xcRefreshLayout.setHandler(new XCRefreshHandler() {
            @Override
            public boolean canRefresh() {
                return true;
            }

            @Override
            public boolean canLoad() {
                return true;
            }

            @Override
            public void refresh(View view, int request_page) {
                XCApp.shortToast(request_page + "--refresh");

                xcRefreshLayout.updateListNoAdd(getList(), adapter);
                xcRefreshLayout.completeRefresh();
            }

            @Override
            public void load(View view, int request_page) {
                XCApp.shortToast(request_page + "--load");

                xcRefreshLayout.updateListAdd(getList(), adapter);
                xcRefreshLayout.completeRefresh();
            }
        });
    }
}
