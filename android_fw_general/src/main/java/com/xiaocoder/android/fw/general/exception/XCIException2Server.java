package com.xiaocoder.android.fw.general.exception;

/**
 * Created by xiaocoder on 2015/10/15.
 * version: 1.2.0
 * description:
 */
public interface XCIException2Server {
    /**
     * 可上传错误信息到服务器
     */
    void uploadException2Server(String info, Throwable ex, Thread thread);
}
