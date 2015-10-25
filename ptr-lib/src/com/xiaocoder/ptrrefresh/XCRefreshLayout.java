package com.xiaocoder.ptrrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.listener.XCScrollListener;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.android.fw.general.view.IRefreshHandler.XCIRefreshHandler;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.R;

/**
 * Created by xiaocoder on 2015/10/9.
 * version: 1.0
 * description: 封装了上下拉 ， 分页 ，无数据背景
 * 仅适用于 abslistview
 *
 * 可配置autorefresh属性
 * */
abstract public class XCRefreshLayout extends FrameLayout implements View.OnClickListener {
    /**
     * 上下拉效果的控件
     */
    protected PtrFrameLayout mPtrRefreshLayout;
    private AbsListView absListView;
    /**
     * 上拉加载的dialog
     */
    private RelativeLayout mProgressBarContainer;
    /**
     * 一进入页面是否自动加载
     */
    private boolean autoRefresh;
    private XCIRefreshHandler mRefreshHandler;
    /**
     * 数据为0的背景
     */
    public LinearLayout base_listview_zero_bg;
    /**
     * 当前页
     */
    public int base_currentPage;
    /**
     * 总页数
     */
    public int base_totalPage;
    /**
     * 是否正在刷新(下拉和上拉)
     */
    public boolean base_isRequesting;
    /**
     * 装分页数据的
     */
    public List base_all_beans;
    /**
     * 每页的数量（最后还是要靠页数决定的）
     */
    public static int PER_PAGE_NUM = 30;
    /**
     * 数据为0时，背景上显示的图片
     */
    public ImageView base_zero_imageview;
    /**
     * 数据为0时，背景上显示的文字
     */
    public TextView base_zero_textview;

    /**
     * 数据为0时，背景上显示的点击按钮
     */
    public Button base_zero_button;
    /**
     * 显示的文本信息
     */
    public String zero_text_hint;
    public String zero_button_hint;
    /**
     * 图片id
     */
    public int zero_imageview_hint;

    public XCRefreshLayout(Context context) {
        super(context);
    }

