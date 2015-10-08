/**
 *
 */
package com.xiaocoder.android.fw.general.fragment.listview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xiaocoder.android_fw_general.R;

public class XCMaterialRefreshFragment extends XCBaseAbsListFragment<ListView> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return init(inflater, R.layout.xc_l_fragment_listview_plus);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void initWidgets() {
	}

	@Override
	public void listeners() {

	}
}