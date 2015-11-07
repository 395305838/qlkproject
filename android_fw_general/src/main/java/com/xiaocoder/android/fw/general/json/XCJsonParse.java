package com.xiaocoder.android.fw.general.json;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.util.UtilString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 1 JsonType的过滤标记可以查看返回的json串的每个字段的类型
 * 2 JsonBean的标记中可以查看该json中的字段常量，复制后格式化即可生成jsonbean类
 */
public class XCJsonParse {

    // { } 解析json对象
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

    // [ ] 解析json数组
    public static <T extends XCJsonBean> List<T> getJsonListParseData(String json, Class<T> beanClass) {
        List<T> list = new ArrayList<T>();
        try {
            XCApp.tempPrint(json);
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

    private static <T extends XCJsonBean> T getBean(Class<T> beanClass) {
        try {
            Constructor<T> con = beanClass.getConstructor();
            return con.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                    if (XCApp.getBase_log().is_OutPut()) {
                        if (o instanceof Boolean) {
                            XCApp.i(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is boolean");
                        } else if (o instanceof Integer) {
                            XCApp.i(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is Integer");
                        } else if (o instanceof String) {
                            XCApp.i(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is String");
                        } else {
                            XCApp.i(XCConfig.TAG_JSON_TYPE, key.toString() + "---->" + o.toString() + "----is Else Type");
                        }
                    }
                    result.add(key, o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 创建假bean类 , 这里只是打印出来了而已,然后复制粘贴字段
    public static void json2Bean(String json) {

        if (XCApp.getBase_log().is_OutPut() && json != null) {
            LinkedHashSet<String> set = new LinkedHashSet<String>();
            json = json.replace("\"", "");
            json = json.replace(" ", "");
            json = json.replace("{", "{,");
            json = json.replace("[", "[,");
            String[] items = json.split(",");
            StringBuilder builder = new StringBuilder("");

            builder.append("public class  文件名  extends QlkBean {");

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
                        .append("() { return getString(" + key + ");")
                        .append("}");

                builder.append("public void set")
                        .append(UtilString.setFirstLetterBig(key))
                        .append("( Object value) { add(" + key + " , value);")
                        .append("}");

            }

            builder.append("}");

            XCApp.i(XCConfig.TAG_JSON_BEAN, builder.toString());
        }
    }
}
