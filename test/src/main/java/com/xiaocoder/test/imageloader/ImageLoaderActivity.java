package com.xiaocoder.test.imageloader;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class ImageLoaderActivity extends MActivity {

    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_jsimage_loader);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetRefresh() {

    }

    @Override
    public void initWidgets() {
        image1 = getViewById(R.id.image1);
        image2 = getViewById(R.id.image2);
        image3 = getViewById(R.id.image3);
        image4 = getViewById(R.id.image4);

        XCApp.displayImage("http://cdn3.nflximg.net/images/3093/2043093.jpg", image1);
        XCApp.displayImage("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg", image2);
        XCApp.displayImage("http://tvfiles.alphacoders.com/100/hdclearart-10.png", image3);
        XCApp.displayImage("http://www.baidu.com/img/bdlogo.png", image4);
    }

    @Override
    public void listeners() {

    }

}