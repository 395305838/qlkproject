/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.xiaocoder.android.fw.general.base.XLBaseExpandAbsListFragment;
import com.xiaocoder.android_fw_general.R;

public class XLExpandListViewFragment extends XLBaseExpandAbsListFragment<ExpandableListView> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return init(inflater, R.layout.xl_l_fragment_expandlistview_plus);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(R.id.xc_id_expandlistview_plus, R.id.xc_id_listview_plus_zero_bg, whichMode);
	}

	@Override
	public void initWidgets() {

	}

	@Override
	public void listeners() {

	}
}