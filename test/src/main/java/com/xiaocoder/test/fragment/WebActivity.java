package com.xiaocoder.test.fragment;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.fragment.XCTitleCommonFragment;
import com.xiaocoder.android.fw.general.fragment.XCWebViewFragment;
import com.xiaocoder.middle.QlkActivity;
import com.xiaocoder.test.R;

public class WebActivity extends QlkActivity {
	XCWebViewFragment web_view_fragment;
	XCTitleCommonFragment title_fragment_common;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_web);
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onNetRefresh() {
	}

	@Override
	public void initWidgets() {
		title_fragment_common = new XCTitleCommonFragment();
		title_fragment_common.setTitleCenter(true, "新增地址");
		addFragment(R.id.xc_id_model_titlebar, title_fragment_common);

		web_view_fragment = new XCWebViewFragment();
		web_view_fragment.setUrl("http://www.baidu.com");
		addFragment(R.id.xc_id_model_content, web_view_fragment);
	}

	@Override
	public void listeners() {

	}

}
