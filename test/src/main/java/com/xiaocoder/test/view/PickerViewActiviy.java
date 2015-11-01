package com.xiaocoder.test.view;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.view.SXPickerView;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;

public class PickerViewActiviy extends MActivity {

    SXPickerView viewLeft;
    SXPickerView viewRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_picker_view_activiy);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initWidgets() {
        viewLeft = getViewById(R.id.xc_id_picker_left);
        viewRight = getViewById(R.id.xc_id_picker_right);

        ArrayList<String> left = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            left.add(i + "");
        }

        ArrayList<String> right = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            right.add("0" + i);
        }

        viewLeft.setData(left);
        viewRight.setData(right);

        // 默认选中的索引位置
        viewLeft.setSelected(1);

    }

    @Override
    public void listeners() {
        viewLeft.setOnSelectListener(new SXPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                XCApp.shortToast(text);
            }
        });

        viewRight.setOnSelectListener(new SXPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                XCApp.shortToast(text);
            }
        });
    }

}