package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.buffer.QlkResponseHandler;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestModel;

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
                new QlkResponseHandler<TestModel>(this, TestModel.class) {

                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);

                        dShortToast("success");

                        if (result_boolean) {

                            XCApplication.printi(result_model.toString());
                            XCApplication.printi(result_model.getMsg());
                            XCApplication.printi(result_model.getCode() + "");
                            XCApplication.printi(result_model.getData().getResult().toString() + "");
                            XCApplication.printi(result_model.getData().getTotalCount() + "");
                            XCApplication.printi(result_model.getData().getTotalPages() + "");
                            XCApplication.printi(result_model.getData().getResult().get(0).getCommission() + "");
                            XCApplication.printi(result_model.getData().getResult().get(0).getImgUrl() + "");
                            XCApplication.printi(result_model.getData().getResult().get(0).getMarketPrice() + "");
                            XCApplication.printi(result_model.getData().getResult().get(0).getName() + "");
                            XCApplication.printi(result_model.getData().getResult().get(1).getRebate() + "");
                            XCApplication.printi(result_model.getData().getResult().get(2).getType() + "");
                            XCApplication.printi(result_model.getData().getResult().get(2).getShare().getBaseUrl() + "");
                            XCApplication.printi(result_model.getData().getResult().get(2).getShare().getContent() + "");
                            XCApplication.printi(result_model.getData().getResult().get(2).getShare().getTitle() + "");
                            XCApplication.printi(result_model.getData().getResult().get(2).getShare().getIcon() + "");
                            XCApplication.printi(result_model.getData().getResult().get(1).getImgUrl());

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
