package com.xiaocoder.test;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonParse;
import com.xiaocoder.android.fw.general.util.UtilSystem;
import com.xiaocoder.test.buffer.QlkActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class TextActivity extends QlkActivity {

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
        test1();
//        test2();

    }

    private void test2() {
        String str = "";
    }

    private void test1() {
        String str = "{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"data\": [\n" +
                "\"/cr/100/200/aaa.mp4\"\n" +
                "    ]\n" +
                "}\n";

        String str2 = " {\"code\":0,\"msg\":\"成功\",\"data\":[[\"板蓝根\",\"白云山\"]]}";

        XCJsonBean bean = XCJsonParse.getJsonParseData(str2, XCJsonBean.class);
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

        printi(UtilSystem.getDeviceId(this) + "--------------deviceId");
    }

    // 设置监听
    @Override
    public void listeners() {

    }

}
