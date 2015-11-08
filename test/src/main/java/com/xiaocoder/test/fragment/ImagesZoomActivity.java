package com.xiaocoder.test.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.views.fragment.XCTitleCommonFragment;
import com.xiaocoder.views.fragment.XCImagesZoomFragment;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;

public class ImagesZoomActivity extends MActivity {

    XCImagesZoomFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_images_zoom_fragment);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {
        // title
        XCTitleCommonFragment title_fragment = new XCTitleCommonFragment();
        title_fragment.setTitleCenter(true, "嗯嗯");
        title_fragment.setTitleLeft(false, "");
        addFragment(R.id.xc_id_model_titlebar, title_fragment);
        // viewpager
        fragment = new XCImagesZoomFragment();
        ArrayList<String> list = new ArrayList<String>();
        list.add("http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        list.add("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        list.add("http://www.baidu.com/img/bdlogo.png");
        list.add("http://cdn3.nflximg.net/images/3093/2043093.jpg");
        fragment.setData(list);
        fragment.setDefaultSelectedIndex(1);
        addFragment(R.id.xc_id_model_content, fragment);


    }

    @Override
    public void listeners() {
        fragment.setOnLoadImageListener(new XCImagesZoomFragment.OnLoadImage() {

            @Override
            public void onLoadImage(ImageView imageview, String url) {
                // 这里用本地的图片模拟 ,
                // ------------->补充这里即可--------->用你的图片加载方式加载--->url为图片的链接
                //imageview.setImageResource(R.drawable.ic_launcher);
                XCApp.i(url);
                XCApp.displayImage(url, imageview);
            }
        });

        fragment.setOnImageClickListener(new XCImagesZoomFragment.OnImageClickListener() {

            @Override
            public void onImageClickListener(int position) {
                XCApp.shortToast(position + "");
            }
        });
    }

    @Override
    protected void initSlideDestroyActivity() {
    }
}
