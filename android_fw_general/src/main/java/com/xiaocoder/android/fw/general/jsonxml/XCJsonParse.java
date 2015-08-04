package com.xiaocoder.android.fw.general.jsonxml;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.io.XCIO;
import com.xiaocoder.android.fw.general.util.UtilString;

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

    public static <T extends XCJsonBean> T getBean(Class<T> beanClass) {
        try {
            Constructor<T> con = beanClass.getConstructor();
            return con.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // { }
    public static <T extends XCJsonBean> T getJsonParseData(String json, Class<T> beanClass) {
        T result = getBean(beanClass);
        try {
            if (UtilString.isBlank(json)) {
                return null;
            }
            JSONObject obj = new JSONObject(json);
            parse(result, obj, beanClass);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    // [ ]
    public static <T extends XCJsonBean> List<T> getJsonListParseData(String json, Class<T> beanClass) {
        List<T> list = new ArrayList<T>();
        try {
            XCApplication.tempPrint(json);
            JSONArray array = new JSONArray(json);
            int size = array.length();
            for (int i = 0; i < size; i++) {
                JSONObject json_object = array.getJSONObject(i);
                if (json_object != null) {
                    T bean = getJsonParseData(json_object.toString(), beanClass);
                    list.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }


    private static <T extends XCJsonBean> void parse(XCJsonBean result, JSONObject obj, Class<T> beanClass) {
        try {
            Object o, object;
            JSONArray array;
            Iterator it = obj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                o = obj.get(key);
                if (o instanceof JSONObject) {
                    T bean = getBean(beanClass);
                    parse(bean, (JSONObject) o, beanClass);
                    result.add(key, bean);
                } else if (o instanceof JSONArray) {
                    array = (JSONArray) o;
                    List list = new ArrayList();
                    result.add(key, list);
                    for (int i = 0; i < array.length(); i++) {
                        object = array.get(i);
                        if (object instanceof JSONObject) {
                            T bean = getBean(beanClass);
                            parse(bean, (JSONObject) object, beanClass);
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


    // ---------------------------------------------------------------------------------------------

    // 创建bean类 , 这里只是打印出来了而已,然后复制粘贴字段到bean
    public static void json2Bean(String json) {

        if (XCApplication.getBase_log().is_OutPut() && json != null) {
            LinkedHashSet<String> set = new LinkedHashSet<String>();
            json = json.replace("\"", "");
            json = json.replace(" ", "");
            json = json.replace("{", "{,");
            json = json.replace("[", "[,");
            String[] items = json.split(",");
            StringBuilder builder = new StringBuilder("");

            builder.append("public class  文件名  extends QlkBean<文件名> {");

            LinkedHashSet<String> sub = new LinkedHashSet<String>();

            for (String item : items) {

                //可能中文的语句之间有，
                if (!item.contains(":")) {
                    continue;
                }

                String[] keyvalues = item.split(":");

                if (UtilString.isBlank(keyvalues[0]) || set.contains(keyvalues[0])) {
                    // key为空  或者  已经加入了集合,则返回
                    continue;
                } else {
                    // 未加入集合
                    if (keyvalues[1] != null && (keyvalues[1].contains("[") || keyvalues[1].contains("{"))) {
                        // do nothing
                        // 表示该字段是 如  list：{}  ， list：[]  set 和 get方法不需要该字段，先记录这些字段
                        sub.add(keyvalues[0]);
                    }
                    set.add(keyvalues[0]);
                    builder.append("public String " + keyvalues[0].trim() + "=" + "\"" + keyvalues[0].trim() + "\"" + ";");
                }
            }

            // 把get 与 set 不需要的key删除
            for (String sub_key : sub) {
                set.remove(sub_key);
            }

            for (String key : set) {

                builder.append("public String get")
                        .append(UtilString.setFirstLetterBig(key))
                        .append("() { return obtString(" + key + ");")
                        .append("}");

                builder.append("public void set")
                        .append(UtilString.setFirstLetterBig(key))
                        .append("( Object value) { add(" + key + " , value);")
                        .append("}");

            }

            builder.append("}");

            XCApplication.printi(XCConfig.TAG_JSON_BEAN, builder.toString());
        }
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
}
