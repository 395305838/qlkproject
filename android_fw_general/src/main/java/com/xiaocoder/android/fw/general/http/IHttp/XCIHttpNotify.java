package com.xiaocoder.android.fw.general.http.IHttp;

import org.apache.http.Header;

/**
 * Created by xiaocoder on 2015/10/30.
 * description: 对http请求回调的刚开始和结束的拦截
 */
public interface XCIHttpNotify {

    /**
     * 一个http刚返回，还没开始调用sucess 、 failure
     *
     * @param httpCode    http返回的状态码
     * @param parseResult http如果失败，该值为false
     *                    http如果成功，再根据解析string后的状态码来判断
     * @return 是否接下来调用success or failure
     */
    boolean httpBackNotify(XCIResponseHandler resHandler, boolean parseResult, int httpCode, Header[] headers, byte[] data, Throwable e);


    /**
     * 一个http请求完全结束后，即在调用完finish后
     *
     * @param httpCode    http返回的状态码
     * @param parseResult http如果失败，该值为false
     *                    http如果成功，再根据解析string后的状态码来判断
     */
    void httpEndNotify(XCIResponseHandler resHandler, boolean parseResult, int httpCode, Header[] headers, byte[] data, Throwable e);

}
