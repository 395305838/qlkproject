package com.xiaocoder.android.fw.general.http;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.IHttp.XCHttpModel;
import com.xiaocoder.android.fw.general.http.IHttp.HttpType;
import com.xiaocoder.android.fw.general.http.IHttp.SyncType;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpEndNotify;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xiaocoder on 2015/10/30.
 * version: 1.2.0
 * description: 串行的http请求
 */
public class XCHttpSync implements XCIHttpEndNotify {

    AsyncHttpClient client;

    /**
     * 可以装很多个“line” ，每个“line”即一个BlockingQueue<XCHttpModel>,line里面装了很多待请求的http任务
     * <p/>
     * 每个line里的多个http请求都是串行的
     * <p/>
     * 但是多个line之间，不一定是串行的
     */
    ConcurrentMap<String, BlockingQueue<XCHttpModel>> group;

    public XCHttpSync() {
        initAsynHttpClient();
    }

    public void initAsynHttpClient() {
        group = new ConcurrentHashMap<String, BlockingQueue<XCHttpModel>>();
        client = new AsyncHttpClient();
        client.setTimeout(10000);
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    /**
     * 获取该line存在group里的key
     */
    public String getLineKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * 把一个http请求数据包装为一个model，存入集合
     *
     * @param lineKey      存入到哪一个line中
     * @param httpType     GET/POST
     * @param syncType     串行的模式，见枚举值
     * @param needSecret   是否加密
     * @param isShowDialog 是否showdialog
     * @param activity
     * @param urlString    接口地址
     * @param map          参数
     * @param res          回调handler
     */
    public void packToLine(String lineKey, SyncType syncType, HttpType httpType, boolean needSecret, boolean isShowDialog, Activity activity, String urlString, Map<String, Object> map, XCIResponseHandler res) {

        if (UtilString.isBlank(lineKey) || syncType == null) {
            throw new RuntimeException(this + "---packToLine(参数) 传入的参数为空");
        }

        BlockingQueue<XCHttpModel> line = group.get(lineKey);

        if (line == null) {
            line = new LinkedBlockingQueue<XCHttpModel>();
            group.put(lineKey, line);
        }

        line.add(new XCHttpModel(lineKey, syncType, httpType, needSecret, null, isShowDialog, activity, urlString, map, res));
    }

    /**
     * 执行某一个line里面的http请求
     */
    public void launchLine(String lineKey) {

        if (UtilString.isBlank(lineKey)) {
            throw new RuntimeException(this + "---launchLine(参数) 传入的参数为空");
        }

        BlockingQueue<XCHttpModel> line = group.get(lineKey);
        if (line == null) {
            XCApp.e(this + "---launchLine()中的group.get(lineKey)获取的value为null，return");
            return;
        }

        if (line.size() == 0) {
            line.clear();
            group.remove(lineKey);
            XCApp.e(this + "---launchLine()中的group.get(lineKey)获取的value.size()为0，return");
            return;
        }

        // 取队列中的第一个httt请求，然后删除这个请求
        XCHttpModel model = line.poll();
        if (model != null) {
            send(model, model.lineKey, model.httpType, model.needSecret, model.isShowDialog, model.activity, model.urlString, model.map, model.res);
        } else {
            XCApp.e(this + "---launchLine()中的获取的http的model为null，return");
        }
    }

    public void send(XCHttpModel model, String lineKey, HttpType type, boolean needSecret, boolean isShowDialog, Activity activity, String urlString, Map<String, Object> map, XCIResponseHandler res) {
        RequestParams params = new RequestParams();

        for (Map.Entry<String, Object> item : map.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            params.put(key, value);
        }

        XCApp.i(XCConfig.TAG_HTTP, params.toString());
        res.setHttpEndNotify(this);
        res.setHttpModel(model);
        res.setContext(activity);
        res.yourCompanySecret(params, client, needSecret);

        if (isShowDialog && activity != null) {
            res.showHttpDialog();
        }

        if (res instanceof AsyncHttpResponseHandler) {
            if (type == HttpType.GET) {
                XCApp.i(XCConfig.TAG_HTTP, urlString + "------>get sync http url----" + lineKey);
                client.get(urlString, params, (AsyncHttpResponseHandler) res);
            } else if (type == HttpType.POST) {
                XCApp.i(XCConfig.TAG_HTTP, urlString + "------>post sync http url---" + lineKey);
                client.post(urlString, params, (AsyncHttpResponseHandler) res);
            }
        } else {
            throw new RuntimeException("XCHttpAsyn中的Handler类型不匹配");
        }
    }


    @Override
    public void httpEndNotify(XCHttpModel httpModel, boolean isSuccess) {
        if (!UtilString.isBlank(httpModel.lineKey) && httpModel.syncType != null) {
            // 表示是串行
            if (isSuccess || httpModel.syncType == SyncType.GO_ON) {
                // 执行下一个请求
                launchLine(httpModel.lineKey);
            } else {
                if (httpModel.syncType == SyncType.DELETE_ALL) {
                    // 不执行后面的请求了，清空
                    BlockingQueue<XCHttpModel> line = group.get(httpModel.lineKey);
                    if (line != null) {
                        // 清空line中的model队列
                        line.clear();
                    }
                    // 移除group中的该line
                    group.remove(httpModel.lineKey);
                } else if (httpModel.syncType == SyncType.LOOP) {
                    // 继续执行该httpmodel
                    send(httpModel, httpModel.lineKey, httpModel.httpType, httpModel.needSecret,
                            httpModel.isShowDialog, httpModel.activity, httpModel.urlString, httpModel.map, httpModel.res);
                }
            }
        }
    }
}
