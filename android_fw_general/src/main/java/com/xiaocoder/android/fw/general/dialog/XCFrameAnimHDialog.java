package com.xiaocoder.android.fw.general.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCFrameAnimHDialog extends XCBaseDialog {

    public AnimationDrawable animDrawable;

    public XCFrameAnimHDialog(Context context, int style) {
        super(context, style);
        initDialog();
    }

    public void initDialog() {

        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_animation_h, null);

        ImageView anim_imageview = (ImageView) dialogLayout.getChildAt(0);

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.xc_dd_anim_framelist);

        anim_imageview.setImageDrawable(animDrawable = (AnimationDrawable) drawable);

        // 可以在onstart()方法中判断 isRunning 与stop

        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }

    public void setWindowLayoutStyleAttr() {
        dialogLayout.setBackgroundColor(mContext.getResources().getColor(R.color.c_trans));
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        if (animDrawable != null) {
            animDrawable.start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (animDrawable != null) {
            animDrawable.stop();
        }
    }
}



