package com.xiaocoder.buffer.parse;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.XCIHttpResult;
import com.xiaocoder.android.fw.general.json.XCJsonParse;

/**
 * 通用的jsonbean解析
 */
public  class QlkResponseHandlerBean<T extends QlkBean> extends QlkResponseHandler<T> {

    public QlkResponseHandlerBean(XCIHttpResult result_http,
                                  int content_type,
                                  boolean show_background_when_net_fail,
                                  Class<T> result_bean_class) {
        super(result_http, content_type, show_background_when_net_fail, result_bean_class);

    }

    public QlkResponseHandlerBean(XCIHttpResult result_http, Class<T> result_bean_class) {
        super(result_http, result_bean_class);
    }

    @Override
    public T parseWay(String responseStr , byte[] response_bytes) {

        XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----parseWay()");

        return XCJsonParse.getJsonParseData(responseStr, result_bean_class);
    }


}
