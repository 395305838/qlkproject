package com.xiaocoder.android.fw.general.http;

/**
 * @author xiaocoder
 * 2015-1-16 下午1:55:28
 */
public interface XCIHttpResult {
	// 访问失败的回调, 默认 true为显示无网络时的背景界面+吐司 ,false为仅显示toast,可以重写XCHttpAsyn的fail()方法
	void onNetFail(boolean show_background_when_net_fail);

	// 访问成功的回调-->显示内容视图
	void onNetSuccess();

	// 访问失败后点击按钮或屏幕刷新的回调
	void onNetRefresh();

}
