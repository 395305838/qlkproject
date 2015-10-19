package com.xiaocoder.android.fw.general.view.refreshLayout;

import android.view.View;

/**
 * Created by xiaocoder on 2015/10/10.
 * version: 1.2.0
 * description:
 */
public interface XCRefreshHandler {
    /**
     * 是否能够下拉刷新
     */
    boolean canRefresh();

    /**
     * 是否能够上拉加载
     */
    boolean canLoad();

    /**
     * 下拉刷新
     * 1 处理获取到的数据
     * 2 调用refreshComplete()方法
     */
    void refresh(View view, int request_page);

    /**
     * 上拉加载
     */
    void load(View view, int request_page);

}
