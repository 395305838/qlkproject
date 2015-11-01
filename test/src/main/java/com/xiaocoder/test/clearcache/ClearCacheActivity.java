package com.xiaocoder.test.clearcache;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCRotateDialog;
import com.xiaocoder.android.fw.general.helper.XCCleanCacheHelper;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.middle.MConfig;
import com.xiaocoder.test.R;

import java.io.File;

public class ClearCacheActivity extends MActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_clear_cache);
        super.onCreate(savedInstanceState);
    }

    Button clear;

    XCCleanCacheHelper helper;
    File dir;

    @Override
    public void initWidgets() {
        clear = getViewById(R.id.clear);
        // 如果没有该dir会创建再返回，有则返回该dir
        dir = XCIOAndroid.createDirInAndroid(getApplicationContext(), MConfig.APP_ROOT);
        helper = new XCCleanCacheHelper(new XCRotateDialog(this, XCBaseDialog.TRAN_STYLE, R.drawable.xc_d_dialog_loading_round));
    }

    @Override
    public void listeners() {
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        helper.removeFileAsyn(dir);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.quit();
    }
}