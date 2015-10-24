package com.xiaocoder.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class XCListView extends PullToRefreshListView {

	public XCListView(Context context) {
		super(context);
	}

	public XCListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XCListView(Context context, Mode mode) {
		super(context, mode);
	}

	public XCListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}
}
