package com.xiaocoder.android.fw.general.fragment;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * Created by xilinch on 2015/6/23.
 */
public class XLExpandableListView extends PullToRefreshExpandableListView {

    public XLExpandableListView(Context context) {
        super(context);

    }

    public XLExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public XLExpandableListView(Context context, Mode mode) {
        super(context, mode);
    }

    public XLExpandableListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }
}


