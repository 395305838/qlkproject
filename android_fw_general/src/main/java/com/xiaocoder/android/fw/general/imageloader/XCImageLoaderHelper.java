package com.xiaocoder.android.fw.general.imageloader;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseConfig;
import com.xiaocoder.android_fw_general.R;

import java.io.File;

/*
 url的规范
 String imageUri = "http://site.com/image.png"; // from Web  
 String imageUri = "file:///mnt/sdcard/image.png"; // from SD card  
 String imageUri = "content://media/external/audio/albumart/13"; // from content provider  
 String imageUri = "assets://image.png"; // from assets  
 String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)  
 */

// 1 可以获取到universal的imageloader （一个开源的第三方库 ， 强大 ， 用这个）
// 2 也可以 获取到自己写的asynimageloader（方便改代码 ）
public class XCImageLoaderHelper {

    private XCImageLoaderHelper() {
    }

    public static DisplayImageOptions options = new DisplayImageOptions.Builder()

            .showImageOnLoading(R.drawable.xc_d_ymz_img_default) // 设置图片在下载期间显示的图片

            .showImageForEmptyUri(R.drawable.xc_d_ymz_img_default)// 设置图片Uri为空或是错误的时候显示的图片

            .showImageOnFail(R.drawable.xc_d_ymz_img_default) // 设置图片加载/解码过程中错误时候显示的图片

            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中

            .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中

            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示

            .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//

            .displayer(new FadeInBitmapDisplayer(0))// 是否图片加载好后渐入的动画时间

            .build();// 构建完成

    public static DisplayImageOptions getDisplayImageOptions() {
        return options;
    }

    public static DisplayImageOptions getDisplayImageOptions(int drawable_id) {
        return new DisplayImageOptions.Builder()

                .showImageOnLoading(drawable_id) // 设置图片在下载期间显示的图片

                .showImageForEmptyUri(drawable_id)// 设置图片Uri为空或是错误的时候显示的图片

                .showImageOnFail(drawable_id) // 设置图片加载/解码过程中错误时候显示的图片

                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中

                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中

                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示

                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//

                .displayer(new FadeInBitmapDisplayer(0))// 是否图片加载好后渐入的动画时间

                .build();// 构建完成

    }

    public static ImageLoaderConfiguration getImageLoaderConfiguration(
            Context context, File cacheDir) {
        return new ImageLoaderConfiguration

                .Builder(context)

                .memoryCacheExtraOptions(480, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽

                .threadPoolSize(3)
                        // 线程池内加载的数量

                .threadPriority(Thread.NORM_PRIORITY - 2)

                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new WeakMemoryCache())
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                        // .memoryCacheSize(5 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)

                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密

                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .discCacheFileCount(500)
                        // 缓存的文件数量

                .discCache(new UnlimitedDiscCache(cacheDir))
                        // 自定义缓存路径

                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                        // (5
                        // s),
                        // readTimeout
                        // (30
                        // s)超时时间

                .writeDebugLogs() // Remove for release app

                .build();// 开始构建
    }

    // 获取universal框架的imageloader , 默认用的就是这一个
    public static ImageLoader getInitedImageLoader(
            ImageLoaderConfiguration config) {
        ImageLoader.getInstance().init(config);
        return ImageLoader.getInstance();
    }

    // 获取自己写的imageloader
    public static XCAsynImageLoader asynimageloader;

    public static XCAsynImageLoader getAsynImageLoader(Context context,
                                                       int default_image_id, File dir) {
        if (asynimageloader == null) {
            synchronized (XCImageLoaderHelper.class) {
                if (asynimageloader == null) {
                    asynimageloader = new XCAsynImageLoader(context,
                            dir, true,
                            500, 100, 600);
                }
            }
        }
        return asynimageloader;
    }
}
