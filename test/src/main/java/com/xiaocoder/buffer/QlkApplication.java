package com.xiaocoder.buffer;

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
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.exception.XLCrashHandler;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.util.UtilSystem;
import com.xiaocoder.test.R;

/**
 * Created by xiaocoder on 2015/7/14.
 */
public class QlkApplication extends XCApplication {

    static {

        display_image_options = new DisplayImageOptions.Builder()

                .showImageOnLoading(R.drawable.ic_launcher) // 设置图片在下载期间显示的图片

                .showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片

                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片

                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中

                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中

                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示

                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//

                .displayer(new FadeInBitmapDisplayer(0))// 是否图片加载好后渐入的动画时间

                .build();// 构建完成

    }


    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化一些路径
        initPath();

        // 初始化imageloader
        initImageloader();

        // 打印一些简单的设备信息
        simpleDeviceInfo();

    }

    private void simpleDeviceInfo() {
        printi(UtilSystem.getDeviceId(getApplicationContext()) + "--deviceId , "
                + UtilSystem.getVersionCode(getApplicationContext())
                + "--versionCode , " + UtilSystem.getVersionName(getApplicationContext()) + "--versionName , "
                + UtilScreen.getScreenHeightPx(getApplicationContext()) + "--screenHeightPx , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthPx , "
                + UtilScreen.getDensity(getApplicationContext()) + "--density , "
                + UtilScreen.getScreenHeightDP(getApplicationContext()) + "--screenHeightDP , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthDP");
    }

    private void initImageloader() {

        ImageLoader.getInstance().init(
                new ImageLoaderConfiguration

                        .Builder(getApplicationContext())

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

                        .discCache(new UnlimitedDiscCache(base_io.createDirInAndroid(QlkConfig.CACHE_DIRECTORY)))
                                // 自定义缓存路径

                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

                        .imageDownloader(
                                new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout

                        .writeDebugLogs() // Remove for release app

                        .build()// 开始构建
        );

        base_imageloader = ImageLoader.getInstance();

    }

    /**
     * 各个初始化的顺序不要去改变
     */
    private void initPath() {

        // log , 可以打印日志 与 toast
        base_log = new XCLog(getApplicationContext(),
                QlkConfig.IS_DTOAST, QlkConfig.IS_OUTPUT, QlkConfig.IS_PRINTLOG,
                QlkConfig.APP_ROOT, QlkConfig.LOG_FILE, QlkConfig.TEMP_PRINT_FILE, XCConfig.ENCODING_UTF8);

        // sp保存路径
        base_sp = new XCSP(getApplicationContext(), QlkConfig.SP_SETTING, Context.MODE_APPEND);// Context.MODE_MULTI_PROCESS

        // 图片视频等保存的路径
        base_io.createDirInAndroid(QlkConfig.CHAT_MOIVE_FILE);
        base_io.createDirInAndroid(QlkConfig.CHAT_VIDEO_FILE);
        base_io.createDirInAndroid(QlkConfig.CHAT_PHOTO_FILE);
        base_io.createDirInAndroid(QlkConfig.CRASH_FILE);

        // 是否开启异常日志捕获，以及异常日志的存储路径等
        XLCrashHandler.getInstance().init(QlkConfig.IS_INIT_CRASH_HANDLER,
                getApplicationContext(), QlkConfig.CRASH_FILE, QlkConfig.IS_SHOW_EXCEPTION_ACTIVITY);
    }
}
