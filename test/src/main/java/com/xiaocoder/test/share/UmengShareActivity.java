package com.xiaocoder.test.share;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qlk.umeng.UtilUmengShare;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;

public class UmengShareActivity extends QlkActivity {
    Button umeng;
    Button define;
    UMSocialService mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_umeng_share);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {

        umeng = getViewById(R.id.umeng);
        define = getViewById(R.id.define);

        // 首先在您的Activity中添加如下成员变量
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");

        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();

        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();

        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                , SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL);

        UtilUmengShare.initWX(this, "wxe1733b127abc3406", "34ee5399fae0786bbe754f8b4cfadde6");
        UtilUmengShare.shareWX(mController, this, "right", "this title", "www.qq.com", R.drawable.ic_launcher);
        UtilUmengShare.shareWXCircle(mController, this, "right", "this title", "www.qq.com", "http://www.baidu.com/img/bdlogo.png");
    }

    @Override
    public void listeners() {

        umeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.openShare(UmengShareActivity.this, false);
            }
        });

        define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilUmengShare.shareDefine(mController, SHARE_MEDIA.WEIXIN, getApplicationContext());
            }
        });
    }

    @Override
    public void onNetRefresh() {

    }

}
