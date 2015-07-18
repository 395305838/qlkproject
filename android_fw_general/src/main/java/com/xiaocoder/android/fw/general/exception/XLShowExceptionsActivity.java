package com.xiaocoder.android.fw.general.exception;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;


/**
 * Created by xilinch on 2015/5/20.
 */
public class XLShowExceptionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exception);
        TextView tv = (TextView) findViewById(R.id.tv);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String text = bundle.getString("text");
            tv.setText(text);
        }
    }
}
