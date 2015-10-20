package reference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApp;
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
 * 1构建该类的对象后,不要放在子线程中,因为该类内部维护了一个主线程的handler
 * 2 adapter的getview中,在用imageloader加载之前,先设置一张默认的图片如holder.imageview.setImageResource
 * (R.drawable.ic_launcher),因为考虑到没缓存的情况下的效果
 * 3 使用前 :默认不缓存，设置是否缓存 ，设置内存中缓存图片的数量
 * 注: 使用完记得关闭线程池
 */
public class XCHttpImageLoader {
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
    private Drawable load_fail_drawable;

    /**
     * @param context
     * @param cacheToLocalDirectory  本地缓存目录file
     * @param cacheToLocalNum        本地缓存文件夹的图片数量
     * @param cacheToMemoryNum       缓存到内存中的图片数量
     * @param image_size_limit_by_kb 多大文件才开始压缩
     */

    public XCHttpImageLoader(Context context, File cacheToLocalDirectory,
                             int cacheToLocalNum, int cacheToMemoryNum,
                             double image_size_limit_by_kb) {
        super();
        if (context == null) {
            return;
        }
        this.context = context;
        this.handler = XCApp.getBase_handler();
        this.lock = new Object();
        this.threadservice = XCApp.getBase_cache_threadpool();

        if (cacheToLocalDirectory != null && cacheToLocalDirectory.exists()
                && cacheToLocalNum > 0) {
            this.cacheToLocalDirectory = cacheToLocalDirectory;
            this.cacheToLocalNum = cacheToLocalNum;
            this.directoryFiles = new ArrayList<File>();
        } else {
            throw new RuntimeException(this + "---未设置图片缓存目录");
        }

        if (cacheToMemoryNum > 0) {
            if (cacheToMemoryNum < 50) {
                this.cacheToMemoryNum = 50;
                // 防止整个屏幕如果显示20条数据,而只设置了10个,那么当删除正在使用的bitmap时,可能会报异常
            } else {
                this.cacheToMemoryNum = cacheToMemoryNum;
            }
            this.isCacheToMemory = true;
            this.bitmaps = new LinkedHashMap<String, Bitmap>();
        } else {
            // 表示不缓存到内存
            this.isCacheToMemory = false;
            this.bitmaps = null;
            this.cacheToMemoryNum = 0;
        }

        if (image_size_limit_by_kb < 10) {
            this.image_size_limit_by_byte = 10240;
        } else {
            this.image_size_limit_by_byte = image_size_limit_by_kb * 1024;
        }
        /*
         * 不等于null,表示之前设置了缓存目录和数量,则先检查本地缓存的数量,如果超过,则删除
		 */
        if (this.directoryFiles != null) {
            countCache(cacheToLocalDirectory);
        }
    }

