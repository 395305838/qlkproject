/**
 *
 */
package com.xiaocoder.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCBaseFragment;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.views.R;

/*
 * 动态创建滑块
 */
public class XCMoveBlockPlusFragment extends XCBaseFragment {
    // scrollview下面的linearlayout
    private LinearLayout xc_id_move_block_linearlayout;
    // 内容数组 如 电影 音乐 软件 游戏
    private String[] contents;
    // 图片
    private String[] imageUris;
    // 整个屏幕的宽度显示几个item，多出的部分可以滑动
    private float per_line_size;
    // 初始化时选中第几个（从0 开始，如电影0，音乐1 ，软件2，游戏3，不管是否有分割线都是这样）
    private int init_selected;
    // 是否需要创建分割线
    private boolean is_need_split_line;
    // 滑块距离左右边的间隔
    private float gap_margin;
    // 这个是默认的scrollview的布局
    private int move_block_layout_id = R.layout.xc_l_fragment_move_block_plus;
    // 这个是默认的scrollview的item的布局
    private int move_block_item_id = R.layout.xc_l_view_item_move_block_plus;
    // 记录上一次选中的位置，即linearlayout中的位置
    private int record_selected_position;
    // 是否显示滑块
    private boolean isHiddenBlock;

    int line_margin_top;
    int line_margin_bottom;

    public void setDivideLineMargin(int line_margin_bottom, int line_margin_top) {
        this.line_margin_bottom = line_margin_bottom;
        this.line_margin_top = line_margin_bottom;
    }

    // 可以更换moveblock的布局的layout, 如背景 高度 颜色等需要改变时
    public void setMove_block_layout_id(int move_block_layout_id) {
        this.move_block_layout_id = move_block_layout_id;
    }

    // 可以更换moveblock的item的layout, 如背景 高度 颜色等需要改变时
    public void setMove_block_item_id(int move_block_item_id) {
        this.move_block_item_id = move_block_item_id;
    }

    public void setIsHiddenBlock(boolean isHiddenBlock) {
        this.isHiddenBlock = isHiddenBlock;
    }

    /**
     * @param selected_position  默认选中的位置，0 1 2 3
     * @param per_line_size      每行显示多少个item
     * @param is_need_split_line 是否需要分隔线
     * @param gap_margin         水平线的padding
     */
    public void setInitSelected(int selected_position, float per_line_size, boolean is_need_split_line, float gap_margin) {
        this.per_line_size = per_line_size;
        this.is_need_split_line = is_need_split_line;
        this.gap_margin = gap_margin;
        if (selected_position + 1 > per_line_size) {
            this.init_selected = (int) Math.ceil(per_line_size - 1);
        } else {
            this.init_selected = selected_position;
        }
    }

    /**
     * @param contents items的内容
     */
    public void setContents(String[] contents) {
        this.contents = contents;
    }

