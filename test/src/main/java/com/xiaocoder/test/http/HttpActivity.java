package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.buffer.parse.QlkResponseHandlerModel;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestModel;

import org.apache.http.Header;

import java.util.HashMap;

public class HttpActivity extends QlkActivity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http);
        super.onCreate(savedInstanceState);

    }

    public void request() {
        XCHttpAsyn.getAsyn(true, this, "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95"
                , new HashMap<String,Object>(),
                new QlkResponseHandlerModel<TestModel>(this, TestModel.class) {

                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);

                        XCApp.dShortToast("success");

                        if (result_boolean) {

                            XCApp.i(result_bean.toString());
                            XCApp.i(result_bean.getMsg());
                            XCApp.i(result_bean.getCode() + "");
                            XCApp.i(result_bean.getData().getResult().toString() + "");
                            XCApp.i(result_bean.getData().getTotalCount() + "");
                            XCApp.i(result_bean.getData().getTotalPages() + "");
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
