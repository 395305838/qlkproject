package com.xiaocoder.android.fw.general.base;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.adapter.XLBaseExpandableListViewAdapter;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.util.UtilAbsListStyle;
import com.xiaocoder.android_fw_general.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xilinch
 * @Description: 封装了分页 , 目前只支持一个页面中只有一个可下拉刷新的listview或gridview
 * @date 2014-12-31 上午11:30:24
 */
public abstract class XLBaseExpandAbsListFragment<T extends AbsListView> extends XCBaseFragment implements OnItemClickListener, OnRefreshListener2<T> {

    // 可以刷新的expandListView
    public PullToRefreshAdapterViewBase<T> base_refresh_abs_listview;
    // 就是一个expandListView
    public T base_abs_listview;
    // 数据为0的背景
    public LinearLayout base_listview_zero_bg;
    // 当前页
    public int base_currentPage;
    // 总页数
    public int base_totalPage;
    // 是否正在刷新
    public boolean base_isPullRefreshing;
    // 装分页数据的
    @SuppressWarnings("rawtypes")
    public List base_all_beans;
    // 每页的数量
    public static int PER_PAGE_NUM = 20;

    // 不可上下拉
    public static final int MODE_NOT_PULL = 0;
    // 可以上下拉
    public static final int MODE_UP_DOWN = 1;
    // 仅可上拉
    public static final int MODE_UP = 2;
    // 仅可下拉
    public static final int MODE_DOWN = 3;

    // 设置的whichMode
    public int whichMode;

    int refresh_listview_id;

    @SuppressWarnings("rawtypes")
    public XLBaseExpandableListViewAdapter base_adapter;

    // 滚动到最底部
    public void scrollToDown() {
        base_abs_listview.setSelection(base_abs_listview.getBottom());
    }

    // 滚动到最顶部
    public void scrollToUp() {
        base_abs_listview.setSelection(0);
    }

    // 获取listview或gridview
    public T getAbsListView() {
        return base_abs_listview;
    }

    // 拿到数据为0的背景， 如果没有传这个布局的id，那么可能这个空间为null
    public LinearLayout getBgZeroLayout() {
        return base_listview_zero_bg;
    }

    @SuppressWarnings("rawtypes")
    public void setAdapter(XLBaseExpandableListViewAdapter adapter) {
        base_adapter = adapter;
    }

    @SuppressWarnings("rawtypes")
    public XLBaseExpandableListViewAdapter getAdapter() {
        return base_adapter;
    }

    // 初始化listview
    @SuppressWarnings("rawtypes")
    public void initList(int refresh_listview_id, int model) {

        base_all_beans = new ArrayList();

        base_refresh_abs_listview = getViewById(refresh_listview_id);
        base_abs_listview = base_refresh_abs_listview.getRefreshableView();
        if (base_abs_listview instanceof ExpandableListView) {
            UtilAbsListStyle.setExpandListViewStyle(getBaseActivity(), ((ExpandableListView) base_abs_listview), show_bar, 0);
            ((ExpandableListView) base_abs_listview).setAdapter(base_adapter);
            if (base_adapter != null && base_adapter.list != null && base_adapter.list.size() > 0) {
                for (int i = 0; i < base_adapter.list.size(); i++) {
                    ((ExpandableListView) base_abs_listview).expandGroup(i);
                }
            }
        }

        // 设置上下拉的模式
        if (model == MODE_NOT_PULL) {
            base_refresh_abs_listview.setMode(Mode.DISABLED);
        } else if (model == MODE_UP_DOWN) {
            base_refresh_abs_listview.setMode(Mode.BOTH);
        } else if (model == MODE_DOWN) {
            base_refresh_abs_listview.setMode(Mode.PULL_FROM_START);
        } else if (model == MODE_UP) {
            base_refresh_abs_listview.setMode(Mode.PULL_FROM_END);
        } else {
            base_refresh_abs_listview.setMode(Mode.DISABLED);
        }

        // 设置监听
        base_refresh_abs_listview.setScrollingWhileRefreshingEnabled(true);
        base_refresh_abs_listview.setOnRefreshListener(this);
        // 设置listview的监听
        base_abs_listview.setOnItemClickListener(this);
        // 默认当前页为1
        base_currentPage = 1;
    }

    // 数据为0时，背景上显示的图片
    public ImageView base_zero_imageview;
    // 数据为0时，背景上显示的文字
    public TextView base_zero_textview;
    // 数据为0时，背景上显示的点击按钮
    public Button base_zero_button;

    public CharSequence zero_text_hint;
    public CharSequence zero_button_hint;
    public int zero_imageview_hint;

    public void init(int refresh_listview_id, int listview_zero_bg_layout, int model) {
        initList(refresh_listview_id, model);
        initZeroLayout(listview_zero_bg_layout);
    }

