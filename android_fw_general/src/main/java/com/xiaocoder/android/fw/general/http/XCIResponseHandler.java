package com.xiaocoder.android.fw.general.http;

import android.app.Dialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * @author xiaocoder
 *         2015-1-16 下午1:55:28
 */
public interface XCIResponseHandler<T> {

    void success(int code, Header[] headers, byte[] bytes);

    void failure(int code, Header[] headers, byte[] bytes, Throwable e);

    void finish();

    /**
     * activity是否销毁
     */
    boolean isActivityDestroy(Context context);

    void setContext(Context context);

    /**
     * 在子线程运行的，所以不要有ui或toast等操作
     */
    void parse(byte[] response_bytes);

    /**
     * 子线程中运行的，所以不要有ui或toast等操作
     */
    T parseWay(String responseStr, byte[] response_bytes);

    /**
     * 加密
     */
    void yourCompanySecret(RequestParams params, AsyncHttpClient client, boolean needSecret);

    /**
     * 对返回状态码的一个判断，每个项目的认定操作成功的状态码或结构可能不同，在这里统一拦截
     * <p/>
     * 即设置 result_boolean 为false 或 true
     */
    void yourCompanyResultRule();

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


}
