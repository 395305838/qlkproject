package com.xiaocoder.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.helper.XCTimeHelper;
import com.xiaocoder.android.fw.general.helper.XCTimeHelper.CustomTimer;
import com.xiaocoder.android.fw.general.pop.XCPhotoPopupWindow;
import com.xiaocoder.android.fw.general.pop.XCPhotoPopupWindow.onPhotoPopupItemClickListener;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.test.anim.AnimationActivity;
import com.xiaocoder.test.contacts.ContactsActivity;
import com.xiaocoder.test.dialogs.DialogActivity3;
import com.xiaocoder.test.fragment.CamareActivity;
import com.xiaocoder.test.fragment.MoveBlockActivity;
import com.xiaocoder.test.fragment.SearchActivity;
import com.xiaocoder.test.fragment.ViewPagerActivity;
import com.xiaocoder.test.fragment.WebActivity;
import com.xiaocoder.test.http.HttpActivity;
import com.xiaocoder.test.http.HttpDownLoadActivity;
import com.xiaocoder.test.line_point.LinePointActivityGC;
import com.xiaocoder.test.list.GridActivity;
import com.xiaocoder.test.list.ListActivity;
import com.xiaocoder.test.pop.PopActivity;
import com.xiaocoder.test.scan.CodeActivity;
import com.xiaocoder.test.scan.ScanActivity;
import com.xiaocoder.test.slidingmenu.SlidingMenuActivity;
import com.xiaocoder.test.slidingmenu.SlidingMenuActivity2;
import com.xiaocoder.test.view.ScrollActivity;
import com.xiaocoder.test.viewpagerindicator.ListSamples;


public class MainActivity extends Activity implements OnClickListener {

    public static String TEST_HOST = "10.7.30.121";
    Button test_dialog;
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
    Button test_dialog2;
    Button test_http_download;
    Button test_code;

    Button xc_id_test;
    Button xc_id_anim;
    Button test_dialog3;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test_dialog3 = (Button) findViewById(R.id.test_dialog3);
        test_dialog3.setOnClickListener(this);
        test_code = (Button) findViewById(R.id.test_code);
        test_code.setOnClickListener(this);

        XCTimeHelper timeHelper = new XCTimeHelper(1000000, new CustomTimer() {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        });
        // timeHelper.start();

        test_point_line = (Button) findViewById(R.id.test_point_line);
        test_point_line.setOnClickListener(this);

        test_scan = (Button) findViewById(R.id.test_scan);
        test_scan.setOnClickListener(this);

        xc_id_test = (Button) findViewById(R.id.xc_id_test);
        xc_id_test.setOnClickListener(this);

        test_http = (Button) findViewById(R.id.test_http);
        test_http.setOnClickListener(this);

        test_http_download = (Button) findViewById(R.id.test_http_download);
        test_http_download.setOnClickListener(this);

        test_pop = (Button) findViewById(R.id.test_pop);
        test_pop.setOnClickListener(this);

        test_webview = (Button) findViewById(R.id.test_webview);
        test_webview.setOnClickListener(this);

        test_viewpager = (Button) findViewById(R.id.test_viewpager);
        test_viewpager.setOnClickListener(this);

        test_move_block = (Button) findViewById(R.id.test_move_block);
        test_move_block.setOnClickListener(this);

        test_camare = (Button) findViewById(R.id.test_camare);
        test_camare.setOnClickListener(this);
        UtilString.setLightAppendString("你好:123", test_camare,"#ff00cccc");

        test_search = (Button) findViewById(R.id.test_search);
        test_search.setOnClickListener(this);

        test_scroll = (Button) findViewById(R.id.test_scroll);
        test_scroll.setOnClickListener(this);

        test_pop2 = (Button) findViewById(R.id.test_pop2);
        test_pop2.setOnClickListener(this);

        test_contacts = (Button) findViewById(R.id.test_contacts);
        test_contacts.setOnClickListener(this);

