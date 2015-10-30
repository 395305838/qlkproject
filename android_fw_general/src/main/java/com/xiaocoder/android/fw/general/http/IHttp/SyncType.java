package com.xiaocoder.android.fw.general.http.IHttp;

/**
 * Created by xiaocoder on 2015/10/30.
 * version: 1.2.0
 * description: http串行用的到
 * <p/>
 * GO_ON: 不管line中的前一个http是否请求成功，都会执行下一个http请求
 * DELETE_ALL：前一个http请求失败了，line中的剩余http请求都不会执行且line中的剩余http任务会被清空，也会从groups中清空
 * LOOP：前一个http请求失败了，会循环该请求，直到该请求成功，才会继续下一个新的请求
 */

public enum SyncType {
    GO_ON,
    DELETE_ALL,
    LOOP
}