    // 初始化数据为0的背景布局
    public void initZeroLayout(int listview_zero_bg_layout) {
        base_listview_zero_bg = getViewById(listview_zero_bg_layout);
        base_zero_imageview = (ImageView) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_imageview);
        base_zero_textview = (TextView) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_hint_textview);
        base_zero_button = (Button) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_do_button);
        base_zero_button.setOnClickListener(this);
        base_zero_textview.setOnClickListener(this);
    }

    // 下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<T> refreshView) {
        if (!base_isPullRefreshing) {
            base_isPullRefreshing = true;
            base_currentPage = 1;
            if (onRefreshNextPageListener != null) {
                onRefreshNextPageListener.onRefreshNextPageListener(base_currentPage);
            }
        }
    }

    // 上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<T> refreshView) {
        if (!base_isPullRefreshing) {
            base_isPullRefreshing = true;
            base_currentPage = base_currentPage + 1;
            if (onRefreshNextPageListener != null) {
                onRefreshNextPageListener.onRefreshNextPageListener(base_currentPage);
            }
        }
    }

    // 刷新完成
    public void completeRefresh() {
        if (base_refresh_abs_listview != null) {
            base_refresh_abs_listview.onRefreshComplete();
        }
        base_isPullRefreshing = false;
    }

    // 是否是底部
    public boolean isBottom() {
        if (base_totalPage != 0 && base_currentPage > base_totalPage) {
            // 是底部则结束
            completeRefresh();
            shortToast("已经是最后一页了");
            return true;
        }
        return false;
    }

    // 如果是第一页则需要清空集合
    public void clearWhenPageOne() {
        if (base_currentPage == 1) {
            base_all_beans.clear();
        }
    }

    public boolean hasSetStyle;

    public boolean hasSetStyle() {
        return hasSetStyle;
    }


    boolean show_bar = false;
    int grid_line_num = 1;
    int grid_space_h_dp = 1;
    int grid_space_v_dp = 1;
    int list_height_dp = 1;
    Drawable list_divider_drawable = new ColorDrawable(0x666666);

    public void setGridViewStyleParam(boolean show_bar, int num) {
        setGridViewStyleParam(show_bar, grid_space_h_dp, grid_space_v_dp, num);
    }

    public void setGridViewStyleParam(boolean show_bar) {
        setGridViewStyleParam(show_bar, grid_space_h_dp, grid_space_v_dp, grid_line_num);
    }

    public void setGridViewStyleParam(boolean show_bar, int space_h_dp, int space_v_dp, int num) {
        hasSetStyle = true;
        this.show_bar = show_bar;
        this.grid_space_h_dp = space_h_dp;
        this.grid_space_v_dp = space_v_dp;
        this.grid_line_num = num;
    }

    public void setListViewStyleParam(Drawable divider_drawable, int height_dp, boolean show_bar) {
        hasSetStyle = true;
        this.list_divider_drawable = divider_drawable;
        this.list_height_dp = height_dp;
        this.show_bar = show_bar;
    }

    public void setListViewStyleParam(boolean show_bar) {
        setListViewStyleParam(list_divider_drawable, list_height_dp, show_bar);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.xc_id_data_zero_do_button || id == R.id.xc_id_data_zero_imageview) {
            if (onBgZeroButtonClickToDoListener != null) {
                onBgZeroButtonClickToDoListener.onBgZeroButtonClickToDo();
            }
        } else if (id == R.id.xc_id_data_zero_hint_textview) {
            if (onBgZeroTextViewClickToDoListener != null) {
                onBgZeroTextViewClickToDoListener.onBgZeroButtonClickToDo();
            }
        }
    }

    public void whichShow(int size, CharSequence text, int drawable_id, CharSequence button_text) {
        if (size > 0) {
            setViewGone(false, base_listview_zero_bg);
            setViewVisible(true, base_refresh_abs_listview);
        } else {
            base_zero_button.setText(button_text);
            base_zero_imageview.setImageResource(drawable_id);
            base_zero_textview.setText(text);


            setViewGone(true, base_listview_zero_bg);
            setViewVisible(false, base_refresh_abs_listview);
        }
    }

    // public abstract void requestForPage(int currentPage);
    //
    // public abstract void onBgZeroButtonClickToDo();

    // 分页监听
    public interface OnRefreshNextPageListener {
        void onRefreshNextPageListener(int current_page);
    }

    OnRefreshNextPageListener onRefreshNextPageListener;

    public void setOnRefreshNextPageListener(OnRefreshNextPageListener onRefreshNextPageListener) {
        this.onRefreshNextPageListener = onRefreshNextPageListener;
    }

    // 背景点击监听
    OnBgZeroButtonClickToDoListener onBgZeroButtonClickToDoListener;

    // 文字点击
    OnBgZeroTextViewClickToDoListener onBgZeroTextViewClickToDoListener;

    public interface OnBgZeroButtonClickToDoListener {
        void onBgZeroButtonClickToDo();
    }

    public void setOnBgZeroButtonClickToDoListener(OnBgZeroButtonClickToDoListener onBgZeroButtonClickToDoListener) {
        this.onBgZeroButtonClickToDoListener = onBgZeroButtonClickToDoListener;
    }

    public interface OnBgZeroTextViewClickToDoListener {
        void onBgZeroButtonClickToDo();
    }

    public void OnBgZeroTextViewClickToDoListener(OnBgZeroTextViewClickToDoListener onBgZeroTextViewClickToDoListener) {
        this.onBgZeroTextViewClickToDoListener = onBgZeroTextViewClickToDoListener;
    }

    // 点击item监听
    OnAbsListItemClickListener onAbsListItemClickListener;

    public interface OnAbsListItemClickListener {
        void onAbsListItemClickListener(AdapterView<?> arg0, View arg1, int arg2, long arg3);
    }

    public void setOnListItemClickListener(OnAbsListItemClickListener onAbsListItemClickListener) {
        this.onAbsListItemClickListener = onAbsListItemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onAbsListItemClickListener != null) {
            onAbsListItemClickListener.onAbsListItemClickListener(parent, view, position, id);
        }
    }

    // 需要在http请求的返回结果中调用该方法 或者 调用setTotalNum方法也可以
    public void setTotalPage(String total_page) {
        if (TextUtils.isEmpty(total_page)) {
            total_page = 1 + "";
        }
        base_totalPage = Integer.parseInt(total_page);
    }

    public void setPerPageNum(String num) {

        // 默认是每页20个
        PER_PAGE_NUM = Integer.parseInt(num);
    }

    // 设置总数，会自动计算总页数
    public void setTotalNum(String total_num) {
        if (TextUtils.isEmpty(total_num)) {
            total_num = 1 + "";
        }
        setTotalNum(PER_PAGE_NUM + "", total_num);
    }

    public void setTotalNum(String page_size, String total_num) {
        setPerPageNum(page_size);
        Integer total = Integer.parseInt(total_num);
        base_totalPage = total % PER_PAGE_NUM == 0 ? total / PER_PAGE_NUM : (total / PER_PAGE_NUM) + 1;
    }

    // 需要在http请求的返回结果中调用该方法
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void updateList(List list) {
        if (base_all_beans == null) {
            base_all_beans = new ArrayList();
        }
        base_all_beans.addAll(list);
        base_adapter.update(base_all_beans);
        base_adapter.notifyDataSetChanged();

        ((ExpandableListView) base_abs_listview).setAdapter(base_adapter);
        if (base_adapter != null && base_adapter.list != null && base_adapter.list.size() > 0) {
            for (int i = 0; i < base_adapter.list.size(); i++) {
                ((ExpandableListView) base_abs_listview).expandGroup(i);
            }
        }
    }

    public void resetCurrentPage() {
        base_currentPage = 1;
    }

    public void resetCurrentPageAndList() {
        base_currentPage = 1;
        base_all_beans.clear();
    }

    public void updateListNoAdd(List list) {
        if (base_all_beans == null) {
            base_all_beans = new ArrayList();
        } else {
            base_all_beans.clear();
            base_all_beans.addAll(list);
            base_adapter.update(base_all_beans);
        }
        base_adapter.notifyDataSetChanged();
    }


    // 需要在http请求的返回结果中调用该方法
    // 检查是否是底部 与 检测是否是刷新
    public boolean checkGoOn() {
        if (isBottom()) {
            return false;
        }
        clearWhenPageOne();
        return true;
    }

    /*
     * 用的多的为XCBaseAbsListFragment.MODE_NOT_PULL
     * 与XCBaseAbsListFragment.MODE_UP_DOWN
     * ，如果不设置，默认为MODE_NOT_PULL，即不可以刷新的listview
     */
    public void setMode(int mode) {
        whichMode = mode;
    }

    // 设置数据为零时候的背景
    public void setBgZeroHintInfo(CharSequence zero_text_hint, CharSequence zero_button_hint, int zero_imageview_hint) {
        this.zero_button_hint = zero_button_hint;
        this.zero_imageview_hint = zero_imageview_hint;
        this.zero_text_hint = zero_text_hint;

    }

