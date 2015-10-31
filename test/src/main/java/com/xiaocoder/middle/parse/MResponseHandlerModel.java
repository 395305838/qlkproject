package com.xiaocoder.middle.parse;

import android.app.Activity;

import com.google.gson.Gson;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;

/**
 * gson 解析
 */
public abstract class MResponseHandlerModel<T extends MModel> extends MResponseHandler<T> {

    public MResponseHandlerModel(XCIHttpResult result_http,
                                 Activity activity,
                                 int content_type,
                                 boolean show_background_when_net_fail,
                                 Class<T> result_bean_class) {
        super(result_http, activity, content_type, show_background_when_net_fail, result_bean_class);

    }

    public MResponseHandlerModel(XCIHttpResult result_http, Activity activity, Class<T> result_bean_class) {
        super(result_http, activity, result_bean_class);
    }

    @Override
    public T parseWay(String responseStr, byte[] response_bytes) {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----parseWay()");

        return new Gson().fromJson(responseStr, result_bean_class);
    }

}
