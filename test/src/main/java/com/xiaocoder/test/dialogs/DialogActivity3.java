package com.xiaocoder.test.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaocoder.android.fw.general.dialog.SKShareDialog;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

public class DialogActivity3 extends QlkBaseActivity {
    Button share;
    SKShareDialog share_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dialog_activity3);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initWidgets() {
        share = getViewById(R.id.xc_id_dialog_share);
    }

    @Override
    public void listeners() {
        share.setOnClickListener(this);

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
            default:
                break;
        }
    }

    public void showShareDialog() {
        share_dialog = new SKShareDialog(this, XCBaseDialog.TRAN_STYLE);
        share_dialog.initShareDialog(new SKShareDialog.onShareListener() {
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
}
