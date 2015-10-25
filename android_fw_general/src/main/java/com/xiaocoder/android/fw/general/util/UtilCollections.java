package com.xiaocoder.android.fw.general.util;


import com.xiaocoder.android.fw.general.json.XCJsonBean;

import java.util.List;

public class UtilCollections {

    public static List sortByKey(List<XCJsonBean> list, String key) {
        if (list == null) {
            return list;
        }

        int size = list.size();
        for(int i = 0 ; i < size ; i++){

            for(int j = i+1 ; j < size ; j++){

                XCJsonBean temp ;
                long i_t = list.get(i).getLong(key);
                long j_t = list.get(j).getLong(key);
                if(i_t > j_t){
                    temp = list.get(j);
                    list.set(j,list.get(i));
                    list.set(i,temp);
                }
            }
        }

        return list;
    }

}
