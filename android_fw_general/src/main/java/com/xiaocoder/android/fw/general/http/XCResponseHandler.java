package com.xiaocoder.android.fw.general.http;

import android.app.Dialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonParse;

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
 * success() 与 finish() 为主线程中执行的，发了一个handler
 * <p/>
 * 如果有特殊的解析需求，可以新建一个handler类实现parse() 或 parseWay方法
 */
public abstract class XCResponseHandler<T> extends AsyncHttpResponseHandler {
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

    /**
     * "0"表示成功 ，非“0”表示失败
     */
    public static String YES = "0";

    public static int JSON = 1;
    public static int XML = 2;
    public static int ELSE = 3;

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

    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 主线程
     */
    @Override
    public final void onFinish() {
        super.onFinish();
    }

    /**
     * 主线程
     */
    public void finish() {
        XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString()+"---onFinish()");
        XCHttpAsyn.resetNetingStatus();
        closeHttpDialog();
    }

    /**
     * 主线程
     */
    @Override
    public final void onFailure(int code, Header[] headers, byte[] arg2, Throwable e) {

        if (isDestroy()) {
            return;
        }

        XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString()+"-----onFailure()");
        XCApplication.printi(XCConfig.TAG_HTTP, "onFailure----->status code " + code + "----e.toString()" + e.toString());

        e.printStackTrace();

        if (headers != null) {
            for (Header header : headers) {
                XCApplication.printi(XCConfig.TAG_HTTP, "headers----->" + header.toString());
            }
        }

        failure();
        finish();
    }


    /**
     * 主线程中
     */
    public void failure() {

        XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString()+"-----failure()");

        if (result_http != null) {
            // 回调访问网络失败时的界面
            result_http.onNetFail(show_background_when_net_fail);
        } else {
            // 显示吐司
            XCApplication.shortToast("网络有误");
        }
    }


    /**
     * 数据解析的代码 -->在子线程中执行
     * success()和 finish()是在主线程中的
     */
    @Override
    public final void onSuccess(final int code, final Header[] headers, final byte[] bytes) {

        if (isDestroy()) {
            return;
        }

        XCApplication.getBase_fix_threadpool().execute(new Runnable() {
            @Override
            public void run() {

                XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString()+"-----onSuccess()");
                XCApplication.printi(XCConfig.TAG_HTTP, "onSuccess----->status code " + code);

                if (headers != null) {
                    for (Header header : headers) {
                        XCApplication.printi(XCConfig.TAG_HTTP, "headers----->" + header.toString());
                    }
                }

                parse(bytes);

                XCApplication.getBase_handler().post(new Runnable() {
                    @Override
                    public void run() {
                        // 增加activity是否销毁的判断
                        if (isDestroy()) {
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
    public void success(int code, Header[] headers, byte[] arg2) {
        XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString()+"-----success()");

        if (result_http != null) {
            result_http.onNetSuccess();
        }
    }

    public boolean isDestroy() {

        XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString()+"-----isDestroy()");

        if (mContext == null) {
            XCApplication.printe(this.toString() + "---activity被销毁了");
            return true;
        }

        if (mContext instanceof XCBaseActivity) {
            if (((XCBaseActivity) mContext).isActivityDestroied()) {
                XCApplication.printe(this.toString() + "---activity被销毁了");
                return true;
            }
        }
        return false;
    }

    /**
     * 在子线程运行的，所以不要有ui或toast等操作
     */
    public void parse(byte[] response_bytes) {
        try {
            XCApplication.printi(XCConfig.TAG_HTTP_HANDLER, this.toString()+"-----parse()");
            // 这里仅提供通用的json格式的解析 ，有些不通用的json，需要重写parse
            if (content_type == JSON) {
                String response = new String(response_bytes, XCConfig.ENCODING_UTF8);
                // 把json串打印到控制台
                XCApplication.printi(XCConfig.TAG_HTTP, response);
                // 有的时候json太长，控制台无法全部打印出来，就打印到本地的文件中，该方法受log的isoutput开关控制
                XCApplication.tempPrint(response);
                // 打印bean到控制台， 然后复制，格式化，即为自动生成的假bean，该方法受log的isoutput开关控制
                XCJsonParse.json2Bean(response);

                result_bean = parseWay(response);

                if (result_bean == null) {
                    result_boolean = false;
                    XCApplication.printi(XCConfig.TAG_HTTP, this.toString()+"---parse() , 解析数据失败");
                    return;
                }

                yourCompanyResultRule();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result_boolean = false;
            XCApplication.printe("解析数据异常---" + this.toString() + "---" + e.toString());
        }
    }

    /**
     * 子线程中运行的，所以不要有ui或toast等操作
     */
    public abstract T parseWay(String responseStr);

    /**
     * 加密
     */
    public abstract void yourCompanySecret(RequestParams params, AsyncHttpClient client, boolean needSecret);

    /**
     * 对返回状态码的一个判断，每个项目的认定操作成功的状态码或结构可能不同，在这里统一拦截
     */
    public abstract void yourCompanyResultRule();

    /**
     * http开始的dialog
     */
    public abstract void showHttpDialog();

    /**
     * http结束，关闭dialog
     */
    public abstract void closeHttpDialog();

}

