package com.xiaocoder.views.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaocoder.views.R;


public class SKShareDialog extends XCBaseDialog {

    public SKShareDialog(Context context, int style) {
        super(context, style);
        initDialog();
    }

    public void initDialog() {

        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.sk_l_dialog_share, null);

        TextView sk_id_share_wx_friend_tv;
        TextView sk_id_share_wx_friends_tv;
        TextView sk_id_share_copy_link_tv;
        TextView sk_id_share_cancel_tv;

        sk_id_share_wx_friend_tv = (TextView) dialogLayout.findViewById(R.id.sk_id_share_wx_friend_tv);
        sk_id_share_wx_friends_tv = (TextView) dialogLayout.findViewById(R.id.sk_id_share_wx_friends_tv);
        sk_id_share_copy_link_tv = (TextView) dialogLayout.findViewById(R.id.sk_id_share_copy_link_tv);
        sk_id_share_cancel_tv = (TextView) dialogLayout.findViewById(R.id.sk_id_share_cancel_tv);

        sk_id_share_wx_friend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到微信好友
                if (shareListener != null) {
                    shareListener.shareFriend();
                }
                dismiss();
            }
        });
        sk_id_share_wx_friends_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到微信朋友圈
                if (shareListener != null) {
                    shareListener.shareFriends();
                }
                dismiss();

            }
        });
        sk_id_share_copy_link_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //复制链接
                if (shareListener != null) {
                    shareListener.copyLink();
                }
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
        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }

    public void setWindowLayoutStyleAttr() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.pop);
        window.setAttributes(lp);
    }

    public interface OnShareListener {

        void shareFriend();

        void shareFriends();

        void copyLink();
    }

    public OnShareListener shareListener;

    public void setOnShareListener(OnShareListener listener) {
        shareListener = listener;
    }

}



