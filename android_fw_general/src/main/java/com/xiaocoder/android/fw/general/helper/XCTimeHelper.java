package com.xiaocoder.android.fw.general.helper;

import android.os.CountDownTimer;

// 该类似handler机制的倒计时
public class XCTimeHelper extends CountDownTimer {

	public interface CustomTimer {
		void onTick(long millisUntilFinished);

		void onFinish();
	}

	public CustomTimer custom_timer;

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
