package com.xiaocoder.android.fw.general.dialog;

import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.util.UtilAnim;
import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/10/24.
 * version: 1.2.0
 * description:
 */
public class XCRotateDialog extends XCBaseDialog {

    ImageView imageview;
    Animation anim;
    TextView textview;

    public ImageView getImageview() {
        return imageview;
    }

    public TextView getTextview() {
        return textview;
    }

    public Animation getAnim() {
        return anim;
    }

    public XCRotateDialog(Context context, int theme, int imageViewId) {
        super(context, theme);
        initDialog(imageViewId);
    }

    public void initDialog(int imageViewId) {
        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_rotate_imageview, null);
        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();

        textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_rotate_textview);

        anim = UtilAnim.getRatoteAnimation();

        imageview = (ImageView) dialogLayout.findViewById(R.id.xc_id_dialog_rotate_imageview);
        imageview.setImageResource(imageViewId);

    }

    public void setWindowLayoutStyleAttr() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        lp.dimAmount = 0.2f;
        window.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        imageview.startAnimation(anim);
    }
}
