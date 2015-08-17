package com.xiaocoder.android.fw.general.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.application.XCConfig;

/*单例
 * 1如果是上传文件:
 * 	可以 params.put("字段", new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/0912/compass.png"));
 *  同时支持流和字节
 *
 * 2不管是失败还是完成都会调用onFinish()方法
 * 
 * 3下载数据可以用byte[]，而不用result_bean
 * 
 * 4是异步的
 * isShowDialog 是否在加载网络时显示dialog
 * isNeting 是只有当前网络请求完了, 再点击访问网络时才有效, 如果正在请求网络或者说网络访问没有结束,那么此时点击访问网络无效 , 默认是同一时刻只能有一个网络在请求
 * isAllowConcurrent 如果为true，则允许同时访问多个网络连接
 *
 * 5 onRetry（） onStart（） onCancle（） onProgress（）
 *
 * 6  PersistentCookieStore 见github文档
 *
 * 7  Adding HTTP Basic Auth credentials 见github文档
 */

public class XCHttpAsyn {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static boolean isNeting;

    static {
        client.setTimeout(10000);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    public static void getAsyn(boolean needSecret, boolean isAllowConcurrent, boolean isShowDialog, Context context, String urlString, RequestParams params, XCResponseHandler res) {
        XCApplication.printi(XCConfig.TAG_HTTP, params.toString());
        if (isAllowConcurrent || !isNeting) {
            isNeting = true;
            XCApplication.printi(XCConfig.TAG_HTTP, urlString + "------>get http url");
            res.setContext(context);
            res.yourCompanySecret(params, client, needSecret);
            if (isShowDialog) {
                res.showHttpDialog();
            }
            client.get(urlString, params, res);
        }
    }

    public static void getAsyn(boolean isAllowConcurrent, boolean isShowDialog, XCBaseActivity context, String urlString, RequestParams params, XCResponseHandler res) {
        getAsyn(true, isAllowConcurrent, isShowDialog, context, urlString, params, res);
    }

    public static void getAsyn(boolean isShowDialog, XCBaseActivity context, String urlString, RequestParams params, XCResponseHandler res) {
        getAsyn(true, false, isShowDialog, context, urlString, params, res);
    }

    public static void postAsyn(boolean needSecret, boolean isAllowConcurrent, boolean isShowDialog, XCBaseActivity context, String urlString, RequestParams params, XCResponseHandler res) {
        XCApplication.printi(XCConfig.TAG_HTTP, params.toString());
        if (isAllowConcurrent || !isNeting) {
            isNeting = true;
            XCApplication.printi(XCConfig.TAG_HTTP, urlString + "------>post http url");
            res.setContext(context);
            res.yourCompanySecret(params, client, needSecret);
            if (isShowDialog) {
                res.showHttpDialog();
            }
            client.post(urlString, params, res);
        }
    }

    public static void postAsyn(boolean isAllowConcurrent, boolean isShowDialog, XCBaseActivity context, String urlString, RequestParams params, XCResponseHandler res) {
        postAsyn(true, isAllowConcurrent, isShowDialog, context, urlString, params, res);

    }

    public static void postAsyn(boolean isShowDialog, XCBaseActivity context, String urlString, RequestParams params, XCResponseHandler res) {
        postAsyn(true, false, isShowDialog, context, urlString, params, res);
    }

    public static void resetNetingStatus() {
        XCHttpAsyn.isNeting = false;
    }

}
