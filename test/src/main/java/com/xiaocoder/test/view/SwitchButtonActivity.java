package com.xiaocoder.test.view;

import android.os.Bundle;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.view.XCSwitchButton;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class SwitchButtonActivity extends MActivity implements XCSwitchButton.SwitchButtonListener {

    TextView txt;
    XCSwitchButton slide;
    XCSwitchButton slide2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_switchbutton);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {
        slide = (XCSwitchButton) findViewById(R.id.swit);
        slide2 = (XCSwitchButton) findViewById(R.id.swit2);

        slide.setState(false);
        txt = (TextView) findViewById(R.id.txt);
    }

    @Override
    public void listeners() {
        slide.setSlideListener(this);
    }

    @Override
    public void open() {
        txt.setText("first switch is opend, and set the second one is 'slideable'");
        slide2.setSlideable(true);
    }

    @Override
    public void close() {
        txt.setText("first switch is closed,and set the second one is 'unslideable'");
        slide2.setSlideable(false);
    }

    @Override
    public void onNetRefresh() {

    }
}

