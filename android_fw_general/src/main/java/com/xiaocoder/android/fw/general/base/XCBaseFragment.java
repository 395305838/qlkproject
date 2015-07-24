package com.xiaocoder.android.fw.general.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * MyFragment onAttach() 粘贴到activity上
 * <p/>
 * MyFragment onCreate() fragment创建
 * <p/>
 * MyFragment onCreateView() fragment创建自己的视图
 * <p/>
 * MainActivity onCreate()
 * <p/>
 * MyFragment onActivityCreated() 可以处理fragment数据的初始化
 * <p/>
 * MainActivity onStart()
 * <p/>
 * MyFragment onStart()
 * <p/>
 * MainActivity onResume()
 * <p/>
 * MyFragment onResume()
 * <p/>
 * 按后退键 MyFragment onPause()
 * <p/>
 * MainActivity onPause()
 * <p/>
 * MyFragment onStop()
 * <p/>
 * MainActivity onStop()
 * <p/>
 * MyFragment onDestoryView() 销毁掉自己的视图
 * <p/>
 * MyFragment onDestory()
 * <p/>
 * MyFragment onDetach() 解除和activity的关系
 * <p/>
 * MainActivity onDetory()
 */
public abstract class XCBaseFragment extends Fragment implements OnClickListener {

    public ViewGroup mContainer;

    @SuppressWarnings("unchecked")
    public <T extends View> T getViewById(int id) {
        return (T) mContainer.findViewById(id);
    }

