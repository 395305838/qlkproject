package com.xiaocoder.buffer.parse;

import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;

/**
 * Created by xiaocoder on 2015/7/29.
 */
public class QlkBean<T extends XCJsonBean> extends XCJsonBean<T> implements IQlkResponseInfo {

    /**
     * 返回的状态码
     */
    public int getCode() {
        return this.getInt(RESPONSE_CODE, 0);
    }

    /**
     * 返回的信息
     */
    public String getMsg() {
        return this.getString(RESPONSE_MSG, "");
    }

}