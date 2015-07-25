package com.xiaocoder.android.fw.general.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class XCReflectPopupWindow extends XCBasePopupWindow implements View.OnClickListener {

    // 改pop所属的activity销毁时，才会消失

    public XCReflectPopupWindow(Context context, int width, int height) {
        super((ViewGroup) LayoutInflater.from(context).inflate(R.layout.xc_l_view_reflect_pop, null), width, height);
    }

    @Override
    public void initWidgets() {
        setAllTime(this);
        setOutsideTouchable(false);
    }

    @Override
    public void listener() {
    }

    @Override
    public void onClick(View v) {

    }


}
