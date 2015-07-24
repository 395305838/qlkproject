package com.xiaocoder.android.fw.general.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by xiaocoder on 2015/7/24.
 */
public class UtilAbsListStyle {


    public static void setGridViewStyle(GridView view, boolean show_bar, int num) {
        setGridViewStyle(view, show_bar, 0, 0, num);
    }

    public static void setGridViewStyle(GridView view, boolean show_bar) {
        setGridViewStyle(view, show_bar, 0, 0, 1);
    }

    public static void setGridViewStyle(GridView view, boolean show_bar, int space_h_px, int space_v_px, int num) {
        view.setCacheColorHint(0x00000000);
        view.setSelector(new ColorDrawable(0x00000000));
        view.setVerticalScrollBarEnabled(show_bar);
        view.setHorizontalSpacing(space_h_px);
        view.setVerticalSpacing(space_v_px);
//        view.setHorizontalSpacing(UtilImage.dip2px(this, space_h_px));
//        view.setVerticalSpacing(UtilImage.dip2px(this, space_v_px));
        view.setNumColumns(num);
    }

    public static void setExpandListViewStyle(Context context, ExpandableListView view, boolean show_bar, int groupIndicate) {
        view.setCacheColorHint(0x00000000);
        view.setSelector(new ColorDrawable(0x00000000));
        view.setVerticalScrollBarEnabled(show_bar);
        if (groupIndicate <= 0) {
            view.setGroupIndicator(null);
        } else {
            view.setGroupIndicator(context.getResources().getDrawable(groupIndicate));
        }

    }

    public static void setListViewStyle(ListView view, Drawable divider_drawable, int height_px, boolean show_bar) {
        view.setCacheColorHint(0x00000000);
        view.setSelector(new ColorDrawable(0x00000000));
        view.setDivider(divider_drawable);
        view.setDividerHeight(height_px);
        view.setVerticalScrollBarEnabled(show_bar);
    }

    public static void setListViewStyle(ListView view, boolean show_bar) {
        setListViewStyle(view, null, 0, show_bar);
    }
}
