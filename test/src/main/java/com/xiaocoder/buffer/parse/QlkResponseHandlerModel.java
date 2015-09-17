package com.xiaocoder.buffer.parse;

import com.google.gson.Gson;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.XCIHttpResult;

/**
 * gson 解析
 */
public abstract class QlkResponseHandlerModel<T extends QlkModel> extends QlkResponseHandler<T> {

    public QlkResponseHandlerModel(XCIHttpResult result_http,
                                   int content_type,
                                   boolean show_background_when_net_fail,
                                   Class<T> result_bean_class) {
        super(result_http, content_type, show_background_when_net_fail, result_bean_class);

    }

    public QlkResponseHandlerModel(XCIHttpResult result_http, Class<T> result_bean_class) {
        super(result_http, result_bean_class);
    }

    @Override
    public T parseWay(String responseStr , byte[] response_bytes) {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----parseWay()");

        return new Gson().fromJson(responseStr, result_bean_class);
    }

}
