package com.xiaocoder.android.fw.general.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class SKShareDialog extends XCBaseDialog {

    public SKShareDialog(Context context) {
        super(context);
    }

    public SKShareDialog(Context context, int style) {
        super(context, style);
    }

    /**
     * 初始化分享dialog
     */
    public void initShareDialog(final onShareListener shareListener) {

        View view = getLayoutInflater().inflate(R.layout.sk_l_dialog_share, null);

        TextView sk_id_share_wx_friend_tv;
        TextView sk_id_share_wx_friends_tv;
        TextView sk_id_share_copy_link_tv;
        TextView sk_id_share_cancel_tv;

        sk_id_share_wx_friend_tv = (TextView) view.findViewById(R.id.sk_id_share_wx_friend_tv);
        sk_id_share_wx_friends_tv = (TextView) view.findViewById(R.id.sk_id_share_wx_friends_tv);
        sk_id_share_copy_link_tv = (TextView) view.findViewById(R.id.sk_id_share_copy_link_tv);
        sk_id_share_cancel_tv = (TextView) view.findViewById(R.id.sk_id_share_cancel_tv);

        sk_id_share_wx_friend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到微信好友
                shareListener.shareFriend();
                dismiss();
            }
        });
        sk_id_share_wx_friends_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到微信朋友圈
                shareListener.shareFriends();
                dismiss();

            }
        });
        sk_id_share_copy_link_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //复制链接
                shareListener.copyLink();
                dismiss();

            }
        });
        sk_id_share_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                dismiss();
            }
        });
        setContentView(view);
        setWindowLayoutStyle();
    }

    public void setWindowLayoutStyle() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = 100;
//        lp.height = 200;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp);
    }

    public interface onShareListener {

        void shareFriend();

        void shareFriends();

        void copyLink();
    }
}



