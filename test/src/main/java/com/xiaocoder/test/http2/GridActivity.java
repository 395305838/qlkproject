package com.xiaocoder.test.http2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.fragment.listview.XCBaseAbsListFragment.OnAbsListItemClickListener;
import com.xiaocoder.android.fw.general.fragment.listview.XCBaseAbsListFragment.OnRefreshNextPageListener;
import com.xiaocoder.android.fw.general.fragment.listview.XCGridViewFragment;
import com.xiaocoder.android.fw.general.fragment.listview.XCListViewFragment;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.util.Utils;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.buffer.parse.QlkResponseHandlerModel;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestModel;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;


public class GridActivity extends QlkActivity {
    XCGridViewFragment grid_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_list);
        super.onCreate(savedInstanceState);
    }

    public void request() {

        HashMap<String,Object> params =  new HashMap<String,Object>();
        XCHttpAsyn.getAsyn(true, this,
                "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95",
                params,
                new QlkResponseHandlerModel<TestModel>(this, TestModel.class) {
                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);

                        if (result_boolean) {
                            if (!grid_fragment.checkGoOn()) {
                                return;
                            }

                            TestModel.DataEntity data = result_bean.getData();

                            List<TestModel.DataEntity.ResultEntity> result = data.getResult();

                            XCApp.i(result_bean.toString());
                            XCApp.i(result_bean.getMsg());
                            XCApp.i(result_bean.getCode() + "");
                            XCApp.i(result_bean.getData().getTotalCount() + "");
                            XCApp.i(result_bean.getData().getTotalPages() + "");

                            if (!Utils.isListBlank(result)) {
                                XCApp.i(result_bean.getData().getResult().toString() + "");
                                XCApp.i(result_bean.getData().getResult().get(0).getCommission() + "");
                                XCApp.i(result_bean.getData().getResult().get(0).getImgUrl() + "");
                                XCApp.i(result_bean.getData().getResult().get(0).getMarketPrice() + "");
                                XCApp.i(result_bean.getData().getResult().get(0).getName() + "");
                                XCApp.i(result_bean.getData().getResult().get(1).getRebate() + "");
                                XCApp.i(result_bean.getData().getResult().get(2).getType() + "");
                                XCApp.i(result_bean.getData().getResult().get(2).getShare().getBaseUrl() + "");
                                XCApp.i(result_bean.getData().getResult().get(2).getShare().getContent() + "");
                                XCApp.i(result_bean.getData().getResult().get(2).getShare().getTitle() + "");
                                XCApp.i(result_bean.getData().getResult().get(2).getShare().getIcon() + "");
                                XCApp.i(result_bean.getData().getResult().get(1).getImgUrl());
                            }

                            // grid_fragment.setTotalNum("100");// 或者setTotalPage也可以
                            // grid_fragment.setTotalPage(totalnum % PER_PAGE_NUM == 0 ? totalnum/PER_PAGE_NUM :(totalnum / PER_PAGE_NUM) + 1)
                            grid_fragment.setTotalPage("3");
                            grid_fragment.updateList(result);
                        }

                    }

                    @Override
                    public void finish() {
                        super.finish();
                        if (grid_fragment != null) {
                            grid_fragment.completeRefresh();
                        }
                    }
                });
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
        request();
    }

    class TestAdatpter extends XCBaseAdapter<TestModel.DataEntity.ResultEntity> {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            bean = list.get(position);
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_test_item, null);
                holder = new ViewHolder();
                holder.xc_id_adapter_test_textview = (TextView) convertView.findViewById(R.id.xc_id_adapter_test_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 获取和设置控件的显示值
            holder.xc_id_adapter_test_textview.setText(bean.getCommission());
            // 加载图片
            return convertView;
        }

        public TestAdatpter(Context context, List<TestModel.DataEntity.ResultEntity> list) {
            super(context, list);
        }

        class ViewHolder {
            TextView xc_id_adapter_test_textview;

        }
    }

    @Override
    public void initWidgets() {
        grid_fragment = new XCGridViewFragment();
        grid_fragment.setAdapter(new TestAdatpter(this, null));
//        grid_fragment.setAdapter(new XCAdapterTest(this, null));
        // 可以不设置Mode， 默认是不可以拉的listview
        grid_fragment.setMode(XCListViewFragment.MODE_UP_DOWN);
        grid_fragment.setBgZeroHintInfo("数据为0", "重新加载", R.drawable.xc_d_chat_face);
        grid_fragment.setGridViewStyleParam(false, 1, 1, 2);
        addFragment(R.id.xc_id_model_content, grid_fragment);
        request();
    }

    @Override
    public void listeners() {
        // 设置背景为0的监听
        grid_fragment.setOnBgZeroButtonClickToDoListener(new XCListViewFragment.OnBgZeroButtonClickToDoListener() {

            @Override
            public void onBgZeroButtonClickToDo() {
                request();
            }
        });

        grid_fragment.setOnRefreshNextPageListener(new OnRefreshNextPageListener() {

            @Override
            public void onRefreshNextPageListener(int current_page) {
                request();
            }
        });

        grid_fragment.setOnListItemClickListener(new OnAbsListItemClickListener() {

            @Override
            public void onAbsListItemClickListener(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                XCApp.dShortToast(arg2 + "");
                XCApp.i(arg2 + "");
            }
        });
    }

}
