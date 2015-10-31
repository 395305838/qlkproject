package com.xiaocoder.android.fw.general.http;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.IHttp.XCHttpModel;
import com.xiaocoder.android.fw.general.http.IHttp.XCHttpType;
import com.xiaocoder.android.fw.general.http.IHttp.XCSyncType;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpNotify;
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
public class XCHttpSync implements XCIHttpNotify {

    AsyncHttpClient client;

    /**
     * 可以装很多个“line” ，每个“line”即一个BlockingQueue<XCHttpModel>,line里面装了很多待请求的http任务
     * <p/>
     * 每个line里的多个http请求都是串行的
     * <p/>
     * 但是多个line之间，不一定是串行的
     */
    ConcurrentMap<String, BlockingQueue<XCIResponseHandler>> group;

    public XCHttpSync() {
        initAsynHttpClient();
    }

    public void initAsynHttpClient() {
        group = new ConcurrentHashMap<String, BlockingQueue<XCIResponseHandler>>();
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
     * @param xcSyncType   串行的模式，见枚举值
     * @param xcHttpType   GET/POST
     * @param needSecret   是否加密
     * @param isShowDialog 是否showdialog
     * @param urlString    接口地址
     * @param map          参数
     * @param res          回调handler
     */
    public void packToLine(String lineKey, XCSyncType xcSyncType, XCHttpType xcHttpType, boolean needSecret, boolean isShowDialog,
                           String urlString, Map<String, Object> map, XCIResponseHandler res) {

        if (UtilString.isBlank(lineKey) || xcSyncType == null) {
            throw new RuntimeException(this + "---packToLine(参数) 传入的参数为空");
        }

        BlockingQueue<XCIResponseHandler> line = group.get(lineKey);

        if (line == null) {
            line = new LinkedBlockingQueue<XCIResponseHandler>();
            group.put(lineKey, line);
        }

        XCHttpModel model = new XCHttpModel(lineKey, xcSyncType, xcHttpType, needSecret, null, isShowDialog, urlString, map);
        res.setXCHttpModel(model);
        line.add(res);
    }

    /**
     * 执行某一个line里面的http请求
     */
    public void launchLine(String lineKey) {

        if (UtilString.isBlank(lineKey)) {
            throw new RuntimeException(this + "---launchLine(参数) 传入的参数为空");
        }

        BlockingQueue<XCIResponseHandler> line = group.get(lineKey);
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
        XCIResponseHandler handler = line.poll();
        if (handler != null) {
            XCHttpModel model = handler.getXCHttpModel();
            sendSync(handler);
        } else {
            XCApp.e(this + "---launchLine()中的获取的http的model为null，return");
        }
    }

    public void sendSync(XCIResponseHandler res) {

        XCHttpModel model = res.getXCHttpModel();

        Map<String, Object> map = model.getMap();
        boolean needSecret = model.isNeedSecret();
        boolean isShowDialog = model.isShowDialog();
        XCHttpType httpType = model.getXcHttpType();
        String urlString = model.getUrlString();
        String lineKey = model.getLineKey();

        RequestParams params = new RequestParams();

        for (Map.Entry<String, Object> item : map.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            params.put(key, value);
        }

        XCApp.i(XCConfig.TAG_HTTP, params.toString());

        res.yourCompanySecret(params, client, needSecret);

        if (isShowDialog && res.obtainActivity() != null) {
            res.showHttpDialog();
        }

        if (res instanceof AsyncHttpResponseHandler) {
            if (httpType == XCHttpType.GET) {
                XCApp.i(XCConfig.TAG_HTTP, urlString + "------>get sync http url----" + lineKey);
                client.get(urlString, params, (AsyncHttpResponseHandler) res);
            } else if (httpType == XCHttpType.POST) {
                XCApp.i(XCConfig.TAG_HTTP, urlString + "------>post sync http url---" + lineKey);
                client.post(urlString, params, (AsyncHttpResponseHandler) res);
            }
        } else {
            throw new RuntimeException("XCHttpAsyn中的Handler类型不匹配");
        }
    }

    @Override
    public void startNotify(XCIResponseHandler handler, boolean isSuccess) {

    }

    @Override
    public void endNotify(XCIResponseHandler handler, boolean isSuccess) {
        XCHttpModel httpModel = handler.getXCHttpModel();
        if (!UtilString.isBlank(httpModel.getLineKey()) && httpModel.getXcSyncType() != null) {
            // 表示是串行
            if (isSuccess || httpModel.getXcSyncType() == XCSyncType.GO_ON) {
                // 执行下一个请求
                launchLine(httpModel.getLineKey());
            } else {
                if (httpModel.getXcSyncType() == XCSyncType.DELETE_ALL) {
                    // 不执行后面的请求了，清空
                    BlockingQueue<XCIResponseHandler> line = group.get(httpModel.getLineKey());
                    if (line != null) {
                        // 清空line中的handler队列
                        line.clear();
                    }
                    // 移除group中的该line
                    group.remove(httpModel.getLineKey());
                } else if (httpModel.getXcSyncType() == XCSyncType.LOOP) {
                    // 继续执行该handler
                    sendSync(handler);
                }
            }
        }
    }
}
