package com.xiaocoder.android.fw.general.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

public class UtilVibrator {

	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	public static void Vibrate(final Activity activity, long[] pattern,boolean isRepeat) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}

}
/*需要在AndroidManifest.xml 中添加震动权限：
<uses-permission android:name="android.permission.VIBRATE" />

long milliseconds ：震动的时长，单位是毫秒

long[] pattern  ：自定义震动模式 。数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒

boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次*/