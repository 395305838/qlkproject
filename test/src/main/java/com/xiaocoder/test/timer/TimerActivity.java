package com.xiaocoder.test.timer;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.helper.XCTimeHelper;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerActivity extends MActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_timer);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initWidgets() {
        timer();
        timer2();
        timer3();
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


    Timer timer;

    private void timer2() {

        timer = new Timer();
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
        timer.schedule(task, 1000, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        scheduled.shutdown();
    }

    ScheduledExecutorService scheduled;

    private void timer3() {
        scheduled = XCExecutorHelper.getExecutorHelperInstance().getScheduledFix(5);
        scheduled.schedule(new Runnable() {
            @Override
            public void run() {
                XCApp.i("5秒后执行一次");
            }
        }, 5, TimeUnit.SECONDS);

        scheduled.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                XCApp.i("2秒后开始执行，每隔6秒执行一次");
            }
        }, 2, 6, TimeUnit.SECONDS);
    }


}
