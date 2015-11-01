package com.xiaocoder.android.fw.general.http.IHttp;

import android.app.Activity;
import android.app.Dialog;

import com.xiaocoder.android.fw.general.http.XCHttpModel;

import org.apache.http.Header;

/**
 * @author xiaocoder
 *         2015-1-16 下午1:55:28
 *         http回调的handler ,业务逻辑的判断，json/xml2model的解析，dialog的显示等
 */
public interface XCIResponseHandler<T> {

    int JSON = 1;
    int XML = 2;
    int PHOTO = 3;
    int ELSE = 4;

    /**
     * http请求成功，回调success方法
     */
    void success(int code, Header[] headers, byte[] bytes);

    /**
     * http请求失败，回调failure方法
     */
    void failure(int code, Header[] headers, byte[] bytes, Throwable e);

    /**
     * success/failure 执行完后，会调用finish方法
     */
    void finish();

    /**
     * 获取activity
     */
    Activity obtainActivity();

    /**
     * 如果传入的是XCActivity，则会判断activity是否销毁
     * 如果传入的是Activity，则不会判断activity是否销毁
     */
    boolean isXCActivityDestroy(Activity activity);

    /**
     * http请求的参数
     */
    void setXCHttpModel(XCHttpModel model);

    XCHttpModel getXCHttpModel();

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
     * 对返回状态码的一个判断，每个项目的认定操作成功的状态码或结构可能不同，在这里统一判断
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


}
