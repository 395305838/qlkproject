package com.xiaocoder.android.fw.general.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.io.XCIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by xiaocoder on 2015/10/21.
 * version: 1.2.0
 * description:
 */
public class XCImageLoader implements XCIImageLoader {
    public static String TAG = "XCImageLoader";
    private Context context;
    private Handler handler;
    /**
     * 是否在内存中建立缓存引用
     */
    private boolean isCacheToMemory;
    /**
     * 本地缓存多少张图片
     */
    private int cacheToLocalNum;
    /**
     * 内存里维护多少张图片的缓存引用
     */
    private int cacheToMemoryNum;
    /**
     * 装本地缓存文件的集合
     */
    private ArrayList<File> directoryFiles;
    /**
     * 装内存中缓存引用的集合
     */
    private LinkedHashMap<String, Bitmap> bitmaps;
    /**
     * 本地文件缓存目录
     */
    private File cacheToLocalDirectory;
    /**
     * 线程池加载图片
     */
    public ExecutorService threadservice;
    /**
     * 锁,防止并发异常
     */
    private Object lock;
    /**
     * 设置超过多大的图片需要压缩,-->图片的大小限制,这个限制是加载到内存的bitmap的限制,如果是300kb,则只有bitmap达到700多kb时
     * ,才会压缩,压缩的参数是int型的
     */
    private double image_size_limit_by_byte;
    /**
     * 当前使用的默认图片
     */
    private int defaultImageId;

    /**
     * @param context
     * @param cacheToLocalDirectory 本地缓存目录file
     */
    public XCImageLoader(Context context, File cacheToLocalDirectory, int defaultImageId) {
        super();
        if (context == null) {
            return;
        }

        if (cacheToLocalDirectory != null && cacheToLocalDirectory.exists() && cacheToLocalNum > 0) {
            this.cacheToLocalDirectory = cacheToLocalDirectory;
            this.cacheToLocalNum = 500;
            this.directoryFiles = new ArrayList<File>();
        } else {
            throw new RuntimeException(this + "---未设置图片缓存目录");
        }

        this.context = context;
        this.defaultImageId = defaultImageId;

        this.handler = XCApp.getBase_handler();
        this.lock = new Object();
        this.threadservice = XCApp.getBase_cache_threadpool();

        this.cacheToMemoryNum = 30;
        this.isCacheToMemory = true;

        this.bitmaps = new LinkedHashMap<String, Bitmap>();

        this.image_size_limit_by_byte = 400 * 1024;

        if (this.directoryFiles != null) {
            countCache(cacheToLocalDirectory);
        }
    }

    public void countCache(File cacheDirectory) {
        File[] tempfiles = cacheDirectory.listFiles();
        if (tempfiles == null) {
            throw new RuntimeException("遍历该缓存目录失败 -- 有些目录访问需要权限");
        }
        int temp = tempfiles.length;
        for (int i = 0; i < temp; i++) {
            File tempfile = tempfiles[i];
            if (tempfile.isFile()) {// 排除是文件夹的可能
                directoryFiles.add(tempfile);
            }
        }
        // 如果缓存目录里的图片数量大于指定的缓存图片的数量,则清空缓存
        if (directoryFiles != null && directoryFiles.size() >= cacheToLocalNum) {
            for (File file : directoryFiles) {
                file.delete();// 删除文件
            }
            directoryFiles.clear(); // 清空集合
        }
    }

    @Override
    public void display(String url, ImageView imageview) {
        display(url, imageview, defaultImageId);
    }

    /**
     * @param url
     * @param imageview
     * @param obj       默认第一个参数是int
     */
    @Override
    public void display(final String url, ImageView imageview, Object... obj) {

        getParams(obj);

        /**
         * 先到内存中找
         */
        if (isCacheToMemory) {
            if (bitmaps.containsKey(url)) {
                XCApp.i(TAG, url + "---to get bitmap from memory");
                Bitmap bitmap = bitmaps.get(url);
                if (bitmap != null && !bitmap.isRecycled()) {
                    imageview.setImageBitmap(bitmap);
                    XCApp.i(TAG, url + "---get bitmap form memory success");
                    return;
                }
            }
            XCApp.i(TAG, url + "---get bitmap from memory faile ----bitmaps contains url is " + bitmaps.containsKey(url));
        }

        /**
         * 内存中没有，从本地缓存找
         */
        // File file = new File(cacheToLocalDirectory, EncryptUtil.MD5(url)+ url.substring(url.lastIndexOf(".")));
        File file = new File(cacheToLocalDirectory, url.substring(url.lastIndexOf("/") + 1));
        if (file.exists()) {
            XCApp.i(TAG, url + "---to get bitmap from local cache file");
            threadservice.execute(new LocalPictureRunnable(imageview, file, url));
            return;
        }
        XCApp.i(TAG, url + "---local cache file not exists");

        if (url.startsWith(HTTP_HEAD)) {
            /**
             * 如果本地缓存没有,且是网络文件，就去网络下载
             */
            XCApp.i(TAG, url + "---to get bitmap from net");
            // 这里要用trim(),因为如果服务端是用println()发过来的,那么最后的url为url+"/r/n"
            threadservice.execute(new NetPictureRunnable(imageview, url.trim()));
        } else {
            /**
             * 如果本地缓存没有，但是本地文件
             */
            File localFile = new File(url);
            if (localFile.exists()) {
                threadservice.execute(new LocalPictureRunnable(imageview, file, url));
                XCApp.i(TAG, url + "---to get bitmap from local file");
            } else {
                XCApp.e(this + "---" + url + "---传入的图片路径有误");
            }
        }
    }

