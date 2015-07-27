package com.xiaocoder.test.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.fragment.XCTitleCommonFragment;
import com.xiaocoder.android.fw.general.fragment.XCViewPagerFragment;
import com.xiaocoder.android.fw.general.fragment.XCViewPagerFragment.OnImageClickListener;
import com.xiaocoder.android.fw.general.fragment.XCViewPagerFragment.OnLoadImage;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

import java.util.ArrayList;

public class ViewPagerActivity extends QlkBaseActivity {
    XCTitleCommonFragment title_fragment;
    XCViewPagerFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_pager);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetRefresh() {

    }

    @Override
    public void initWidgets() {
        // title
        title_fragment = new XCTitleCommonFragment();
        title_fragment.setTitleCenter(true, "嗯嗯");
        title_fragment.setTitleLeft(false, "");
        addFragment(R.id.xc_id_model_titlebar, title_fragment);
        // viewpager
        fragment = new XCViewPagerFragment();
        fragment.setAllowAutoSlide(true, 4000); // 设置是否可以自动滑动, 以及设置滑动的间隔时间
        ArrayList<String> list = new ArrayList<String>();
        list.add("http://1");
        list.add("http://2");
        list.add("http://3");
        list.add("http://4");
        fragment.setData(list); // 设置数据 , 会根据list的size , 动态创建 滚动的点
        addFragment(R.id.test_viewpager_fragment, fragment, fragment.getClass().getSimpleName());

    }

    @Override
    public void listeners() {
        fragment.setOnLoadImageListener(new OnLoadImage() {

            @Override
            public void onLoadImage(ImageView imageview, String url) {
                // 这里用本地的图片模拟 ,
                // ------------->补充这里即可--------->用你的图片加载方式加载--->url为图片的链接
                // XCApplication.base_imageloader.displayImage(url, imageview,
                // XCImageLoaderFactory.getDisplayImageOptions());
                //imageview.setImageResource(R.drawable.ic_launcher);
                displayImage(url, imageview);
            }
        });

        fragment.setOnImageClickListener(new OnImageClickListener() {

            @Override
            public void onImageClickListener(int position) {
                shortToast(position + "");
            }
        });
    }

}
