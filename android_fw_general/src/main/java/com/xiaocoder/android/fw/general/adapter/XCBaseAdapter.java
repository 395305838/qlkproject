/**
 *
 */
package com.xiaocoder.android.fw.general.adapter;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class XCBaseAdapter<T> extends BaseAdapter {

    public List<T> list;
    public Context context;
    public ImageLoader imageloader;
    public DisplayImageOptions options;
    public T bean;

    public XCBaseAdapter(Context context, List<T> list) {
        this.list = list;
        this.context = context;
        this.options = XCApplication.getDisplay_image_options();
        this.imageloader = XCApplication.getBase_imageloader();
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
        // if (MyName.equals(bean.getString(bean_flag.sender))) {
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

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        imageloader.displayImage(uri, imageView, options);
    }

    public void displayImage(String uri, ImageView imageView) {
        imageloader.displayImage(uri, imageView, options);
    }

}


// T 为List集合中的model或bean的泛型

/*

 //继承之后拷贝这里，重写该getView方法， 与补充ViewHolder

 @Override
 public View getView(int position, View convertView, ViewGroup parent) {

 bean = list.get(position);

 ViewHolder holder = null;

 if (convertView == null) {

 convertView = LayoutInflater.from(context).inflate(R.layout.id, null);//填入布局id
 holder = new ViewHolder();//创建holder对象
 holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);//初始化控件
 convertView.setTag(holder); // 设置标记

 } else {

 holder = (ViewHolder)convertView.getTag(); // 获取holder

 }

 // 获取和设置控件的显示值

 // 加载图片

 return convertView;

 }

 class ViewHolder{

 ImageView
 TextView

 }

 public NameAdapter(Context context, List<XCJsonBean> list) {
 super(context, list);
 }
 */
