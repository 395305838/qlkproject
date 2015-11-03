package com.xiaocoder.android.fw.general.dialog;

import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCSystemVDialog extends XCBaseDialog {

    TextView textview;

    public TextView getTextview() {
        return textview;
    }

    public XCSystemVDialog(Context context, int style) {
        super(context, style);
        initDialog();
    }

    public void initDialog() {
        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_system_circle_v, null);
        textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_sys_v_textview);
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



