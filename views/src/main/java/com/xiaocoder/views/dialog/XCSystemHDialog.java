package com.xiaocoder.views.dialog;

import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaocoder.views.R;


/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCSystemHDialog extends XCBaseDialog {
    TextView textview;

    public TextView getTextview() {
        return textview;
    }

    public XCSystemHDialog(Context context, int style) {
        super(context, style);
        initDialog();
    }

    public void initDialog() {
        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_system_circle_h, null);
        textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_sys_h_textview);
        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }

    public void setWindowLayoutStyleAttr() {
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }

}



