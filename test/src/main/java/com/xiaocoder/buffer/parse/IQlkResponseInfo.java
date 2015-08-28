package com.xiaocoder.buffer.parse;

/**
 * Created by xiaocoder on 2015/8/28.
 */
public interface IQlkResponseInfo {

    String RESPONSE_CODE = "code";
    String RESPONSE_MSG = "msg";

    int getCode();

    String getMsg();

}
