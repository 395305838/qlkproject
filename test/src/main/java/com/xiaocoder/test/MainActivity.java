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
import com.xiaocoder.android.fw.general.util.UtilActivity;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.middle.QlkConfig;
import com.xiaocoder.middle.function.QlkMainActivity;
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
import com.xiaocoder.test.http2.GridMaterialActivity;
import com.xiaocoder.test.http2.ListActivity;
import com.xiaocoder.test.http2.GridRefreshActivity;
import com.xiaocoder.test.http2.ListMaterialActivity;
import com.xiaocoder.test.http2.ListRefreshActivity;
import com.xiaocoder.test.imageloader.JSImageLoaderActivity;
import com.xiaocoder.test.line_point.LinePointActivityGC;
import com.xiaocoder.test.pop.PopActivity;
import com.xiaocoder.test.scan.CodeActivity;
import com.xiaocoder.test.scan.ScanActivity;
import com.xiaocoder.test.share.UmengShareActivity;
import com.xiaocoder.test.slidingmenu.SlidingMenuActivity;
import com.xiaocoder.test.slidingmenu.SlidingMenuActivity2;
import com.xiaocoder.test.view.PickerViewActiviy;
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
    Button test_umengshare;
    Button test_list_refresh_layout;
    Button test_grid_refresh_layout;
    Button test_list_material_layout;
    Button test_grid_material_layout;
    Button pickerView;
    Button xc_id_imageloader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        timer();
        recoderButton();
        log();

    }

    private void log() {

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

        XCApp.i(XCIO.getAllFilesByDirQueue(XCIOAndroid.createDirInSDCard(QlkConfig.APP_ROOT), new ArrayList<File>()));

        XCIO.toFileByBytes(XCIOAndroid.createFileInAndroid(this, QlkConfig.APP_ROOT, "lalala.txt"), "写入的内容--1234567890987654321abc".getBytes(), true);
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

        test_list_refresh_layout = getViewById(R.id.test_list_refresh_layout);

        test_grid_refresh_layout = getViewById(R.id.test_grid_refresh_layout);

        test_grid_material_layout = getViewById(R.id.test_grid_material_layout);

        test_list_material_layout = getViewById(R.id.test_list_material_layout);

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

        test_umengshare = getViewById(R.id.test_umengshare);

        pickerView = getViewById(R.id.test_pickerView);

        xc_id_imageloader = getViewById(R.id.xc_id_imageloader);


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
        test_umengshare.setOnClickListener(this);
        test_grid_material_layout.setOnClickListener(this);
        test_grid_refresh_layout.setOnClickListener(this);
        test_list_refresh_layout.setOnClickListener(this);
        test_list_material_layout.setOnClickListener(this);
        pickerView.setOnClickListener(this);
        xc_id_imageloader.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.test_point_line) {
            UtilActivity.myStartActivity(this, LinePointActivityGC.class);

        } else if (id == R.id.test_scan) {
            UtilActivity.myStartActivity(this, ScanActivity.class);
        } else if (id == R.id.test_http) {
            UtilActivity.myStartActivity(this, HttpActivity.class);
        } else if (id == R.id.test_pop) {
            UtilActivity.myStartActivity(this, PopActivity.class);
        } else if (id == R.id.test_webview) {
            UtilActivity.myStartActivity(this, WebActivity.class);

        } else if (id == R.id.test_viewpager) {
            UtilActivity.myStartActivity(this, ViewPagerActivity.class);

        } else if (id == R.id.test_move_block) {
            UtilActivity.myStartActivity(this, MoveBlockActivity.class);

        } else if (id == R.id.test_camare) {
            UtilActivity.myStartActivity(this, CamareActivity.class);

        } else if (id == R.id.test_search) {
            UtilActivity.myStartActivity(this, SearchActivity.class);

        } else if (id == R.id.test_scroll) {
            UtilActivity.myStartActivity(this, ScrollActivity.class);

        } else if (id == R.id.test_contacts) {
            UtilActivity.myStartActivity(this, ContactsActivity.class);

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
            UtilActivity.myStartActivity(this, ListActivity.class);

        } else if (id == R.id.test_grid_fragment) {
            UtilActivity.myStartActivity(this, GridActivity.class);

        } else if (id == R.id.test_sliding_menu) {
            UtilActivity.myStartActivity(this, SlidingMenuActivity.class);

        } else if (id == R.id.test_sliding_menu2) {
            UtilActivity.myStartActivity(this, SlidingMenuActivity2.class);

        } else if (id == R.id.test_viewpager_indicator) {
            startActivity(new Intent(this, ListSamples.class));

        } else if (id == R.id.test_http_download) {
            UtilActivity.myStartActivity(this, HttpDownLoadActivity.class);
        } else if (id == R.id.test_code) {
            UtilActivity.myStartActivity(this, CodeActivity.class);
        } else if (id == R.id.xc_id_test) {
            UtilActivity.myStartActivity(this, TextActivity.class);
        } else if (id == R.id.xc_id_anim) {
            UtilActivity.myStartActivity(this, AnimationActivity.class);
        } else if (id == R.id.test_dialog3) {
            UtilActivity.myStartActivity(this, DialogActivity3.class);
        } else if (id == R.id.test_baidumap) {
            UtilActivity.myStartActivity(this, MapActivity.class);
        } else if (id == R.id.test_switchbutton) {
            UtilActivity.myStartActivity(this, SwitchButtonActivity.class);
        } else if (id == R.id.test_circleprogress) {
            UtilActivity.myStartActivity(this, CircleProgressBarActivity.class);
        } else if (id == R.id.test_numberprogress) {
            UtilActivity.myStartActivity(this, LineProgressBarActivity.class);
        } else if (id == R.id.test_expandablelistview) {
            UtilActivity.myStartActivity(this, ExpandListActivity.class);
        } else if (id == R.id.test_round_imageview) {
            UtilActivity.myStartActivity(this, RoundImageViewActivity.class);
        } else if (id == R.id.test_viewpager_num) {
            UtilActivity.myStartActivity(this, ImagesZoomActivity.class);
        } else if (id == R.id.test_viewpager_slider) {
            UtilActivity.myStartActivity(this, ViewpagerSliderActivity.class);
        } else if (id == R.id.test_progress_view) {
            UtilActivity.myStartActivity(this, ProgressViewActivity.class);
        } else if (id == R.id.test_umengshare) {
            UtilActivity.myStartActivity(this, UmengShareActivity.class);
        } else if (id == R.id.test_grid_material_layout) {
            UtilActivity.myStartActivity(this, GridMaterialActivity.class);
        } else if (id == R.id.test_grid_refresh_layout) {
            UtilActivity.myStartActivity(this, GridRefreshActivity.class);
        } else if (id == R.id.test_list_refresh_layout) {
            UtilActivity.myStartActivity(this, ListRefreshActivity.class);
        } else if (id == R.id.test_list_material_layout) {
            UtilActivity.myStartActivity(this, ListMaterialActivity.class);
        } else if (id == R.id.test_pickerView) {
            UtilActivity.myStartActivity(this, PickerViewActiviy.class);
        } else if (id == R.id.xc_id_imageloader) {
            UtilActivity.myStartActivity(this, JSImageLoaderActivity.class);
        }

    }

    @Override
    public void onNetRefresh() {

    }
}
