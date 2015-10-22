package com.xiaocoder.test.timer;

import android.os.Bundle;
import android.os.Message;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.helper.XCTimeHelper;
import com.xiaocoder.middle.QlkActivity;
import com.xiaocoder.test.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends QlkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_timer);
        super.onCreate(savedInstanceState);
    }

    //
    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
    }

    @Override
    public void initWidgets() {
        timer();
        timer2();
    }

    @Override
    public void listeners() {

    }

    int count = 0;

    private void timer() {
        XCTimeHelper timeHelper = new XCTimeHelper(10000, new XCTimeHelper.CustomTimer() {

            @Override
            public void onTick(long millisUntilFinished) {
                XCApp.i(count++);
            }

            @Override
            public void onFinish() {
                XCApp.i(XCConfig.TAG_TEST, count + "--finish");
            }
        });
        timeHelper.start();
    }

    private void timer2() {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int index = 0;

            @Override
            public void run() {

                XCApp.getBase_handler().post(new Runnable() {
                    @Override
                    public void run() {
                        index = index + 1;
                        XCApp.shortToast(index + "");
                    }
                });

            }
        };
        timer.schedule(task, 1000, 5000);
    }

}
