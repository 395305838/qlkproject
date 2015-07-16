package com.xiaocoder.test.pop;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.pop.XCPop;
import com.xiaocoder.android.fw.general.util.UtilImage;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

public class PopActivity extends QlkBaseActivity {
	Button test_pop_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pop);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onNetRefresh() {

	}

	@Override
	public void initWidgets() {
		test_pop_button = (Button) findViewById(R.id.test_pop_button);
		XCPop.getXCPopInstance().getPopupWindow(this, R.layout.view_pop, XCApplication.getScreenWidthPx(), UtilImage.dip2px(this, 100), R.style.xc_s_pop_from_down_to_up);
	}

	@Override
	public void listeners() {
		test_pop_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				XCPop.getXCPopInstance().showPopupWindow((View) (test_pop_button.getParent()), 0, -UtilImage.dip2px(PopActivity.this, 105));
			}
		});
	}

	boolean isFirst = true;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus && isFirst) {
			isFirst = false;
			dShortToast(hasFocus + "");
			XCPop.getXCPopInstance().showPopupWindow((View) (test_pop_button.getParent()), 0, -UtilImage.dip2px(PopActivity.this, 105));
		}
	}

}
