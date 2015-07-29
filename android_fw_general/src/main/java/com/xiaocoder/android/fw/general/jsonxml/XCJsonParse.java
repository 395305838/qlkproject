package com.xiaocoder.android.fw.general.jsonxml;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.io.XCIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class XCJsonParse {
    public static XCJsonBean getJsonParseData(String json) {
        XCJsonBean result = new XCJsonBean();
        try {
            JSONObject obj = new JSONObject(json);
            parse(result, obj);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    // 解析json一,即通用的bean
    private static void parse(XCJsonBean result, JSONObject obj) {
        try {
            Object o, object;
            JSONArray array;
            Iterator it = obj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                o = obj.get(key);
                if (o instanceof JSONObject) {
                    XCJsonBean bean = new XCJsonBean();
                    parse(bean, (JSONObject) o);
                    result.add(key, bean);
                } else if (o instanceof JSONArray) {
                    array = (JSONArray) o;
                    List list = new ArrayList();
                    result.add(key, list);
                    for (int i = 0; i < array.length(); i++) {
                        object = array.get(i);
                        if (object instanceof JSONObject) {
                            XCJsonBean bean = new XCJsonBean();
                            parse(bean, (JSONObject) object);
                            list.add(bean);
                        } else {
                            list.add(object);
                        }
                    }
                } else {
                    if (XCApplication.getBase_log().is_OutPut()) {
                        if (o instanceof Boolean) {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is boolean");
                        } else if (o instanceof Integer) {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is Integer");
                        } else if (o instanceof String) {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is String");
                        } else {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is Else Type");
                        }
                    }
                    result.add(key, o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<XCJsonBean> getJsonListParseData(String json) {
        List<XCJsonBean> list = new ArrayList<XCJsonBean>();
        try {
            XCApplication.tempPrint(json);
            JSONArray array = new JSONArray(json);
            int size = array.length();
            for (int i = 0; i < size; i++) {
                JSONObject json_object = array.getJSONObject(i);
                if (json_object != null) {
                    XCJsonBean bean = getJsonParseData(json_object.toString());
                    list.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    public static void getJsonFiled(InputStream in) throws UnsupportedEncodingException {
        String json = new String(XCIO.toBytesByInputStream(in), "utf-8");
        json = json.replace("\"", "");
        String[] items = json.split(",");
        for (String item : items) {
            String[] keyvalues = item.split(":");
            System.out.println("bean." + keyvalues[0].trim() + "= jsonObj.getString(\"" + keyvalues[0].trim() + "\");");
        }
    }

    // ---------------------------------------------------------------------------------------------
    // 解析json二:创建bean类 , 这里只是打印出来了而已,然后复制粘贴字段到bean
    public static void json2Bean(String json) {
        if (XCApplication.getBase_log().is_OutPut() && json != null) {
            LinkedHashSet<String> set = new LinkedHashSet<String>();
            json = json.replace("\"", "");
            json = json.replace("{", ",");
            json = json.replace("[", "");
            String[] items = json.split(",");
            StringBuilder builder = new StringBuilder("");
            for (String item : items) {
                String[] keyvalues = item.split(":");
                if ("".equals(keyvalues[0]) || set.contains(keyvalues[0])) {
                    continue;
                } else {
                    set.add(keyvalues[0]);
                }
                builder.append("public String " + keyvalues[0].trim() + "=" + "\"" + keyvalues[0].trim() + "\"" + ";");
            }
            XCApplication.printi(XCConfig.TAG_JSON_BEAN, builder.toString());
        }
    }

    public static <T extends XCJsonBean> T getJsonParseData(String json, Class<T> jsonbean_class) {
        T result = getJsonBean(jsonbean_class);
        try {
            JSONObject obj = new JSONObject(json);
            parse(result, obj, jsonbean_class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static <T extends XCJsonBean> T getJsonBean(Class<T> jsonbean_class) {
        try {
            Constructor<T> con = jsonbean_class.getConstructor();
            return con.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T extends XCJsonBean> void parse(XCJsonBean result, JSONObject obj, Class<T> jsonbean_classs) {
        try {
            Object o, object;
            JSONArray array;
            Iterator it = obj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                o = obj.get(key);
                if (o instanceof JSONObject) {
                    T bean = getJsonBean(jsonbean_classs);
                    parse(bean, (JSONObject) o);
                    result.add(key, bean);
                } else if (o instanceof JSONArray) {
                    array = (JSONArray) o;
                    List list = new ArrayList();
                    result.add(key, list);
                    for (int i = 0; i < array.length(); i++) {
                        object = array.get(i);
                        if (object instanceof JSONObject) {
                            T bean = getJsonBean(jsonbean_classs);
                            parse(bean, (JSONObject) object);
                            list.add(bean);
                        } else {
                            list.add(object);
                        }
                    }
                } else {
                    if (XCApplication.getBase_log().is_OutPut()) {
                        if (o instanceof Boolean) {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is boolean");
                        } else if (o instanceof Integer) {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is Integer");
                        } else if (o instanceof String) {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is String");
                        } else {
                            XCApplication.printi(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is Else Type");
                        }
                    }
                    result.add(key, o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
