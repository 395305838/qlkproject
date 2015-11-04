package com.xiaocoder.test.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.io.XCIO;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.MainActivity;
import com.xiaocoder.test.R;

public class StackActivity extends MActivity {

    TextView stack_desc;
    Button to_main_activity;
    Button to_search_activity1;
    Button to_search_activity2;
    Button finish_activity;
    Button finish_activity2;
    Button finish_current_activity;
    Button finish_all_activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_stack);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {
        stack_desc = getViewById(R.id.stack_desc);
        to_main_activity = getViewById(R.id.to_main_activity);
        to_search_activity1 = getViewById(R.id.to_search_activity1);
        to_search_activity2 = getViewById(R.id.to_search_activity2);
        finish_activity = getViewById(R.id.finish_activity);
        finish_activity2 = getViewById(R.id.finish_activity2);
        finish_current_activity = getViewById(R.id.finish_current_activity);
        finish_all_activity = getViewById(R.id.finish_all_activity);

        desc();

    }

    @Override
    protected void onStart() {
        super.onStart();
        showTitleLayout(false);
    }

    private void desc() {
        stack_desc.setText("");
        stack_desc.append(XCApp.getBase_ActivityHelper().getStack().size() + "-----栈的大小---" + XCIO.LINE_SEPARATOR);
        for (Activity item : XCApp.getBase_ActivityHelper().getStack()) {
            stack_desc.append(item.getClass() + "----" + XCIO.LINE_SEPARATOR);
        }
        stack_desc.append("MainActivity是否存在--" + XCApp.isActivityExist(MainActivity.class) + XCIO.LINE_SEPARATOR);
        stack_desc.append("SearchActivity是否存在--" + XCApp.isActivityExist(SearchActivity.class) + XCIO.LINE_SEPARATOR);
        stack_desc.append("SearchActivity2是否存在--" + XCApp.isActivityExist(SearchActivity2.class) + XCIO.LINE_SEPARATOR);
        stack_desc.append("StackActivity是否存在--" + XCApp.isActivityExist(StackActivity.class) + XCIO.LINE_SEPARATOR);
        stack_desc.append("当前页面--" + XCApp.getCurrentActivity() + XCIO.LINE_SEPARATOR);
        stack_desc.append("SearchActivity是否存在--" + XCApp.getActivity(SearchActivity.class) + XCIO.LINE_SEPARATOR);
        stack_desc.append("SearchActivity2是否存在--" + XCApp.getActivity(SearchActivity2.class) + XCIO.LINE_SEPARATOR);
    }

    @Override
    public void listeners() {
        to_main_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.toActivity(MainActivity.class);
            }
        });

        to_search_activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.toActivity(SearchActivity.class);
            }
        });

        to_search_activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.toActivity(SearchActivity2.class);
            }
        });

        finish_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.finishActivity(SearchActivity.class);
                desc();
            }
        });
        finish_activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.finishActivity(SearchActivity2.class);
                desc();
            }
        });

        finish_all_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.finishAllActivity();
            }
        });

        finish_current_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.finishCurrentActivity();
            }
        });

    }

}