    public void getParams(Object[] obj) {
        if (obj != null && obj[0] instanceof Integer) {
            defaultImageId = (int) obj[0];
        }
    }

    class LocalPictureRunnable implements Runnable {
        private ImageView imageview;
        private String url;
        private File file;
        private Bitmap bitmap;

        public LocalPictureRunnable(ImageView iamgeview, File file, String url) {
            super();
            this.imageview = iamgeview;
            this.url = url;
            this.file = file;
        }

        @Override
        public void run() {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] data = XCIO.toBytesByInputStream(in);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inDither = false;
                options.inPurgeable = true;
                options.inInputShareable = true;
                // 仅获取大小来获取压缩比率
                // -->注意这里的data.length不一定是图片的真实大小,可能是网络压缩过的,或者非bmp格式的
                try {
                    BitmapFactory.decodeByteArray(data, 0, data.length, options);
                    // Bitmap.Config.ALPHA_8
                    // Bitmap.Config.ARGB_4444都不相同,这里的*4写固定了,按理是应该判断下的
                    XCApp.i(TAG, url + "---local file size--- " + data.length + "---local file size of bitmap------ "
                            + options.outWidth * options.outHeight * 4);
                    int ratio = (int) Math.round(Math.sqrt(options.outWidth * options.outHeight * 4
                            / image_size_limit_by_byte));
                    if (ratio < 1) {
                        options.inSampleSize = 1;// 保持原有大小
                        XCApp.i(TAG, url + "---options.inSampleSize---" + 1);
                    } else {
                        options.inSampleSize = ratio;
                        XCApp.i(TAG, url + "---options.inSampleSize---" + ratio);
                    }
                    options.inJustDecodeBounds = false;
                    // 真实压缩图片
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                } catch (Exception e) {
                    XCApp.e(context, "--XCImageLoader--LocalPictureRunnable()", e);
                    e.printStackTrace();
                    System.gc();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            XCApp.i(TAG, url + "---oom , so set the default_bitmap");
                            imageview.setImageResource(defaultImageId);
                        }
                    });
                    return;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (bitmap == null) {
                    if (url.startsWith(HTTP_HEAD)) {
                        // 有一种情况是,之前从网络获取的图片写到本地的时候出错了,图片无法解析,所以为null
                        threadservice.execute(new NetPictureRunnable(imageview, url.trim()));
                    } else {
                        XCApp.e(url + "---bitmap is null");
                    }
                    return;
                }

                // 先检查内存中缓存的数量是否超过了规定,如果超过了规定,则删除最前面的1/5 -->这里用锁来判断,必须的
                XCApp.i(TAG, bitmaps.size() + "---bitmaps.size()");
                if (isCacheToMemory && bitmaps.size() >= cacheToMemoryNum) {
                    synchronized (lock) {
                        if (bitmaps.size() >= cacheToMemoryNum) {
                            XCApp.i(TAG, "bitmaps is full ,now delete 20% ");
                            int delete = cacheToMemoryNum / 5;
                            for (Iterator<Map.Entry<String, Bitmap>> it = bitmaps.entrySet().iterator(); it.hasNext(); ) {
                                if (delete != 0) {
                                    it.next();
                                    it.remove();
                                    delete--;
                                } else {
                                    // System.gc();
                                    break;
                                }
                            }
                        }
                    }
                }

                if (bitmap != null && isCacheToMemory && !bitmaps.containsKey(url)) {
                    synchronized (lock) {
                        bitmaps.put(url, bitmap);
                        XCApp.i(TAG, "add_to_memory--" + url + "--have added to bitmaps-------now the size fo bitmaps is "
                                + bitmaps.size());
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            imageview.setImageBitmap(bitmap);
                            XCApp.i(TAG, url + "---set bitmap from local file success");
                        } else {
                            XCApp.e(url + "bitmap is null , fail");
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String HTTP_HEAD = "http://";

    class NetPictureRunnable implements Runnable {
        private ImageView imageview;
        private String url;
        private Bitmap bitmap;

        public NetPictureRunnable(ImageView imageview, String url) {
            super();
            this.imageview = imageview;
            this.url = url;
        }

        @Override
        public void run() {
            try {
                // 注明:这里没有用httpclient或asynhttp框架,网络加载图片本来就耗资源且费时,而且这里也没有特殊的需求,所以用这种最轻便的网络访问方式,如有特殊的需求,再改
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod(XCConfig.GET);
                conn.setConnectTimeout(10000);
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                    InputStream in = conn.getInputStream();
                    byte[] data = XCIO.toBytesByInputStream(in);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inDither = false;
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    // 仅获取大小来获取压缩比率
                    // -->注意这里用data.length获取的数据不是图片的真实大小,可能是服务端那边也经过压缩了的
                    try {
                        BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        XCApp.i(TAG, url + "---net file size--- " + data.length + "---net file size of bitmap------ "
                                + options.outWidth * options.outHeight * 4);
                        // XCApp.i(TAG,options.inDensity+"------------options.inDensity");
                        // XCApp.i(TAG,options.inScreenDensity+"------------options.inScreenDensity");
                        // XCApp.i(TAG,options.outWidth+"------------options.outWidth");
                        // XCApp.i(TAG,options.outHeight+"------------options.outHeight");
                        // XCApp.i(TAG,options.inTargetDensity+"------------options.inTargetDensity");
                        // XCApp.i(TAG,options.inDensity+"------------options.inDensity");
                        // XCApp.i(TAG,options.inPreferredConfig+"------------options.inPreferredConfig");
                        int ratio = (int) Math.round(Math.sqrt(options.outWidth * options.outHeight * 4
                                / image_size_limit_by_byte));
                        if (ratio < 1) {
                            options.inSampleSize = 1;// 保持原有大小
                            XCApp.i(TAG, url + "---options.inSampleSize---" + 1);
                        } else {
                            options.inSampleSize = ratio;
                            XCApp.i(TAG, url + "---options.inSampleSize---" + ratio);
                        }
                        options.inJustDecodeBounds = false;
                        // 真实压缩图片
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                    } catch (Exception e) {
                        XCApp.e(context, "---XCImageLoader--NetPictureRunnable", e);
                        e.printStackTrace();
                        System.gc();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                XCApp.i(TAG, url + "---oom , so set the default_bitmap");
                                imageview.setImageResource(defaultImageId);
                            }
                        });
                        return;
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (bitmap == null) {
                        XCApp.e(url + "---bitmap is null");
                        return;
                    }

                    // 先检查内存中缓存的数量是否超过了规定,如果超过了规定,则删除最前面的1/5
                    if (isCacheToMemory && bitmaps.size() > cacheToMemoryNum) {
                        synchronized (lock) {
                            if (bitmaps.size() > cacheToMemoryNum) {
                                XCApp.i(TAG, "bitmaps is full ,now delete 20% ");
                                int delete = cacheToMemoryNum / 5;
                                for (Iterator<Map.Entry<String, Bitmap>> it = bitmaps.entrySet().iterator(); it.hasNext(); ) {
                                    if (delete != 0) {
                                        it.next();
                                        it.remove();
                                        delete--;
                                    } else {
                                        // System.gc();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    // 加入构建的内存缓存中
                    synchronized (lock) {
                        if (bitmap != null && isCacheToMemory && !bitmaps.containsKey(url)) {
                            bitmaps.put(url, bitmap);
                        }
                    }

                    // 设置图片
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null && !bitmap.isRecycled()) {
                                imageview.setImageBitmap(bitmap);
                                XCApp.i(TAG, url + "---set bitmap from net success");
                            } else {
                                XCApp.e(url + "bitmap is null , fail");
                            }
                        }
                    });

                    // 如果指定了缓存目录,且是从网络获取的,那么缓存到本地的设置
                    if (cacheToLocalDirectory != null) {
                        // String filename = EncryptUtil.MD5(url) +url.substring(url.lastIndexOf("."));
                        String filename = url.substring(url.lastIndexOf("/") + 1);
                        File file = new File(cacheToLocalDirectory, filename);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(CompressFormat.PNG, 100, fos); // 压缩的格式
                            // 100为原样,越小越不清楚,输出流
                            fos.close();
                        } else {
                            return;
                        }

                        if (directoryFiles != null) {
                            synchronized (lock) {
                                // 添加到缓存记录中
                                directoryFiles.add(file);
                            }
                            // 如果超出缓存数量规定,删除本地缓存文件
                            if (directoryFiles.size() > cacheToLocalNum) {
                                synchronized (lock) {
                                    if (directoryFiles.size() > cacheToLocalNum) {
                                        // 删除文件
                                        for (File deletefile : directoryFiles) {
                                            deletefile.delete();
                                        }
                                        // 删除引用
                                        directoryFiles.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getDefaultImageId() {
        return defaultImageId;
    }

}
