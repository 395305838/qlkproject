package com.xiaocoder.android.fw.general.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 不会回收的viewpager
 */
public class XCAdapterViewPager extends PagerAdapter {
	private List<? extends View> viewList;
	private Map<View, Boolean> map;

	public XCAdapterViewPager(List<? extends View> viewList) {
		super();
		this.viewList = viewList;
		this.map = new HashMap<View, Boolean>();
	}

	@Override
	public int getCount() {
		if (viewList != null) {
			return viewList.size();
		}
		return 0;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	// instantiate实例化
	// 根据下标实例显示的View
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = viewList.get(position);
		// 防止回收重新创建
		if (map.containsKey(view)) {
			return view;
		} else {
			container.addView(view);
			map.put(view, true);
		}
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// View view = viewList.get(position);
		// container.removeView(view);
	}
}