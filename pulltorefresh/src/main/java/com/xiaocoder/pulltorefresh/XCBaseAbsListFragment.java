package com.xiaocoder.pulltorefresh;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.R;
import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCBaseFragment;
import com.xiaocoder.android.fw.general.util.UtilAbsListStyle;
import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaocoder
 * @Description: 封装了分页 ,下拉刷新，数据0背景的fragment
 * @date 2014-12-31 上午11:30:24
 */
@Deprecated
public abstract class XCBaseAbsListFragment<T extends AbsListView> extends XCBaseFragment implements OnItemClickListener, OnRefreshListener2<T> {

    // 可以刷新的listview或gridview
    public PullToRefreshAdapterViewBase<T> base_refresh_abs_listview;
    // 就是一个listview 或 gridview
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

    @SuppressWarnings("rawtypes")
    public XCBaseAdapter base_adapter;

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
    public void setAdapter(XCBaseAdapter adapter) {

        base_adapter = adapter;

    }

    @SuppressWarnings("rawtypes")
    public XCBaseAdapter getAdapter() {

        return base_adapter;

    }

    // 初始化listview
    @SuppressWarnings("rawtypes")
    public void initList(int refresh_listview_id, int model) {

        base_all_beans = new ArrayList();

        base_refresh_abs_listview = getViewById(refresh_listview_id);
        base_abs_listview = base_refresh_abs_listview.getRefreshableView();
        if (base_abs_listview instanceof GridView) {
            UtilAbsListStyle.setGridViewStyle(((GridView) base_abs_listview), show_bar, grid_space_h_dp, grid_space_v_dp, grid_line_num);
            ((GridView) base_abs_listview).setAdapter(base_adapter);
        } else {
            UtilAbsListStyle.setListViewStyle(((ListView) base_abs_listview), list_divider_drawable, list_height_dp, show_bar);
            ((ListView) base_abs_listview).setAdapter(base_adapter);
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

    public String zero_text_hint;
    public String zero_button_hint;
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
        base_zero_imageview.setOnClickListener(this);
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

        if (base_refresh_abs_listview != null && whichMode != XCBaseAbsListFragment.MODE_NOT_PULL) {
            base_refresh_abs_listview.onRefreshComplete();
            XCApp.i("completeRefresh()");
        }

        base_isPullRefreshing = false;

        whichShow(base_all_beans.size());

    }

    // 是否是底部
    public boolean isBottom() {
        if (base_totalPage != 0 && base_currentPage > base_totalPage) {
            // 是底部则结束
            completeRefresh();
            XCApp.shortToast("已经是最后一页了");
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

    public void resetCurrentPage() {

        base_currentPage = 1;

    }

    public void resetCurrentPageAndList() {
        base_currentPage = 1;
        base_all_beans.clear();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.xc_id_data_zero_do_button || id == R.id.xc_id_data_zero_imageview) {
            if (onBgZeroButtonClickToDoListener != null) {
                onBgZeroButtonClickToDoListener.onBgZeroButtonClickToDo();
            }
        }
    }

    public void whichShow(int size) {
        if (size > 0) {
            setViewGone(false, base_listview_zero_bg);
            setViewVisible(true, base_refresh_abs_listview);
        } else {
            base_zero_button.setText(zero_button_hint);
            base_zero_imageview.setImageResource(zero_imageview_hint);
            base_zero_textview.setText(zero_text_hint);

            setViewGone(true, base_listview_zero_bg);
            setViewVisible(false, base_refresh_abs_listview);
        }
    }

    // 需要在http请求的返回结果中调用该方法 或者 调用setTotalNum方法也可以
    public void setTotalPage(String total_page) {
        if (TextUtils.isEmpty(total_page)) {
            total_page = 1 + "";
        }
        base_totalPage = UtilString.toInt(total_page, 0);
    }

    public void setPerPageNum(String num) {

        // 默认是每页20个
        PER_PAGE_NUM = UtilString.toInt(num, PER_PAGE_NUM);

    }

    // 设置总数，会根据每页默认的数量，计算总页数
    public void setTotalNum(String total_num) {
        if (TextUtils.isEmpty(total_num)) {
            total_num = 1 + "";
        }
        setTotalNum(PER_PAGE_NUM + "", total_num);
    }

    // 根据指定的每页数量，计算总数量
    public void setTotalNum(String page_size, String total_num) {
        setPerPageNum(page_size);
        Integer total = UtilString.toInt(total_num, 0);
        base_totalPage = total % PER_PAGE_NUM == 0 ? total / PER_PAGE_NUM : (total / PER_PAGE_NUM) + 1;
    }

    // 需要在http请求的返回结果中调用该方法
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void updateList(List list) {

        if (list == null) {
            list = new ArrayList();
        }

        if (base_all_beans == null) {
            base_all_beans = new ArrayList();
        }
        base_all_beans.addAll(list);
        base_adapter.update(base_all_beans);
        base_adapter.notifyDataSetChanged();
    }

    public void updateListNoAdd(List list) {

        if (list == null) {
            list = new ArrayList();
        }

        if (base_all_beans == null) {
            base_all_beans = new ArrayList();
        }

        base_all_beans.clear();

        base_all_beans.addAll(list);
        base_adapter.update(base_all_beans);
        base_adapter.notifyDataSetChanged();
    }


    // 检查是否是底部 与 检测是否是刷新
    public boolean checkGoOn() {
        if (isBottom()) {
            return false;
        }
        clearWhenPageOne();
        return true;
    }

    /*
     * 如果不设置，默认为MODE_NOT_PULL，即不可以刷新的listview
     */
    public void setMode(int mode) {

        whichMode = mode;

    }

    // 设置数据为零时候的背景
    public void setBgZeroHintInfo(String zero_text_hint, String zero_button_hint, int zero_imageview_hint) {
        if (zero_button_hint == null) {
            this.zero_button_hint = "";
        } else {
            this.zero_button_hint = zero_button_hint;
        }

        this.zero_imageview_hint = zero_imageview_hint;

        if (zero_text_hint == null) {
            this.zero_text_hint = "";
        } else {
            this.zero_text_hint = zero_text_hint;
        }
    }

    public void setBase_currentPage(int base_currentPage) {

        this.base_currentPage = base_currentPage;

    }

    boolean show_bar = false;
    int grid_line_num = 1;
    int grid_space_h_dp = 1;
    int grid_space_v_dp = 1;
    int list_height_dp = 1;
    Drawable list_divider_drawable = new ColorDrawable(0x666666);

    public boolean hasSetStyle;

    public boolean hasSetStyle() {

        return hasSetStyle;

    }

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

    public interface OnBgZeroButtonClickToDoListener {
        void onBgZeroButtonClickToDo();
    }

    public void setOnBgZeroButtonClickToDoListener(OnBgZeroButtonClickToDoListener onBgZeroButtonClickToDoListener) {
        this.onBgZeroButtonClickToDoListener = onBgZeroButtonClickToDoListener;
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

}
