package com.xiaocoder.android.fw.general.jsonxml;

import android.util.Log;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.util.UtilCommon;
import com.xiaocoder.android.fw.general.util.UtilString;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

// 1 JsonType 的过滤标记可以打印出 返回的json串的每个字段的 类型
// 2 解析的时候如解析的类型错误了， 该bean里内置了try catch，并会返回正确的类型，仅 int String  long  double
// 3 bean内的字段可以自动生成， JsonBean的标记中可以 打印出bean， 复制后格式化即可
public class XCJsonBean implements Serializable {

    private static final long serialVersionUID = 8461633826093329307L;

    private HashMap<String, Object> paraMap = new HashMap<String, Object>();

    public Boolean obtBoolean(String name) {
        return obtBoolean(name, false);
    }

    public Boolean obtBoolean(String name, boolean default_value) {
        Object value = paraMap.get(name.toLowerCase());

        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return default_value;
        }
    }

    public String obtString(String name) {
        return obtString(name, "");
    }

    public String obtString(String name, String default_value) {

        Object value = paraMap.get(name.toLowerCase());

        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        }

        return value + "";
    }

    public Integer obtInt(String name) {
        return obtInt(name, 0);
    }

    public Integer obtInt(String name, int default_value) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        }
        try {
            if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                return default_value;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return default_value;
        }
    }

    public Long obtLong(String name) {
        return obtLong(name, 0);
    }

    public Long obtLong(String name, long default_value) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        }
        try {
            if (value instanceof Long) {
                return (Long) value;
            } else if (value instanceof String) {
                return Long.parseLong((String) value);
            } else {
                return default_value;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return default_value;
        }
    }

    public Double obtDouble(String name) {
        return obtDouble(name, 0);
    }

    public Double obtDouble(String name, double default_value) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        }
        try {
            if (value instanceof Double) {
                return (Double) value;
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                return default_value;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return default_value;
        }
    }

    public XCJsonBean obtModel(String name, XCJsonBean default_value) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        }
        return (XCJsonBean) value;
    }


    @SuppressWarnings("unchecked")
    public List<XCJsonBean> obtList(String name, List<XCJsonBean> defualt_value) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL)) {
            return defualt_value;
        }
        return (List<XCJsonBean>) value;
    }

    @SuppressWarnings("unchecked")
    public List<String> obtStringList(String name, List<String> default_value) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        }
        return (List<String>) value;
    }

    @SuppressWarnings("unchecked")
    public List<ArrayList> obtListList(String name, List<ArrayList> default_value) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL)) {
            return default_value;
        }
        return (List<ArrayList>) value;
    }

    public void add(String name, Object value) {
        if (UtilString.isBlank(name))
            return;
        paraMap.put(name.toLowerCase(), value);
    }


    public void remove(String name) {
        paraMap.remove(name.toLowerCase());
    }

    public Iterator<String> keys() {
        return paraMap.keySet().iterator();
    }

    public void clear() {
        paraMap.clear();
    }

    @Override
    public String toString() {
        return paraMap.toString();
    }
}
