package com.xiaocoder.test.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.view.XCRecordVoiceButton;
import com.xiaocoder.android.fw.general.view.XCRecordVoiceButtonPlus;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.io.File;

public class RecoderButtonActivity extends MActivity {

    XCRecordVoiceButton button;
    XCRecordVoiceButtonPlus button2;

    Dialog dialog2;
    TextView hint2;
    TextView time_view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recoder_button);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {
        button = getViewById(R.id.test_recodervoice);
        button2 = getViewById(R.id.test_recodervoice_plus);
        createDialog(this);
    }

    public void createDialog(Context context) {
        dialog2 = new Dialog(context, com.xiaocoder.android_fw_general.R.style.xc_s_dialog);
        dialog2.setContentView(com.xiaocoder.android_fw_general.R.layout.xc_l_view_recoder_voicebutton_hint);
        dialog2.setCanceledOnTouchOutside(false);
        Window window = dialog2.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
        hint2 = (TextView) dialog2.findViewById(com.xiaocoder.android_fw_general.R.id.xc_id_voice_recoder_hint_textview);
        time_view2 = (TextView) dialog2.findViewById(com.xiaocoder.android_fw_general.R.id.xc_id_voice_recoder_time);
    }

    @Override
    public void listeners() {

        button.setOnRecordVoiceSuccessListener(new XCRecordVoiceButton.OnRecordVoiceSuccessListener() {
            @Override
            public void onRecordVoiceSuccessListener(File file, float duration) {
                XCApp.shortToast(file.toString() + "---" + duration);
            }
        });

        button2.setOnButtonStatus(new XCRecordVoiceButtonPlus.OnButtonStatus() {
            @Override
            public void onBeforeRecoder() {

            }

            @Override
            public void onUpdateTime(int time) {
                time_view2.setText(time + "---plus");
            }

            @Override
            public void onStartTouch() {
                hint2.setText("松开发送---plus");
                dialog2.show();
            }

            @Override
            public void onMoveOut() {
                hint2.setText("取消发送---plus");
            }

            @Override
            public void onMoveIn() {
                hint2.setText("松开发送---plus");
            }

            @Override
            public void onEndTouch(RecoderStop stop) {
                if (stop == RecoderStop.OUT_TIME_STOP) {
                    hint2.setText("录音时间超出范围了");
                    XCApp.shortToast("录音时间超出范围了");
                } else if (stop == RecoderStop.LESS_TIME_STOP) {
                    hint2.setText("录音时间少了");
                    XCApp.shortToast("录音时间少了");
                }
                dialog2.cancel();
            }

            @Override
            public void onRecoderSuccess(File file, double time) {
                XCApp.shortToast(time + "----" + file);
            }
        });


    }

    @Override
    protected void initSlideDestroyActivity() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        button.onActivityPaused();
        button2.onActivityPaused();
    }
}