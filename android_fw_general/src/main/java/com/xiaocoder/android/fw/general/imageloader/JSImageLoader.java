package com.xiaocoder.android.fw.general.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.imageloader.IImageLoader.XCIImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * 图片加载处理类
 *
 * @author 徐金山
 * @version 1.0
 */
public class JSImageLoader implements XCIImageLoader {

    int mDefaultImageResourceId;
    /**
     * 一级缓存的最大空间
     */
    private static final int MAX_CAPACITY = 10;
    /**
     * 一级缓存
     * 0.75是加载因子为经验值，true则表示按照最近访问量的高低排序，false则表示按照插入顺序排序
     */
    private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(MAX_CAPACITY / 2, 0.75f, true) {
        private static final long serialVersionUID = 1L;

        protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            // 当超过一级缓存值的时候，将老的值从一级缓存搬到二级缓存
            if (size() > MAX_CAPACITY) {
                mSecondLevelCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            }
            return false;
        }
    };
    /**
     * 二级缓存
     * 采用的是软应用，只有在内存吃紧的时候软应用才会被回收，有效的避免了oom
     */
    private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(MAX_CAPACITY / 2);

    /**
     * 定时清理缓存的时间间隔（单位：毫秒）
     */
    private static final long DELAY_BEFORE_PURGE = 10 * 1000;
    /**
     * 定时清理缓存
     */
    private Runnable mClearCache = new Runnable() {
        public void run() {
            mFirstLevelCache.clear();
            mSecondLevelCache.clear();
        }
    };


    /**
     * Handler处理
     */
    private Handler mPurgeHandler = new Handler();
    /**
     * 任务消费者
     */
    private Executor mExecutor = new Executor();
    /**
     * 图片加载异步任务队列
     */
    private static final BlockingQueue<ImageLoadTask> mTasks = new LinkedBlockingQueue<ImageLoadTask>();
    /**
     * 通过信号量控制同时执行的线程数
     */
    private Semaphore mSemaphore = new Semaphore(50);

    /**
     * 这里是任务的消费者，去任务队列取出下载任务，然后执行，当没有任务的时候消费者就等待
     */
    private class Executor extends Thread {
        public void run() {
            while (true) {
                ImageLoadTask task = null;
                try {
                    task = mTasks.take();
                    if (task != null) {
                        mSemaphore.acquire();
                        task.execute();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 构造方法
     */
    public JSImageLoader(int defaultImageId) {
        mDefaultImageResourceId = defaultImageId;
        mExecutor.start();
    }

    /**
     * 返回缓存，如果没有则返回null
     *
     * @param url 图片的url
     * @return Bitmap 图片对象
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap = null;

        if (null == url || url.trim().length() == 0) {
            return bitmap;
        } else {
            bitmap = getFromFirstLevelCache(url); // 从一级缓存中拿
            if (bitmap != null) {
                return bitmap;
            }
            bitmap = getFromSecondLevelCache(url); // 从二级缓存中拿
            return bitmap;
        }
    }

    /**
     * 从二级缓存中拿
     *
     * @param url 图片的url
     * @return Bitmap 图片对象
     */
    private Bitmap getFromSecondLevelCache(String url) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
        if (softReference != null) {
            bitmap = softReference.get();
            // 由于内存吃紧，软引用已经被gc回收了
            if (bitmap == null) {
                mSecondLevelCache.remove(url);
            }
        }
        return bitmap;
    }

    /**
     * 从一级缓存中拿
     *
     * @param url 图片的url
     * @return Bitmap 图片对象
     */
    private Bitmap getFromFirstLevelCache(String url) {
        Bitmap bitmap = null;
        synchronized (mFirstLevelCache) {
            bitmap = mFirstLevelCache.get(url);
            // 将最近访问的元素放到链的头部，提高下一次访问该元素的检索速度（LRU算法）
            if (bitmap != null) {
                mFirstLevelCache.remove(url);
                mFirstLevelCache.put(url, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 重置缓存清理的timer
     */
    private void resetPurgeTimer() {
        mPurgeHandler.removeCallbacks(mClearCache);
        mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
    }

    /**
     * @param url
     * @param imageview
     * @param obj       第一个参数约定好是 mDefaultImageResourceId
     */
    @Override
    public void display(String url, ImageView imageview, Object... obj) {
        resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(url); // 从缓存中读取
        if (bitmap == null) {
            // 先设置默认图
            if (obj != null && obj[0] instanceof Integer) {
                imageview.setImageResource((int) obj[0]);
            } else {
                imageview.setImageResource(mDefaultImageResourceId);
            }
            // 网络请求
            ImageLoadTask imageLoadTask = new ImageLoadTask(url, imageview);
            try {
                mTasks.put(imageLoadTask); // 将任务放入队列中
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            imageview.setImageBitmap(bitmap); // 设为缓存图片
        }
    }

    @Override
    public void display(String url, ImageView imageview) {
        display(url, imageview, mDefaultImageResourceId);
    }

    /**
     * 放入缓存
     *
     * @param url   Key
     * @param value Value
     */
    public void addImage2Cache(String url, Bitmap value) {
        if (value == null || url == null) {
            return;
        }
        synchronized (mFirstLevelCache) {
            mFirstLevelCache.put(url, value);
        }
    }

    /**
     * 图片加载异步任务类
     */
    public class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
        String url;
        ImageView imageview;

        public ImageLoadTask(String url, ImageView imageview) {
            this.url = url;
            this.imageview = imageview;
        }

        protected Bitmap doInBackground(Object... params) {
            Bitmap drawable = loadImageFromInternet(url); // 获取网络图片
            return drawable;
        }

        protected void onPostExecute(Bitmap result) {
            mSemaphore.release();
            if (result == null) {
                return;
            }
            addImage2Cache(url, result); // 放入缓存
            imageview.setImageBitmap(result);
        }

    }

    /**
     * 加载网络图片
     *
     * @param url 图片的Url
     * @return 图片实体对象
     */
    public Bitmap loadImageFromInternet(String url) {
        Bitmap bitmap = null;
        HttpClient client = AndroidHttpClient.newInstance("Android");
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSocketBufferSize(params, 10000);

        HttpResponse response = null;
        InputStream inputStream = null;
        HttpGet httpGet = null;
        try {
            // 校验图片的URL（保证图片的URL是以"http://"开头，以".png"或".jpg"结束）
            String temp_url = url.trim().toLowerCase();
            if (temp_url.startsWith("http://")) {
                // do nothing
            } else {
                url = "http://" + url.trim();
            }

            if (temp_url.endsWith(".png") || temp_url.endsWith(".jpg") || temp_url.endsWith(".jpeg")) {
                httpGet = new HttpGet(url);
                response = client.execute(httpGet);
                int stateCode = response.getStatusLine().getStatusCode();
                if (stateCode != HttpStatus.SC_OK) {
                    return bitmap;
                }
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try {
                        inputStream = entity.getContent();
                        return bitmap = BitmapFactory.decodeStream(inputStream);
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } else {
                bitmap = null;
            }
        } catch (ClientProtocolException e) {
            httpGet.abort();
            e.printStackTrace();
        } catch (IOException e) {
            httpGet.abort();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((AndroidHttpClient) client).close();
        }

        return bitmap;
    }
}
