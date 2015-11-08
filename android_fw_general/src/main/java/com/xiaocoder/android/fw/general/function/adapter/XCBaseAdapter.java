package com.xiaocoder.android.fw.general.function.adapter;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import com.xiaocoder.android.fw.general.application.XCBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 有一个tadapter模板
 *
 * @param <T> T 为List集合中的model或bean的泛型
 */
public abstract class XCBaseAdapter<T> extends BaseAdapter {

    public List<T> list;
    public Context context;
    public T bean;

    public XCBaseAdapter(Context context, List<T> list) {
        this.list = list;
        this.context = context;
    }

    public List<T> getList() {
        if (list == null) {
            return list = new ArrayList<T>();
        }
        return list;
    }

    public void update(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // 默认是返回1的 ， 即只有一种类型
        return super.getViewTypeCount();

        // 如果有多种类型，重写该方法
        // return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

        // 如果有多种类型,重写该方法
        // XCJsonBean bean = list.get(position);
        // if (MY.equals(bean.getString(bean.sender))) {
        // return MY;
        // } else {
        // return SHE;
        // }
    }

    public void setViewGone(boolean isGone, View view) {
        if (context != null) {
            ((XCBaseActivity) context).setViewGone(isGone, view);
        }
    }

    public void setViewVisible(boolean isVisible, View view) {
        if (context != null) {
            ((XCBaseActivity) context).setViewVisible(isVisible, view);
        }
    }

}

