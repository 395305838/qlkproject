package com.xiaocoder.test.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.xiaocoder.android.fw.general.adapter.XCAdapterTest;
import com.xiaocoder.android.fw.general.adapter.XCAdapterViewPager;
import com.xiaocoder.views.view.XCNoScrollListview;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;

public class ScrollActivity extends MActivity {

	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_scroll);
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initWidgets() {
		pager = (ViewPager) findViewById(R.id.viewpager);
		ArrayList<View> list = new ArrayList<View>();
		View view = LayoutInflater.from(this).inflate(R.layout.viewpager_layout, null);
		View view2 = LayoutInflater.from(this).inflate(R.layout.viewpager_layout2, null);
		View view3 = LayoutInflater.from(this).inflate(R.layout.viewpager_layout3, null);

		XCNoScrollListview listview_refresh = (XCNoScrollListview) view3.findViewById(R.id.listview_refresh);

		// addHeadView
		View item = LayoutInflater.from(this).inflate(R.layout.viewpager_layout3_item, null);
		View item2 = LayoutInflater.from(this).inflate(R.layout.viewpager_layout3_item2, null);
		View item3 = LayoutInflater.from(this).inflate(R.layout.viewpager_layout3_item2, null);
		listview_refresh.addHeaderView(item);
		listview_refresh.addHeaderView(item2);

		listview_refresh.setAdapter(new XCAdapterTest(this, null));

		list.add(view);
		list.add(view2);
		list.add(view3);

		XCAdapterViewPager adapter = new XCAdapterViewPager(list);
		pager.setAdapter(adapter);
	}

	@Override
	public void listeners() {

	}

	@Override
	protected void initSlideDestroyActivity() {
	}
}
