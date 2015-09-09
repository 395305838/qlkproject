package com.xiaocoder.test.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.fragment.XCTitleCommonFragment;
import com.xiaocoder.android.fw.general.fragment.XCViewPagerFragmentShowNum;
import com.xiaocoder.test.R;

import java.util.ArrayList;

public class ViewPagerNumFragmentActivity extends XCBaseActivity {

    XCViewPagerFragmentShowNum fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_view_pager_num_fragment);
        super.onCreate(savedInstanceState);
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {

    }

    @Override
    public void initWidgets() {
        // title
        XCTitleCommonFragment title_fragment = new XCTitleCommonFragment();
        title_fragment.setTitleCenter(true, "嗯嗯");
        title_fragment.setTitleLeft(false, "");
        addFragment(R.id.xc_id_model_titlebar, title_fragment);
        // viewpager
        fragment = new XCViewPagerFragmentShowNum();
        ArrayList<String> list = new ArrayList<String>();
        list.add("http://www.baidu.com/img/bdlogo.png");
        list.add("http://www.baidu.com/img/bdlogo.png");
        list.add("http://www.baidu.com/img/bdlogo.png");
        list.add("http://www.baidu.com/img/bdlogo.png");
        fragment.setData(list);
        addFragment(R.id.xc_id_model_content, fragment);


    }

    @Override
    public void listeners() {
        fragment.setOnLoadImageListener(new XCViewPagerFragmentShowNum.OnLoadImage() {

            @Override
            public void onLoadImage(ImageView imageview, String url) {
                // 这里用本地的图片模拟 ,
                // ------------->补充这里即可--------->用你的图片加载方式加载--->url为图片的链接
                //imageview.setImageResource(R.drawable.ic_launcher);
                XCApplication.printi(url);
                XCApplication.displayImage(url, imageview);
            }
        });

        fragment.setOnImageClickListener(new XCViewPagerFragmentShowNum.OnImageClickListener() {

            @Override
            public void onImageClickListener(int position) {
                XCApplication.shortToast(position + "");
            }
        });
    }

}
