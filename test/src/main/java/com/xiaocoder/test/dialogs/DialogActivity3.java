package com.xiaocoder.test.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaocoder.android.fw.general.dialog.SKShareDialog;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCSystemHDialog;
import com.xiaocoder.android.fw.general.dialog.XCSystemVDialog;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

public class DialogActivity3 extends QlkBaseActivity {

    Button share;
    SKShareDialog share_dialog;

    Button systemh;
    XCSystemHDialog systemh_dialog;

    Button systemv;
    XCSystemVDialog systemv_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dialog_activity3);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initWidgets() {
        share = getViewById(R.id.xc_id_dialog_share);
        systemh = getViewById(R.id.xc_id_dialog_systemh);
        systemv = getViewById(R.id.xc_id_dialog_systemv);
    }

    @Override
    public void listeners() {
        share.setOnClickListener(this);
        systemh.setOnClickListener(this);
        systemv.setOnClickListener(this);
    }

    @Override
    public void onNetRefresh() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.xc_id_dialog_share:
                showShareDialog();
                break;
            case R.id.xc_id_dialog_systemh:
                showSystemHDialog();
                break;
            case R.id.xc_id_dialog_systemv:
                showSystemVDialog();
                break;
            default:
                break;
        }
    }

    public void showShareDialog() {
        share_dialog = new SKShareDialog(this, XCBaseDialog.TRAN_STYLE);
        share_dialog.setOnShareListener(new SKShareDialog.OnShareListener() {
            @Override
            public void shareFriend() {
                share_dialog.dismiss();
            }

            @Override
            public void shareFriends() {
                share_dialog.dismiss();
            }

            @Override
            public void copyLink() {
                share_dialog.dismiss();

            }
        });
        share_dialog.show();
    }

    public void showSystemHDialog() {
        systemh_dialog = new XCSystemHDialog(this, XCBaseDialog.TRAN_STYLE);
        systemh_dialog.show();
    }

    public void showSystemVDialog() {
        systemv_dialog = new XCSystemVDialog(this, XCBaseDialog.TRAN_STYLE);
        systemv_dialog.show();
    }
}
