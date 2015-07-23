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
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;
import com.xiaocoder.android.fw.general.listener.XCScrollListener;

import java.util.ArrayList;
import java.util.List;

// T 为List集合中的model或bean的泛型 ，一般都是用通用XCJsonBean

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

public abstract class XCBaseAdapter<T> extends BaseAdapter {

    public List<T> list;
    public Context context;
    public ImageLoader imageloader;
    public XCScrollListener listener;
    public DisplayImageOptions options;
    public T bean;

    /**
     * @param context
     * @param list
     * @param imageloader 如果传null 则用默认的imageloader， 该默认的为universal imageloader框架
     */
    public XCBaseAdapter(Context context, List<T> list, ImageLoader imageloader) {
        this.list = list;
        this.context = context;
        this.listener = new XCScrollListener();
        this.options = XCImageLoaderHelper.getDisplayImageOptions();
        if (imageloader == null) {
            this.imageloader = XCApplication.getBase_imageloader();
        } else {
            this.imageloader = imageloader;
        }
    }

    public XCBaseAdapter(Context context, List<T> list) {
        this(context, list, null);
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

    // 获取滚动的监听器
    public XCScrollListener getXCScrollListener() {
        return listener;
    }

    @Override
    public int getViewTypeCount() {
        // 默认是返回1的 ， 即只有一种类型
        return super.getViewTypeCount();

        // 如果有多种类型， 注释上面return代码，打开以下的
        // return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // 默认的代码
        return super.getItemViewType(position);

        // 如果有多种类型， 注释上面return代码，打开以下的
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

    // ----------------图片加载--------------------
    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        imageloader.displayImage(uri, imageView, options);
    }

    public void displayImage(String uri, ImageView imageView) {
        imageloader.displayImage(uri, imageView, options);
    }

    // ----------------图片加载--------------------

    // ------------------------------------调试-----------------------------------------------
    // 以下受debug控制的
    public void printi(String msg) {
        if (context != null) {
            ((XCBaseActivity) context).printi(msg);
        }
    }

    public void printi(String tag, String msg) {
        if (context != null) {
            ((XCBaseActivity) context).printi(tag, msg);
        }
    }

    public void dShortToast(String msg) {
        if (context != null) {
            ((XCBaseActivity) context).dShortToast(msg);
        }
    }

    public void dLongToast(String msg) {
        if (context != null) {
            ((XCBaseActivity) context).dLongToast(msg);
        }
    }

    public void tempPrint(String msg) {
        if (context != null) {
            ((XCBaseActivity) context).tempPrint(msg);
        }
    }

    // 以下不受debug控制
    public void shortToast(String msg) {
        if (context != null) {
            ((XCBaseActivity) context).shortToast(msg);
        }
    }

    public void longToast(String msg) {
        if (context != null) {
            ((XCBaseActivity) context).longToast(msg);
        }
    }

    public void printe(Context context, Exception e) {
        if (context != null) {
            ((XCBaseActivity) context).printe(context, e);
        }
    }

    public void printe(String hint, Exception e) {
        if (context != null) {
            ((XCBaseActivity) context).printe(hint, e);
        }
    }

    public void printe(Context context, String hint, Exception e) {
        if (context != null) {
            ((XCBaseActivity) context).printe(context, hint, e);
        }
    }

    // ----------------以上调试-----------------------------------

    // ----------------写入和获取配置文件的数据--------------------
    public void spPut(String key, boolean value) {
        if (context != null) {
            ((XCBaseActivity) context).spPut(key, value);
        }
    }

    public void spPut(String key, int value) {
        if (context != null) {
            ((XCBaseActivity) context).spPut(key, value);
        }
    }

    public void spPut(String key, long value) {
        if (context != null) {
            ((XCBaseActivity) context).spPut(key, value);
        }
    }

    public void spPut(String key, float value) {
        if (context != null) {
            ((XCBaseActivity) context).spPut(key, value);
        }
    }

    public void spPut(String key, String value) {
        if (context != null) {
            ((XCBaseActivity) context).spPut(key, value);
        }
    }

    public String spGet(String key, String default_value) {
        if (context != null) {
            return ((XCBaseActivity) context).spGet(key, default_value);
        }
        return null;
    }

    public boolean spGet(String key, boolean default_value) {
        if (context != null) {
            return ((XCBaseActivity) context).spGet(key, default_value);
        }
        return false;
    }

    public int spGet(String key, int default_value) {
        if (context != null) {
            return ((XCBaseActivity) context).spGet(key, default_value);
        }
        return -1;
    }

    public long spGet(String key, long default_value) {
        if (context != null) {
            return ((XCBaseActivity) context).spGet(key, default_value);
        }
        return -1;
    }

    public float spGet(String key, float default_value) {
        if (context != null) {
            return ((XCBaseActivity) context).spGet(key, default_value);
        }
        return -1;
    }

}
