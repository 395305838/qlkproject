package com.xiaocoder.views.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.function.adapter.XCAdapterViewPagerRecyle;
import com.xiaocoder.android.fw.general.application.XCBaseFragment;
import com.xiaocoder.views.R;
import com.xiaocoder.views.view.open.OPZoomImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaocoder
 * @date 2015-1-6 下午10:50:22
 * 1 图片可以触摸缩放 2可以初始化选中第几张图
 */
public class XCImagesZoomFragment extends XCBaseFragment {

    ViewPager qlk_id_viewpager;
    TextView xc_id_fragment_viewpager_count;
    List<ImageView> imageviews;
    List<String> urls;
    XCAdapterViewPagerRecyle adapter;

    int current_location = 0;
    int total_images;

    public void setDefaultSelectedIndex(int defaultSelectedIndex) {
        this.current_location = defaultSelectedIndex;
    }

    public interface OnLoadImage {
        void onLoadImage(ImageView imageview, String url);
    }

    public interface OnImageClickListener {
        void onImageClickListener(int position);
    }

    OnImageClickListener listener;

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    OnLoadImage on_load_image_listener;

    public void setOnLoadImageListener(OnLoadImage on_load_image_listener) {
        this.on_load_image_listener = on_load_image_listener;
    }

    // 会等父fragment的onActivityCreated完成后才会调用子fragment的onCreate()方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_viewpager_zoom);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onImageClickListener((Integer) (v.getTag()));
        }
    }

    public void setData(List<String> urls) {
        this.urls = urls;
    }

    private void createImageViews() {
        imageviews = new ArrayList<ImageView>();
        total_images = urls.size();
        for (int i = 0; i < total_images; i++) {
            // 创建images
            ImageView imageview = getZoomImageView(i);
            // 加载图片
            if (on_load_image_listener != null) {
                on_load_image_listener.onLoadImage(imageview, urls.get(i));
            } else {
                return;
            }

            imageviews.add(imageview);
        }
    }

    @NonNull
    private OPZoomImageView getZoomImageView(int index) {
        OPZoomImageView imageview = new OPZoomImageView(getActivity());
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.enable();
        imageview.setOnClickListener(this);
        imageview.setTag(index);
        return imageview;
    }

    protected void createViewPager() {
        adapter = new XCAdapterViewPagerRecyle(imageviews);
        qlk_id_viewpager.setAdapter(adapter);
        qlk_id_viewpager.setCurrentItem(current_location);
    }

    @Override
    public void initWidgets() {

        qlk_id_viewpager = getViewById(R.id.xc_id_fragment_viewpager);
        xc_id_fragment_viewpager_count = getViewById(R.id.xc_id_fragment_viewpager_count);

        if (urls != null) {
            total_images = urls.size();
            createImageViews();
            createViewPager();
            update();
        }

    }

    public void update() {
        int index = current_location + 1;
        xc_id_fragment_viewpager_count.setText(index + " / " + total_images);
    }

    @Override
    public void listeners() {
        qlk_id_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                current_location = position;
                update();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

}
