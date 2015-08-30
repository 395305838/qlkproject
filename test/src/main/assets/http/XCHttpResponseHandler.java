package http;

import android.app.Dialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.*;
import com.xiaocoder.android.fw.general.json.XCJsonBean;
import com.xiaocoder.android.fw.general.json.XCJsonParse;

import org.apache.http.Header;

/**
 * @author xiaocoder
 * @date 2014-12-30 下午5:04:48
 */
public abstract class XCHttpResponseHandler<T extends XCJsonBean> extends AsyncHttpResponseHandler {
    // 是json还是xml或图片等,默认是json
    public int content_type;
    // 如果result_boolean为真就开始处理业务, 如果为false则不处理
    public boolean result_boolean;
    // 访问网络失败时,是否显示失败页面的背景
    public boolean show_background_when_net_fail;
    public Context mContext;
    public Dialog httpDialog;
    // 如果返回的是json格式,则内容都存在json_result中
    public T result_bean;
    public Class<T> result_bean_class;
    // 回调的接口
    public XCIHttpResult result_http;

    // "0"表示成功 ，非“0”表示失败
    public static String YES = "0";

    public static int JSON = 1;
    public static int XML = 2;
    public static int ELSE = 3;

    // show_background_when_net_fail true 为展示背景和toast , false仅展示吐司
    @SuppressWarnings("rawtypes")
    public XCHttpResponseHandler(XCIHttpResult result_http,
                                 int content_type,
                                 boolean show_background_when_net_fail,
                                 Class<T> result_bean_class) {
        super();
        this.result_boolean = false;
        this.content_type = content_type;
        this.result_http = result_http;
        this.show_background_when_net_fail = show_background_when_net_fail;
        this.result_bean_class = result_bean_class;

    }

    public XCHttpResponseHandler(XCIHttpResult result_http, Class<T> result_bean_class) {
        this(result_http, JSON, true, result_bean_class);
    }

    public XCHttpResponseHandler(XCIHttpResult result_http) {
        this(result_http, JSON, true, (Class<T>) XCJsonBean.class);
    }


    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onFailure(int code, Header[] headers, byte[] arg2, Throwable e) {
        XCApplication.printi(XCConfig.TAG_HTTP, "onFailure----->status code " + code + "----e.toString()" + e.toString());

        e.printStackTrace();

        if (headers != null) {
            for (Header header : headers) {
                XCApplication.printi(XCConfig.TAG_HTTP, "headers----->" + header.toString());
            }
        }

        fail();
    }

    public void fail() {
        if (result_http != null) {
            // 回调访问网络失败时的界面
            result_http.onNetFail(show_background_when_net_fail);
        } else {
            // 显示吐司
            XCApplication.shortToast("网络有误");
        }
    }

    @Override
    public void onSuccess(int code, Header[] headers, byte[] arg2) {
        XCApplication.printi(XCConfig.TAG_HTTP, "onSuccess----->status code " + code);

        if (headers != null) {
            for (Header header : headers) {
                XCApplication.printi(XCConfig.TAG_HTTP, "headers----->" + header.toString());
            }
        }

        check(arg2);
        success();
    }

    public void success() {
        if (result_http != null) {
            result_http.onNetSuccess();
        }
    }

    // onFailure和onSuccess方法调用完成后,都会调用onFinish()
    @Override
    public void onFinish() {
        super.onFinish();
        XCApplication.printi(XCConfig.TAG_HTTP, "onFinish");
        com.xiaocoder.android.fw.general.http.XCHttpAsyn.resetNetingStatus();
        closeHttpDialog();
    }

    private void check(byte[] response_bytes) {
        try {
            // 这里仅提供通用的json格式的解析
            if (content_type == JSON) {
                // 解析数据
                String response = new String(response_bytes, "utf-8");
                // 把json串打印到控制台
                XCApplication.printi(XCConfig.TAG_HTTP, response);

                // 打印bean到控制台， 然后复制
                XCJsonParse.json2Bean(response);

                result_bean = XCJsonParse.getJsonParseData(response, result_bean_class);

                if (result_bean == null) {
                    result_boolean = false;
                    XCApplication.printi(XCConfig.TAG_HTTP, "onSuccess , 解析数据失败");
                    return;
                }

                yourCompanyLogic();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result_boolean = false;
            XCApplication.shortToast("解析数据异常");
        }
    }

    public abstract void yourCompanySecret(RequestParams params, AsyncHttpClient client, boolean needSecret);

    // 对返回状态码的一个判断，每个项目的认定操作成功的状态码或结构可能不同，在这里统一拦截
    public abstract void yourCompanyLogic();

    public abstract void showHttpDialog();

    public abstract void closeHttpDialog();

}
