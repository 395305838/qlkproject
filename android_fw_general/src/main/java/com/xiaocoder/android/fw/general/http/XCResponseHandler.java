package com.xiaocoder.android.fw.general.http;

import android.app.Dialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.json.XCJsonParse;

import org.apache.http.Header;

/**
 * @author xiaocoder
 * @date 2014-12-30 下午5:04:48
 * <p/>
 * 该类的解析代码是异步的
 * <p/>
 * onFinish()不能被重写，这里设置为了final. 如果需要重写，则重写finish（）方法
 * onSuccess()不能被重写，这里设置为了final，重写success（）方法
 * onFailure()不能被重写，这里设置为了final，重写failure（）方法
 * <p/>
 * finish会在success或failure之后运行
 * <p/>
 * model数据解析的代码 -->在子线程中执行的
 * <p/>
 * <p/>
 * 如果有特殊的解析需求，可以新建一个handler类重写parse() 或 parseWay方法
 */
public abstract class XCResponseHandler<T> extends AsyncHttpResponseHandler implements XCIResponseHandler<T> {
    /**
     * 是json还是xml或图片等,默认是json
     */
    public int content_type;
    /**
     * 如果result_boolean为真就开始处理业务, 如果为false则不处理
     */
    public boolean result_boolean;
    /**
     * 结果集
     */
    public T result_bean;
    /**
     * 访问网络失败时,是否显示失败页面的背景
     */
    public boolean show_background_when_net_fail;
    public Context mContext;

    /**
     * 加载中的dialog
     */
    public Dialog httpDialog;
    /**
     * 返回的结果数据
     */
    public Class<T> result_bean_class;
    /**
     * 回调的接口
     */
    public XCIHttpResult result_http;

    public static int JSON = 1;
    /**
     * 暂时未用到
     */
    public static int XML = 2;
    public static int ELSE = 3;

    @Override
    public Dialog getHttpDialog() {
        return httpDialog;
    }

    /**
     * show_background_when_net_fail true 为展示背景和toast , false仅展示吐司
     */
    public XCResponseHandler(XCIHttpResult result_http,
                             int content_type,
                             boolean show_background_when_net_fail,
                             Class<T> result_bean_class
    ) {
        super();
        this.result_boolean = false;
        this.content_type = content_type;
        this.result_http = result_http;
        this.show_background_when_net_fail = show_background_when_net_fail;
        this.result_bean_class = result_bean_class;

    }

    public XCResponseHandler(XCIHttpResult result_http, Class<T> result_bean_class) {
        this(result_http, JSON, true, result_bean_class);
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 防止该方法被重写，主线程
     */
    @Override
    public final void onFinish() {
        super.onFinish();
    }

    /**
     * 主线程
     */
    @Override
    public void finish() {
        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "---onFinish()");
        XCApp.resetNetingStatus();
        closeHttpDialog();
    }

    /**
     * 主线程
     */
    @Override
    public final void onFailure(int code, Header[] headers, byte[] arg2, Throwable e) {

        if (isXCActivityDestroy(mContext)) {
            return;
        }

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----onFailure()");
        XCApp.i(XCConfig.TAG_HTTP, "onFailure----->status code " + code + "----e.toString()" + e.toString());

        e.printStackTrace();

        if (headers != null) {
            for (Header header : headers) {
                XCApp.i(XCConfig.TAG_HTTP, "headers----->" + header.toString());
            }
        }

        failure(code, headers, arg2, e);
        finish();
    }

    /**
     * 主线程中
     */
    @Override
    public void failure(int code, Header[] headers, byte[] bytes, Throwable e) {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----failure()");

        if (result_http != null) {
            // 回调访问网络失败时的界面
            result_http.onNetFail(show_background_when_net_fail);
        } else {
            // 显示吐司
            XCApp.shortToast("网络有误");
        }
    }


    /**
     * 这里有数据解析的代码 -->在子线程中执行
     */
    @Override
    public final void onSuccess(final int code, final Header[] headers, final byte[] bytes) {

        if (isXCActivityDestroy(mContext)) {
            return;
        }
        // 子线程
        XCApp.getBase_fix_threadpool().execute(new Runnable() {
            @Override
            public void run() {

                XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----onSuccess()");
                XCApp.i(XCConfig.TAG_HTTP, "onSuccess----->status code " + code);

                if (headers != null) {
                    for (Header header : headers) {
                        XCApp.i(XCConfig.TAG_HTTP, "headers----->" + header.toString());
                    }
                }

                parse(bytes);

                XCApp.getBase_handler().post(new Runnable() {
                    @Override
                    public void run() {
                        // 增加activity是否销毁的判断
                        if (isXCActivityDestroy(mContext)) {
                            return;
                        }
                        success(code, headers, bytes);
                        finish();
                    }
                });
            }
        });
    }

    /**
     * 主线程
     */
    @Override
    public void success(int code, Header[] headers, byte[] arg2) {
        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----success()");

        if (result_http != null) {
            result_http.onNetSuccess();
        }
    }

    /**
     * 主线程
     */
    @Override
    public boolean isXCActivityDestroy(Context context) {

        XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----isXCActivityDestroy()");

        if (context == null) {
            XCApp.e(this.toString() + "---activity被销毁了");
            return true;
        }

        if (context instanceof XCBaseActivity) {
            if (((XCBaseActivity) context).isActivityDestroied()) {
                XCApp.e(this.toString() + "---activity被销毁了");
                return true;
            }
        }
        return false;
    }

    /**
     * 在子线程运行的，所以不要有ui或toast等操作
     */
    @Override
    public void parse(byte[] response_bytes) {
        try {
            XCApp.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----parse()");
            // 这里仅提供通用的json格式的解析 ，有些不通用的json，需要重写parse
            if (content_type == JSON) {
                String response_str = new String(response_bytes, XCConfig.ENCODING_UTF8);
                // 把json串打印到控制台
                XCApp.i(XCConfig.TAG_HTTP, response_str);
                // 有的时候json太长，控制台无法全部打印出来，就打印到本地的文件中，该方法受log的isoutput开关控制
                XCApp.tempPrint(response_str);
                // 打印bean到控制台， 然后复制，格式化，即为自动生成的假bean，该方法受log的isoutput开关控制
                XCJsonParse.json2Bean(response_str);

                result_bean = parseWay(response_str, response_bytes);

                if (result_bean == null) {
                    result_boolean = false;
                    XCApp.i(XCConfig.TAG_HTTP, this.toString() + "---parse() , 解析数据失败");
                    return;
                }

                result_boolean = yourCompanyResultRule();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result_boolean = false;
            XCApp.e("解析数据异常---" + this.toString() + "---" + e.toString());
        }
    }


}

