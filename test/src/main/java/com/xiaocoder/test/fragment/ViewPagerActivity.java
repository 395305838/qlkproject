package com.xiaocoder.test.fragment;

import android.os.Bundle;

import com.daimajia.slider.library.SliderLayout;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.views.fragment.XCTitleCommonFragment;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;
import com.xiaocoder.viewpagerslider.XCViewPagerFragment;

import java.util.HashMap;

public class ViewPagerActivity extends MActivity {
    XCTitleCommonFragment title_fragment;
    XCViewPagerFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_pager);
        super.onCreate(savedInstanceState);
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
        HashMap<String, Object> file_maps = new HashMap<String, Object>();
        file_maps.put("Hannibal", R.drawable.hannibal);
        file_maps.put("Big Bang Theory", R.drawable.bigbang);
        file_maps.put("House of Cards", R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);
        fragment.setMap(file_maps);
        fragment.setIsAutoSlider(false);
        fragment.setTf(SliderLayout.Transformer.Default);
        addFragment(R.id.test_viewpager_fragment, fragment);

    }

    @Override
    public void listeners() {

        fragment.setOnImageClickListener(new XCViewPagerFragment.OnImageClickListener() {

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