        test_chat_bottom = (Button) findViewById(R.id.test_chat_bottom);
        test_chat_bottom.setOnClickListener(this);

        test_list_fragment = (Button) findViewById(R.id.test_list_fragment);
        test_list_fragment.setOnClickListener(this);

        test_grid_fragment = (Button) findViewById(R.id.test_grid_fragment);
        test_grid_fragment.setOnClickListener(this);

        test_sliding_menu = (Button) findViewById(R.id.test_sliding_menu);
        test_sliding_menu.setOnClickListener(this);

        test_sliding_menu2 = (Button) findViewById(R.id.test_sliding_menu2);
        test_sliding_menu2.setOnClickListener(this);

        test_viewpager_indicator = (Button) findViewById(R.id.test_viewpager_indicator);
        test_viewpager_indicator.setOnClickListener(this);

        test_anim = (Button) findViewById(R.id.test_anim);
        test_anim.setOnClickListener(this);

        xc_id_anim = (Button) findViewById(R.id.xc_id_anim);
        xc_id_anim.setOnClickListener(this);

        // test_recodervoice = (XCRecordVoiceButton)
        // findViewById(R.id.test_recodervoice);
        // test_recodervoice.setOnRecordVoiceSuccessListener(new
        // OnRecordVoiceSuccessListener() {
        //
        // @Override
        // public void onRecordVoiceSuccessListener(File file) {
        // XCApplication.base_log.debugShortToast(file.toString());
        // }
        // });

        try {
            XCApplication.getBase_log().writeLog2File("123", false);
            XCApplication.getBase_log().writeLog2File("345", true);
            XCApplication.getBase_log().writeLog2File("678", true);
//            int i = 1 / 0;
        } catch (Exception e) {
            XCApplication.printe(this, "--oncreate()--", e);
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.test_point_line) {
            startActivity(new Intent(this, LinePointActivityGC.class));

        } else if (id == R.id.test_scan) {
            startActivity(new Intent(this, ScanActivity.class));
        } else if (id == R.id.test_http) {
            startActivity(new Intent(this, HttpActivity.class));
        } else if (id == R.id.test_pop) {
            startActivity(new Intent(this, PopActivity.class));
        } else if (id == R.id.test_webview) {
            startActivity(new Intent(this, WebActivity.class));

        } else if (id == R.id.test_viewpager) {
            startActivity(new Intent(this, ViewPagerActivity.class));

        } else if (id == R.id.test_move_block) {
            startActivity(new Intent(this, MoveBlockActivity.class));

        } else if (id == R.id.test_camare) {
            startActivity(new Intent(this, CamareActivity.class));

        } else if (id == R.id.test_search) {
            startActivity(new Intent(this, SearchActivity.class));

        } else if (id == R.id.test_scroll) {
            startActivity(new Intent(this, ScrollActivity.class));

        } else if (id == R.id.test_contacts) {
            startActivity(new Intent(this, ContactsActivity.class));

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
            startActivity(new Intent(this, ListActivity.class));

        } else if (id == R.id.test_grid_fragment) {
            startActivity(new Intent(this, GridActivity.class));

        } else if (id == R.id.test_sliding_menu) {
            startActivity(new Intent(this, SlidingMenuActivity.class));

        } else if (id == R.id.test_sliding_menu2) {
            startActivity(new Intent(this, SlidingMenuActivity2.class));

        } else if (id == R.id.test_viewpager_indicator) {
            startActivity(new Intent(this, ListSamples.class));

        } else if (id == R.id.test_http_download) {
            startActivity(new Intent(this, HttpDownLoadActivity.class));
        } else if (id == R.id.test_code) {
            startActivity(new Intent(this, CodeActivity.class));
        } else if (id == R.id.xc_id_test) {
            startActivity(new Intent(this, TextActivity.class));
        } else if (id == R.id.xc_id_anim) {
            startActivity(new Intent(this, AnimationActivity.class));
        } else if (id == R.id.test_dialog3) {
            startActivity(new Intent(this, DialogActivity3.class));
        }

    }

}
