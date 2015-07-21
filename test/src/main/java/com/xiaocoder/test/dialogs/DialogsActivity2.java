package com.xiaocoder.test.dialogs;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiaocoder.android.fw.general.dialog.XCdialog;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

public class DialogsActivity2 extends QlkBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dialogs2);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onNetRefresh() {

    }

    @Override
    public void initWidgets() {
        Button dialog1 = getViewById(R.id.dialog1);
        Button dialog2 = getViewById(R.id.dialog2);
        Button dialog3 = getViewById(R.id.dialog3);
        Button dialog4 = getViewById(R.id.dialog4);

        dialog1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeDialog();
                showSystemWaitingDialogH("正在加载..");
            }
        });

        dialog2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeDialog();
                showSystemWaitingDialogV("加载中..");
            }
        });

        dialog3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeDialog();
                showQueryDialogOneButton("温馨提示", "123456789", new String[]{"ok"}, new XCdialog.DialogCallBack() {

                    @Override
                    public void confirm() {
                        closeDialog();
                    }

                    @Override
                    public void cancle() {

                    }

                });
            }
        });

        dialog4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeDialog();
                showQueryDialogOneButton("温馨提示", "123456789", new String[]{"ok1", "ok2"}, new XCdialog.DialogCallBack() {

                    @Override
                    public void confirm() {
                        closeDialog();
                    }

                    @Override
                    public void cancle() {
                        closeDialog();
                    }

                });
            }
        });

    }

    @Override
    public void listeners() {

    }
}
