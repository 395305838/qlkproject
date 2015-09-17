package com.xiaocoder.buffer.parse;

import android.content.DialogInterface;
import android.view.KeyEvent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCSystemHDialog;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.http.XCIHttpResult;
import com.xiaocoder.android.fw.general.http.XCResponseHandler;
import com.xiaocoder.android.fw.general.util.UtilSystem;
import com.xiaocoder.buffer.function.QlkMainActivity;

/**
 * Created by xiaocoder on 2015/8/28.
 * <p/>
 * 已有的两个实现类
 * QlkResponseHandlerBean（jsonbean的解析）    QlkResponseHandlerModel（gson的解析）
 * <p/>
 * 也可以创建一个实现类，手动解析
 */
public abstract class QlkResponseHandler<T> extends XCResponseHandler<T> {

    public QlkResponseHandler(XCIHttpResult result_http, int content_type, boolean show_background_when_net_fail, Class<T> result_bean_class) {
        super(result_http, content_type, show_background_when_net_fail, result_bean_class);
    }

    public QlkResponseHandler(XCIHttpResult result_http, Class<T> result_bean_class) {
        super(result_http, result_bean_class);
    }

    /**
     * 解析是否成功的规则，根据项目的json而定
     */
    public void yourCompanyResultRule() {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---yourCompanyResultRule()");

        if (result_bean instanceof IQlkResponseInfo) {

            if (((IQlkResponseInfo) result_bean).getCode() == 0) {
                result_boolean = true;
            } else {
                result_boolean = false;
                XCApp.shortToast(((IQlkResponseInfo) result_bean).getMsg());
            }

        } else {
            XCApp.e("yourCompanyResultRule()中的返回结果不是IQlkResponseInfo类型");
            throw new RuntimeException("yourCompanyResultRule()中的返回结果不是IQlkResponseInfo类型");
        }

    }

    /**
     * 提交请求前的加密
     *
     * @param needSecret 是否要加密
     */
    @Override
    public void yourCompanySecret(RequestParams params, AsyncHttpClient client, boolean needSecret) {

        if (needSecret) {

            XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---yourCompanySecret()");

            client.addHeader("_v", UtilSystem.getVersionCode(mContext) + "");// 版本号，必填
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
            XCApp.i(XCConfig.TAG_HTTP, "plus public params-->" + params.toString());

        }

    }


    /**
     * 如果不同页面的dialog不同，可以重写该方法
     */
    @Override
    public void closeHttpDialog() {
        if (httpDialog != null && httpDialog.isShowing()) {
            httpDialog.dismiss();
            httpDialog.setOnKeyListener(null);
            httpDialog.cancel();
            XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---closeHttpDialog()");
        }
    }

    /**
     * 如果不同页面的dialog不同，可以重写该方法
     */
    @Override
    public void showHttpDialog() {
        if (httpDialog == null) {
            httpDialog = new XCSystemHDialog(mContext, XCBaseDialog.TRAN_STYLE);
            httpDialog.setCanceledOnTouchOutside(false);
            httpDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        closeHttpDialog();
                        XCHttpAsyn.resetNetingStatus();
                        if (!(mContext instanceof QlkMainActivity)) {
                            ((XCBaseActivity) mContext).myFinish();
                        }
                    }
                    return false;
                }
            });
            httpDialog.show();
            XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---showHttpDialog()");
        }
    }
}
