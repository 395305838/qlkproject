package com.xiaocoder.test.view;

import android.os.Bundle;
import android.widget.TextView;

import com.xiaocoder.views.view.open.OPSwitchButton;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class SwitchButtonActivity extends MActivity implements OPSwitchButton.SwitchButtonListener {

    TextView txt;
    OPSwitchButton slide;
    OPSwitchButton slide2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_switchbutton);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {
        slide = (OPSwitchButton) findViewById(R.id.swit);
        slide2 = (OPSwitchButton) findViewById(R.id.swit2);

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

}

