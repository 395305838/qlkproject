package com.xiaocoder.android.fw.general.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;

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

    public void addChildFragment(int layout_id, Fragment fragment, String tag) {
        addChildFragment(layout_id, fragment, tag, false);
    }

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

    /**
     * 页面滑动切换的时候，用得到
     *
     * 如果是bodyfragment，则子类中应该重写返回true
     * @return
     */
    public boolean isBodyFragment() {
        return false;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 调用子fragment中的onActivityResult方法
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                XCApp.i(fragment.toString() + "----onActivityResult");
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

}
