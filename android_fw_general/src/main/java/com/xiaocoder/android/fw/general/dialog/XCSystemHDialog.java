package com.xiaocoder.android.fw.general.dialog;

import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCSystemHDialog extends XCBaseDialog {

    public XCSystemHDialog(Context context, int style) {
        super(context, style);
        initDialog();
    }

    /**
     * 初始化分享dialog
     */
    public void initDialog() {
        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_system_circle_h, null);
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


