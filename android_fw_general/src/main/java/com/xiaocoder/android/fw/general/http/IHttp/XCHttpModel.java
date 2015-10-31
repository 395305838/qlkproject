package com.xiaocoder.android.fw.general.http.IHttp;

import java.util.Map;

/**
 * Created by xiaocoder on 2015/10/30.
 * version: 1.2.0
 * description:
 */
public class XCHttpModel {

    /**
     * line的唯一标识符
     * <p/>
     * 串行的时候，会用到(一般很少串行http请求)，
     */
    private String lineKey;
    /**
     * 串行的模式，见SyncType类
     * <p/>
     * 串行的时候，会用到(一般很少串行http请求)
     */
    private XCSyncType xcSyncType;
    /**
     * POST  GET
     */
    private XCHttpType xcHttpType;
    /**
     * 该次http请求是否加密，不会影响别的http请求
     */
    private boolean needSecret;
    /**
     * 是否可以频繁点击
     * 并行的情形下：如果为false，即第一次http请求没回来，后面发送的http请求无效。如果为true，则http请求无限制
     * （主要应用场景：频繁点击一个带有http请求的按钮，比如验证码按钮、登录按钮、收藏按钮、购物车中的一些相关按钮等）
     * <p/>
     * 串行的情形下（很少）：该值默认为null，即使设置true、false也没有效果，即多个line之间可以并行
     * （如果想控制多个line之间串行，可在上一个line执行完了后，再重启下一个line，endNotify()会在line执行结束后调用lineFinish()）
     */
    private Boolean isFrequentlyClick;
    /**
     * 请求的过程中是否显示进度dialog
     */
    private boolean isShowDialog;
    /**
     * 接口地址
     */
    private String urlString;
    /**
     * http请求参数
     */
    private Map<String, Object> map;

    public XCHttpModel(String lineKey, XCSyncType xcSyncType, XCHttpType xcHttpType, boolean needSecret,
                       Boolean isFrequentlyClick, boolean isShowDialog, String urlString, Map<String, Object> map) {
        this.lineKey = lineKey;
        this.xcSyncType = xcSyncType;
        this.xcHttpType = xcHttpType;
        this.needSecret = needSecret;
        this.isFrequentlyClick = isFrequentlyClick;
        this.isShowDialog = isShowDialog;
        this.urlString = urlString;
        this.map = map;
    }

    public String getLineKey() {
        return lineKey;
    }

    public void setLineKey(String lineKey) {
        this.lineKey = lineKey;
    }

    public XCSyncType getXcSyncType() {
        return xcSyncType;
    }

    public void setXcSyncType(XCSyncType xcSyncType) {
        this.xcSyncType = xcSyncType;
    }

    public XCHttpType getXcHttpType() {
        return xcHttpType;
    }

    public void setXcHttpType(XCHttpType xcHttpType) {
        this.xcHttpType = xcHttpType;
    }

    public boolean isNeedSecret() {
        return needSecret;
    }

    public void setNeedSecret(boolean needSecret) {
        this.needSecret = needSecret;
    }

    public Boolean getIsFrequentlyClick() {
        return isFrequentlyClick;
    }

    public void setIsFrequentlyClick(Boolean isFrequentlyClick) {
        this.isFrequentlyClick = isFrequentlyClick;
    }

    public boolean isShowDialog() {
        return isShowDialog;
    }

    public void setIsShowDialog(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}