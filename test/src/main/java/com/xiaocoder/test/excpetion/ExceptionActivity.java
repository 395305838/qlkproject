package com.xiaocoder.test.excpetion;

import android.os.Bundle;

import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class ExceptionActivity extends MActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exception);
        super.onCreate(savedInstanceState);

        int a = 1 / 0;

    }

    @Override
    public void initWidgets() {
    }

    @Override
    public void listeners() {

    }

}