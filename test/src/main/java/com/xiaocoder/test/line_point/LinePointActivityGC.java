package com.xiaocoder.test.line_point;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.fragment.XCTitleCommonFragment;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

public class LinePointActivityGC extends QlkBaseActivity {
    int i = 10;
    ImageView test_imageview;
    XCTitleCommonFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_point);
        test_imageview = getViewById(R.id.test_imageview);
        addFragment(R.id.test, fragment = new XCTitleCommonFragment());
    }

    @Override
    public void initWidgets() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(test_imageview);
                System.out.println(i);

                System.gc();
                System.gc();

                System.out.println(fragment.toString());
                System.out.println(fragment.getActivity());

                XCApplication.getBase_handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("handler --" + test_imageview); // 不为空
                        System.out.println("handler --" + i);// 不为空

                        System.out.println("handler --" + fragment.toString()); // 不为空
                        System.out.println("handler --" + fragment.getActivity()); // 空
                    }
                }, 10000);

            }
        }).start();

    }

    @Override
    public void listeners() {

    }

    @Override
    public void onNetRefresh() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy()");
    }
}
