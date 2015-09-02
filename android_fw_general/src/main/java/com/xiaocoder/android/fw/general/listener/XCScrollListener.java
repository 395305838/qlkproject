package com.xiaocoder.android.fw.general.listener;

import android.os.Parcelable;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * onScrollListener的change的三个状态和onscroll getview onitemclick的调用顺序
 * 
 * 如果listview设置了scrolllistener,那么lisetview一加载的时候就会调用onscroll()方法,调用了onscroll之后,才会调用listview的adapter的getView()
 * 
 * 当手指触摸item没有滑动时(相当于点击): onscroll方法先执行(只要碰到了item项就会调用该方法) ,然后执行 onitemclick方法,(再执行getView方法,如果这一层有item的话,如进入文件夹),最后再执行一次onscroll方法 当手指触摸item且有滑动时: 触摸状态触发,然后是onscroll方法执行 ,再然后getView方法执行
 * 仅当手指离开触摸的item后: (滑动状态触发--可能,因为可能是没滑动时离开屏幕的), (onscroll方法执行--可能,同理),最后是停止状态触发
 * 
 * 在滑动时,getview方法会夹杂在onscroll方法之中执行,且在滑动时getview的调用次数明显少于onscroll的调用次数 不管是滑动还是点击,getView都在onscroll和onitemclick之后执行
 */
public class XCScrollListener implements OnScrollListener {
	private int per_page_num;
	private int start_index;
	private int end_index;
	private Parcelable onSaveInstanceState;

	public XCScrollListener() {
	}

	public Parcelable getOnSaveInstanceState() {
		return onSaveInstanceState;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			// onSaveInstanceState = view.onSaveInstanceState(); // 每当滑动停止的时候,保存listView的状态

			// int index = _end_index - _start_index;
			// for (int i = 0; i <= index; i++) {
			// if (view.getAdapter().getCount() < (_start_index + i + 1)) {
			// return; // 因为有时候 visibleItemCount会变动+1,所以判断以免数组越界
			// }
			// View refreshview = view.getChildAt(i); // 得到局部刷新的viewtiem
			// if (refreshview == null) {
			// return;
			// }
			// }
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// 设置当前屏幕显示的起始index和结束index
		start_index = firstVisibleItem;
		end_index = firstVisibleItem + visibleItemCount - 1;
		per_page_num = visibleItemCount;
		if (end_index > totalItemCount) {
			end_index = totalItemCount - 1;
		}
	}

	public int getPerPageNum() {
		return per_page_num;
	}

	public int get_start_index() {
		return start_index;
	}

	public int get_end_index() {
		return end_index;
	}
}
