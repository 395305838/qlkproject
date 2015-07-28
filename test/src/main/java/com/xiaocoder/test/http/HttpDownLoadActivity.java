package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCQueryDialog;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;
import com.xiaocoder.test.buffer.QlkHttpResponseHandler;

import org.apache.http.Header;

public class HttpDownLoadActivity extends QlkBaseActivity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http_download);
        super.onCreate(savedInstanceState);
    }

    public void request() {
//        XCHttpAsyn.getAsyn(true,true, this, "http://" + MainActivity.TEST_HOST + ":8080/qlktest/test.mp3", new RequestParams(), new QlkHttpResponseHandler(HttpDownLoadActivity.this) {
        XCHttpAsyn.getAsyn(true, true, this, "http://www.baidu.com", new RequestParams(), new QlkHttpResponseHandler(HttpDownLoadActivity.this) {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                super.onSuccess(arg0, arg1, arg2);

                closeHttpDialog();

                final XCQueryDialog dialog = new XCQueryDialog(HttpDownLoadActivity.this, XCBaseDialog.TRAN_STYLE, "下载提示", "该文件大小为" + UtilString.getFileSizeUnit(arg2.length), new String[]{"下载", "取消"}, false);

                dialog.setOnDecideListener(new XCQueryDialog.OnDecideListener() {
                    @Override
                    public void confirm() {
                        dialog.dismiss();
                    }

                    @Override
                    public void cancle() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            @Override
            public void onFinish() {

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
