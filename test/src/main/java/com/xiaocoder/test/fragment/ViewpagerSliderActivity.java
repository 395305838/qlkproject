package com.xiaocoder.test.fragment;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.fragment.XCViewPagerFragment;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;

import java.util.HashMap;


public class ViewpagerSliderActivity extends QlkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_viewpager_slider);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetRefresh() {
    }

    XCViewPagerFragment slider;

    @Override
    public void initWidgets() {

        HashMap<String, Object> url_maps = new HashMap<String, Object>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String, Object> file_maps = new HashMap<String, Object>();
        file_maps.put("Hannibal", R.drawable.hannibal);
        file_maps.put("Big Bang Theory", R.drawable.bigbang);
        file_maps.put("House of Cards", R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);

        slider = new XCViewPagerFragment();
        slider.setMap(file_maps);
        slider.setAnim(null);
        slider.setIsNeedDes(true);
        addFragment(R.id.viewpager_slider, slider);
    }

    @Override
    public void listeners() {
        slider.setOnImageClickListener(new XCViewPagerFragment.OnImageClickListener() {
            @Override
            public void onImageClickListener(int position) {
                XCApplication.shortToast(position + "");
            }
        });
    }
}