    public void countCache(File cacheDirectory) {
        // 缓存目录中可能有文件夹,比如用户加进去的,这里只计算图片数量
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

    public void loadImage(ImageView imageview, final String url, Drawable load_fail_drawable) {
        // 先获取默认的图片
        this.load_fail_drawable = load_fail_drawable;
        /**
         * 如果之前设置了缓存到内存,那么先到内存中找
         */
        if (isCacheToMemory) {
            if (bitmaps.containsKey(url)) {
                XCApp.i(url + "----------------to get bitmap from memory");
                Bitmap bitmap = bitmaps.get(url);
                if (bitmap != null && !bitmap.isRecycled()) {
                    imageview.setImageBitmap(bitmap);
                    XCApp.i(url + "---" + "get bitmap form memory success");
                    return;
                }
            }
            XCApp.i(url + "----------------get bitmap from memory faile ----bitmaps contains url ?--" + bitmaps.containsKey(url));
        }

        /**
         * 内存中没有，从本地缓存找
         */
        // File file = new File(cacheToLocalDirectory, EncryptUtil.MD5(url)+ url.substring(url.lastIndexOf(".")));
        File file = new File(cacheToLocalDirectory, url.substring(url.lastIndexOf("/") + 1));
        if (file.exists()) {
            XCApp.i(url + "----------------to get bitmap from local file");
            threadservice.execute(new LocalPictureRunnable(imageview, file, url));
            return;
        }
        XCApp.i("local file not exists");


        if (url.startsWith(HTTP_HEAD)) {
            /**
             * 如果本地缓存没有,就去网络下载
             */
            XCApp.i(url + "----------------to get bitmap from net");
            // 这里要用trim(),因为如果服务端是用println()发过来的,那么最后的url为url+"/r/n"
            threadservice.execute(new NetPictureRunnable(imageview, url.trim()));
        } else {
            /**
             * 如果是本地文件
             */
            File localFile = new File(url);
            if (localFile.exists()) {
                threadservice.execute(new LocalPictureRunnable(imageview, file, url));
            } else {
                XCApp.e(this + "---传入的图片路径有误");
            }
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
                    XCApp.i("local file size------ " + data.length);
                    XCApp.i("local file size of bitmap------ "
                                    + options.outWidth * options.outHeight * 4);
                    int ratio = (int) Math.round(Math.sqrt(options.outWidth
                            * options.outHeight * 4
                            / image_size_limit_by_byte));
                    if (ratio < 1) {
                        options.inSampleSize = 1;// 保持原有大小
                        XCApp.i(1 + "----local file ---ratio");
                    } else {
                        options.inSampleSize = ratio;
                        XCApp.i(ratio + "-----local file ---ratio");
                    }
                    options.inJustDecodeBounds = false;
                    // 真实压缩图片
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                } catch (Exception e) {
                    XCApp.e(context, "--XCHttpImageLoader--LocalPictureRunnable()", e);
                    e.printStackTrace();
                    System.gc();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            XCApp.i(
                                    "oom , so set the default_bitmap");
                            imageview.setImageDrawable(load_fail_drawable);
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

                if (bitmap == null && url.startsWith(HTTP_HEAD)) {
                    // 有一种情况是,之前从网络获取的图片写到本地的时候出错了,图片无法解析,所以为null
                    threadservice.execute(new NetPictureRunnable(imageview, url.trim()));
                    return;
                }

                // 先检查内存中缓存的数量是否超过了规定,如果超过了规定,则删除最前面的1/5 -->这里用锁来判断,必须的
                XCApp.i(bitmaps.size() + "----bitmaps.size()");
                if (isCacheToMemory && bitmaps.size() >= cacheToMemoryNum) {
                    synchronized (lock) {
                        if (bitmaps.size() >= cacheToMemoryNum) {
                            XCApp.i(
                                    "bitmaps is full ,now delete 20% ");
                            int delete = cacheToMemoryNum / 5;
                            for (Iterator<Map.Entry<String, Bitmap>> it = bitmaps
                                    .entrySet().iterator(); it.hasNext(); ) {
                                if (delete != 0) {
                                    it.next();
                                    it.remove();
                                    delete--;
                                } else {
                                    // System.gc();
                                    // //这个gc很耗性能,而且会在url有softreference引用的时候把softReference销毁掉
                                    break;
                                }
                            }
                        }
                    }
                }
                if (bitmap != null && isCacheToMemory
                        && !bitmaps.containsKey(url)) {
                    synchronized (lock) {
                        bitmaps.put(url, bitmap);
                        XCApp.i("add_to_memory",
                                url
                                        + "--have added to bitmaps-------now the size fo bitmaps is "
                                        + bitmaps.size());
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!bitmap.isRecycled()) {
                            imageview.setImageBitmap(bitmap);
                        }
                        XCApp.i(url
                                + "----------have got from local file");
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
                // 注明:这里没有用httpclient或asynhttp框架,因为网络加载图片本来就耗资源且费时,而且这里也没有特殊的需求,所以用这种最轻便的网络访问方式,如有特殊的需求,再改
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
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
                        BitmapFactory.decodeByteArray(data, 0, data.length,
                                options);
                        XCApp.i("net file size------ " + data.length);
                        XCApp.i("net file size of bitmap------ "
                                        + options.outWidth * options.outHeight
                                        * 4);
                        // XCApp.i(options.inDensity+"------------options.inDensity");
                        // XCApp.i(options.inScreenDensity+"------------options.inScreenDensity");
                        // XCApp.i(options.outWidth+"------------options.outWidth");
                        // XCApp.i(options.outHeight+"------------options.outHeight");
                        // XCApp.i(options.inTargetDensity+"------------options.inTargetDensity");
                        // XCApp.i(options.inDensity+"------------options.inDensity");
                        // XCApp.i(options.inPreferredConfig+"------------options.inPreferredConfig");
                        int ratio = (int) Math.round(Math.sqrt(options.outWidth
                                * options.outHeight * 4
                                / image_size_limit_by_byte));
                        if (ratio < 1) {
                            options.inSampleSize = 1;// 保持原有大小
                            XCApp.i(1 + "net file ---ratio");
                        } else {
                            options.inSampleSize = ratio;
                            XCApp.i(ratio + "net file---ratio");
                        }
                        options.inJustDecodeBounds = false;
                        // 真实压缩图片
                        bitmap = BitmapFactory.decodeByteArray(data, 0,
                                data.length, options);
                    } catch (Exception e) {
                        XCApp.e(context, "--XCHttpImageLoader--NetPictureRunnable", e);
                        e.printStackTrace();
                        System.gc();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                XCApp.i(
                                        "oom , so set the default_bitmap");
                                imageview.setImageDrawable(load_fail_drawable);
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
                        return;
                    }
                    // 先检查内存中缓存的数量是否超过了规定,如果超过了规定,则删除最前面的1/5
                    if (isCacheToMemory && bitmaps.size() > cacheToMemoryNum) {
                        synchronized (lock) {
                            if (bitmaps.size() > cacheToMemoryNum) {
                                XCApp.i(

                                        "bitmaps is full ,now delete 20% ");
                                int delete = cacheToMemoryNum / 5;
                                for (Iterator<Map.Entry<String, Bitmap>> it = bitmaps
                                        .entrySet().iterator(); it.hasNext(); ) {
                                    if (delete != 0) {
                                        it.next();
                                        it.remove();
                                        delete--;
                                    } else {
                                        // System.gc();
                                        // //这个gc很耗性能,而且会在url有softreference引用的时候把softReference销毁掉
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    // 加入构建的内存缓存中
                    synchronized (lock) {
                        if (bitmap != null && isCacheToMemory
                                && !bitmaps.containsKey(url)) {
                            bitmaps.put(url, bitmap);
                        }
                    }
                    // 设置bitmap
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            XCApp.i(
                                    "have got from net");
                            if (!bitmap.isRecycled()) {
                                imageview.setImageBitmap(bitmap);
                            }
                        }
                    });

                    // 如果指定了缓存目录,且是从网络获取的,那么缓存到本地的设置
                    if (cacheToLocalDirectory != null) {
                        // String filename = EncryptUtil.MD5(url) +
                        // url.substring(url.lastIndexOf("."));
                        String filename = url
                                .substring(url.lastIndexOf("/") + 1);
                        File file = new File(cacheToLocalDirectory, filename);
                        if (!bitmap.isRecycled()) {
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

                    // 设置图片
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!bitmap.isRecycled()) {
                                imageview.setImageBitmap(bitmap);
                            }
                        }
                    });
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

    public Drawable getDefaultDrawable() {
        return load_fail_drawable;
    }

    public void closeThreadPool() {
        try {
            threadservice.shutdown();
        } catch (Exception e) {
            XCApp.e(context, "--XCHttpImageLoader--closeThreadPool()", e);
        }
        threadservice = null;
    }
}
