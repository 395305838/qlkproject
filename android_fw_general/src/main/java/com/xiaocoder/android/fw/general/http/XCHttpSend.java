package com.xiaocoder.android.fw.general.http;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.IHttp.XCHttpType;
import com.xiaocoder.android.fw.general.http.IHttp.XCHttpModel;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpNotify;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;

import java.util.Map;

/**
 * asyn-http-android库
 * 1 文件上传： params.put("字段", new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/0912/compass.png"));
 * 同时支持流和字节
 * <p/>
 * 2不管是失败还是完成都会调用finish()方法
 * <p/>
 * 3下载数据可以用byte[]，而不用result_bean
 * <p/>
 * 4是异步的
 * isShowDialog 是否在加载网络时显示dialog
 * isNeting 是只有当前网络请求完了, 再点击访问网络时才有效, 如果正在请求网络或者说网络访问没有结束,那么此时点击访问网络无效 , 默认是同一时刻只能有一个网络在请求
 * isFrequentlyClick 如果为true，则允许同时访问多个网络连接
 * needSecret 是否要加密
 * <p/>
 * 5 onRetry（） onStart（） onCancle（） onProgress（）
 * <p/>
 * 6  PersistentCookieStore 见github文档
 * <p/>
 * 7  Adding HTTP Basic Auth credentials 见github文档
 */

/**
 * 默认使用了asyn-http-android库
 * <p/>
 * 如果项目不用该库， 可以继承该类 ，重写get 与 post等方法，然后 XCAPP.setBase_xcHttpSend()
 */
public class XCHttpSend implements XCIHttpNotify{

    private AsyncHttpClient client;

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
     * 当isFrequentlyClick为true时：只有当前请求返回了，调用该方法后，才可以继续下一个请求，
     */
    public void resetNetingStatus() {
        isNeting = false;
    }

    /**
     * 这里改为了hashmap，便于以后更改http请求库
     */
    public void getAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog, String urlString, Map<String, Object> map, XCIResponseHandler res) {

        sendAsyn(XCHttpType.GET, needSecret, isFrequentlyClick, isShowDialog, urlString, map, res);

    }

    public void postAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog, String urlString, Map<String, Object> map, XCIResponseHandler res) {

        sendAsyn(XCHttpType.POST, needSecret, isFrequentlyClick, isShowDialog, urlString, map, res);

    }

    public void sendAsyn(XCHttpType xcHttpType, boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog, String urlString,
                         Map<String, Object> map, XCIResponseHandler res) {
        RequestParams params = new RequestParams();

        for (Map.Entry<String, Object> item : map.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            params.put(key, value);
        }

        XCApp.i(XCConfig.TAG_HTTP, params.toString());
        if (isFrequentlyClick || !isNeting) {
            isNeting = true;
            res.setXCHttpModel(new XCHttpModel(null, null, xcHttpType, needSecret, isFrequentlyClick, isShowDialog, urlString, map));
            res.yourCompanySecret(params, client, needSecret);
            if (isShowDialog && res.obtainActivity() != null) {
                res.showHttpDialog();
            }

            if (res instanceof AsyncHttpResponseHandler) {
                if (xcHttpType == XCHttpType.GET) {
                    XCApp.i(XCConfig.TAG_HTTP, urlString + "------>get http url");
                    client.get(urlString, params, (AsyncHttpResponseHandler) res);
                } else if (xcHttpType == XCHttpType.POST) {
                    XCApp.i(XCConfig.TAG_HTTP, urlString + "------>post http url");
                    client.post(urlString, params, (AsyncHttpResponseHandler) res);
                }
            } else {
                throw new RuntimeException("XCHttpAsyn中的Handler类型不匹配");
            }
        } else {
            XCApp.e(urlString + "--该请求无效，前一个请求还未返回");
        }
    }

    @Override
    public void startNotify(XCIResponseHandler handler, boolean isSuccess) {

    }

    @Override
    public void endNotify(XCIResponseHandler handler, boolean isSuccess) {

    }
}
