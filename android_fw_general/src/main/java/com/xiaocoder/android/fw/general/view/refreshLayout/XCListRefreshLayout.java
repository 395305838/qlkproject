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
 * <p/>
 * xml可配置autorefresh属性
 */
public class XCListRefreshLayout extends XCRefreshLayout {

    public XCListRefreshLayout(Context context) {
        super(context);
    }

    public XCListRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XCListRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inflaterLayout(LayoutInflater mInflater) {
        mInflater.inflate(R.layout.xc_l_view_list_refresh, this, true);
    }

    @Override
    public void initHeadStyle() {
        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        mPtrRefreshLayout.setHeaderView((PtrClassicDefaultHeader) mPtrClassicHeader);
        mPtrRefreshLayout.addPtrUIHandler(mPtrClassicHeader);
    }

    @Override
    public void initXCRefreshLayoutParams() {
        super.initXCRefreshLayoutParams();
        // mPtrRefreshLayout.setEnabledNextPtrAtOnce(true);
    }
}
