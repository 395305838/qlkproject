package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCQueryDialog;
import com.xiaocoder.android.fw.general.helper.XCDownloadHelper;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.buffer.QlkApplication;
import com.xiaocoder.buffer.QlkConfig;
import com.xiaocoder.buffer.QlkResponseHandler;
import com.xiaocoder.test.R;

import org.apache.http.Header;

import java.io.File;

public class HttpDownLoadActivity extends QlkActivity {

    Button button;
    XCQueryDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http_download);
        super.onCreate(savedInstanceState);
    }

    public void request() {
//        XCHttpAsyn.getAsyn(true,true, this, "http://" + MainActivity.TEST_HOST + ":8080/qlktest/test.mp3", new RequestParams(), new QlkHttpResponseHandler(HttpDownLoadActivity.this) {
        XCHttpAsyn.getAsyn(true, true, this, "http://www.baidu.com"
                , new RequestParams()
                , new QlkResponseHandler<XCJsonBean>(this, XCJsonBean.class) {

            @Override
            public void success(int code, Header[] headers, byte[] arg2) {
                super.success(code, headers, arg2);
                closeHttpDialog();

                dialog = new XCQueryDialog(HttpDownLoadActivity.this, XCBaseDialog.TRAN_STYLE, "下载提示", "该文件大小为" + UtilString.getFileSizeUnit(arg2.length), new String[]{"下载", "取消"}, false);

                dialog.setOnDecideListener(new XCQueryDialog.OnDecideListener() {
                    @Override
                    public void confirm() {
                        downLoad();
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
            public void finish() {

            }
        });
    }

    private void downLoad() {

        XCDownloadHelper downloadHelper = new XCDownloadHelper("http://www.baidu.com"
                , QlkApplication.getBase_io().createFileInAndroid(QlkConfig.APP_ROOT, "downfile"));

        downloadHelper.setDownloadListener(new XCDownloadHelper.DownloadListener() {
            @Override
            public void downloadFinished(long totalSize, File file) {

            }

            @Override
            public void downloadProgress(int len, long totalProgress, long totalSize, File file) {

            }

            @Override
            public void downloadStart(long totalSize, File file) {

            }

            @Override
            public void netFail(File file) {

            }
        });

        QlkApplication.getBase_cache_threadpool().execute(downloadHelper);
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
