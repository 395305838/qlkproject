package com.xiaocoder.android.fw.general.jsonxml;

import android.util.Log;

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

    public static String CODE = "code";
    public static String MSG = "msg";

    /*
     * 对对象的操作
     */
    private HashMap<String, Object> paraMap = new HashMap<String, Object>();

    public Boolean getBoolean(String name) {
        Object value = paraMap.get(name.toLowerCase());
        return (Boolean) value;
    }

    public String getString(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL))
            return "";

        return value + "";

    }

    public Integer getInt(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null) {
            return 0;
        }
        try {
            return (Integer) value;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Long getLong(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return null;
        try {
            return (Long) value;
        } catch (Exception e) {
            e.printStackTrace();
            return Long.parseLong(value + "");
        }
    }

    public Double getDouble(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return null;
        try {
            return (Double) value;
        } catch (Exception e) {
            e.printStackTrace();
            return Double.parseDouble(value + "");
        }
    }

    public Date getDate(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return null;
        return (Date) value;
    }

    public void setString(String name, String value) {
        if (value == null)
            return;
        paraMap.put(name.toLowerCase(), value);
    }

    public void setBoolean(String name, Boolean value) {
        if (value == null)
            return;
        paraMap.put(name.toLowerCase(), value);
    }

    public void setDouble(String name, Double value) {
        if (value == null)
            return;
        paraMap.put(name.toLowerCase(), value);
    }

    public void setLong(String name, Long value) {
        if (value == null)
            return;
        paraMap.put(name.toLowerCase(), value);
    }

    public void setInt(String name, Integer value) {
        if (value == null)
            return;
        paraMap.put(name.toLowerCase(), value);
    }

    public void setDate(String name, Date value) {
        if (value == null)
            return;
        paraMap.put(name.toLowerCase(), value);
    }

    public void set(String name, Object value) {
        if (value == null)
            return;
        paraMap.put(name.toLowerCase(), value);
    }


    public void remove(String name) {
        paraMap.remove(name.toLowerCase());
    }

    public Object get(String name) {
        Object value = paraMap.get(name.toLowerCase());
        return value;
    }

    public Iterator<String> keys() {
        return paraMap.keySet().iterator();
    }

    public void clear() {
        paraMap.clear();
    }

    public XCJsonBean getModel(String name) {
        Object value = paraMap.get(name.toLowerCase());

        if (null == value || value.equals(JSONObject.NULL))
            return new XCJsonBean();
        return (XCJsonBean) value;
    }

    public XCJsonBean optModel(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (null == value || value.equals(JSONObject.NULL))
            return new XCJsonBean();
        return (XCJsonBean) value;
    }

    @SuppressWarnings("unchecked")
    public List<XCJsonBean> getList(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return new ArrayList<XCJsonBean>();
        return (List<XCJsonBean>) value;
    }

    public List<XCJsonBean> getListIsNull(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return null;
        return (List<XCJsonBean>) value;
    }

    @SuppressWarnings("unchecked")
    public List<XCJsonBean> optList(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL))
            return new ArrayList<XCJsonBean>();
        return (List<XCJsonBean>) value;
    }

    public List<XCJsonBean> getList(String name, String itemName) {
        XCJsonBean listObj = getModel(name);
        if (listObj == null)
            return new ArrayList<XCJsonBean>();
        return listObj.getList(itemName);
    }

    public List<XCJsonBean> optList(String name, String itemName) {
        XCJsonBean listObj = getModel(name);
        if (listObj == null || listObj.equals(JSONObject.NULL))
            return new ArrayList<XCJsonBean>();
        return listObj.getList(itemName);
    }

    public List<String> getStringList(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return new ArrayList<String>();
        return (List<String>) value;
    }

    public List<String> optStringList(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL))
            return new ArrayList<String>();
        return (List<String>) value;
    }

    public List<ArrayList> getListList(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return new ArrayList<ArrayList>();
        return (List<ArrayList>) value;
    }

    public List<String> getStringList(String name, String itemName) {
        XCJsonBean listObj = getModel(name);
        if (listObj == null)
            return new ArrayList<String>();
        return listObj.getStringList(itemName);
    }

    public List<String> optStringList(String name, String itemName) {
        XCJsonBean listObj = getModel(name);
        if (listObj == null || listObj.equals(JSONObject.NULL))
            return new ArrayList<String>();
        return listObj.getStringList(itemName);
    }

    public List<Integer> getIntegerList(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null)
            return new ArrayList<Integer>();
        return (List<Integer>) value;
    }

    public List<Integer> optIntegerList(String name) {
        Object value = paraMap.get(name.toLowerCase());
        if (value == null || value.equals(JSONObject.NULL))
            return new ArrayList<Integer>();
        return (List<Integer>) value;
    }

    public List<Integer> getIntegerList(String name, String itemName) {
        XCJsonBean listObj = getModel(name);
        if (listObj == null)
            return new ArrayList<Integer>();
        return listObj.getIntegerList(itemName);
    }

    public List<Integer> optIntegerList(String name, String itemName) {
        XCJsonBean listObj = getModel(name);
        if (listObj == null || listObj.equals(JSONObject.NULL))
            return new ArrayList<Integer>();
        return listObj.getIntegerList(itemName);
    }

    @Override
    public String toString() {
        return paraMap.toString();
    }
}
