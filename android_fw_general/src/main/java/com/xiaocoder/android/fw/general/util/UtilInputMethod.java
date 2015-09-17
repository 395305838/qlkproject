/**
 * 
 */
package com.xiaocoder.android.fw.general.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xiaocoder.android.fw.general.application.XCApp;

/**
 * @Description: 输入法的弹出 与 关闭
 * @author xiaocoder
 * @date 2015-3-2 下午4:48:56
 */
public class UtilInputMethod {

	// 代码弹出输入法
	public static void openInputMethod(EditText view, final Context context) {
		// 弹出输入法
		view.setFocusable(true);
		view.requestFocus();
		view.selectAll();
		// 必须是handler.否则无法弹出 why?
		XCApp.getBase_handler().postDelayed(new Runnable() {
			public void run() {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				// imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
			}
		}, 500);
	}

	public static void hiddenInputMethod(Context context) {
		if (((Activity) context).getCurrentFocus() != null) {
			if (((Activity) context).getCurrentFocus().getWindowToken() != null) {
				// 先隐藏键盘
				((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

}