    public void setImageUris(String[] imageUris) {
        this.imageUris = imageUris;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, move_block_layout_id);
    }

    @Override
    public void initWidgets() {
        create();
    }

    @Override
    public void listeners() {

    }

    public void doSomething(LinearLayout item_layout, int position) {

    }

    int perItemWidth;

    public int getItemWidth() {
        return perItemWidth;
    }

    public void create() {
        // 获取scrollview下面的linearlayout
        xc_id_move_block_linearlayout = getViewById(R.id.xc_id_move_block_linearlayout);
        // 添加布局item
        if (contents != null && per_line_size > 0) {
            Context context = getActivity();
            // 计算每个item的宽度
            int perWidth = (int) (UtilScreen.getScreenSize(context)[1] / per_line_size);
            perItemWidth = perWidth;
            // 分隔线
            int lineWidth = UtilScreen.dip2px(context, 1);
            int lineMarginTop = UtilScreen.dip2px(context, line_margin_top);
            int lineMarginBottom = UtilScreen.dip2px(context, line_margin_bottom);
            // 从第0个位置开始添加
            int position = 0;
            int index = 0;
            for (String content : contents) {
                LinearLayout item = (LinearLayout) LayoutInflater.from(context).inflate(move_block_item_id, null);
                TextView xc_id_move_block_text = (TextView) item.findViewById(R.id.xc_id_move_block_text);
                ImageView xc_id_move_block_imageview = (ImageView) item.findViewById(R.id.xc_id_move_block_imageview);
                View xc_id_move_block_line = (View) item.findViewById(R.id.xc_id_move_block_line);
                // textview显示的内容,null为不显示， “”与“1”显示
                if (content == null) {
                    setViewGone(false, xc_id_move_block_text);
                } else {
                    xc_id_move_block_text.setText(content);
                    setViewGone(true, xc_id_move_block_text);
                }

                if (imageUris != null && imageUris.length > 0) {
                    String uri = null;
                    if (index <= imageUris.length - 1) {
                        uri = imageUris[index++];
                    }
                    if (uri != null) {
                        XCApp.displayImage(uri, xc_id_move_block_imageview);
                        setViewGone(true, xc_id_move_block_imageview);
                    } else {
                        setViewGone(false, xc_id_move_block_imageview);
                    }
                } else {
                    setViewGone(false, xc_id_move_block_imageview);
                }
                item.setTag(position);
                item.setOnClickListener(this);
                // 设置item的宽高
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(perWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                item.setLayoutParams(ll);
                // 水平线的宽度
                int gap_h_line = UtilScreen.dip2px(context, gap_margin);
                LinearLayout.LayoutParams ll2 = (LayoutParams) xc_id_move_block_line.getLayoutParams();
                ll2.setMargins(gap_h_line, 0, gap_h_line, 0);
                xc_id_move_block_line.setLayoutParams(ll2);
                // item添加到布局
                xc_id_move_block_linearlayout.addView(item);

                // 默认选中
                defaultSelected(position);
                // 是否需要创建分割线
                if (is_need_split_line) {
                    View line = new View(context);
                    LinearLayout.LayoutParams line_ll = new LinearLayout.LayoutParams(lineWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                    line_ll.setMargins(0, lineMarginTop, 0, lineMarginBottom);
                    line.setLayoutParams(line_ll);
                    line.setBackgroundColor(0xffCCCCCC);
                    xc_id_move_block_linearlayout.addView(line);
                    position++;
                    doSomething(item, position / 2);
                } else {
                    doSomething(item, position);
                }
                position++;
            }
        }
    }

    /**
     * 一进入页面的默认选中（从0 开始，如电影0，音乐1 ，软件2，游戏3，不管是否有分割线都是这样）
     *
     * @param position
     */
    public void defaultSelected(int position) {
        if (!is_need_split_line) {
            // 如果没有分割线
            if (position == init_selected) {
                showSelectedStatus(position);
            }
        } else {
            // 如果有分割线
            if (position == init_selected * 2) {
                showSelectedStatus(position);
            }
        }
    }

    /**
     * 点击选中
     *
     * @param position
     */
    public void pressSelected(int position) {
        resetLastSelectedStatus(record_selected_position);
        showSelectedStatus(position);
    }

    public void showSelectedStatus(int position) {
        LinearLayout item = (LinearLayout) xc_id_move_block_linearlayout.getChildAt(position);
        TextView xc_id_move_block_text = (TextView) item.findViewById(R.id.xc_id_move_block_text);
        View xc_id_move_block_line = (View) item.findViewById(R.id.xc_id_move_block_line);
        if (isHiddenBlock) {
            setViewVisible(false, xc_id_move_block_line);
        } else {
            setViewVisible(true, xc_id_move_block_line);
        }
        xc_id_move_block_text.setTextColor(0xff288DE5);
        record_selected_position = position;
    }

    public void resetLastSelectedStatus(int record_selected_position) {
        LinearLayout item = (LinearLayout) xc_id_move_block_linearlayout.getChildAt(record_selected_position);
        TextView xc_id_move_block_text = (TextView) item.findViewById(R.id.xc_id_move_block_text);
        View xc_id_move_block_line = (View) item.findViewById(R.id.xc_id_move_block_line);
        xc_id_move_block_text.setTextColor(0xff000000);
        setViewVisible(false, xc_id_move_block_line);
    }

    @Override
    public void onClick(View v) {
        // if ((Integer) v.getTag() == record_selected_position) {
        // return;
        // }
        int position = (Integer) (v.getTag());

        LinearLayout current_item = (LinearLayout) xc_id_move_block_linearlayout.getChildAt(position);
        ImageView current_imageview = (ImageView) current_item.findViewById(R.id.xc_id_move_block_imageview);

        LinearLayout last_item = (LinearLayout) xc_id_move_block_linearlayout.getChildAt(record_selected_position);
        ImageView last_imageview = (ImageView) last_item.findViewById(R.id.xc_id_move_block_imageview);

        if (listener != null) {
            // 传出去的是不带分割线的 , 即0 1 2 3
            if (is_need_split_line) {
                listener.onClickMoveListener(position / 2, current_item, current_imageview, last_item, last_imageview);
            } else {
                listener.onClickMoveListener(position, current_item, current_imageview, last_item, last_imageview);
            }
        }

        pressSelected(position);
    }

    public interface OnClickMoveListener {
        void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview);
    }

    public OnClickMoveListener listener;

    public void setOnClickMoveListener(OnClickMoveListener listener) {
        this.listener = listener;
    }

}
