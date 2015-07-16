package com.xiaocoder.test.viewpagerindicator;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.UnderlinePageIndicator;
import com.xiaocoder.test.R;

public class SampleUnderlinesNoFade extends BaseSampleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_underlines);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        UnderlinePageIndicator indicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFades(false);
        mIndicator = indicator;
    }
}