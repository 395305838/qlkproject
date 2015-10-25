package com.xiaocoder.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;

@Deprecated
public class XCGridView extends PullToRefreshGridView{

	public XCGridView(Context context) {
		super(context);
	}

	public XCGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XCGridView(Context context, Mode mode) {
		super(context, mode);
	}

	public XCGridView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}
}
