package com.xiaocoder.test.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.dialog.SKShareDialog;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCFrameAnimHDialog;
import com.xiaocoder.android.fw.general.dialog.XCFrameAnimVDialog;
import com.xiaocoder.android.fw.general.dialog.XCQueryDialog;
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

    Button animframe_h;
    XCFrameAnimHDialog animframe_dialog_h;

    Button animframe_v;
    XCFrameAnimVDialog animframe_dialog_v;

    Button query;
    XCQueryDialog query_dialog;

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
        animframe_h = getViewById(R.id.xc_id_dialog_animframe_h);
        animframe_v = getViewById(R.id.xc_id_dialog_animframe_v);
        query = getViewById(R.id.xc_id_dialog_query);
    }

    @Override
    public void listeners() {
        share.setOnClickListener(this);
        systemh.setOnClickListener(this);
        systemv.setOnClickListener(this);
        animframe_h.setOnClickListener(this);
        animframe_v.setOnClickListener(this);
        query.setOnClickListener(this);
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
            case R.id.xc_id_dialog_animframe_h:
                showAnimFrameHDialog();
                break;
            case R.id.xc_id_dialog_animframe_v:
                showAnimFrameVDialog();
                break;
            case R.id.xc_id_dialog_query:
                showQueryDialog();
            default:
                break;
        }
    }

    private void showQueryDialog() {
        query_dialog = new XCQueryDialog(this, XCBaseDialog.TRAN_STYLE, "温馨提示", "123456\r\n123456789", new String[]{"取消", "确定"}, false);
        query_dialog.setOnDecideListener(new XCQueryDialog.OnDecideListener() {
            @Override
            public void confirm() {
                XCApplication.dShortToast("confirm");
                query_dialog.dismiss();
            }

            @Override
            public void cancle() {
                XCApplication.dShortToast("cancle");
                query_dialog.dismiss();
            }
        });
        query_dialog.show();
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

    private void showAnimFrameHDialog() {
        animframe_dialog_h = new XCFrameAnimHDialog(this, XCBaseDialog.TRAN_STYLE);
        animframe_dialog_h.show();
    }

    private void showAnimFrameVDialog() {
        animframe_dialog_v = new XCFrameAnimVDialog(this, XCBaseDialog.TRAN_STYLE);
        animframe_dialog_v.show();
    }
}
