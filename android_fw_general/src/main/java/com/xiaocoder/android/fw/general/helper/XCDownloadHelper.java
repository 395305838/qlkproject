package com.xiaocoder.android.fw.general.helper;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.io.XCIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaocoder on 2015/6/29.
 *
 * 这是个Runnable
 */

public class XCDownloadHelper implements Runnable {

    String tag = XCConfig.TAG_TEMP;
    String url = "";
    File file;

    public interface DownloadListener {

        /**
         * @param totalSize 服务器上的该文件的大小
         * @param file      下载完成的文件
         */
        void downloadFinished(long totalSize, File file);

        /**
         * @param len           每一次读取的大小
         * @param totalProgress 累计读取的大小
         * @param totalSize     服务器上的该文件的大小服务器上的该文件的大小
         * @param file          正在下载的文件
         */
        void downloadProgress(int len, long totalProgress, long totalSize, File file);

        /**
         * 即将开始下载，还未开始
         *
         * @param totalSize 服务器上的该文件的大小
         * @param file      下载到这个file中
         */
        void downloadStart(long totalSize, File file);

        /**
         * 还未开始下载，网络连接就失败
         * 或者是下载过程中，出现异常
         *
         * @param file
         */
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
            XCApp.i(tag, "----进入下载的run方法");
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                in = conn.getInputStream();
                long totalSize = conn.getContentLength();
                XCApp.i(tag, "----开始下载了");
                XCIO.toFileByInputStream(in, file, totalSize, downloadListener);
                if (downloadListener != null) {
                    XCApp.i(tag, "----下载完成----" + Thread.currentThread());
                    downloadListener.downloadFinished(totalSize, file);
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
            XCApp.i(tag, "--下载excpetion---" + e.toString());
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
