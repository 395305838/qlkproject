package com.xiaocoder.test.bean;

import com.xiaocoder.test.buffer.QlkBean;

/**
 * Created by xiaocoder on 2015/7/29.
 */
public class TestBean extends QlkBean {

    public String code = "code";
    public String msg = "msg";
    public String data = "data";

    public String getCode() {
        return obtString(code);
    }

    public void setCode(String key, Object value) {
        add(key, value);
    }

    public String getMsg() {
        return obtString(msg);
    }

    public void setMsg(String key, Object value) {
        add(key, value);
    }

    public String getData() {
        return obtString(data);
    }

    public void setData(String key, Object value) {
        add(key, value);
    }
}