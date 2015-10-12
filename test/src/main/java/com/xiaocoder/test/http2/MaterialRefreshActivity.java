package com.xiaocoder.test.http2;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.xiaocoder.android.fw.general.adapter.XCAdapterTest;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.view.XCRefreshHandler;
import com.xiaocoder.android.fw.general.view.XCListRefreshLayout;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;

public class MaterialRefreshActivity extends QlkActivity {

    XCAdapterTest adapter;
    XCListRefreshLayout xcListRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_material_refresh);
        super.onCreate(savedInstanceState);
    }

    public static int i = 1 ;

    @NonNull
    private ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<String>();
        String[] strs = {"0", "1", "2", "3", "4", "5", "6", "7", "00", "11", "22", "33", "44", "55", "66", "77", "0", "1", "2", "3", "4", "5", "6", "7", "000", "111", "222", "333", "444", "555",
                "666", "777"};
        for (String str : strs) {
            list.add(str+"--"+i);
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

        xcListRefreshLayout = getViewById(R.id.xc_id_refreshlayout);
        xcListRefreshLayout.getListView().setAdapter(adapter);

        // http请求中获取，这里为模拟数据
        xcListRefreshLayout.setBgZeroHintInfo("无数据","点击刷新",R.drawable.icon);
        xcListRefreshLayout.setTotalPage("0");// 数据为0的背景
//        xcListRefreshLayout.setTotalPage("3");

    }

    @Override
    public void listeners() {
        xcListRefreshLayout.setHandler(new XCRefreshHandler() {
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
                XCApp.i(request_page+"--refresh");
                i=1;
                xcListRefreshLayout.updateListNoAdd(getList(), adapter);
                xcListRefreshLayout.completeRefresh();
            }

            @Override
            public void load(View view, int request_page) {
                XCApp.shortToast(request_page + "--load");
                XCApp.i(request_page + "--load");
                i++;
                xcListRefreshLayout.updateListAdd(getList(), adapter);
                xcListRefreshLayout.completeRefresh();
            }
        });

        xcListRefreshLayout.setOnBgZeroButtonClickToDoListener(new XCListRefreshLayout.OnBgZeroButtonClickToDoListener() {
            @Override
            public void onBgZeroButtonClickToDo() {
                XCApp.shortToast("click");
            }
        });

    }
}
