package com.xiaocoder.android.fw.general.pop;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class XCBasePopupWindow extends PopupWindow {

    protected ViewGroup mContentView;

    public XCBasePopupWindow(ViewGroup contentView, int width, int height) {
        super(contentView, width, height, true);
        mContentView = contentView;
        initWidgets();
        listener();
    }

    public abstract void initWidgets();

    public abstract void listener();

    public View popFindViewById(int id) {
        return mContentView.findViewById(id);
    }

    /**
     * 显示在parent的上部并水平居中
     *
     * @param parent
     */
    public void showViewTopCenter(View parent) {
        showAtLocation(parent, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 显示在parent的中心
     *
     * @param parent
     */
    public void showViewCenter(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示在parent的下方
     *
     * @param parent
     */
    public void showViewBelow(View parent) {
        showAsDropDown(parent);//动画主键的显示
    }

    public void showPopupWindow(View view, int xoff, int yoff) {
        showAsDropDown(view, xoff, yoff);
    }

    public void setAllTime(PopupWindow popupWindow) {
        Class clazz = PopupWindow.class;
        try {
            Method method = clazz.getMethod("setWindowLayoutType", int.class);
            try {
                method.invoke(popupWindow, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}


/**
 * 参考：http://blog.csdn.net/hlyjunhe/article/details/6572159
 * http://www.cnblogs.com/noTice520/archive/2011/08/16/2140356.html
 * http://www.2cto.com/kf/201108/100378.html
 * http://www.cnblogs.com/noTice520/archive/2011/02/15/1955541.html
 * <p/>
 * <p/>
 * 使用PopupWindow可实现弹出窗口效果,，其实和AlertDialog一样，也是一种对话框，两者也经常混用，但是也各有特点。下面就看看使用方法。
 * 首先初始化一个PopupWindow，指定窗口大小参数。
 * <p/>
 * PopupWindow mPop = new PopupWindow(getLayoutInflater().inflate(R.layout.window, null),
 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
 * 也可以分开写：
 * LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
 * //自定义布局
 * ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
 * R.layout.window, null, true);
 * PopupWindow mPop = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,
 * LayoutParams.WRAP_CONTENT, true);
 * 当然也可以手动设置PopupWindow大小。
 * mPop.setContentView(menuView );//设置包含视图
 * mPop.setWidth(int )
 * mPop.setHeight(int )//设置弹出框大小
 * <p/>
 * 设置进场动画：
 * mPop.setAnimationStyle(R.style.AnimationPreview);//设置动画样式
 * <p/>
 * mPop.setOutsideTouchable(true);//这里设置显示PopuWindow之后在外面点击是否有效。如果为false的话，那么点击PopuWindow外面并不会关闭PopuWindow。当然这里很明显只能在Touchable下才能使用。
 * <p/>
 * 当有mPop.setFocusable(false);的时候，说明PopuWindow不能获得焦点，即使设置设置了背景不为空也不能点击外面消失，只能由dismiss()消失，但是外面的View的事件还是可以触发,back键也可以顺利dismiss掉。当设置为popuWindow.setFocusable(true);的时候，加上下面两行设置背景代码，点击外面和Back键才会消失。
 * mPop.setFocusable(true);
 * 需要顺利让PopUpWindow dimiss（即点击PopuWindow之外的地方此或者back键PopuWindow会消失）；PopUpWindow的背景不能为空。必须在popuWindow.showAsDropDown(v);或者其它的显示PopuWindow方法之前设置它的背景不为空：
 * <p/>
 * mPop.setBackgroundDrawable(new ColorDrawable(0));
 * <p/>
 * <p/>
 * <p/>
 * mPop.showAsDropDown(anchor, 0, 0);//设置显示PopupWindow的位置位于View的左下方，x,y表示坐标偏移量
 * <p/>
 * mPop.showAtLocation(findViewById(R.id.parent), Gravity.LEFT, 0, -90);（以某个View为参考）,表示弹出窗口以parent组件为参考，位于左侧，偏移-90。
 * mPop.setOnDismissListenerd(new PopupWindow.OnDismissListener(){})//设置窗口消失事件
 * <p/>
 * 注：window.xml为布局文件
 * <p/>
 * 总结：
 * 1、为PopupWindow的view布局，通过LayoutInflator获取布局的view.如:
 * LayoutInflater inflater =(LayoutInflater)
 * this.anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 * View textEntryView =  inflater.inflate(R.layout.paopao_alert_dialog, null);
 * <p/>
 * 2、显示位置，可以有很多方式设置显示方式
 * pop.showAtLocation(findViewById(R.id.ll2), Gravity.LEFT, 0, -90);
 * 或者
 * pop.showAsDropDown(View anchor, int xoff, int yoff)
 * <p/>
 * 3、进出场动画
 * pop.setAnimationStyle(R.style.PopupAnimation);
 * <p/>
 * 4、点击PopupWindow区域外部,PopupWindow消失
 * this.window = new PopupWindow(anchor.getContext());
 * <p/>
 * this.window.setTouchInterceptor(new OnTouchListener() {
 *
 * @Override public boolean onTouch(View v, MotionEvent event) {
 * if(event.getAction() ==MotionEvent.ACTION_OUTSIDE) {
 * BetterPopupWindow.this.window.dismiss();
 * return true;
 * }
 * return false;
 * }
 * });
 * 总结:showAsDropDown方法: 弹出来的popwindow可以覆盖到v控件上 x轴方向,不管设置多大的正负数,都不会移出屏幕的宽度,即总是在屏幕的宽度中,但有的时候如果过大,可能在底部的会跑到顶部去,也不是很清楚,反正尽量不好超过屏幕就行
 * y轴的偏移量会移出屏幕,比如,pop的高为200,移出了屏幕50,那么pop的空间就只有150了,一般都是前50被遮住,但是pop这个是在剩下的150中重新布局,即效果是后50像素被遮住
 * 偏移量的参考点是 v的左下角的那个点,即(0,0) 不要在oncreate方法没有执行完的时候show,否则badtoken异常
 * 如果反射了那个方法,则pop会一直显示在最顶层,如果是home键不会消失,但是如果按了返回键即推出则会消失,
 * popupwindow的大小是由new PopupWindow时指定的


 */

/*
 // popupwindow一直浮现在应用最顶层 , 但是应用完全退出时会消失
 Class clazz = PopupWindow.class;
 try {
 Method method = clazz.getMethod("setWindowLayoutType", int.class);
 try {
 method.invoke(mPopupWindow, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
 } catch (IllegalArgumentException e) {
 e.printStackTrace();
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 } catch (InvocationTargetException e) {
 e.printStackTrace();
 }
 } catch (SecurityException e) {
 e.printStackTrace();
 } catch (NoSuchMethodException e) {
 e.printStackTrace();
 }*/

/**
 * 总结:showAsDropDown方法: 弹出来的popwindow可以覆盖到v控件上 x轴方向,不管设置多大的正负数,都不会移出屏幕的宽度,即总是在屏幕的宽度中,但有的时候如果过大,可能在底部的会跑到顶部去,也不是很清楚,反正尽量不好超过屏幕就行
 * y轴的偏移量会移出屏幕,比如,pop的高为200,移出了屏幕50,那么pop的空间就只有150了,一般都是前50被遮住,但是pop这个是在剩下的150中重新布局,即效果是后50像素被遮住 偏移量的参考点是 v的左下角的那个点,即(0,0) 不要在oncreate方法没有执行完的时候show,否则badtoken异常
 * 如果反射了那个方法,则pop会一直显示在最顶层,如果是home键不会消失,但是如果按了返回键即推出则会消失,
 * popupwindow的大小是由new PopupWindow时指定的
 */

/*
 * android:pivotX="50"使用绝对坐标
 android:pivotX="50%"相对自己
 android:pivotX="50%p"相对父控件
 */
