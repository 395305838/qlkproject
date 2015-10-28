package com.xiaocoder.test.view;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.view.XCRecordVoiceButton;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.io.File;

public class RecoderButtonActivity extends MActivity {

    XCRecordVoiceButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recoder_button);
        super.onCreate(savedInstanceState);
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
    }

    @Override
    public void initWidgets() {
        button = getViewById(R.id.test_recodervoice);

    }

    @Override
    public void listeners() {
        button.setOnRecordVoiceSuccessListener(new XCRecordVoiceButton.OnRecordVoiceSuccessListener() {
            @Override
            public void onRecordVoiceSuccessListener(File file, float duration) {
                XCApp.shortToast(file.toString() + "---" + duration);
            }
        });
    }

    @Override
    protected void initSlideDestroyActivity() {
    }
}