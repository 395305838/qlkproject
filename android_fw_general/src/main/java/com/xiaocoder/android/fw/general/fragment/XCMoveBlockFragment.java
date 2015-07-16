/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.base.XCBaseFragment;
import com.xiaocoder.android.fw.general.util.UtilImage;
import com.xiaocoder.android_fw_general.R;

/*
 * 动态创建滑块,不要用这个
 */
@Deprecated
public class XCMoveBlockFragment extends XCBaseFragment {
	private LinearLayout xc_id_move_content_layout;
	private View xc_id_move_block_view;

	private String[] contents;
	private boolean is_need_split_line; // 是否需要创建分割线
	private int record_last_position; // 记录上一次选中的位置,包括分割线

	private int init_selected; // 初始化时选中第几个
	private int move_block_layout_id = R.layout.xc_l_fragment_move_block; // 这个是默认的
																			// ,如果要修改layout可以setMove_block_layout

	// 可以更换整个moveblock的布局的layout, 如背景 高度等需要改变时
	public void setMove_block_layout_id(int move_block_layout_id) {
		this.move_block_layout_id = move_block_layout_id;
	}

	// 这个位置不包括分割线, 如果有3个textview,则默认就是1 2 3 , 该类会自动根据是否有分割线对 这123进行位置的重新计算
	public void setInitSelected(int position) {
		this.init_selected = position;
	}

	private float gap_move_block; // 滑块距离左右边的间隔
	double[] location;

	public interface OnClickMoveListener {
		void onClickMoveListener(int position);
	}

	public OnClickMoveListener listener;

	public void setOnClickMoveListener(OnClickMoveListener listener) {
		this.listener = listener;
	}

	public void setContents(String[] contents, float gap_move_block, boolean is_need_split_line) {
		this.contents = contents;
		this.is_need_split_line = is_need_split_line;
		this.gap_move_block = gap_move_block;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return init(inflater, move_block_layout_id);
	}

	public void create() {
		int size = contents == null ? 0 : contents.length;
		if (contents != null && size > 1) {
			Context context = getActivity();
			// 创建滑块
			createMoveBlock(xc_id_move_block_view, context, size);
			// 创建滑块之上的textview
			int perWidth = UtilImage.getScreenSize(context)[1] / size;
			int perHeight = UtilImage.dip2px(context, 49);
			int perPaddingTop = UtilImage.dip2px(context, 6);
			int perPaddingBottom = UtilImage.dip2px(context, 2);
			int lineWidth = UtilImage.dip2px(context, 1);
			int lineMarginTop = UtilImage.dip2px(context, 12);
			int lineMarginBottom = UtilImage.dip2px(context, 8);

			int position = 0;
			for (String content : contents) {
				TextView textview = new TextView(context);
				textview.setGravity(Gravity.CENTER);
				textview.setTextSize(16);
				textview.setPadding(0, perPaddingTop, 0, perPaddingBottom);
				textview.setTextColor(0xff000000);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(perWidth, perHeight);
				textview.setLayoutParams(ll);
				textview.setText(content);
				textview.setTag(position);
				textview.setOnClickListener(this);
				xc_id_move_content_layout.addView(textview);

				// 默认选中的设置
				if (!is_need_split_line) {
					// 如果有分割线
					if (position == init_selected) {
						textview.setTextColor(0xffff0000);
						record_last_position = position;
						moveBlock(position); // 该方法接收的位置是带有分割线的
					}
				} else {
					// 如果没有分割线
					if (position == init_selected * 2) {
						textview.setTextColor(0xffff0000);
						record_last_position = position;
						moveBlock(position); // 该方法接收的位置是带有分割线的
					}
				}

				// 是否需要创建分割线
				if (is_need_split_line) {
					View line = new View(context);
					LinearLayout.LayoutParams line_ll = new LinearLayout.LayoutParams(lineWidth, LinearLayout.LayoutParams.MATCH_PARENT);
					line_ll.setMargins(0, lineMarginTop, 0, lineMarginBottom);
					line.setLayoutParams(line_ll);
					line.setBackgroundColor(0xffCCCCCC);
					xc_id_move_content_layout.addView(line);
					position++;
				}
				position++;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// if ((Integer) v.getTag() == record_last_position) {
		// return;
		// }
		// 移动滑块 , 接收的位置是带有分割线的
		moveBlock((Integer) (v.getTag()));
		// 先把上一次的textview设置成原始颜色
		((TextView) xc_id_move_content_layout.getChildAt(record_last_position)).setTextColor(0xff000000);

		// 获得此次点击的textview
		TextView textview = (TextView) v;
		textview.setTextColor(0xffff0000);
		// 记录位置, 留到下次用 ,该位置是带有分割线的
		record_last_position = (Integer) textview.getTag();

		if (listener != null) {
			// 传出去的是不带分割线的 , 即是第几个textview
			if (is_need_split_line) {
				listener.onClickMoveListener(record_last_position / 2);
			} else {
				listener.onClickMoveListener(record_last_position);
			}
		}
	}

	/*
	 * 创建滑块
	 */
	private void createMoveBlock(View move_block, Context context, int size) {
		int width = UtilImage.getScreenSize(context)[1];// 获取屏幕尺寸
		int padding = UtilImage.dip2px(context, gap_move_block);// 两边的间隔
		int line = 0;// 分割线的宽度
		if (is_need_split_line) {
			line = UtilImage.dip2px(context, 1);// 竖线的间隔
		}
		double moveWidth = width / (size * 1.0) - 2 * padding;
		location = new double[size];
		// 不写固定, 动态获取
		for (int i = 0; i < size; i++) {
			location[i] = moveWidth * i + padding * (i * 2 + 1) + line * i;
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) moveWidth, UtilImage.dip2px(context, 2.0f)); // 像素构建,滑块的高度是1dp
		// 剛進入界面默認選中時的間隔
		params.setMargins(padding, 0, 0, 0);
		move_block.setLayoutParams(params);
	}

	/*
	 * 移动滑块 , 该类里面用 , 接收分割线的位置
	 */
	private void moveBlock(int to_position) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) xc_id_move_block_view.getLayoutParams();
		if (is_need_split_line) {
			lp.setMargins((int) location[to_position / 2], 0, 0, 0);
		} else {
			lp.setMargins((int) location[to_position], 0, 0, 0);
		}
		xc_id_move_block_view.setLayoutParams(lp);
	}

	/*
	 * 滑块和字全部移动, 接收是不带分割线的位置
	 */
	public void allMove(int position) {
		if (is_need_split_line) {
			position = position * 2;
			// 移动滑块
			moveBlock(position);
			// 设置字体
			// 先把上次的字体重新设置
			((TextView) xc_id_move_content_layout.getChildAt(record_last_position)).setTextColor(0xff000000);
			// 设置这次的字体
			((TextView) xc_id_move_content_layout.getChildAt(position)).setTextColor(0xffff0000);
			// 记录位置
			record_last_position = position;
		} else {
			// 移动滑块
			moveBlock(position);
			// 设置字体
			// 先把上次的字体重新设置
			((TextView) xc_id_move_content_layout.getChildAt(record_last_position)).setTextColor(0xff000000);
			// 设置这次的字体
			((TextView) xc_id_move_content_layout.getChildAt(position)).setTextColor(0xffff0000);
			// 记录位置
			record_last_position = position;
		}
	}

	@Override
	public void initWidgets() {
		xc_id_move_content_layout = getViewById(R.id.xc_id_move_content_layout);
		xc_id_move_block_view = getViewById(R.id.xc_id_move_block_view);
		create();
	}

	@Override
	public void listeners() {

	}
}
