/**
 *
 */
package copy_new;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaocoder.android.fw.general.application.XCBaseFragment;
import com.xiaocoder.android_fw_general.R;

public class NameFragment extends XCBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 设置布局
		return init(inflater, R.layout.item_l_fragment_name);
	}

	// 初始化控件
	@Override
	public void initWidgets() {

	}

	// 设置监听
	@Override
	public void listeners() {

	}

}