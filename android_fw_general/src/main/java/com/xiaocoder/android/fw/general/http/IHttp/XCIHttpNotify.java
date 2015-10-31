package com.xiaocoder.android.fw.general.http.IHttp;

import org.apache.http.Header;

/**
 * Created by xiaocoder on 2015/10/30.
 * version: 1.2.0
 * description: 对http请求回调的刚开始和结束的拦截
 * <p/>
 * 比如并行的时候，用httpBackNotify可判断哪个应用先回来的
 * 比如串行的时候，用httpEndNotify可判断上一个request是否结束，以及返回的状态
 */
public interface XCIHttpNotify {

    /**
     * 一个http刚刚返回，还没开始调用sucess 、 failure
     */
    void httpBackNotify(XCIResponseHandler resHandler, int httpCode, Header[] headers, byte[] data, Throwable e);


    /**
     * 一个http请求完全结束后，即在调用完finish后
     *
     * @param httpCode http返回的状态码
     * @param parseResult 根据接口成功返回的string，解析该次操作是否是通过的
     */
    void httpEndNotify(XCIResponseHandler resHandler, boolean parseResult, int httpCode, Header[] headers, byte[] data, Throwable e);

}
