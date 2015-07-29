package com.xiaocoder.test.pop;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.pop.XCHintPopupWindow;
import com.xiaocoder.android.fw.general.pop.XCPhotoPopupWindow;
import com.xiaocoder.android.fw.general.pop.XCReflectPopupWindow;
import com.xiaocoder.android.fw.general.util.UtilImage;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkActivity;

public class PopActivity extends QlkActivity {
    Button test_pop_button;
    Button test_pop_button2;
    Button test_pop_button3;

    XCHintPopupWindow hintPopupWindow;
    XCPhotoPopupWindow photoPopupWindow;
    XCReflectPopupWindow reflectPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pop);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetRefresh() {

    }

    @Override
    public void initWidgets() {

        test_pop_button = getViewById(R.id.test_pop_button);
        test_pop_button2 = getViewById(R.id.test_pop_button2);
        test_pop_button3 = getViewById(R.id.test_pop_button3);

        reflectPopupWindow = new XCReflectPopupWindow(this, XCApplication.getScreenWidthPx(), UtilImage.dip2px(this, 100));

        hintPopupWindow = new XCHintPopupWindow
                (this, (int) (XCApplication.getScreenWidthPx() / 2.3), ViewGroup.LayoutParams.WRAP_CONTENT);

        photoPopupWindow = new XCPhotoPopupWindow
                (this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void listeners() {
        test_pop_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reflectPopupWindow.showPopupWindow((View) (test_pop_button.getParent()), 0, -UtilImage.dip2px(PopActivity.this, 105));
            }
        });

        test_pop_button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPopupWindow.showAtLocation((View) test_pop_button2.getParent(), Gravity.BOTTOM, 0, 0);
            }
        });

        test_pop_button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hintPopupWindow.showAtLocation(test_pop_button3, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, UtilImage.dip2px(PopActivity.this, 75));
            }
        });

        hintPopupWindow.setOnHintPopupItemClickListener(new XCHintPopupWindow.OnHintPopupItemClickListener() {
            @Override
            public void hint1(TextView textview) {
                dShortToast("1");
                hintPopupWindow.dismiss();
            }

            @Override
            public void hint2(TextView textview) {
                dShortToast("2");
                hintPopupWindow.dismiss();
            }

            @Override
            public void hint3(TextView textview) {
                dShortToast("3");
                hintPopupWindow.dismiss();
            }
        });

        photoPopupWindow.setOnPhotoPopupItemClickListener(new XCPhotoPopupWindow.onPhotoPopupItemClickListener() {
            @Override
            public void onPhotoUpload() {
                dShortToast("1");
                photoPopupWindow.dismiss();
            }

            @Override
            public void onLocalAlbum() {
                dShortToast("2");
                photoPopupWindow.dismiss();
            }

            @Override
            public void onCancel() {
                dShortToast("3");
                photoPopupWindow.dismiss();
            }

            @Override
            public void onNetPrescription() {
                dShortToast("4");
                photoPopupWindow.dismiss();
            }
        });
    }

    boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && isFirst) {
            isFirst = false;
        }
    }

}
