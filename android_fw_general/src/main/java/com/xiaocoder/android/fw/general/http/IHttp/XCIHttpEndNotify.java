package com.xiaocoder.android.fw.general.http.IHttp;

/**
 * Created by xiaocoder on 2015/10/30.
 * version: 1.2.0
 * description: 一个http请求完全结束后（即在调用完onFinish后，会调用该接口对象）
 */
public interface XCIHttpEndNotify {

    void httpEndNotify(XCHttpModel httpModel, boolean isSuccess);

}
