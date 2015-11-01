package com.xiaocoder.middle.parse;

import android.app.Activity;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpNotify;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;
import com.xiaocoder.android.fw.general.json.XCJsonParse;

/**
 * 通用的jsonbean解析
 */
public class MResponseHandlerBean<T extends MBean> extends MResponseHandler<T> {

    public MResponseHandlerBean(XCIHttpResult result_http, XCIHttpNotify notify, Activity activity, int content_type, boolean show_background_when_net_fail, Class<T> result_bean_class) {
        super(result_http, notify, activity, content_type, show_background_when_net_fail, result_bean_class);
    }

    public MResponseHandlerBean(XCIHttpResult result_http, XCIHttpNotify notify, Activity activity, Class<T> result_bean_class) {
        super(result_http, notify, activity, result_bean_class);
    }

    public MResponseHandlerBean(XCIHttpResult result_http,Activity activity, Class<T> result_bean_class) {
        super(result_http,activity, result_bean_class);
    }

    public MResponseHandlerBean(Activity activity, Class<T> result_bean_class) {
        super(activity, result_bean_class);
    }

    @Override
    public T parseWay(String responseStr , byte[] response_bytes) {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----parseWay()");

        return XCJsonParse.getJsonParseData(responseStr, result_bean_class);
    }


}
