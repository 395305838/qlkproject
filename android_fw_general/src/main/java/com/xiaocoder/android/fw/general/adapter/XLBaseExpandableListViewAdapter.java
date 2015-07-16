/**
 *
 */
package com.xiaocoder.android.fw.general.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.dialog.XCdialog;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.listener.XCScrollListener;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

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

public abstract class XLBaseExpandableListViewAdapter extends BaseExpandableListAdapter {

    public List<XCJsonBean> list;
    public Context context;
    public ImageLoader imageloader;
    public XCScrollListener listener;
    public DisplayImageOptions options;
    public XCJsonBean bean;
    public XCJsonBean childBean;

    /**
     * @param context
     * @param list
     * @param imageloader 如果传null 则用默认的imageloader， 该默认的为universal imageloader框架
     */
    public XLBaseExpandableListViewAdapter(Context context, List<XCJsonBean> list, ImageLoader imageloader) {
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

    public XLBaseExpandableListViewAdapter(Context context, List<XCJsonBean> list) {
        this(context, list, null);
    }

    public List<XCJsonBean> getList() {
        return list;
    }

    public void update(List<XCJsonBean> list) {
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        if (list != null) {
            XCJsonBean childBean = list.get(i);
            List<XCJsonBean> childList = childBean.getList("list");
            if (childList != null) {
                return childList.size();
            }
        }
        return 0;
    }

    @Override
    public XCJsonBean getGroup(int i) {
        if (list != null) {
            return list.get(i);
        }
        return null;
    }

    @Override
    public XCJsonBean getChild(int i, int i1) {
        if (list != null) {
            return list.get(i).getList("list").get(i1);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    // 获取滚动的监听器
    public XCScrollListener getXCScrollListener() {
        return listener;
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
        imageloader.displayImage(uri, imageView);
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

    // ----------------以上写入和获取配置文件的数据--------------------

    public void showSystemWaitingDialogH(String desc) {
        if (context != null) {
            ((XCBaseActivity) context).showSystemWaitingDialogH(desc);
        }
    }

    public void showSystemWaitingDialogV(String desc) {
        if (context != null) {
            ((XCBaseActivity) context).showSystemWaitingDialogV(desc);
        }
    }

    public void closeDialog() {
        if (context != null) {
            ((XCBaseActivity) context).closeDialog();
        }
    }

    /**
     * @param titleHint  标题提示 ， 如 “温馨提示”
     * @param desc       内容 "您好,以上药品为处方药,根据国家规定需要上传处方单或专业医师开单才可以购买"
     * @param buttonHint 两个按钮 如 new String[] { "取消" ， "确定"}
     * @param callBack   重写confirm 与 cancle方法
     */

    public void showQueryDialogTwoButton(String titleHint, String desc, String[] buttonHint, XCdialog.DialogCallBack callBack) {
        if (context != null) {
            ((XCBaseActivity) context).showQueryDialogTwoButton(titleHint, desc, buttonHint, callBack);
        }
    }

    /**
     * @param titleHint  标题提示 ， 如 “温馨提示”
     * @param desc       内容 "您好,以上药品为处方药,根据国家规定需要上传处方单或专业医师开单才可以购买"
     * @param buttonHint 两个按钮 如 new String[] { "知道了"}
     * @param callBack   只需要重写confirm方法
     */
    public void showQueryDialogOneButton(String titleHint, String desc, String[] buttonHint, XCdialog.DialogCallBack callBack) {
        if (context != null) {
            ((XCBaseActivity) context).showQueryDialogOneButton(titleHint, desc, buttonHint, callBack);
        }
    }


}