//    public void setBgZeroHintInfo(int zero_text_hint_type,String zero_text_hint, String zero_button_hint, int zero_imageview_hint) {
//        this.zero_button_hint = zero_button_hint;
//        this.zero_imageview_hint = zero_imageview_hint;
//        this.zero_text_hint = zero_text_hint;
//    }

}

// demo

// base_currentPage = page;
// MyHttpAsyn.get(true, true, getActivity(), MyConfig.ORDERS_API, params, new
// MyHttpResponseHandler(this) {
//
// @Override
// public void onSuccess(int i, Header[] headers, byte[] bytes) {
// super.onSuccess(i, headers, bytes);
// if (result) {
// OrdersBean orders_bean_flag = new OrdersBean();

// 检测
// if(!listgragment.checkGoOn()){
// return;
// }
// 设置总页数
// int totalnum =
// Integer.parseInt(origin_bean.getString(orders_bean_flag.total));
// listgragment.setTotalPage(totalnum % PER_PAGE_NUM == 0 ?
// totalnum/PER_PAGE_NUM :(totalnum / PER_PAGE_NUM) + 1);
// 或者设置总数 （这种方式简单）
// listfragment.setTotalNum(total_num);

// 添加集合 与 更新界面
// List<JsonBean> beans = origin_bean.getList(orders_bean_flag.orders);
// listgragment.updateList(beans);
// }
// }
// });