    public XCRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public XCRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs);
    }

    public void initLayout(Context context, AttributeSet attrs) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflaterLayout(mInflater);
        mPtrRefreshLayout = (PtrClassicFrameLayout) findViewById(R.id.xc_id_refresh_layout);
        absListView = (AbsListView) findViewById(R.id.xc_id_refresh_content_abslistview);
        mProgressBarContainer = (RelativeLayout) findViewById(R.id.xc_id_progressBar_container);
        base_listview_zero_bg = (LinearLayout) findViewById(R.id.xc_id_listview_plus_zero_bg);

        initXCRefreshLayoutParams();

        initHeadStyle();

        checkAutoRefresh(context, attrs);

        initZeroBgLayout();

    }

    // mInflater.inflate(R.layout.xc_l_view_grid_refresh, this, true);
    abstract public void inflaterLayout(LayoutInflater mInflater);

    private void initZeroBgLayout() {
        base_all_beans = new ArrayList();
        // 默认当前页为1
        base_currentPage = 1;
        // 还未设置总页数
        base_totalPage = -1;
        base_zero_imageview = (ImageView) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_imageview);
        base_zero_textview = (TextView) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_hint_textview);
        base_zero_button = (Button) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_do_button);

        base_zero_button.setOnClickListener(this);
        base_zero_imageview.setOnClickListener(this);
    }

    public void setHandler(XCIRefreshHandler refreshHandler) {
        this.mRefreshHandler = refreshHandler;

        registerRefreshHandler();

        registerLoadHandler();
    }

    public void registerLoadHandler() {

        if (mRefreshHandler.canLoad()) {
            absListView.setOnScrollListener(new XCScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

                    if (base_totalPage <= 1 || base_isRequesting) {
                        // 如果是空数据0页或者只有1页
                        // 或者是正在访问就不去加载下一页
                        return;
                    }

                    // 当前页滚动到了底部 且 不是最后一页
                    if (isBottom() && hasNext()) {
                        // 继续加载下一页
                        loading();
                    }
                }
            });
        }
    }

    public void registerRefreshHandler() {

        // 由于该库提供的只有下拉刷新的handler，没有上拉加载的监听，所以另外得实现上拉的逻辑(在setHandler（）方法里)
        // 试过了，如果不设置ptrhandler，也可以下拉，所以得在checkCanDoRefresh里控制是否可以下拉
        mPtrRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 这个refreshing()只有在checkCanDoRefresh()返回为true时才会调用
                refreshing();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mRefreshHandler.canRefresh()) {
                    /**
                     * 如果是可下拉刷新的listview，这句必须调用，否则下拉与listview滑动会有冲突
                     */
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * 该控件下有一个autoRefresh的属性 ，用于控制一进入页面是否自动刷新
     * <p/>
     * autoRefresh：true:一进入页面就自动刷新
     * autoRefresh：false：不会自动刷新
     */
    private void checkAutoRefresh(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.XCRefreshLayout, 0, 0);
        if (arr != null) {
            autoRefresh = arr.getBoolean(R.styleable.XCRefreshLayout_autorefresh, false);
        }

        if (autoRefresh) {
            mPtrRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPtrRefreshLayout.autoRefresh();
                }
            }, 150);
        }
    }

    /**
     * 这里没用xml ， 如果是xml则默认的配置
     * xmlns:cube_ptr="http://schemas.android.com/apk/res-auto"
     * cube_ptr:ptr_duration_to_close="200"
     * cube_ptr:ptr_duration_to_close_header="1000"
     * cube_ptr:ptr_ratio_of_header_height_to_refresh="1.2"
     * cube_ptr:ptr_resistance="1.7"
     * cube_ptr:ptr_keep_header_when_refresh="true"
     * cube_ptr:ptr_pull_to_fresh="false"
     */
    public void initXCRefreshLayoutParams() {

        // 默认的设置
        mPtrRefreshLayout.setResistance(1.7f);
        mPtrRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrRefreshLayout.setDurationToClose(200);
        mPtrRefreshLayout.setDurationToCloseHeader(1000);
        /*
         * true为拉的时候就刷新， false为拉了之后且释放时才刷新
         */
        mPtrRefreshLayout.setPullToRefresh(false);
        /*
         * 刷新的过程中，是否显示head
         */
        mPtrRefreshLayout.setKeepHeaderWhenRefresh(true);

    }

    PtrUIHandler mPtrClassicHeader;

    public abstract  void initHeadStyle();

    public PtrFrameLayout getmPtrRefreshLayout() {
        return mPtrRefreshLayout;
    }

    public AbsListView getListView() {
        return absListView;
    }

    public RelativeLayout getmProgressBarLayout() {
        return mProgressBarContainer;
    }

    public LinearLayout getBgZeroLayout() {
        return base_listview_zero_bg;
    }

    // 下拉刷新
    protected void refreshing() {
        if (!base_isRequesting) {
            base_isRequesting = true;
            base_currentPage = 1;
            if (mRefreshHandler != null) {
                mRefreshHandler.refresh(mPtrRefreshLayout, base_currentPage);
            } else {
                throw new RuntimeException(this + "回调handler为null");
            }
        }
    }

    // 上拉加载
    protected void loading() {
        if (!base_isRequesting) {
            base_isRequesting = true;
            mProgressBarContainer.setVisibility(View.VISIBLE);
            base_currentPage = base_currentPage + 1;
            if (mRefreshHandler != null) {
                mRefreshHandler.load(mPtrRefreshLayout, base_currentPage);
            } else {
                throw new RuntimeException(this + "回调handler为null");
            }
        }
    }

    // 刷新完成,需要外部调用
    public void completeRefresh() {

        mPtrRefreshLayout.refreshComplete();

        mProgressBarContainer.setVisibility(View.GONE);

        whichShow(base_all_beans.size());

        base_isRequesting = false;

    }

    // 是否还有下一页
    protected boolean hasNext() {
        // 这里的base_currentPage代表当前已经加载到了第几页
        if (base_currentPage >= base_totalPage) {
            // 当前页大于等于总页数时，是底部
            if (base_totalPage > 1) {
                // 如果是空数据0页，或者只有1页，则不提示
                XCApp.shortToast("已经是最后一页了");
            }
            return false;
        }
        return true;
    }

    // 如果是第一页则需要清空集合
    protected void clearWhenPageOne() {
        if (base_currentPage == 1) {
            base_all_beans.clear();
        }
    }

    public void resetCurrentPage() {

        base_currentPage = 1;

    }

    public void resetCurrentPageAndList() {
        base_currentPage = 1;
        clearWhenPageOne();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xc_id_data_zero_do_button || id == R.id.xc_id_data_zero_imageview) {
            if (onBgZeroButtonClickToDoListener != null) {
                onBgZeroButtonClickToDoListener.onBgZeroButtonClickToDo();
            }
        }
    }

    protected void whichShow(int size) {
        if (size > 0) {
            base_listview_zero_bg.setVisibility(View.GONE);
            absListView.setVisibility(View.VISIBLE);
        } else {
            base_zero_button.setText(zero_button_hint);
            base_zero_imageview.setImageResource(zero_imageview_hint);
            base_zero_textview.setText(zero_text_hint);

            base_listview_zero_bg.setVisibility(View.VISIBLE);
            absListView.setVisibility(View.INVISIBLE);
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

    protected void updateList(boolean append, List list, XCBaseAdapter adapter) {

        if (list == null) {
            list = new ArrayList();
        }

        if (!append) {
            base_all_beans.clear();
        } else {
            clearWhenPageOne();
        }

        base_all_beans.addAll(list);
        adapter.update(base_all_beans);
        adapter.notifyDataSetChanged();
    }

    public void updateListAdd(List list, XCBaseAdapter adapter) {
        updateList(true, list, adapter);
    }

    public void updateListNoAdd(List list, XCBaseAdapter adapter) {
        updateList(false, list, adapter);
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

    // 0数据背景的点击监听
    OnBgZeroButtonClickToDoListener onBgZeroButtonClickToDoListener;

    public interface OnBgZeroButtonClickToDoListener {
        void onBgZeroButtonClickToDo();
    }

    public void setOnBgZeroButtonClickToDoListener(OnBgZeroButtonClickToDoListener onBgZeroButtonClickToDoListener) {
        this.onBgZeroButtonClickToDoListener = onBgZeroButtonClickToDoListener;
    }

}
