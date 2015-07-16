/**
 *
 */
package com.xiaocoder.android.fw.general.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;

/**
 * @author xiaocoder
 * @Description:
 * @date 2014-12-10 上午9:31:21
 */
public class XCDaoFactory {
    public static LinkedHashMap<String, XCIDao> map;

    private XCDaoFactory() {

    }

    @SuppressWarnings("unchecked")
    public static <T> XCIDao<T> getDaoInstance(Context context, String dao_class_fullname) {
        if (context == null || dao_class_fullname == null) {
            return null;
        }
        if (map == null) {
            synchronized (XCDaoFactory.class) {
                if (map == null) {
                    map = new LinkedHashMap<String, XCIDao>(6);
                }
            }
        }
        try {
            XCIDao<T> dao = map.get(dao_class_fullname);
            if (dao != null) {
                return dao;
            }
            dao = (XCIDao<T>) Class.forName(dao_class_fullname).getConstructor(Context.class).newInstance(context);
            map.put(dao_class_fullname, dao);
            return dao;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> XCIDao<T> getDaoInstanceForChat(Context context, String dao_class_fullname, String patient_id, String doctor_id) {
        if (context == null || dao_class_fullname == null || patient_id == null || doctor_id == null) {
            return null;
        }
        if (map == null) {
            synchronized (XCDaoFactory.class) {
                if (map == null) {
                    map = new LinkedHashMap<String, XCIDao>();
                }
            }
        }
        try {
            XCIDao<T> dao = map.get(doctor_id + "_" + patient_id);
            if (dao != null) {
                return dao;
            }

            dao = (XCIDao<T>) Class.forName(dao_class_fullname)
                    .getConstructor(Context.class, String.class, String.class)
                    .newInstance(context, patient_id, doctor_id);
            map.put(doctor_id + "_" + patient_id, dao);
            return dao;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> XCIDao<T> getDaoInstanceForSearch(Context context, String dao_class_fullname, String tabName) {
        if (context == null || dao_class_fullname == null || tabName == null) {
            return null;
        }
        if (map == null) {
            synchronized (XCDaoFactory.class) {
                if (map == null) {
                    map = new LinkedHashMap<String, XCIDao>(6);
                }
            }
        }
        try {
            XCIDao<T> dao = map.get(tabName);
            if (dao != null) {
                return dao;
            }

            dao = (XCIDao<T>) Class.forName(dao_class_fullname)
                    .getConstructor(Context.class, String.class)
                    .newInstance(context, tabName);
            map.put(tabName, dao);
            return dao;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearAll() {
        if (map != null) {
            synchronized (XCDaoFactory.class) {
                if (map != null) {
                    map.clear();
                    map = null;
                }
            }
        }
    }
}
