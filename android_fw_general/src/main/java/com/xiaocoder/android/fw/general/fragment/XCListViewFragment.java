/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xiaocoder.android.fw.general.base.function.XCBaseAbsListFragment;
import com.xiaocoder.android_fw_general.R;

public class XCListViewFragment extends XCBaseAbsListFragment<ListView> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return init(inflater, R.layout.xc_l_fragment_listview_plus);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(R.id.xc_id_listview_plus, R.id.xc_id_listview_plus_zero_bg, whichMode);
	}

	@Override
	public void initWidgets() {

	}

	@Override
	public void listeners() {

	}
}