package com.xiaocoder.android.fw.general.http.IHttp;

/**
 * @author xiaocoder
 *         <p/>
 *         activity实现该接口，控制有网 无网 的背景转换 ， 以及点击无网背景再次刷新，以及土司显示
 *         2015-1-16 下午1:55:28
 */
public interface XCIHttpResult {

    /**
     * 访问失败的回调,
     *
     * @param resHandler                    记录下这个失败的resHandler，下次重新访问
     * @param show_background_when_net_fail 如果为true则显示无网络时的背景界面,false为显示toast
     */
    void onNetFail(XCIResponseHandler resHandler, boolean show_background_when_net_fail);

    /**
     * 访问成功的回调-->显示内容视图
     */
    void onNetSuccess();


}
