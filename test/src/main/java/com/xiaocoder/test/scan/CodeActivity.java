package com.xiaocoder.test.scan;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.util.UtilGenerateCode;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;

public class CodeActivity extends QlkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_code);
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initWidgets() {
        ImageView imageview = (ImageView) findViewById(R.id.xc_id_code);

        imageview.setImageBitmap(UtilGenerateCode.generateCode("http://www.baidu.com/1234567890/1234567890/123456789/123456789/1234567/"));

    }

    @Override
    public void listeners() {

    }

    @Override
    public void onNetRefresh() {

    }
}
