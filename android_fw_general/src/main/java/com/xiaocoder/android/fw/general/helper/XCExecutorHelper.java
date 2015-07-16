package com.xiaocoder.android.fw.general.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 该类是单例的 ， 可以获取自动变动大小的线程池 与 single大小的线程池
 * @author xiaocoder
 *  2014-10-17 下午1:52:54
 */
public class XCExecutorHelper {
	private ExecutorService threadpool_single;
	private ExecutorService threadpool_cache;
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

	public void close() {
		try {
			if (threadpool_cache != null) {
				threadpool_cache.shutdown();
			}
			if (threadpool_single != null) {
				threadpool_single.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			threadpool_cache = null;
			threadpool_single = null;
		}
	}
}
