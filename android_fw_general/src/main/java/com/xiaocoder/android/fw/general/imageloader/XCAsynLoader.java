package com.xiaocoder.android.fw.general.imageloader;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xiaocoder on 2015/10/21.
 * version: 1.2.0
 * description: 第三方 universal-image-loader-1.9.0-with-sources.jar
 */
public class XCAsynLoader implements XCIImageLoader {

    /**
     * universal-image-loader-1.9.0-with-sources.jar 的图片加载
     */
    ImageLoader mImageloader;

    /**
     * 默认的加载options
     */
    DisplayImageOptions mDefaultOptions;

    public XCAsynLoader(ImageLoader imageLoader, DisplayImageOptions defaultOptions) {
        this.mImageloader = imageLoader;
        this.mDefaultOptions = defaultOptions;
    }

    @Override
    public void display(String url, ImageView imageview, Object... obj) {
        // TODO 指定配置,判断参数
        if (obj[0] instanceof DisplayImageOptions) {
            mImageloader.displayImage(url, imageview, (DisplayImageOptions) obj[0]);
        }
    }

    @Override
    public void display(String url, ImageView imageview) {
        // 默认配置
        mImageloader.displayImage(url, imageview, mDefaultOptions);
    }
}
