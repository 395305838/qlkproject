package com.xiaocoder.buffer;

import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public abstract class QlkActivity extends XCBaseActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
