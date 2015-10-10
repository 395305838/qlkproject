package com.xiaocoder.test.http2;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xiaocoder.android.fw.general.adapter.XCAdapterTest;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.view.XCRefreshHandler;
import com.xiaocoder.android.fw.general.view.XCRefreshLayout;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;

public class MaterialRefreshActivity extends QlkActivity {
    XCRefreshLayout xcRefreshLayout;
    ListView listview;
    XCAdapterTest adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_material_refresh);
        super.onCreate(savedInstanceState);

        xcRefreshLayout = getViewById(R.id.xc_id_refreshlayout);
        listview = (ListView) base_inflater.inflate(R.layout.activity_material_listview, null);
        adapter = new XCAdapterTest(MaterialRefreshActivity.this, null);

        xcRefreshLayout.setTotalPage("3");
        xcRefreshLayout.setHandlerAndContent(new XCRefreshHandler() {
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
                XCApp.dShortToast(request_page + "--refresh");
                ArrayList<String> list = new ArrayList<String>();
                String[] strs = {"0", "1", "2", "3", "4", "5", "6", "7", "00", "11", "22", "33", "44", "55", "66", "77", "0", "1", "2", "3", "4", "5", "6", "7", "000", "111", "222", "333", "444", "555",
                        "666", "777"};
                for (String str : strs) {
                    list.add(str);
                }
                xcRefreshLayout.updateListNoAdd(list, adapter);
                xcRefreshLayout.completeRefresh();
            }

            @Override
            public void load(View view, int request_page) {
                XCApp.dShortToast(request_page + "--load");
                ArrayList<String> list = new ArrayList<String>();
                String[] strs = {"0", "1", "2", "3", "4", "5", "6", "7", "00", "11", "22", "33", "44", "55", "66", "77", "0", "1", "2", "3", "4", "5", "6", "7", "000", "111", "222", "333", "444", "555",
                        "666", "777"};
                for (String str : strs) {
                    list.add(str);
                }

                xcRefreshLayout.updateListAdd(list, adapter);
                xcRefreshLayout.completeRefresh();

            }

            @Override
            public void complete() {

            }
        }, listview, listview);

        listview.setAdapter(adapter);

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
