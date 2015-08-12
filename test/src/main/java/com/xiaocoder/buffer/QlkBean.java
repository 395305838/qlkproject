package com.xiaocoder.buffer;

import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;

/**
 * Created by xiaocoder on 2015/7/29.
 * 该中间层用于处理：如接口中公共常量的定义，double的位数的定义  项目特殊的返回值的定义等
 */
public class QlkBean<T extends XCJsonBean> extends XCJsonBean<T> {

    public static String CODE = "code";
    public static String MSG = "msg";

}
