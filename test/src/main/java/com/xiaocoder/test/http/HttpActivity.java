package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestBean;
import com.xiaocoder.test.buffer.QlkActivity;
import com.xiaocoder.test.buffer.QlkHttpResponseHandler;

import org.apache.http.Header;

public class HttpActivity extends QlkActivity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http);
        super.onCreate(savedInstanceState);

    }

    public void request() {
        XCHttpAsyn.getAsyn(true, this, "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95"
                , new RequestParams(),
                new QlkHttpResponseHandler<TestBean>(this, TestBean.class) {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        super.onSuccess(arg0, arg1, arg2);
                        shortToast("success");
                    }

                });
    }

    @Override
    public void onNetRefresh() {
        request();
    }

    @Override
    public void initWidgets() {
        button = getViewById(R.id.test_http);
        request();
    }

    @Override
    public void listeners() {
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                request();
            }
        });
    }

}
