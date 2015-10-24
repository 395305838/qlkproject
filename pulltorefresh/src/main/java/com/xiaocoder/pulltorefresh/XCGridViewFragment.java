/**
 *
 */
package com.xiaocoder.pulltorefresh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.R;


@Deprecated
public class XCGridViewFragment extends XCBaseAbsListFragment<GridView> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return init(inflater, R.layout.xc_l_fragment_gridview_plus);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(R.id.xc_id_gridview_plus, R.id.xc_id_gridview_plus_zero_bg, whichMode);
	}

	@Override
	public void initWidgets() {
		
	}

	@Override
	public void listeners() {
		
	}
}