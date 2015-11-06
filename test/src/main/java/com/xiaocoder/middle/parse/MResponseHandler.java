package com.xiaocoder.middle.parse;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;
import com.xiaocoder.views.dialog.XCBaseDialog;
import com.xiaocoder.views.dialog.XCSystemHDialog;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpNotify;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;
import com.xiaocoder.android.fw.general.http.XCResponseHandler;
import com.xiaocoder.android.fw.general.util.UtilSystem;
import com.xiaocoder.middle.function.MMainActivity;

/**
 * Created by xiaocoder on 2015/8/28.
 * <p/>
 * 已有的两个实现类
 * MResponseHandlerBean（jsonbean的解析）    MResponseHandlerModel（gson的解析）
 * <p/>
 * 也可以创建一个实现类，手动解析
 */
public abstract class MResponseHandler<T> extends XCResponseHandler<T> {

    public MResponseHandler(XCIHttpResult result_http, XCIHttpNotify notify, Activity activity, int content_type, boolean show_background_when_net_fail, Class<T> result_bean_class) {
        super(result_http, notify, activity, content_type, show_background_when_net_fail, result_bean_class);
    }

    public MResponseHandler(XCIHttpResult result_http, XCIHttpNotify notify, Activity activity, Class<T> result_bean_class) {
        super(result_http, notify, activity, result_bean_class);
    }

    public MResponseHandler(XCIHttpResult result_http, Activity activity, Class<T> result_bean_class) {
        super(result_http, activity, result_bean_class);
    }

    public MResponseHandler(Activity activity, Class<T> result_bean_class) {
        super(activity, result_bean_class);
    }

    /**
     * 解析是否成功的规则，根据项目的json而定
     */
    public boolean yourCompanyResultRule() {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---yourCompanyResultRule()");

        /**
         * 统一规则 用MModel 或 MBean
         */
        if (result_bean instanceof MIResponseInfo && (result_bean instanceof MModel || result_bean instanceof MBean)) {

            if (((MIResponseInfo) result_bean).getCode() == 0) {
                return true;
            } else {
                XCApp.shortToast(((MIResponseInfo) result_bean).getMsg());
                return false;
            }

        } else {
            XCApp.e("yourCompanyResultRule()中的返回结果不是IQlkResponseInfo, MModel 或 Qlkean 类型");
            throw new RuntimeException("yourCompanyResultRule()中的返回结果不是IQlkResponseInfo ,MModel 或 Qlkean 类型");
        }

    }

    /**
     * 提交请求前的加密
     *
     * @param oParams    http请求的参数，这里暂用obj接收
     * @param oClient    添加http的header用的，这里暂用obj接收
     * @param needSecret 是否要加密
     */
    @Override
    public void yourCompanySecret(Object oParams, Object oClient, boolean needSecret) {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---yourCompanySecret()--" + needSecret);

        if (oClient instanceof AsyncHttpClient) {
            // 添加header
            AsyncHttpClient client = (AsyncHttpClient) oClient;
            client.addHeader("_v", UtilSystem.getVersionCode(XCApp.getBase_applicationContext()) + "");// 版本号，必填
            client.addHeader("_m", UtilSystem.getMacAddress(XCApp.getBase_applicationContext()));// 设备的mac地址，选填
            client.addHeader("_c", "2222");// JSONP的回调函数名 ,可选
            client.addHeader("_p", "1"); // 平台，必填
        } else {
            throw new RuntimeException("yourCompanySecret()---中传入的obj不是AsynHttpClient类型");
        }

        if (oParams instanceof RequestParams) {
            if (needSecret) {
                RequestParams params = (RequestParams) oParams;
                // TODO 补充加密的代码

                XCApp.i(XCConfig.TAG_HTTP, "secret params-->" + params.toString());
            }
        } else {
            throw new RuntimeException("yourCompanySecret()---中传入的params不是RequestParams类型");
        }

    }

    @Override
    public void closeHttpDialog() {
        if (httpDialog != null && httpDialog.isShowing()) {
            httpDialog.cancel();
            XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---closeHttpDialog()");
        }
    }

    /**
     * 如果不同页面的dialog不同，可以重写showHttpDialog，然后设置dialog
     */
    @Override
    public void showHttpDialog() {
        setDialogAndShow(new XCSystemHDialog(activity, XCBaseDialog.TRAN_STYLE));
    }

    public final void setDialogAndShow(Dialog yourDialog) {
        if (httpDialog == null) {
            httpDialog = yourDialog;
            httpDialog.setCanceledOnTouchOutside(false);
            httpDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        closeHttpDialog();
                        XCApp.resetNetingStatus();
                        if (!(activity instanceof MMainActivity)) {
                            ((XCBaseActivity) activity).myFinish();
                        }
                    }
                    return false;
                }
            });
        }
        httpDialog.show();
        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---showHttpDialog()");
    }
}
