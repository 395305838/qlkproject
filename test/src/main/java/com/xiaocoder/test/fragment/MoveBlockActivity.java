package com.xiaocoder.test.fragment;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.views.fragment.XCMoveBlockPlusFragment;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class MoveBlockActivity extends MActivity {
    XCMoveBlockPlusFragment fragment;
    XCMoveBlockPlusFragment plus_fragment;
    XCMoveBlockPlusFragment plus_fragment2;
    XCMoveBlockPlusFragment plus_fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_move_block);
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initWidgets() {
        fragment = new XCMoveBlockPlusFragment();
        // fragment.setContents(new String[] { "电影", "音乐", "游戏" }, 0, false);
        fragment.setContents(new String[]{"电影", "音乐", "游戏", "软件", "学习"});
        fragment.setInitSelected(2, 4, true, 10); // 默认选中第三个
        addFragment(R.id.test_moveblock_fragment, fragment);

        plus_fragment = new XCMoveBlockPlusFragment();
        // plus_fragment.setInitSelected(2, 4, false, 20);
        plus_fragment.setInitSelected(2, 4, true, 10);
        plus_fragment.setImageUris(new String[]{"drawable://" + R.drawable.ic_launcher, "drawable://" + R.drawable.ic_launcher, "drawable://" + R.drawable.ic_launcher,
                "drawable://" + R.drawable.ic_launcher, "drawable://" + R.drawable.ic_launcher, "drawable://" + R.drawable.ic_launcher});
        plus_fragment.setContents(new String[]{null, "音乐", null, "软件", "学习", "棋牌", "水果"});
        addFragment(R.id.test_moveblock_fragment_plus, plus_fragment);

        plus_fragment2 = new XCMoveBlockPlusFragment();
        plus_fragment2.setInitSelected(2, 4, false, 2);
        plus_fragment2.setImageUris(new String[]{"http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png",
                "http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png"});
        plus_fragment2.setContents(new String[]{null, null, "", null, "", "", ""});
        addFragment(R.id.test_moveblock_fragment_plus2, plus_fragment2);

        plus_fragment3 = new XCMoveBlockPlusFragment();
        plus_fragment3.setInitSelected(2, 4, true, 2);
        plus_fragment3.setImageUris(new String[]{"http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png",
                "http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png", "http://www.baidu.com/img/bdlogo.png"});
        plus_fragment3.setContents(new String[]{null, null, null, null, null, null});
        addFragment(R.id.test_moveblock_fragment_plus3, plus_fragment3);
    }

    @Override
    public void listeners() {
        fragment.setOnClickMoveListener(new XCMoveBlockPlusFragment.OnClickMoveListener() {
            @Override
            public void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview) {
                // position 为点击了第几个
                XCApp.shortToast(position);
            }
        });


        plus_fragment.setOnClickMoveListener(new XCMoveBlockPlusFragment.OnClickMoveListener() {
            @Override
            public void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview) {
                XCApp.shortToast(position);
            }
        });

        plus_fragment2.setOnClickMoveListener(new XCMoveBlockPlusFragment.OnClickMoveListener() {
            @Override
            public void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview) {
                XCApp.shortToast(position);
            }
        });

        plus_fragment3.setOnClickMoveListener(new XCMoveBlockPlusFragment.OnClickMoveListener() {
            @Override
            public void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview) {
                XCApp.shortToast(position);
            }
        });
    }

    @Override
    protected void initSlideDestroyActivity() {
    }
}
