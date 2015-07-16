/**
 * 
 */
package com.xiaocoder.android.fw.general.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * @Description: 该viewpager可点击切换,不可滑动
 * @author xiaocoder
 * @date 2014-11-19 下午4:48:31
 */
public class XCViewPagerNoMove extends ViewPager {

	public XCViewPagerNoMove(Context p_context, AttributeSet p_attrs) {
		super(p_context, p_attrs);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}

}