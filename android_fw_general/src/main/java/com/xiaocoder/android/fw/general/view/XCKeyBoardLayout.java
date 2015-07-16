/**
 *
 */
package com.xiaocoder.android.fw.general.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/*
  监听键盘的隐藏和显示
 */
public class XCKeyBoardLayout extends RelativeLayout {

    public XCKeyBoardLayout(Context context) {
        super(context);
    }

    public XCKeyBoardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public XCKeyBoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mListener != null) {
            mListener.OnResize(w, h, oldw, oldh);
        }
    }

    private OnResizeListener mListener;

    public interface OnResizeListener {
        void OnResize(int w, int h, int oldw, int oldh);
    }

}