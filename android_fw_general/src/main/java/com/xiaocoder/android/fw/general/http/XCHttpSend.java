package com.xiaocoder.android.fw.general.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;

import java.util.Map;

/**
 * asyn-http-android库
 * 1 文件上传： params.put("字段", new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/0912/compass.png"));
 * 同时支持流和字节
 * <p/>
 * 2不管是失败还是完成都会调用finish()方法
 * <p/>
 * 3是异步的
 * isShowDialog 是否在加载网络时显示dialog
 * isNeting 是只有当前网络请求完了, 再点击访问网络时才有效, 如果正在请求网络或者说网络访问没有结束,那么此时点击访问网络无效 , 默认是同一时刻只能有一个网络在请求
 * isFrequentlyClick 如果为true，则允许同时访问多个网络连接
 * needSecret 是否要加密
 * <p/>
 * 4 onRetry（） onStart（） onCancle（） onProgress（）
 * <p/>
 * 5  PersistentCookieStore 见github文档
 * <p/>
 * 6  Adding HTTP Basic Auth credentials 见github文档
 */

public class XCHttpSend {

    private AsyncHttpClient client;

    /**
     * 上一个请求是否正在进行
     */
    private boolean isNeting;

    public XCHttpSend() {
        initAsynHttpClient();
    }

    public void initAsynHttpClient() {
        client = new AsyncHttpClient();
        client.setTimeout(10000);
    }

    public boolean isNeting() {
        return isNeting;
    }

    public AsyncHttpClient getClient() {

        return client;
    }

    /**
     * 当isFrequentlyClick为true时：只有当前请求返回了，再调用该方法后，才可以继续下一个请求，
     */
    public void resetNetingStatus() {
        isNeting = false;
    }

    /**
     * 这里改为了hashmap，便于以后更改http请求库
     */
    public void getAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog, String urlString, Map<String, Object> map, XCIResponseHandler resHandler) {

        resHandler.setXCHttpModel(new XCHttpModel(XCHttpType.GET, needSecret, isFrequentlyClick, isShowDialog, urlString, map));

        sendAsyn(resHandler);

    }


    public void postAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog, String urlString, Map<String, Object> map, XCIResponseHandler resHandler) {

        resHandler.setXCHttpModel(new XCHttpModel(XCHttpType.POST, needSecret, isFrequentlyClick, isShowDialog, urlString, map));

        sendAsyn(resHandler);

    }

    public void sendAsyn(XCIResponseHandler resHandler) {

        if (resHandler == null) {
            XCApp.e(this + "---resHandler为null");
            return;
        }

        XCHttpModel model = resHandler.getXCHttpModel();
        XCHttpType httpType = model.getXcHttpType();
        boolean needSecret = model.isNeedSecret();
        boolean isFrequentlyClick = model.isFrequentlyClick();
        boolean isShowDialog = model.isShowDialog();
        String urlString = model.getUrlString();
        Map<String, Object> map = model.getMap();

        if (isFrequentlyClick || !isNeting) {
            isNeting = true;
            XCApp.i(XCConfig.TAG_HTTP_HANDLER, model);
            /*
             * 是否show Dialog
             */
            if (isShowDialog && resHandler.obtainActivity() != null) {
                resHandler.showHttpDialog();
            }

            /*
             * 转换参数
             */
            RequestParams params = new RequestParams();
            for (Map.Entry<String, Object> item : map.entrySet()) {
                String key = item.getKey();
                Object value = item.getValue();
                params.put(key, value);
            }
            XCApp.i(XCConfig.TAG_HTTP, "加密前参数---" + params.toString());

            /*
             * 加密
             */
            resHandler.yourCompanySecret(params, client, needSecret);

            /*
             * 发送请求
             */
            if (resHandler instanceof AsyncHttpResponseHandler) {
                if (httpType == XCHttpType.GET) {
                    XCApp.i(XCConfig.TAG_HTTP, urlString + "------>get http url");
                    client.get(urlString, params, (AsyncHttpResponseHandler) resHandler);
                } else if (httpType == XCHttpType.POST) {
                    XCApp.i(XCConfig.TAG_HTTP, urlString + "------>post http url");
                    client.post(urlString, params, (AsyncHttpResponseHandler) resHandler);
                } else {
                    throw new RuntimeException("XCHttpAsyn中的XCHttpType类型不匹配");
                }
            } else {
                throw new RuntimeException("XCHttpAsyn中的Handler类型不匹配");
            }
        } else {
            XCApp.e(urlString + "--该请求无效，前一个请求还未返回");
        }
    }

}
