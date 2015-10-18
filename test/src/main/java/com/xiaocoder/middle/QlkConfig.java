package com.xiaocoder.middle;

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
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.test.R;

/**
 * 该类配置开发环境  ， 调试开关  ，域名 ， 路径  ，url等
 */
public class QlkConfig {


    // ----------------------------------以下补充接口url---------------------------------------


    // ----------------------------------以上补充接口url---------------------------------------

    public enum RunEnvironment {
        DEV, TEST, ONLINE
    }

    /**
     * OPEN: 默认的配置，开发环境可以用这个值
     * CLOSE：默认的配置，上线版本用这个值
     * DEFINE: 可以修改配置，开发与测试环境可以用这个值
     */
    public enum DebugControl {
        CLOSE, OPEN, DEFINE
    }

    /**
     * 当前的运行环境，即域名的控制, 上线前，改为ONLINE
     */
    public static RunEnvironment CURRENT_RUN_ENVIRONMENT = RunEnvironment.ONLINE;

    /**
     * 是否打开调试日志开关 , 上线前，改为CLOSE
     */
    public static DebugControl DEBUG_CONTROL = DebugControl.DEFINE;

    static {
        if (DEBUG_CONTROL == DebugControl.DEFINE) {

            // i()方法是否打印到控制台
            IS_OUTPUT = true;

            // i()方法是否打印到本地log日志; e()方法都会打印到log日志，不受该值控制
            IS_PRINTLOG = true;

            // 调试土司是否开启
            IS_DTOAST = false;

            // 是否初始化crashhandler
            IS_INIT_CRASH_HANDLER = true;

            // 是否打印出异常界面（只有在IS_INIT_CRASH_HANDLER 为true时，该设置才有效）
            IS_SHOW_EXCEPTION_ACTIVITY = true;

        } else if (DEBUG_CONTROL == DebugControl.OPEN) {

            // i()方法是否打印到控制台
            IS_OUTPUT = true;

            // i()方法是否打印到本地log日志; e()方法都会打印到log日志，不受该值控制
            IS_PRINTLOG = true;

            // 调试土司是否开启
            IS_DTOAST = true;

            // 是否初始化crashhandler
            IS_INIT_CRASH_HANDLER = true;

            // 是否打印出异常界面（只有在IS_INIT_CRASH_HANDLER 为true时，该设置才有效）
            IS_SHOW_EXCEPTION_ACTIVITY = true;

        } else if (DEBUG_CONTROL == DebugControl.CLOSE) {

            // i()方法是否打印到控制台
            IS_OUTPUT = false;

            // i()方法是否打印到本地log日志; e()方法都会打印到log日志，不受该值控制
            IS_PRINTLOG = false;

            // 调试土司是否开启
            IS_DTOAST = false;

            // 是否初始化crashhandler
            IS_INIT_CRASH_HANDLER = true;

            // 是否打印出异常界面（只有在IS_INIT_CRASH_HANDLER 为true时，该设置才有效）
            IS_SHOW_EXCEPTION_ACTIVITY = false;

        } else {
            throw new RuntimeException("QlkConfig的static代码块中没有找到与DEBUG_CONTROL匹配的枚举值");
        }
    }

    /**
     * 域名配置
     */
    public static String ONLINE_HOST = "online.123.cn";
    public static String ONLINE_PORT = "12345";
    public static String ONLINE_ADDR = ONLINE_HOST + ":" + ONLINE_PORT;

    public static String TEST_HOST = "test.123.cn";
    public static String TEST_PORT = "12345";
    public static String TEST_ADDR = TEST_HOST + ":" + TEST_PORT;

    public static String DEV_HOST = "dev.123.cn";
    public static String DEV_PORT = "12345";
    public static String DEV_ADDR = DEV_HOST + ":" + DEV_PORT;

    public static String getUrl(String key) {

        if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.ONLINE) {

            return ONLINE_ADDR + key;

        } else if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.TEST) {

            return TEST_ADDR + key;

        } else if (CURRENT_RUN_ENVIRONMENT == RunEnvironment.DEV) {

            return DEV_ADDR + key;

        } else {

            throw new RuntimeException("QlkConfig中没有找到匹配的url");
        }
    }

    /**
     * app的根目录
     */
    public static String APP_ROOT = "app_test";
    /**
     * crash日志目录
     */
    public static String CRASH_DIR = APP_ROOT + "/crash";
    /**
     * 图片加载缓存目录的目录
     */
    public static String CACHE_DIR = APP_ROOT + "/cache";
    /**
     * chat目录
     */
    public static String CHAT_DIR = APP_ROOT + "/chat";

    public static String CHAT_PHOTO_DIR = CHAT_DIR + "/photo";

    public static String CHAT_VIDEO_DIR = CHAT_DIR + "/voice";

    public static String CHAT_MOIVE_DIR = CHAT_DIR + "/moive";
    /**
     * 打印到approot目录下的日志文件
     */
    public static String LOG_FILE = APP_ROOT + "_log";
    /**
     * 打印测试的文件，有时候控制台可能打印json不全，比如json太长的时候， 可以调用tempPrint方法，打印到本地查看
     */
    public static String TEMP_PRINT_FILE = APP_ROOT + "_temp_print";
    /**
     * sp文件
     */
    public static String SP_FILE = APP_ROOT + "_sp";

    /**
     * 是否打印日志到控制台
     */
    public static boolean IS_OUTPUT;
    /**
     * 是否弹出调试的土司
     */
    public static boolean IS_DTOAST;
    /**
     * 是否初始化crashHandler,上线前得关
     */
    public static boolean IS_INIT_CRASH_HANDLER;
    /**
     * 是否打印异常的日志到屏幕， 上线前得关
     */
    public static boolean IS_SHOW_EXCEPTION_ACTIVITY;
    /**
     * 是否打印日志到手机文件中,i()中的上线前全部关闭
     */
    public static boolean IS_PRINTLOG;
    /**
     * 固定线程池的数量
     */
    public static int THREAD_NUM = 60;
    /**
     * 默认图片加载option的配置
     */
    public static DisplayImageOptions display_image_options = new DisplayImageOptions.Builder()

            .showImageOnLoading(R.drawable.ic_launcher) // 设置图片在下载期间显示的图片

            .showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片

            .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片

            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中

            .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中

            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示

            .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//

                    // .displayer(new FadeInBitmapDisplayer(0))// 是否图片加载好后渐入的动画时间
            .displayer(new SimpleBitmapDisplayer())
                    // .displayer(new RoundedBitmapDisplayer(50)) // 圆形图片 ， 这个不要与XCRoundImageView同时使用，选一个即可

            .build();// 构建完成

    /**
     * imageloader 的配置
     */
    public static ImageLoader getImageloader(Context context) {
        ImageLoader.getInstance().init(
                new ImageLoaderConfiguration

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

                        .discCache(new UnlimitedDiscCache(XCIOAndroid.createDirInAndroid(context, QlkConfig.CACHE_DIR)))
                                // 自定义缓存路径

                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

                        .imageDownloader(
                                new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout

                        .writeDebugLogs() // Remove for release app

                        .build());// 开始构建
        return ImageLoader.getInstance();
    }

}