package com.xiaocoder.android.fw.general.http.IHttp;

import android.app.Activity;

import java.util.Map;

/**
 * Created by xiaocoder on 2015/10/30.
 * version: 1.2.0
 * description:
 */
public class XCHttpModel {

    /**
     * 串行的时候，会用到(一般很少串行http请求)，line的唯一标识符
     */
    public String lineKey;
    /**
     * 串行的时候，会用到(一般很少串行http请求)
     */
    public SyncType syncType;
    /**
     * POST  GET
     */
    public HttpType httpType;
    /**
     * 该次http请求是否加密
     */
    public boolean needSecret;
    /**
     * （只在非串行的情形下 设置该值才有效，即串行的时候，不考虑该值，串行的时候考虑该值无意义）
     * 是否可以频繁点击，即第一次http请求没回来，后面的（按钮点击）发送http请求无效
     * （主要是应对如一个请求http的按钮频繁的点击）
     */
    public Boolean isFrequentlyClick;
    /**
     * 请求的过程中是否显示进度dialog
     */
    public boolean isShowDialog;
    /**
     * 主要是判断该activity是否销毁用的
     */
    public Activity activity;
    /**
     * 接口地址
     */
    public String urlString;
    /**
     * http参数
     */
    public Map<String, Object> map;
    /**
     * 回调的handler
     */
    public XCIResponseHandler res;

    public XCHttpModel(String lineKey, SyncType syncType, HttpType httpType, boolean needSecret,
                       Boolean isFrequentlyClick, boolean isShowDialog, Activity activity, String urlString,
                       Map<String, Object> map, XCIResponseHandler res) {
        this.lineKey = lineKey;
        this.syncType = syncType;
        this.httpType = httpType;
        this.needSecret = needSecret;
        this.isFrequentlyClick = isFrequentlyClick;
        this.isShowDialog = isShowDialog;
        this.activity = activity;
        this.urlString = urlString;
        this.map = map;
        this.res = res;
    }
}