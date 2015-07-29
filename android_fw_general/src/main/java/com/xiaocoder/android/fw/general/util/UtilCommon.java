package com.xiaocoder.android.fw.general.util;

import java.util.List;

/**
 * Created by xiaocoder on 2015/7/28.
 */
public class UtilCommon {

    /*
     * 判断list是不是空的
     */
    public static boolean isListBlank(List list) {

        if (list == null || list.size() < 1) {
            return true;
        }

        return false;
    }


}
