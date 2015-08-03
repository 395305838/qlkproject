package com.xiaocoder.android.fw.general.helper;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.io.XCIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaocoder on 2015/6/29.
 */

public class XCDownloadHelper implements Runnable {

    String tag = XCConfig.TAG_TEMP;
    String url = "";
    File file;

    public interface DownloadListener {

        void downloadFinished(File file);

        void netFail(File file);

    }

    public DownloadListener downloadListener;

    public void setDownloadListener(DownloadListener listener) {
        this.downloadListener = listener;
    }


    public XCDownloadHelper(String url, File file) {
        this.url = url;
        this.file = file;
    }

    @Override
    public void run() {
        InputStream in = null;
        try {
            XCApplication.printi(tag, "----进入下载的run方法");
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(9000);
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                in = conn.getInputStream();
                // conn.getContentLength();
                XCApplication.printi(tag, "----开始下载了");
                XCIO.toFileByInputStream(in, file);
                if (downloadListener != null) {
                    XCApplication.printi(tag, "----下载完成，进入监听----" + Thread.currentThread());
                    downloadListener.downloadFinished(file);
                }
            } else {
                if (downloadListener != null) {
                    downloadListener.netFail(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (downloadListener != null) {
                downloadListener.netFail(file);
            }
            XCApplication.printi(tag, "--下载excpetion---" + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
