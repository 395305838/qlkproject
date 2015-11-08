package com.xiaocoder.android.fw.general.function.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

/**
 * 不会回收的viewpagerfragment
 */
public class XCAdapterViewPagerFragment extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;

	public XCAdapterViewPagerFragment(FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	// 这里空实现， 否则会回收
	@Override
	public void destroyItem(View container, int position, Object object) {

	}
}
