package copy_new;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCBaseActivity;
import com.xiaocoder.android.test.R;

/*
 复制这里：activity在注册清单文件的配置 
 <activity
 android:name="com.xiaocoder.android.NameActivity"
 android:screenOrientation="portrait"
 android:windowSoftInputMode="adjustResize|stateHidden" >
 </activity>*/
public class NameActivity extends XCBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 设置布局
		setContentView(R.layout.item_l_activity_name);
		super.onCreate(savedInstanceState);
	}

	// 无网络时,点击屏幕后回调的方法
	@Override
	public void onNetRefresh() {
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
