package com.xiaocoder.android.fw.general.dialog;

import android.app.Dialog;
import android.content.Context;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/21.
 */
public class XCBaseDialog extends Dialog {

    public static int TRAN_STYLE = R.style.xc_s_dialog;

    public XCBaseDialog(Context context) {
        super(context);
    }

    public XCBaseDialog(Context context, int theme) {
        super(context, theme);
    }
}
