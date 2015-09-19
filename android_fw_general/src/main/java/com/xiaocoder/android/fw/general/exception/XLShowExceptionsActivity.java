package com.xiaocoder.android.fw.general.exception;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;


/**
 * Created by xilinch on 2015/5/20.
 * <p/>
 * 记得在清单文件中注册啊
 */
public class XLShowExceptionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xl_l_activity_show_exception);
        TextView tv = (TextView) findViewById(R.id.tv);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String text = bundle.getString(XLCrashHandler.EXCEPTION_INFO);
            tv.setText(text);
        }
    }
}
