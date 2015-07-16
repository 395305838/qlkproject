package com.xiaocoder.test.dialogs;

import android.app.Activity;
import android.os.Bundle;

import com.xiaocoder.android.fw.general.dialog.XCdialog;
import com.xiaocoder.test.R;

public class DialogsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialogs);

		// 系统等待对话框
		// XCdialog.getXCDialogInstance().getWaitingDialog(this,
		// XCdialog.SYSTEM_CIRCLE_DIALOG, null, false, null, null);
		// XCdialog.getXCDialogInstance().show(true);

		// 帧动画等待对话框
		XCdialog.getXCDialogInstance().getWaitingDialog(this,
				XCdialog.ANIMATION_DIALOG_V, "加载中..", false, null, null);
		XCdialog.getXCDialogInstance().showFrameAnimDialog(
				getResources().getDrawable(R.drawable.xc_dd_anim_framelist),
				true, true);
		// 可以设置背景
		// XCdialog.getXCDialogInstance().getDialogLayout().setBackgroundColor(0x00ff0000);
		// XCdialog.getXCDialogInstance().showFrameAnimDialog(getResources().getDrawable(R.drawable.xc_dd_anim_framelist),
		// true, false);

		// 动画等待对话框
		// XCdialog.getDialogHelperInstance().getWaitingDialog(this,
		// XCdialog.ANIMATION_DIALOG_V, "加载中..", false, null, null);
		// XCdialog.getDialogHelperInstance().showRotateAnimDialog(AnimationUtils.loadAnimation(this,
		// R.anim.xc_anim_rotate_0to180_700),
		// getResources().getDrawable(R.drawable.ic_launcher), false);

		// 询问对话框
		// XCdialog.getDialogHelperInstance().getDefineDialog(this,
		// XCdialog.QUERY_DIALOG, "温馨提示", null, new String[] { "2" });
		// XCdialog.getDialogHelperInstance().setDialogCallBack(new
		// DialogCallBack() {
		//
		// @Override
		// public void confirm() {
		// }
		//
		// @Override
		// public void cancle() {
		// }
		// });
		//
		// XCdialog.getDialogHelperInstance().getContent_textview().setText("文字区域!\r\nhahaha");
		// XCdialog.getDialogHelperInstance().show();

	}
}
