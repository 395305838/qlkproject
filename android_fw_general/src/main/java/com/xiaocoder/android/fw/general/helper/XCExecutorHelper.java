package com.xiaocoder.android.fw.general.helper;

import com.xiaocoder.android.fw.general.application.XCApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 该类是单例的 ， 可以获取自动变动大小的线程池 与 single大小的线程池
 *
 * @author xiaocoder
 *         2014-10-17 下午1:52:54
 */
public class XCExecutorHelper {

    private ExecutorService threadpool_single;
    private ExecutorService threadpool_cache;
    private ExecutorService threadpool_fix;
    private ScheduledExecutorService scheduled_threadpool_fix;

    private static XCExecutorHelper executorHelper = new XCExecutorHelper();

    private XCExecutorHelper() {
    }

    public static XCExecutorHelper getExecutorHelperInstance() {
        return executorHelper;
    }

    public ExecutorService getSingle() {
        if (threadpool_single == null) {
            synchronized (XCExecutorHelper.class) {
                if (threadpool_single == null) {
                    threadpool_single = Executors.newSingleThreadExecutor();
                }
            }
        }
        return threadpool_single;
    }

    public ExecutorService getCache() {
        if (threadpool_cache == null) {
            synchronized (XCExecutorHelper.class) {
                if (threadpool_cache == null) {
                    threadpool_cache = Executors.newCachedThreadPool();
                }
            }
        }
        return threadpool_cache;
    }

    public ExecutorService getFix(int size) {
        if (threadpool_fix == null) {
            synchronized (XCExecutorHelper.class) {
                if (threadpool_fix == null) {
                    threadpool_fix = Executors.newFixedThreadPool(size);
                }
            }
        }
        return threadpool_fix;
    }

    public ScheduledExecutorService getScheduledFix(int size) {
        if (scheduled_threadpool_fix == null) {
            synchronized (XCExecutorHelper.class) {
                if (scheduled_threadpool_fix == null) {
                    scheduled_threadpool_fix = Executors.newScheduledThreadPool(size);
                }
            }
        }
        return scheduled_threadpool_fix;
    }

    public void close() {

        try {
            if (threadpool_cache != null) {
                threadpool_cache.shutdownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
            XCApp.e("XCExecutorHelper()--threadpool_cache--线程池关闭异常");
        } finally {
            threadpool_cache = null;
        }


        try {
            if (threadpool_single != null) {
                threadpool_single.shutdownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
            XCApp.e("XCExecutorHelper()--threadpool_single--线程池关闭异常");
        } finally {
            threadpool_single = null;
        }


        try {
            if (threadpool_fix != null) {
                threadpool_fix.shutdownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
            XCApp.e("XCExecutorHelper()--threadpool_fix--线程池关闭异常");
        } finally {
            threadpool_fix = null;
        }

        try {
            if (scheduled_threadpool_fix != null) {
                scheduled_threadpool_fix.shutdownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
            XCApp.e("XCExecutorHelper()--scheduled_threadpool_fix--线程池关闭异常");
        } finally {
            scheduled_threadpool_fix = null;
        }


    }
}
