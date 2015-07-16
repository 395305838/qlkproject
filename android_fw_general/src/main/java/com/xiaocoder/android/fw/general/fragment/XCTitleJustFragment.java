/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.base.XCTitleFragment;
import com.xiaocoder.android_fw_general.R;

/**
 * @author xiaocoder
 * @Description:仅仅一个标题
 * @date 2015-1-16 下午1:50:29
 */
public class XCTitleJustFragment extends XCTitleFragment {
	TextView xc_id_fragment_titlebar_justtitle_textview;
	Bundle init_bundle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return init(inflater, R.layout.xc_l_fragment_bar_title_just_title);
	}

	public void setTitle(String title) {
		if (xc_id_fragment_titlebar_justtitle_textview == null) {
			if (init_bundle == null) {
				init_bundle = new Bundle();
			}
			init_bundle.putString("title", title);
		} else {
			xc_id_fragment_titlebar_justtitle_textview.setText(title);
		}
	}

	public void onClick(View view) {
	}

	@Override
	public void initWidgets() {
		xc_id_fragment_titlebar_justtitle_textview = getViewById(R.id.xc_id_fragment_titlebar_justtitle_textview);

		if (init_bundle != null) {
			xc_id_fragment_titlebar_justtitle_textview.setText(init_bundle.getString("title"));
		}		
	}

	@Override
	public void listeners() {

	}

}
