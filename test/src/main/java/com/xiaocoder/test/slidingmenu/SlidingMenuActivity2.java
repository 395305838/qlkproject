package com.xiaocoder.test.slidingmenu;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xiaocoder.android.fw.general.base.XCBaseMainActivity;
import com.xiaocoder.android.fw.general.fragment.XCTitleJustFragment;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseMainActivity;


public class SlidingMenuActivity2 extends QlkBaseMainActivity {
    private SlidingMenu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_slidingmenu2);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {

        base_tab_group = getViewById(R.id.xc_id_tab_group);
        addFragment(R.id.xc_id_model_content, new SlidingFragment1());

        XCTitleJustFragment title = new XCTitleJustFragment();
        title.setTitle("title menu");
        addFragment(R.id.xc_id_model_titlebar, title);

        createSlidingMenu();
    }

    @Override
    public void listeners() {
        base_tab_group.setOnCheckedChangeListener(this);
    }

    @Override
    public void showPage() {
        super.showPage();
        showBottomLayout(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        hideBodyFragment();
        base_recoder_selected_tab_item = checkedId;


        if (checkedId == R.id.xc_id_tab_item1) {
            dShortToast("1-->" + base_recoder_selected_tab_item);
            showFragmentByClass(SlidingFragment1.class, R.id.xc_id_model_content);
        } else if (checkedId == R.id.xc_id_tab_item2) {
            dShortToast("2-->" + base_recoder_selected_tab_item);
            showFragmentByClass(SlidingFragment2.class, R.id.xc_id_model_content);
        } else if (checkedId == R.id.xc_id_tab_item3) {
            dShortToast("3-->" + base_recoder_selected_tab_item);
            showFragmentByClass(SlidingFragment3.class, R.id.xc_id_model_content);
        } else if (checkedId == R.id.xc_id_tab_item4) {
            dShortToast("4-->" + base_recoder_selected_tab_item);
            showFragmentByClass(SlidingFragment4.class, R.id.xc_id_model_content);
        } else if (checkedId == R.id.xc_id_tab_item5) {
            dShortToast("5-->" + base_recoder_selected_tab_item);
            showFragmentByClass(SlidingFragment5.class, R.id.xc_id_model_content);
        }

    }

    public void createSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffset(300);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.view_l_slidingmenu);
        menu.setSecondaryMenu(R.layout.view_l_slidingmenu);
    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onNetRefresh() {
    }
}
