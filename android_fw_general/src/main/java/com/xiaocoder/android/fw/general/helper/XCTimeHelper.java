package com.xiaocoder.android.fw.general.helper;

import android.os.CountDownTimer;

/**
 * 该类似handler机制的倒计时
 * <p/>
 * 放在主线程中，子线程中会报错
 */
public class XCTimeHelper extends CountDownTimer {

    public interface CustomTimer {
        void onTick(long millisUntilFinished);

        void onFinish();
    }

    public CustomTimer custom_timer;

    /**
     * @param millisInFuture    多少毫秒后结束计时
     * @param countDownInterval 每多少秒回调一次（如果不传，默认1秒）
     * @param custom_timer      回调监听
     */
    public XCTimeHelper(long millisInFuture, long countDownInterval, CustomTimer custom_timer) {
        super(millisInFuture, countDownInterval);
        this.custom_timer = custom_timer;
    }

    public XCTimeHelper(long millisInFuture, CustomTimer custom_timer) {
        super(millisInFuture, 1000);
        this.custom_timer = custom_timer;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (custom_timer != null) {
            custom_timer.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (custom_timer != null) {
            custom_timer.onFinish();
        }
    }
}
