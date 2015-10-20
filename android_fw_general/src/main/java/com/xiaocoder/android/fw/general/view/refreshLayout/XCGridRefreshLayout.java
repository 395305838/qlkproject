package com.xiaocoder.android.fw.general.view.refreshLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.xiaocoder.android_fw_general.R;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;

/**
 * Created by xiaocoder on 2015/10/9.
 * version: 1.0
 * description: 封装了上下拉 ， 分页 ，无数据背景
 *
 * xml可配置autorefresh属性
 */
public class XCGridRefreshLayout extends XCRefreshLayout {

    public XCGridRefreshLayout(Context context) {
        super(context);
    }

    public XCGridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XCGridRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inflaterLayout(LayoutInflater mInflater) {
        mInflater.inflate(R.layout.xc_l_view_grid_refresh, this, true);
    }

    @Override
    public void initHeadStyle() {
        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        mPtrRefreshLayout.setHeaderView((PtrClassicDefaultHeader) mPtrClassicHeader);
        mPtrRefreshLayout.addPtrUIHandler(mPtrClassicHeader);
    }
}
