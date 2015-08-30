package com.xiaocoder.android.fw.general.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/*
 * 会回收的viewpager
 */
public class XCAdapterViewPagerRecyle extends PagerAdapter {
    private List<? extends View> viewList;

    public XCAdapterViewPagerRecyle(List<? extends View> viewList) {
        super();
        this.viewList = viewList;
    }

    // 决定显示页数
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
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = viewList.get(position);
        container.removeView(view);
    }
}