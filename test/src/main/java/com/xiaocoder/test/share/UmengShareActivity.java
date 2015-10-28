package com.xiaocoder.test.share;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaocoder.umeng.UtilUmeng;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class UmengShareActivity extends MActivity {
    Button umeng;
    Button define;
    Button login;
    UMSocialService umSocial;
    UMSocialService umLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_umeng_share);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {

        umeng = getViewById(R.id.umeng);
        define = getViewById(R.id.define);
        login = getViewById(R.id.login);

        // 首先在您的Activity中添加如下成员变量
        umSocial = UMServiceFactory.getUMSocialService("com.umeng.share");
        umLogin = UMServiceFactory.getUMSocialService("com.umeng.login");

        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();

        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();

        umSocial.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                , SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL);

        UtilUmeng.initWX(this, "wxe1733b127abc3406", "34ee5399fae0786bbe754f8b4cfadde6");
        UtilUmeng.shareWX(umSocial, this, "right", "this title", "www.qq.com", R.drawable.ic_launcher);
        UtilUmeng.shareWXCircle(umSocial, this, "right", "this title", "www.qq.com", "http://www.baidu.com/img/bdlogo.png");


    }

    @Override
    public void listeners() {

        umeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                umSocial.openShare(UmengShareActivity.this, false);
            }
        });

        define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilUmeng.shareDefine(umSocial, SHARE_MEDIA.WEIXIN, getApplicationContext());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XCApp.shortToast("wxlogin");
                UtilUmeng.loginWX(umLogin, UmengShareActivity.this);
            }
        });
    }

    @Override
    public void onNetRefresh() {

    }

}
