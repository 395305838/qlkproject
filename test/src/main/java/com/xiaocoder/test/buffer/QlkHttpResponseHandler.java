package com.xiaocoder.test.buffer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.KeyEvent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.base.XCBaseFragment;
import com.xiaocoder.android.fw.general.base.XCBaseMainActivity;
import com.xiaocoder.android.fw.general.base.XCConfig;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCSystemHDialog;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.http.XCHttpResponseHandler;
import com.xiaocoder.android.fw.general.http.XCIHttpResult;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.util.UtilSystem;

/**
 * @author xiaocoder
 * @date 2014-12-30 下午5:04:48
 */
public class QlkHttpResponseHandler extends XCHttpResponseHandler {


    public QlkHttpResponseHandler(XCIHttpResult result_http, int content_type, boolean show_background_when_net_fail, XCBaseFragment refresh_fragment) {
        super(result_http, content_type, show_background_when_net_fail, refresh_fragment);
    }

    public QlkHttpResponseHandler(XCIHttpResult result_http) {
        super(result_http);
    }

    public QlkHttpResponseHandler(XCIHttpResult result_http, XCBaseFragment refresh_fragment) {
        super(result_http, refresh_fragment);
    }


    // 需要根据公司业务重写
    public void yourCompanyLogic() {

        XCApplication.printi("yourCompanyLogic");

        if (YES.equals(result_bean.getString(XCJsonBean.CODE))) {
            if (mContext instanceof XCBaseActivity) {
                if (((XCBaseActivity) mContext).isActivityDestroied()) {
                    result_boolean = false;
                    return;
                }
            }
            result_boolean = true;
        } else {
            result_boolean = false;
            XCApplication.shortToast(result_bean.getString(XCJsonBean.MSG));
        }

    }

    @Override
    public void yourCompanySecret(RequestParams params, AsyncHttpClient client, boolean needSecret) {

        XCApplication.printi("yourCompanySecret");

        client.addHeader("_v", XCApplication.getVersionCode() + "");// 版本号，必填
        client.addHeader("_m", UtilSystem.getMacAddress(mContext));// 设备的mac地址，选填
        client.addHeader("_c", "2222");// JSONP的回调函数名 ,可选
        client.addHeader("_p", "1"); // 平台，必填

//        String token = Qlk
//        params.put("token", token);
//        if (params != null && params.has("token")) {
//            String ts = System.currentTimeMillis() + "";
//            String sig = UtilMd5.sortAddMD5Params(ts, params);
//            //有token的接口需要添加sig
//            params.put("sig", sig);
//            params.put("ts", ts);
//        }
        XCApplication.printi(XCConfig.TAG_HTTP, "plus public params-->" + params.toString());
    }


    @Override
    public void closeHttpDialog() {
        if (httpDialog != null) {
            httpDialog.dismiss();
            httpDialog.setOnKeyListener(null);
            httpDialog.cancel();
        }
    }

    @Override
    public void showHttpDialog() {
        if (httpDialog == null) {
            httpDialog = new XCSystemHDialog(mContext, XCBaseDialog.TRAN_STYLE);
            httpDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        closeHttpDialog();
                        XCHttpAsyn.resetNetingStatus();
                        if (!(mContext instanceof XCBaseMainActivity)) {
                            ((Activity) mContext).finish();
                        }
                    }
                    return false;
                }
            });
            httpDialog.show();
        }
    }
}
