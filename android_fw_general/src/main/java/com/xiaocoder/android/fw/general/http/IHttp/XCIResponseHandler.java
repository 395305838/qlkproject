package com.xiaocoder.android.fw.general.http.IHttp;

import android.app.Dialog;
import android.content.Context;

import org.apache.http.Header;

/**
 * @author xiaocoder
 *         2015-1-16 下午1:55:28
 *         http回调的handler ,业务逻辑的判断，model的解析，dialog的显示等
 */
public interface XCIResponseHandler<T> {

    void success(int code, Header[] headers, byte[] bytes);

    void failure(int code, Header[] headers, byte[] bytes, Throwable e);

    void finish();

    /**
     * activity是否销毁，这里的Context最好传Activity的实例，这样可以判断activity是否销毁
     */
    boolean isXCActivityDestroy(Context context);

    void setContext(Context context);

    /**
     * 在子线程运行的，所以不要有ui或toast等操作，可以包括一系列的打印日志，byte转json，返回结果的判断等
     */
    void parse(byte[] response_bytes);

    /**
     * 子线程中运行的，所以不要有ui或toast等操作，在parse（）中的方法，parseWay仅处理json到model、bean的解析
     */
    T parseWay(String responseStr, byte[] response_bytes);

    /**
     * 加密
     */
    void yourCompanySecret(Object params, Object client, boolean needSecret);

    /**
     * 对返回状态码的一个判断，每个项目的认定操作成功的状态码或结构可能不同，在这里统一拦截
     * <p/>
     * 即设置 result_boolean 为false 或 true
     */
    boolean yourCompanyResultRule();

    /**
     * http开始的dialog
     */
    void showHttpDialog();

    /**
     * http结束，关闭dialog
     */
    void closeHttpDialog();

    /**
     * 请求中的dialog
     */
    Dialog getHttpDialog();

    /**
     * 请求的相关参数
     */
    void setHttpModel(XCHttpModel httpModel);

    /**
     * 一个http结束时，调用该方法
     */
    void setHttpEndNotify(XCIHttpEndNotify notify);


}
