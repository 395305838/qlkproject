package com.xiaocoder.android.fw.general.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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
        ViewGroup view = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_system_circle_h, null);
        setContentView(view);
        setWindowLayoutStyle();
    }

    public void setWindowLayoutStyle() {
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }

}



