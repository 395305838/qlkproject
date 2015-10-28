package com.xiaocoder.test.fragment;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.fragment.XCSearchRecordFragment;
import com.xiaocoder.android.fw.general.fragment.XCTitleSearchFragment;
import com.xiaocoder.android.fw.general.model.XCSearchRecordModel;
import com.xiaocoder.android.fw.general.util.UtilActivity;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.middle.db.MDb;
import com.xiaocoder.middle.db.MDbHelper;
import com.xiaocoder.test.R;

/**
 * 多个界面都有搜索时，每张表记录的信息类别是不一样的
 */
public class SearchActivity2 extends MActivity {

    // 搜索历史界面
    XCSearchRecordFragment record_fragment;
    // 搜索title
    XCTitleSearchFragment title_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetRefresh() {
    }

    @Override
    public void initWidgets() {
        title_fragment = new XCTitleSearchFragment();
        title_fragment.setDbParams(MDb.DB_TABLE_SEARCH_RECODER_2, MDbHelper.class, MDb.DB_SEARCH_RECODER, MDb.DB_VERSION_SEARCH_RECODER,
                new String[]{MDb.DB_SQL_SEARCH_RECODER_1, MDb.DB_SQL_SEARCH_RECODER_2, MDb.DB_SQL_SEARCH_RECODER_3}
        );
        addFragment(R.id.xc_id_model_titlebar, title_fragment);

    }

    @Override
    public void listeners() {
        // 点击edittext输入框 , 则弹出键盘和历史记录的背景
        title_fragment.setOnEditTextClicklistener(new XCTitleSearchFragment.OnEditTextClickedListener() {
            @Override
            public void clicked() {
                // 点击显示记录 , clicked()仅监听 从隐藏状态 -- > 显示状态

                // 为空则创建并设置监听 , record_fragment里面的监听器可以监听键盘的显示到隐藏的状态
                if (record_fragment == null) {
                    record_fragment = new XCSearchRecordFragment();
                    record_fragment.setDbParams(MDb.DB_TABLE_SEARCH_RECODER_2, MDbHelper.class, MDb.DB_SEARCH_RECODER, MDb.DB_VERSION_SEARCH_RECODER,
                            new String[]{MDb.DB_SQL_SEARCH_RECODER_1, MDb.DB_SQL_SEARCH_RECODER_2, MDb.DB_SQL_SEARCH_RECODER_3}
                    );

                    // 点击键盘中的隐藏键盘按钮
                    record_fragment.setOnKeyBoardStatusListener(new XCSearchRecordFragment.OnKeyBoardStatusListener() {
                        @Override
                        public void onStatusChange(boolean is_key_board_show) {
                            // onStatusChange()仅监听 从显示状态 -- > 隐藏状态 , 关闭记录页面
                            if (!is_key_board_show) {
                                hideFragment(record_fragment);
                            }
                            XCApp.shortToast("change");
                        }
                    });

                    record_fragment.setOnRecordItemClickListener(new XCSearchRecordFragment.OnRecordItemClickListener() {
                        @Override
                        public void onRecordItemClickListener(XCSearchRecordModel model, String key_word, int position) {
                            XCApp.shortToast(key_word);
                            UtilActivity.myStartActivity(SearchActivity2.this, WebActivity.class);
                        }
                    });
                    addFragment(R.id.xc_id_model_content, record_fragment);
                    return;
                }
                // 不为空 , 则显示记录页面 ,隐藏搜索页面
                if (record_fragment.isHidden()) {
                    showFragment(record_fragment);
                }
            }
        });

        // 点击键盘的搜索按钮 , 会关闭键盘, 然后开启一个搜索结果的activity
        title_fragment.setOnPressSearchlistener(new XCTitleSearchFragment.OnKeyBoardSearchListener() {
            @Override
            public void searchKeyDown(String key_word) {
                XCApp.shortToast(key_word);
                UtilActivity.myStartActivity(SearchActivity2.this, WebActivity.class);
            }
        });
    }


}
