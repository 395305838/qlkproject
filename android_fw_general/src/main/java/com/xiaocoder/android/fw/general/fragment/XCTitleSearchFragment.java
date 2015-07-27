/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.xiaocoder.android.fw.general.base.XCConfig;
import com.xiaocoder.android.fw.general.base.XCTitleFragment;
import com.xiaocoder.android.fw.general.db.helper.XCDbHelper;
import com.xiaocoder.android.fw.general.db.impl.XCSearchDao;
import com.xiaocoder.android.fw.general.model.XCSearchRecordModel;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.android.fw.general.view.XCClearEditText;
import com.xiaocoder.android_fw_general.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaocoder
 * @Description: 可以搜索的title
 * @date 2015-1-16 下午1:50:08
 */
public class XCTitleSearchFragment extends XCTitleFragment {
    XCClearEditText xc_id_fragment_search_edittext;
    TextView xc_id_fragment_search_cancle;
    XCSearchDao dao;
    boolean isClickeble = true;

    public void setIsClickble(boolean isClickeble) {
        this.isClickeble = isClickeble;
    }

    // 点击取消按钮时（有时可能不是取消，而是搜索）
    public interface OnClickCancleButtonListener {
        void clicked(String key_word);
    }

    public void setOnClickCancleButtonListener(OnClickCancleButtonListener canclelistener) {
        this.canclelistener = canclelistener;
    }

    OnClickCancleButtonListener canclelistener;

    // 当eidttext整个被点击的时候
    public interface OnEditTextClickedListener {
        void clicked();
    }

    // 当键盘的搜索的按钮点击时
    public interface OnKeyBoardSearchListener {
        void searchKeyDown(String key_word);
    }

    public void setOnClicklistener(OnEditTextClickedListener clicklistener) {
        this.clicklistener = clicklistener;
    }

    public void setOnSearchlistener(OnKeyBoardSearchListener searchlistener) {
        this.searchlistener = searchlistener;
    }

    OnEditTextClickedListener clicklistener;
    OnKeyBoardSearchListener searchlistener;

    int item_layout_id;

    public void setItem_layout_id(int item_layout_id) {
        this.item_layout_id = item_layout_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (item_layout_id == 0) {
            return init(inflater, R.layout.xc_l_fragment_bar_title_search_history);
        } else {
            return init(inflater, item_layout_id);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.xc_id_fragment_search_cancle) {
            if (getActivity().getCurrentFocus() != null) {
                if (getActivity().getCurrentFocus().getWindowToken() != null) {
                    // 先隐藏键盘
                    ((InputMethodManager) xc_id_fragment_search_edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            if (canclelistener != null) {
                String keyword = xc_id_fragment_search_edittext.getText().toString().trim();
                if ("".equals(keyword)) {
                    shortToast("关键字不能为空");
                    return;
                }

                if ("搜索".equals(xc_id_fragment_search_cancle.getText().toString().trim())) {
                    save(keyword);
                }
                canclelistener.clicked(keyword);
            } else {
                myFinish();
            }
        } else if (id == R.id.xc_id_fragment_search_edittext) {
            if (clicklistener != null) {
                clicklistener.clicked();
            }
        }
    }

    public void save(String keyword) {

        if (UtilString.isBlank(keyword)) {
            return;
        }

        List<XCSearchRecordModel> xcSearchRecordModels1 = dao.queryAll(XCSearchDao.SORT_DESC);
        boolean is_exist = false;
        for (XCSearchRecordModel model : xcSearchRecordModels1) {
            if (keyword.equals(model.getKey_word())) {
                is_exist = true;
                break;
            }
        }

        // 记录存入数据库
        if (!is_exist) {
            dao.insert(new XCSearchRecordModel(keyword, System.currentTimeMillis() + ""));
        }

        int count = dao.queryCount();

        if (count > 10) {
            List<XCSearchRecordModel> xcSearchRecordModels = dao.queryAll(XCSearchDao.SORT_DESC);
            for (int i = 10; i < count; i++) {
                XCSearchRecordModel model = xcSearchRecordModels.get(i);
                if (model != null) {
                    dao.delete_unique(model.getTime());
                }
            }
        }
    }

    String mDbName;
    int mVersion;
    String mTableName;
    String[] mSqls;

    public void setDbParams(String dbName, int version, String tabName, String[] sqls) {
        mDbName = dbName;
        mVersion = version;
        mTableName = tabName;
        mSqls = sqls;
    }


    @Override
    public void initWidgets() {
        xc_id_fragment_search_edittext = getViewById(R.id.xc_id_fragment_search_edittext);
        if (!isClickeble) {
            xc_id_fragment_search_edittext.setClickable(false);
            xc_id_fragment_search_edittext.setFocusable(false);
        }
        xc_id_fragment_search_cancle = getViewById(R.id.xc_id_fragment_search_cancle);

        XCDbHelper helper = new XCDbHelper(getBaseActivity(), mDbName, mVersion, mSqls);
        dao = new XCSearchDao(getBaseActivity(), helper, mTableName);

        if (hint != null) {
            xc_id_fragment_search_edittext.setHint(hint);
        }
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    String hint;

    @Override
    public void listeners() {
        xc_id_fragment_search_cancle.setOnClickListener(this);
        xc_id_fragment_search_edittext.setOnClickListener(this);
        xc_id_fragment_search_edittext.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String keyword = xc_id_fragment_search_edittext.getText().toString().trim();
                    if ("".equals(keyword)) {
                        shortToast("关键字不能为空");
                        return false;
                    }
                    if (getActivity().getCurrentFocus() != null) {
                        if (getActivity().getCurrentFocus().getWindowToken() != null) {
                            // 先隐藏键盘
                            ((InputMethodManager) xc_id_fragment_search_edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    save(keyword);
                    // 进入搜索结果页面
                    if (searchlistener != null) {
                        searchlistener.searchKeyDown(keyword);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
