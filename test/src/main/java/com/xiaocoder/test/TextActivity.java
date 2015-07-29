package com.xiaocoder.test;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonParse;
import com.xiaocoder.android.fw.general.util.UtilSystem;
import com.xiaocoder.test.buffer.QlkBaseActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class TextActivity extends QlkBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_text);
        super.onCreate(savedInstanceState);
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
    }

    // 初始化控件
    @Override
    public void initWidgets() {
        String str = "{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"data\": [\n" +
                "\"/cr/100/200/aaa.mp4\"\n" +
                "    ]\n" +
                "}\n";

        String str2 = " {\"code\":0,\"msg\":\"成功\",\"data\":[[\"板蓝根\",\"白云山\"]]}";

        XCJsonBean bean = XCJsonParse.getJsonParseData(str2 , XCJsonBean.class);
        List beans = bean.obtListList("data", new ArrayList<ArrayList>());

        try {
            printi(beans.toString());
            printi(beans.get(0).toString());
            if (beans.get(0) instanceof List) {
                printi("List");
            } else if (beans.get(0) instanceof String[]) {
                printi("string[]");
            } else {
                printi(beans.get(0).getClass().toString());

                JSONArray array = (JSONArray) beans.get(0);
                int count = array.length();
                for (int i = 0; i < count; i++) {
                    printi((String) array.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            printi("exception");
        }

        printi(UtilSystem.getDeviceId(this)+"--------------deviceId");

    }

    // 设置监听
    @Override
    public void listeners() {

    }

}