    public View init(LayoutInflater inflater, int layout_id) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer = (ViewGroup) inflater.inflate(layout_id, null);
        mContainer.setLayoutParams(lp);
        // 如果没有以上的会变成包裹高度
        return mContainer;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidgets();
        listeners();
    }

    public abstract void initWidgets();

    public abstract void listeners();

    public XCBaseActivity getBaseActivity() {
        if (getActivity() != null) {
            return (XCBaseActivity) getActivity();
        }
        return null;
    }

    public void addChildFragment(int layout_id, Fragment fragment, String tag, boolean isToBackStack) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(layout_id, fragment, tag);
        if (isToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();
        getChildFragmentManager().executePendingTransactions();
    }

    // 默认不添加
    public void addChildFragment(int layout_id, Fragment fragment, String tag) {
        addChildFragment(layout_id, fragment, tag, false);
    }

    // 默认不添加
    public void addChildFragment(int layout_id, Fragment fragment) {
        addChildFragment(layout_id, fragment, fragment.getClass().getSimpleName(), false);
    }

    public void hideChildFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }

    public void showChildFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void myFinish() {
        if (getActivity() != null) {
            getBaseActivity().myFinish();
        }
    }

    public Intent myGetIntent() {
        if (getActivity() != null) {
            return getBaseActivity().getIntent();
        }
        return null;
    }

    // hidden为true时, 是隐藏的状态 ; hidden为false时,是显示的状态
    // 该方法只有在对fragment进行了hide和show操作时,才会被调用,如果是被别的界面遮住了,是不会调用的
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    // 注意1 : 这里得重写, 否则如果有嵌套fragment的话,回调不到
    // 注意2 : 需要被回调业务代码的那个fragment中的onActivityResult()不要调用super
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 调用子fragment中的onActivityResult方法
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                printi(fragment.toString() + "----onActivityResult");
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void setViewGone(boolean isGone, View view) {
        if (getBaseActivity() != null) {
            getBaseActivity().setViewGone(isGone, view);
        }
    }

    public void setViewVisible(boolean isVisible, View view) {
        if (getBaseActivity() != null) {
            getBaseActivity().setViewVisible(isVisible, view);
        }
    }

    public void myStartActivity(Class<? extends XCBaseActivity> activity_class,
                                int requestCode,
                                int flags,
                                String[] command_keys,
                                Object[] command_values) {
        if (getBaseActivity() != null) {
            getBaseActivity().myStartActivity(activity_class, requestCode, flags, command_keys, command_values);
        }
    }

    public void myStartActivity(Class<? extends XCBaseActivity> activity_class) {
        if (getBaseActivity() != null) {
            getBaseActivity().myStartActivity(activity_class, -1, -1, null, null);
        }
    }

    public void myStartActivity(Class<? extends XCBaseActivity> activity_class, int flags) {
        if (getBaseActivity() != null) {
            getBaseActivity().myStartActivity(activity_class, -1, flags, null, null);
        }
    }

    // 以下受debug控制的
    public void printi(String msg) {
        if (getBaseActivity() != null) {
            getBaseActivity().printi(msg);
        }
    }

    public void printi(String tag, String msg) {
        if (getBaseActivity() != null) {
            getBaseActivity().printi(tag, msg);
        }
    }

    public void dShortToast(String msg) {
        if (getBaseActivity() != null) {
            getBaseActivity().dShortToast(msg);
        }
    }

    public void dLongToast(String msg) {
        if (getBaseActivity() != null) {
            getBaseActivity().dLongToast(msg);
        }
    }

    public void tempPrint(String msg) {
        if (getBaseActivity() != null) {
            getBaseActivity().tempPrint(msg);
        }
    }

    // 以下不受debug控制
    public void shortToast(String msg) {
        if (getBaseActivity() != null) {
            getBaseActivity().shortToast(msg);
        }
    }

    public void longToast(String msg) {
        if (getBaseActivity() != null) {
            getBaseActivity().longToast(msg);
        }
    }

    public void printe(String hint, Exception e) {
        if (getBaseActivity() != null) {
            getBaseActivity().printe(hint, e);
        }
    }

    public void printe(Context context, String hint, Exception e) {
        if (getBaseActivity() != null) {
            getBaseActivity().printe(context, hint, e);
        }
    }

    public void printe(Context context, String hint) {
        if (getBaseActivity() != null) {
            getBaseActivity().printe(context, hint);
        }
    }

    public void spPut(String key, boolean value) {
        if (getBaseActivity() != null) {
            getBaseActivity().spPut(key, value);
        }
    }

    public void spPut(String key, int value) {
        if (getBaseActivity() != null) {
            getBaseActivity().spPut(key, value);
        }
    }

    public void spPut(String key, long value) {
        if (getBaseActivity() != null) {
            getBaseActivity().spPut(key, value);
        }
    }

    public void spPut(String key, float value) {
        if (getBaseActivity() != null) {
            getBaseActivity().spPut(key, value);
        }
    }

    public void spPut(String key, String value) {
        if (getBaseActivity() != null) {
            getBaseActivity().spPut(key, value);
        }
    }

    public String spGet(String key, String default_value) {
        if (getBaseActivity() != null) {
            return getBaseActivity().spGet(key, default_value);
        }
        return default_value;
    }

    public boolean spGet(String key, boolean default_value) {
        if (getBaseActivity() != null) {
            return getBaseActivity().spGet(key, default_value);
        }
        return default_value;
    }

    public int spGet(String key, int default_value) {
        if (getBaseActivity() != null) {
            return getBaseActivity().spGet(key, default_value);
        }
        return default_value;
    }

    public long spGet(String key, long default_value) {
        if (getBaseActivity() != null) {
            return getBaseActivity().spGet(key, default_value);
        }
        return default_value;
    }

    public float spGet(String key, float default_value) {
        if (getBaseActivity() != null) {
            return getBaseActivity().spGet(key, default_value);
        } else {
            return default_value;
        }
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        if (getBaseActivity() != null) {
            getBaseActivity().displayImage(uri, imageView, options);
        }
    }

    public void displayImage(String uri, ImageView imageView) {
        if (getBaseActivity() != null) {
            getBaseActivity().displayImage(uri, imageView);
        }
    }

}
