package com.qlk.umeng;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.Map;
import java.util.Set;

/**
 * Created by xiaocoder on 2015/9/24.
 * <p/>
 * 在程序入口加入 com.umeng.socialize.utils.Log.LOG = true**，可在LogCat中观察友盟日志
 * <p/>
 * mController.openShare(getActivity(), false);
 * <p/>
 * mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
 * mController.getConfig().setPlatformOrder(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
 * SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);
 */
public class UtilUmeng {

    /**
     * 微信平台注册了一个包名为  com.xiaocoder.test
     *
     * @param appID     "wxe1733b127abc3406"
     * @param appSecret "34ee5399fae0786bbe754f8b4cfadde6"
     */
    public static void initWX(Context context, String appID, String appSecret) {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, appID, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * 微信分享必须设置targetURL，需要为http链接格式
     */
    public static void shareWX(UMSocialService mController, Context context, String rightContent, String title
            , String jumpUrl, String picUrl
    ) {
        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字，右边
        weixinContent.setShareContent(rightContent);
        //设置title , 左上角，图片的上方
        weixinContent.setTitle(title);
        //设置分享内容跳转URL
        weixinContent.setTargetUrl(jumpUrl);
        //设置分享图片
        weixinContent.setShareImage(new UMImage(context, picUrl));
        mController.setShareMedia(weixinContent);
    }

    /**
     * 微信分享必须设置targetURL，需要为http链接格式
     */
    public static void shareWX(UMSocialService mController, Context context, String rightContent, String title
            , String jumpUrl, int picId
    ) {
        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字，右边
        weixinContent.setShareContent(rightContent);
        //设置title , 左上角，图片的上方
        weixinContent.setTitle(title);
        //设置分享内容跳转URL
        weixinContent.setTargetUrl(jumpUrl);
        //设置分享图片
        weixinContent.setShareImage(new UMImage(context, picId));
        mController.setShareMedia(weixinContent);
    }

    /**
     * 微信朋友圈只能显示title，并且过长会被微信截取部分内容
     */
    public static void shareWXCircle(UMSocialService mController, Context context, String rightContent, String title
            , String jumpUrl, int picId) {
        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(rightContent);
        //设置朋友圈title
        circleMedia.setTitle(title);
        circleMedia.setShareImage(new UMImage(context, picId));
        circleMedia.setTargetUrl(jumpUrl);
        mController.setShareMedia(circleMedia);
    }

    /**
     * 微信朋友圈只能显示title，并且过长会被微信截取部分内容
     */
    public static void shareWXCircle(UMSocialService mController, Context context, String rightContent, String title
            , String jumpUrl, String picUrl) {
        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(rightContent);
        //设置朋友圈title
        circleMedia.setTitle(title);
        circleMedia.setShareImage(new UMImage(context, picUrl));
        circleMedia.setTargetUrl(jumpUrl);
        mController.setShareMedia(circleMedia);
    }

    /**
     * 自定义分享列表
     */

    public static void shareDefine(UMSocialService mController, SHARE_MEDIA platform, final Context context) {
        mController.postShare(context, platform,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                        if (eCode == 200) {
                            Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                        } else {
                            String eMsg = "";
                            if (eCode == -101) {
                                eMsg = "没有授权";
                            }
                            Toast.makeText(context, "分享失败[" + eCode + "] " +
                                    eMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * @param mController
     * @param mContext    这里必传activity的实例
     *
     * 注：微信的第三方登录要交钱审核之后才可用
     */
    public static void loginWX(final UMSocialService mController, final Context mContext) {
        mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
                //获取相关授权信息
                mController.getPlatformInfo(mContext, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(mContext, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String key : keys) {
                                sb.append(key + "=" + info.get(key).toString() + "\r\n");
                            }
                            System.out.println(sb.toString());
                        } else {
                            Toast.makeText(mContext, "发生错误：" + status, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
