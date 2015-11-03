package com.xiaocoder.android.fw.general.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.util.UtilAnim;
import com.xiaocoder.android_fw_general.R;

import java.util.List;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCFrameAnimHDialog extends XCBaseDialog {

    public AnimationDrawable animDrawable;

    int anim_framelist_id;

    List<Integer> imageIdList;
    int timeGap;

    ImageView imageView;
    TextView textView;

    public XCFrameAnimHDialog(Context context, int style, int anim_framelist_id) {
        super(context, style);
        this.anim_framelist_id = anim_framelist_id;
        initDialog();
    }

    public XCFrameAnimHDialog(Context context, int style, List<Integer> imageIdList, int timeGap) {
        super(context, style);
        this.imageIdList = imageIdList;
        this.timeGap = timeGap;
        initDialog();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void initDialog() {

        Drawable drawable = getDrawable();

        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_animation_h, null);

        textView = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_anim_h_textview);

        imageView = (ImageView) dialogLayout.findViewById(R.id.xc_id_dialog_anim_h_imageview);

        imageView.setImageDrawable(animDrawable = (AnimationDrawable) drawable);

        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }

    // 可以在onstart()方法中判断 isRunning 与stop
    public Drawable getDrawable() {
        if (imageIdList == null) {
            return mContext.getResources().getDrawable(anim_framelist_id);
        } else {
            return UtilAnim.getAnimationDrawable(XCApp.getBase_applicationContext(), imageIdList, timeGap);
        }
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



