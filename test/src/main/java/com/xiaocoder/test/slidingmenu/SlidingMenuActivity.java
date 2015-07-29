package com.xiaocoder.test.slidingmenu;

import android.os.Bundle;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkActivity;

public class SlidingMenuActivity extends QlkActivity {
	private SlidingMenu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_slidingmenu);
		super.onCreate(savedInstanceState);

		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffset(300);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.view_l_slidingmenu);
		menu.setSecondaryMenu(R.layout.view_l_slidingmenu);
	}

	/*
	 * public void show(View v){ if(menu.isShown()){ menu.showContent(); } else{
	 * menu.showMenu(); } }
	 */

	public void show(View v) {
		menu.showMenu();

	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onNetRefresh() {

	}

	@Override
	public void initWidgets() {

	}

	@Override
	public void listeners() {

	}

}
