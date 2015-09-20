package com.xiaocoder.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.helper.XCTimeHelper;
import com.xiaocoder.android.fw.general.helper.XCTimeHelper.CustomTimer;
import com.xiaocoder.android.fw.general.io.XCIO;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.pop.XCPhotoPopupWindow;
import com.xiaocoder.android.fw.general.pop.XCPhotoPopupWindow.onPhotoPopupItemClickListener;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.buffer.QlkConfig;
import com.xiaocoder.buffer.function.QlkMainActivity;
import com.xiaocoder.test.anim.AnimationActivity;
import com.xiaocoder.test.baidumap.MapActivity;
import com.xiaocoder.test.contacts.ContactsActivity;
import com.xiaocoder.test.dialogs.CircleProgressBarActivity;
import com.xiaocoder.test.dialogs.DialogActivity3;
import com.xiaocoder.test.dialogs.LineProgressBarActivity;
import com.xiaocoder.test.dialogs.ProgressViewActivity;
import com.xiaocoder.test.fragment.CamareActivity;
import com.xiaocoder.test.fragment.ImagesZoomActivity;
import com.xiaocoder.test.fragment.MoveBlockActivity;
import com.xiaocoder.test.fragment.SearchActivity;
import com.xiaocoder.test.fragment.ViewPagerActivity;
import com.xiaocoder.test.fragment.ViewpagerSliderActivity;
import com.xiaocoder.test.fragment.WebActivity;
import com.xiaocoder.test.http.HttpActivity;
import com.xiaocoder.test.http.HttpDownLoadActivity;
import com.xiaocoder.test.http2.ExpandListActivity;
import com.xiaocoder.test.http2.GridActivity;
import com.xiaocoder.test.http2.ListActivity;
import com.xiaocoder.test.line_point.LinePointActivityGC;
import com.xiaocoder.test.pop.PopActivity;
import com.xiaocoder.test.scan.CodeActivity;
import com.xiaocoder.test.scan.ScanActivity;
import com.xiaocoder.test.slidingmenu.SlidingMenuActivity;
import com.xiaocoder.test.slidingmenu.SlidingMenuActivity2;
import com.xiaocoder.test.view.RoundImageViewActivity;
import com.xiaocoder.test.view.ScrollActivity;
import com.xiaocoder.test.view.SwitchButtonActivity;
import com.xiaocoder.test.viewpagerindicator.ListSamples;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends QlkMainActivity {

    public static String TEST_HOST = "10.7.30.121";
    Button test_point_line;
    Button test_scan;
    Button test_http;
    Button test_pop;
    Button test_webview;
    Button test_viewpager;
    Button test_move_block;
    Button test_camare;
    Button test_search;
    Button test_scroll;
    Button test_pop2;
    Button test_contacts;
    // XCRecordVoiceButton test_recodervoice;
    Button test_chat_bottom;
    Button test_list_fragment;
    Button test_grid_fragment;
    Button test_sliding_menu;
    Button test_sliding_menu2;
    Button test_viewpager_indicator;
    Button test_anim;
    Button test_http_download;
    Button test_code;

    Button xc_id_test;
    Button xc_id_anim;
    Button test_dialog3;
    Button test_baidumap;
    Button test_switchbutton;
    Button test_circleprogress;
    Button test_numberprogress;
    Button test_expandablelistview;
    Button test_round_imageview;
    Button test_viewpager_num;
    Button test_viewpager_slider;
    Button test_progress_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        timer();
        recoderButton();
        loge();

    }

    private void loge() {

        XCApp.i(1);
        XCApp.i(true);
        XCApp.i(false);
        Object obj = null;
        XCApp.i(obj);

        try {
            XCApp.e("123");
            XCApp.e("345");
            XCApp.e("678");
            int i = 1 / 0;
        } catch (Exception e) {
            XCApp.e(this, "--oncreate()--", e);
        }
        // XCApp.clearLog();
        XCApp.e(this, "1234567890");
        XCApp.tempPrint("android--" + System.currentTimeMillis());

        XCApp.i(XCIO.getAllFilesByDir2(XCIOAndroid.createDirInSDCard(QlkConfig.APP_ROOT), new ArrayList<File>()));

    }

    private void recoderButton() {
        // test_recodervoice = (XCRecordVoiceButton)
        // findViewById(R.id.test_recodervoice);
        // test_recodervoice.setOnRecordVoiceSuccessListener(new
        // OnRecordVoiceSuccessListener() {
        //
        // @Override
        // public void onRecordVoiceSuccessListener(File file) {
        // XCApp.base_log.debugShortToast(file.toString());
        // }
        // });
    }

    private void timer() {
        XCTimeHelper timeHelper = new XCTimeHelper(1000000, new CustomTimer() {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        });
        // timeHelper.start();
    }

    @Override
    public void initWidgets() {

        XCApp.i(XCConfig.TAG_TEMP, QlkConfig.CURRENT_RUN_ENVIRONMENT.toString() + "-----环境");
        XCApp.i(XCConfig.TAG_TEMP, QlkConfig.DEBUG_CONTROL.toString() + "-----调试");

        test_dialog3 = getViewById(R.id.test_dialog3);

        test_round_imageview = getViewById(R.id.test_round_imageview);

        test_expandablelistview = getViewById(R.id.test_expandablelistview);

        test_code = getViewById(R.id.test_code);

        test_baidumap = getViewById(R.id.test_baidumap);

        test_switchbutton = getViewById(R.id.test_switchbutton);

        test_circleprogress = getViewById(R.id.test_circleprogress);

        test_numberprogress = getViewById(R.id.test_numberprogress);

        test_point_line = getViewById(R.id.test_point_line);

        test_scan = getViewById(R.id.test_scan);

        xc_id_test = getViewById(R.id.xc_id_test);

        test_http = getViewById(R.id.test_http);

        test_http_download = getViewById(R.id.test_http_download);

        test_pop = getViewById(R.id.test_pop);

        test_webview = getViewById(R.id.test_webview);

        test_viewpager = getViewById(R.id.test_viewpager);

        test_move_block = getViewById(R.id.test_move_block);

        test_camare = getViewById(R.id.test_camare);

        UtilString.setLightAppendString("你好:123", test_camare, "#ff00cccc");

        test_search = getViewById(R.id.test_search);

        test_scroll = getViewById(R.id.test_scroll);

        test_pop2 = getViewById(R.id.test_pop2);

        test_contacts = getViewById(R.id.test_contacts);

        test_chat_bottom = getViewById(R.id.test_chat_bottom);

        test_list_fragment = getViewById(R.id.test_list_fragment);

        test_grid_fragment = getViewById(R.id.test_grid_fragment);

        test_sliding_menu = getViewById(R.id.test_sliding_menu);

        test_sliding_menu2 = getViewById(R.id.test_sliding_menu2);

        test_viewpager_indicator = getViewById(R.id.test_viewpager_indicator);

        test_anim = getViewById(R.id.test_anim);

        xc_id_anim = getViewById(R.id.xc_id_anim);

        test_viewpager_num = getViewById(R.id.test_viewpager_num);

        test_viewpager_slider = getViewById(R.id.test_viewpager_slider);

        test_progress_view = getViewById(R.id.test_progress_view);


    }

    @Override
    public void listeners() {

        test_point_line.setOnClickListener(this);
        test_dialog3.setOnClickListener(this);
        test_code.setOnClickListener(this);
        test_baidumap.setOnClickListener(this);
        test_switchbutton.setOnClickListener(this);
        test_circleprogress.setOnClickListener(this);
        test_numberprogress.setOnClickListener(this);
        test_scan.setOnClickListener(this);
        xc_id_test.setOnClickListener(this);
        test_http.setOnClickListener(this);
        test_http_download.setOnClickListener(this);
        test_pop.setOnClickListener(this);
        test_webview.setOnClickListener(this);
        test_viewpager.setOnClickListener(this);
        test_move_block.setOnClickListener(this);
        test_camare.setOnClickListener(this);
        test_search.setOnClickListener(this);
        test_scroll.setOnClickListener(this);
        test_pop2.setOnClickListener(this);
        test_contacts.setOnClickListener(this);
        test_chat_bottom.setOnClickListener(this);
        test_list_fragment.setOnClickListener(this);
        test_grid_fragment.setOnClickListener(this);
        test_sliding_menu.setOnClickListener(this);
        test_sliding_menu2.setOnClickListener(this);
        test_viewpager_indicator.setOnClickListener(this);
        test_anim.setOnClickListener(this);
        xc_id_anim.setOnClickListener(this);
        test_expandablelistview.setOnClickListener(this);
        test_round_imageview.setOnClickListener(this);
        test_viewpager_num.setOnClickListener(this);
        test_viewpager_slider.setOnClickListener(this);
        test_progress_view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.test_point_line) {
            myStartActivity(LinePointActivityGC.class);

        } else if (id == R.id.test_scan) {
            myStartActivity(ScanActivity.class);
        } else if (id == R.id.test_http) {
            myStartActivity(HttpActivity.class);
        } else if (id == R.id.test_pop) {
            myStartActivity(PopActivity.class);
        } else if (id == R.id.test_webview) {
            myStartActivity(WebActivity.class);

        } else if (id == R.id.test_viewpager) {
            myStartActivity(ViewPagerActivity.class);

        } else if (id == R.id.test_move_block) {
            myStartActivity(MoveBlockActivity.class);

        } else if (id == R.id.test_camare) {
            myStartActivity(CamareActivity.class);

        } else if (id == R.id.test_search) {
            myStartActivity(SearchActivity.class);

        } else if (id == R.id.test_scroll) {
            myStartActivity(ScrollActivity.class);

        } else if (id == R.id.test_contacts) {
            myStartActivity(ContactsActivity.class);

        } else if (id == R.id.test_pop2) {
            XCPhotoPopupWindow pop = new XCPhotoPopupWindow(MainActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.setOnPhotoPopupItemClickListener(new onPhotoPopupItemClickListener() {

                @Override
                public void onPhotoUpload() {
                }

                @Override
                public void onNetPrescription() {
                }

                @Override
                public void onLocalAlbum() {
                }

                @Override
                public void onCancel() {
                }
            });
            pop.showViewCenter((View) test_pop2.getParent());

        } else if (id == R.id.test_list_fragment) {
            myStartActivity(ListActivity.class);

        } else if (id == R.id.test_grid_fragment) {
            myStartActivity(GridActivity.class);

        } else if (id == R.id.test_sliding_menu) {
            myStartActivity(SlidingMenuActivity.class);

        } else if (id == R.id.test_sliding_menu2) {
            myStartActivity(SlidingMenuActivity2.class);

        } else if (id == R.id.test_viewpager_indicator) {
            startActivity(new Intent(this, ListSamples.class));

        } else if (id == R.id.test_http_download) {
            myStartActivity(HttpDownLoadActivity.class);
        } else if (id == R.id.test_code) {
            myStartActivity(CodeActivity.class);
        } else if (id == R.id.xc_id_test) {
            myStartActivity(TextActivity.class);
        } else if (id == R.id.xc_id_anim) {
            myStartActivity(AnimationActivity.class);
        } else if (id == R.id.test_dialog3) {
            myStartActivity(DialogActivity3.class);
        } else if (id == R.id.test_baidumap) {
            myStartActivity(MapActivity.class);
        } else if (id == R.id.test_switchbutton) {
            myStartActivity(SwitchButtonActivity.class);
        } else if (id == R.id.test_circleprogress) {
            myStartActivity(CircleProgressBarActivity.class);
        } else if (id == R.id.test_numberprogress) {
            myStartActivity(LineProgressBarActivity.class);
        } else if (id == R.id.test_expandablelistview) {
            myStartActivity(ExpandListActivity.class);
        } else if (id == R.id.test_round_imageview) {
            myStartActivity(RoundImageViewActivity.class);
        } else if (id == R.id.test_viewpager_num) {
            myStartActivity(ImagesZoomActivity.class);
        } else if (id == R.id.test_viewpager_slider) {
            myStartActivity(ViewpagerSliderActivity.class);
        } else if (id == R.id.test_progress_view) {
            myStartActivity(ProgressViewActivity.class);
        }

    }

    @Override
    public void onNetRefresh() {

    }
}
