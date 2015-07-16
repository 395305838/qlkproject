package com.xiaocoder.android.fw.general.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCConfig;
import com.xiaocoder.android.fw.general.dialog.XCdialog;

/*单例
 * 1如果是上传文件:
 * 	可以 params.put("字段", new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/0912/compass.png"));
 * 
 * 2不管是失败还是完成都会调用onFinish()方法
 * 
 * 3下载数据可以用byte[]
 * 
 * 4是异步的
 * 
 */

/*
 * params.put("字段", new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/0912/compass.png"));

 * isShowDialog 是否在加载网络时显示dialog
 * isNeting 是只有当前网络请求完了, 再点击访问网络时才有效, 如果正在请求网络或者说网络访问没有结束,那么此时点击访问网络无效 , 默认是同一时刻只能有一个网络在请求
 * isAllowConcurrent 当设置了该参数时 ,isNeting就无效了 ,如果isAllowConcurrent为true ,则默认同一时刻可以访问很多请求, 如果为false,则只有前一个网络请求完成了,再点击时才有效
 */
public class XCHttpAsyn {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static boolean isNeting; // 是否正在联网, 防止不停的点击带有访问网络的按钮

    static {
        client.setTimeout(9000); // 设置链接超时，如果不设置，默认为9s
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    // universe_asyn框架内部开启线程访问了  第一个参数不为空的时候，取消secret参数
    public static void getAsyn(boolean needSecret, boolean isAllowConcurrent, boolean isShowDialog, Context context, String urlString, RequestParams params, XCHttpResponseHandler res) {
        XCApplication.printi(XCConfig.TAG_HTTP, params.toString());
        if (isAllowConcurrent || !isNeting) {
            isNeting = true;
            showLoadingDialog(isShowDialog, context);
            XCApplication.printi(XCConfig.TAG_HTTP, urlString + "------>get http url");
            res.setContext(context);
            res.yourCompanySecret(params, client, needSecret);
            client.get(urlString, params, res);
        }
    }

    // universe_asyn框架内部开启线程访问了
    public static void getAsyn(boolean isAllowConcurrent, boolean isShowDialog, Context context, String urlString, RequestParams params, XCHttpResponseHandler res) {
        getAsyn(true, isAllowConcurrent, isShowDialog, context, urlString, params, res);
    }

    // universe_asyn框架内部开启线程访问了, 默认不允许同时访问多个链接
    public static void getAsyn(boolean isShowDialog, Context context, String urlString, RequestParams params, XCHttpResponseHandler res) {
        getAsyn(true, false, isShowDialog, context, urlString, params, res);
    }

    // universe_asyn框架内部开启线程访问了
    public static void postAsyn(boolean needSecret, boolean isAllowConcurrent, boolean isShowDialog, Context context, String urlString, RequestParams params, XCHttpResponseHandler res) {
        XCApplication.printi(XCConfig.TAG_HTTP, params.toString());
        if (isAllowConcurrent || !isNeting) {
            isNeting = true;
            showLoadingDialog(isShowDialog, context);
            XCApplication.printi(XCConfig.TAG_HTTP, urlString + "------>post http url");
            res.setContext(context);
            res.yourCompanySecret(params, client, needSecret);
            client.post(urlString, params, res);
        }
    }

    public static void postAsyn(boolean isAllowConcurrent, boolean isShowDialog, Context context, String urlString, RequestParams params, XCHttpResponseHandler res) {
        postAsyn(true, isAllowConcurrent, isShowDialog, context, urlString, params, res);

    }

    // universe_asyn框架内部开启线程访问了, 默认不允许同时访问多个链接
    public static void postAsyn(boolean isShowDialog, Context context, String urlString, RequestParams params, XCHttpResponseHandler res) {
        postAsyn(true, false, isShowDialog, context, urlString, params, res);
    }

    public static void closeLoadingDialog() {
        if (XCApplication.getBase_dialog() != null) {
            XCApplication.getBase_dialog().dismiss();
            XCApplication.printi(XCConfig.TAG_HTTP, "closeLoadingDialog");
        }
    }

    public static void httpFinish() {
        closeLoadingDialog();
        XCHttpAsyn.isNeting = false;
    }

    public static void showLoadingDialog(boolean isShowDialog, Context context) {
        if (isShowDialog) {
            XCApplication.getBase_dialog().getWaitingDialog(context, XCdialog.SYSTEM_CIRCLE_DIALOG_V, null, false, null, null);
            XCApplication.getBase_dialog().show();
            XCApplication.printi(XCConfig.TAG_HTTP, "showLoadingDialog");

            // 自定义的圈圈
            // MyApplication.base_logs.i(MyConfig.TAG_HTTP,
            // "showLoadingDialog");
            // MyApplication.base_dialogs_helper.showRotateAnimDialog(MyApplication.base_dialogs_helper.getWaitingDialog(context,
            // DialogsHelper.ANIMATION_DIALOG_V, null, false, null, null),
            // MyApplication.base_resource.getDrawable(R.drawable.qlk_loading_dialog),
            // false);
            // MyApplication.base_dialogs_helper.show();
        }
    }

}
