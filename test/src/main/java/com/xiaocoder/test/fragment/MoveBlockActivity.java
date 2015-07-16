package com.xiaocoder.test.fragment;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.fragment.XCMoveBlockFragment;
import com.xiaocoder.android.fw.general.fragment.XCMoveBlockFragment.OnClickMoveListener;
import com.xiaocoder.android.fw.general.fragment.XCMoveBlockPlusFragment;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

public class MoveBlockActivity extends QlkBaseActivity {
    XCMoveBlockFragment fragment;
    XCMoveBlockPlusFragment plus_fragment;
    XCMoveBlockPlusFragment plus_fragment2;
    XCMoveBlockPlusFragment plus_fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_move_block);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetRefresh() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initWidgets() {
        fragment = new XCMoveBlockFragment();
        // fragment.setContents(new String[] { "电影", "音乐", "游戏" }, 0, false);
        fragment.setContents(new String[]{"电影", "音乐", "游戏", "软件", "学习"}, 10, true);
        fragment.setInitSelected(2); // 默认选中第三个
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

    @SuppressWarnings("deprecation")
    @Override
    public void listeners() {
        fragment.setOnClickMoveListener(new OnClickMoveListener() {

            @Override
            public void onClickMoveListener(int position) {
                // position 为点击了第几个
                shortToast(position + "");
            }
        });


        plus_fragment.setOnClickMoveListener(new XCMoveBlockPlusFragment.OnClickMoveListener() {
            @Override
            public void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview) {
                shortToast(position + "");
            }
        });

        plus_fragment2.setOnClickMoveListener(new XCMoveBlockPlusFragment.OnClickMoveListener() {
            @Override
            public void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview) {
                shortToast(position + "");
            }
        });

        plus_fragment3.setOnClickMoveListener(new XCMoveBlockPlusFragment.OnClickMoveListener() {
            @Override
            public void onClickMoveListener(int position, ViewGroup current_item, ImageView current_imageview, ViewGroup last_item, ImageView last_imageview) {
                shortToast(position + "");
            }
        });
    }
}
