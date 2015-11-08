package com.xiaocoder.android.fw.general.util;

import java.util.List;

/**
 * Created by xiaocoder on 2015/7/28.
 */
public class Utils {

    /**
     * 判断list是不是空的
     */
    public static boolean isListBlank(List list) {

        if (list == null || list.isEmpty()) {
            return true;
        }

        return false;
    }


}
