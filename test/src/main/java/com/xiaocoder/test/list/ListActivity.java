package com.xiaocoder.test.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.adapter.XCAdapterTest;
import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.base.function.XCBaseAbsListFragment.OnAbsListItemClickListener;
import com.xiaocoder.android.fw.general.base.function.XCBaseAbsListFragment.OnRefreshNextPageListener;
import com.xiaocoder.android.fw.general.fragment.XCListViewFragment;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.http.XCHttpResponseHandler;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.view.XCGridView;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestBean;
import com.xiaocoder.test.buffer.QlkActivity;
import com.xiaocoder.test.buffer.QlkHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/*
 复制这里：activity在注册清单文件的配置 
 <activity
 android:name="com.xiaocoder.android.ContactsActivity"
 android:screenOrientation="portrait"
 android:windowSoftInputMode="adjustResize|stateHidden" >
 </activity>*/
public class ListActivity extends QlkActivity {
    XCListViewFragment list_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_list);
        super.onCreate(savedInstanceState);
    }

    public void request() {

        RequestParams params = new RequestParams();
//        XCHttpAsyn.getAsyn(true, this, "http://" + MainActivity.TEST_HOST + ":8080/qlktest/listdata.json", params, new QlkHttpResponseHandler(this, list_fragment) {
        XCHttpAsyn.getAsyn(true, this, "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95"
                , params,
                new QlkHttpResponseHandler<TestBean>(this, TestBean.class) {
                    @Override
                    public void onSuccess(int code, Header[] headers, byte[] arg2) {
                        super.onSuccess(code, headers, arg2);
                        if (result_boolean) {

                            if (!list_fragment.checkGoOn()) {
                                return;
                            }

                            String msg = result_bean.getMsg();
                            printi("---第一层---->" + msg);

                            TestBean testBean = result_bean.obtModel(result_bean.data);
                            printi("---第二层---->" + testBean.toString());

                            List<TestBean> testBeans = testBean.obtList(result_bean.result);
                            printi("---第三层---->" + testBeans.toString());

                            for (TestBean bean : testBeans) {
                                printi("getCommission()--->" + bean.getCommission());
                                printi("getMarketPrice()--->" + bean.getMarketPrice());
                                printi("getProudctId()--->" + bean.getProudctId());
                            }


                            // grid_fragment.setTotalNum("100");// 或者setTotalPage也可以
                            list_fragment.setTotalPage("3");
                            list_fragment.updateList(testBeans);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (result_boolean && list_fragment != null) {
                            list_fragment.completeRefresh();
                        }
                    }
                });
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
        request();
    }

    class TestAdatpter extends XCBaseAdapter<TestBean> {
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

        public TestAdatpter(Context context, List<TestBean> list) {
            super(context, list);
        }

        class ViewHolder {
            TextView xc_id_adapter_test_textview;
        }
    }

    @Override
    public void initWidgets() {
        list_fragment = new XCListViewFragment();
        list_fragment.setAdapter(new TestAdatpter(this, null));
//        list_fragment.setAdapter(new XCAdapterTest(this, null));
        // 可以不设置Mode， 默认是不可以拉的listview
        list_fragment.setMode(XCListViewFragment.MODE_UP_DOWN);
        list_fragment.setBgZeroHintInfo("数据为0", "重新加载", R.drawable.xc_d_chat_face);
        list_fragment.setListViewStyleParam(false);
        addFragment(R.id.xc_id_model_content, list_fragment);
        request();
    }

    @Override
    public void listeners() {
        // 设置背景为0的监听
        list_fragment.setOnBgZeroButtonClickToDoListener(new XCListViewFragment.OnBgZeroButtonClickToDoListener() {

            @Override
            public void onBgZeroButtonClickToDo() {
                request();
            }
        });

        list_fragment.setOnRefreshNextPageListener(new OnRefreshNextPageListener() {

            @Override
            public void onRefreshNextPageListener(int current_page) {
                request();
            }
        });

        list_fragment.setOnListItemClickListener(new OnAbsListItemClickListener() {

            @Override
            public void onAbsListItemClickListener(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dShortToast(arg2 + "");
                printi(arg2 + "");
            }

        });
    }

}
