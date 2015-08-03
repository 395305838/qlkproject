package com.xiaocoder.test.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.fragment.XCSearchLetterFragment;
import com.xiaocoder.android.fw.general.fragment.XCSearchLetterFragment.OnInnerItemClickListener;
import com.xiaocoder.android.fw.general.fragment.XCSearchLetterFragment.OnOutTitleClickListene;
import com.xiaocoder.android.fw.general.fragment.XCSearchRecordFragment;
import com.xiaocoder.android.fw.general.fragment.XCTitleSearchFragment;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkActivity;
import com.xiaocoder.test.buffer.QlkConfig;
import com.xiaocoder.test.buffer.QlkResponseHandler;

import org.apache.http.Header;

/*
 * 这个例子的恢复历史记录数据在onStart()方法中
 */
public class SearchActivity extends QlkActivity {

    // 带有字母的两层listview列表
    XCSearchLetterFragment letter_fragment;
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
        title_fragment.setDbParams(QlkConfig.DB_NAME, QlkConfig.DB_VERSION, QlkConfig.DB_TABLE_NAME_SEARCH_1,
                new String[]{QlkConfig.DB_TABLE_SQL_SEARCH_1, QlkConfig.DB_TABLE_SQL_SEARCH_2, QlkConfig.DB_TABLE_SQL_SEARCH_3}
        );
        addFragment(R.id.xc_id_model_titlebar, title_fragment);

        letter_fragment = new XCSearchLetterFragment();
        addFragment(R.id.xc_id_model_content, letter_fragment);

        request();
    }

    @Override
    public void listeners() {
        // 点击edittext输入框 , 则弹出键盘和历史记录的背景
        title_fragment.setOnClicklistener(new XCTitleSearchFragment.OnEditTextClickedListener() {
            @Override
            public void clicked() {
                // 点击显示记录 , clicked()仅监听 从隐藏状态 -- > 显示状态

                // 为空则创建并设置监听 , record_fragment里面的监听器可以监听键盘的显示到隐藏的状态
                if (record_fragment == null) {
                    record_fragment = new XCSearchRecordFragment();
                    record_fragment.setDbParams(QlkConfig.DB_NAME, QlkConfig.DB_VERSION, QlkConfig.DB_TABLE_NAME_SEARCH_1,
                            new String[]{QlkConfig.DB_TABLE_SQL_SEARCH_1, QlkConfig.DB_TABLE_SQL_SEARCH_2, QlkConfig.DB_TABLE_SQL_SEARCH_3}
                    );
                    // 点击键盘中的隐藏键盘按钮
                    record_fragment.setOnKeyBoardStatusListener(new XCSearchRecordFragment.OnKeyBoardStatusListener() {
                        @Override
                        public void onStatusChange(boolean is_key_board_show) {
                            // onStatusChange()仅监听 从显示状态 -- > 隐藏状态 , 关闭记录页面
                            if (!is_key_board_show) {
                                hideFragment(record_fragment);
                                showFragment(letter_fragment);
                            }
                            shortToast("change");
                        }
                    });
                    hideFragment(letter_fragment);
                    addFragment(R.id.xc_id_model_content, record_fragment);
                    return;
                }
                // 不为空 , 则显示记录页面 ,隐藏搜索页面
                if (record_fragment.isHidden()) {
                    showFragment(record_fragment);
                    hideFragment(letter_fragment);
                }
            }
        });

        // 点击键盘的搜索按钮 , 会关闭键盘, 然后开启一个搜索结果的activity
        title_fragment.setOnSearchlistener(new XCTitleSearchFragment.OnKeyBoardSearchListener() {
            @Override
            public void searchKeyDown(String key_word) {
                XCApplication.shortToast("点击了");
            }
        });

        letter_fragment.setOnOutTitleClickListene(new OnOutTitleClickListene() {

            @Override
            public void onOutTitleClickListener(int position) {
                shortToast(position + "");
            }
        });
    }

    public void request() {
        XCHttpAsyn.getAsyn(true, this, "http://18620909598.sinaapp.com/pinpailiebiao.json",
                new RequestParams(), new QlkResponseHandler<XCJsonBean>(this, XCJsonBean.class) {

                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);
                        if (result_boolean) {
                            letter_fragment.setOnInnerItemClickListener(new OnInnerItemClickListener() {

                                @Override
                                public void onInnerItemClick(AdapterView<?> parent, View view, int position, long id) {

                                }
                            });
                            letter_fragment.setData(result_bean);
                        }
                    }
                });
    }

}
