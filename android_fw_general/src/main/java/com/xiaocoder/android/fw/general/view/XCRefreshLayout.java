package com.xiaocoder.android.fw.general.view;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.helper.XCScrollListener;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.android_fw_general.R;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by xiaocoder on 2015/10/9.
 * version: 1.0
 * description: 封装了上下拉 ， 分页 ，无数据背景
 */
public class XCRefreshLayout extends FrameLayout implements View.OnClickListener {

    /**
     * 上下拉效果的控件
     */
    private PtrClassicFrameLayout mPtrRefreshLayout;
    /**
     * 该控件在PtrClassicFrameLayout里面
     */
    private RelativeLayout mRefreshContentContainer;
    /**
     * mRefreshContentContainer里面装的控件，即展示的内容
     */
    private View mRefreshContent;
    /**
     * 上拉加载的dialog
     */
    private ProgressBar mProgressBar;
    /**
     * 一进入页面是否自动加载
     */
    private boolean autoRefresh;
    private XCRefreshHandler mRefreshHandler;
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
     * 是否正在刷新
     */
    public boolean base_isPullRefreshing;
    /**
     * 装分页数据的
     */
    public List base_all_beans;
    /**
     * 每页的数量
     */
    public static int PER_PAGE_NUM = 32;
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

    /**
     * @param refreshHandler
     * @param refreshContent 可以使listview  textview linearlayout等
     * @param absListView    如果是需要上拉加载的，则传入listview 或 gridview，否则传入null
     */
    public void setHandlerAndContent(XCRefreshHandler refreshHandler, View refreshContent, AbsListView absListView) {

        this.mRefreshContent = refreshContent;
        this.mRefreshHandler = refreshHandler;

        mRefreshContentContainer.addView(mRefreshContent);

        if (refreshHandler.canLoad() && absListView != null) {
            absListView.setOnScrollListener(new XCScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                    // 当前页滚动到了底部 且 不是最后一页
                    if (isBottom() && !isEnd()) {
                        // 继续加载下一页
                        loading();
                    }
                }
            });
        }

    }

    public XCRefreshLayout(Context context) {
        super(context);
    }

    public XCRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout(context, attrs);
    }

    public XCRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateLayout(context, attrs);
    }

    public void inflateLayout(Context context, AttributeSet attrs) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.xc_l_view_refresh, this, true);
        mPtrRefreshLayout = (PtrClassicFrameLayout) findViewById(R.id.xc_id_refresh_layout);
        mRefreshContentContainer = (RelativeLayout) findViewById(R.id.xc_id_refresh_content_container);
        mProgressBar = (ProgressBar) findViewById(R.id.xc_id_progressBar);
        base_listview_zero_bg = (LinearLayout) findViewById(R.id.xc_id_listview_plus_zero_bg);

        initXCRefreshLayoutParams();

        checkAutoRefresh(context, attrs);

        initZeroBgLayout();

        registerRefreshHandler();
    }

    private void initZeroBgLayout() {
        base_all_beans = new ArrayList();
        // 默认当前页为1
        base_currentPage = 1;
        base_zero_imageview = (ImageView) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_imageview);
        base_zero_textview = (TextView) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_hint_textview);
        base_zero_button = (Button) base_listview_zero_bg.findViewById(R.id.xc_id_data_zero_do_button);

        base_zero_button.setOnClickListener(this);
        base_zero_imageview.setOnClickListener(this);
    }

    public void registerRefreshHandler() {

        // 由于该库提供的只有下拉刷新的handler，没有上拉加载的监听，所以另外得实现上拉的逻辑(在setHandlerAndContent（）方法里)
        mPtrRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshing();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                if (mRefreshHandler != null) {
                    return mRefreshHandler.canRefresh();
                }
                return false;
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

        mPtrRefreshLayout.setLastUpdateTimeRelateObject(this);
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

    public PtrClassicFrameLayout getmPtrRefreshLayout() {
        return mPtrRefreshLayout;
    }

    public ProgressBar getmProgressBar() {
        return mProgressBar;
    }

    public LinearLayout getBgZeroLayout() {
        return base_listview_zero_bg;
    }

    // 下拉刷新
    protected void refreshing() {
        if (!base_isPullRefreshing) {
            base_isPullRefreshing = true;
            base_currentPage = 1;
            if (mRefreshHandler != null) {
                mRefreshHandler.refresh(mPtrRefreshLayout, base_currentPage);
            }
        }
    }

    // 上拉加载
    protected void loading() {
        if (!base_isPullRefreshing) {
            base_isPullRefreshing = true;
            base_currentPage = base_currentPage + 1;
            if (mRefreshHandler != null) {
                mRefreshHandler.load(mPtrRefreshLayout, base_currentPage);
            }
        }
    }

    // 刷新完成,需要外部调用
    public void completeRefresh() {

        mPtrRefreshLayout.refreshComplete();

        if (mRefreshHandler != null) {
            mRefreshHandler.complete();
        }

        base_isPullRefreshing = false;

        whichShow(base_all_beans.size());

    }

    // 是否是底部
    protected boolean isEnd() {
        if (base_totalPage != 0 && base_currentPage > base_totalPage) {
            // 是底部则结束
            completeRefresh();
            XCApp.shortToast("已经是最后一页了");
            return true;
        }
        return false;
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
        base_all_beans.clear();
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
            mRefreshContent.setVisibility(View.VISIBLE);
        } else {
            base_zero_button.setText(zero_button_hint);
            base_zero_imageview.setImageResource(zero_imageview_hint);
            base_zero_textview.setText(zero_text_hint);

            base_listview_zero_bg.setVisibility(View.VISIBLE);
            mRefreshContent.setVisibility(View.INVISIBLE);
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
        }

        clearWhenPageOne();

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

    // 检查是否是最后一页
    public boolean checkGoOn() {
        if (isEnd()) {
            return false;
        }
        clearWhenPageOne();
        return true;
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
