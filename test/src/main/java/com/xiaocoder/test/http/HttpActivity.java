package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.http.XCHttpResponseHandler;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;
import com.xiaocoder.test.buffer.QlkHttpResponseHandler;

import org.apache.http.Header;

public class HttpActivity extends QlkBaseActivity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http);
        super.onCreate(savedInstanceState);

    }

    public void request() {
        XCHttpAsyn.getAsyn(true, this, "http://18620909598.sinaapp.com/sangouzhuanchang.json", new RequestParams(), new QlkHttpResponseHandler(HttpActivity.this) {

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
