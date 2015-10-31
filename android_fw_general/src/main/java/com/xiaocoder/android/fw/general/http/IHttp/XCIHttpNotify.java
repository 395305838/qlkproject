package com.xiaocoder.android.fw.general.http.IHttp;

/**
 * Created by xiaocoder on 2015/10/30.
 * version: 1.2.0
 * description: 对所有http请求的开始和结束的拦截
 * <p/>
 * 比如并发的时候，需要在一个（锁）方法里面判断哪个应用先回来的，可以用startNotify拦截
 * 比如串行的时候，需要在一个（锁）方法里面判断该次http请求的结果，从而执行不同的synctype的流程，可以用endNotify拦截
 */
public interface XCIHttpNotify {

    /**
     * 一个http请求刚开始（即 在调用success/fail 之前会调用该方法）
     */
    void startNotify(XCIResponseHandler handler, boolean isSuccess);

    /**
     * 一个http请求完全结束后（即在调用完onFinish后，会调用该接口对象的endNotify）
     */
    void endNotify(XCIResponseHandler handler, boolean isSuccess);

}
