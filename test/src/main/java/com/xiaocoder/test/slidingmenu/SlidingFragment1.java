/**
 *
 */
package com.xiaocoder.test.slidingmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaocoder.android.fw.general.application.XCBaseFragment;
import com.xiaocoder.test.R;


public class SlidingFragment1 extends XCBaseFragment {

    @Override
    public boolean isBodyFragment() {
        return true;
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 设置布局
		return init(inflater, R.layout.fragment_sliding1);
	}

	// 初始化控件
	@Override
	public void initWidgets() {

	}

	// 设置监听
	@Override
	public void listeners() {

	}

}