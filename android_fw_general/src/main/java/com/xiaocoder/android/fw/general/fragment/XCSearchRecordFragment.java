/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.base.XCConfig;
import com.xiaocoder.android.fw.general.base.XCBaseFragment;
import com.xiaocoder.android.fw.general.db.XCDaoFactory;
import com.xiaocoder.android.fw.general.db.XCIDao;
import com.xiaocoder.android.fw.general.model.XCSearchRecordModel;
import com.xiaocoder.android.fw.general.util.UtilAbsListStyle;
import com.xiaocoder.android.fw.general.view.XCKeyBoardLayout;
import com.xiaocoder.android_fw_general.R;

import java.util.List;

/*
 * 搜索记录界面
 */
public class XCSearchRecordFragment extends XCBaseFragment implements AdapterView.OnItemClickListener {

    ListView xc_id_fragment_search_record_listview;
    Button xc_id_fragment_search_record_clear_button;
    // 键盘中的 关闭键盘按钮点击时 , 该layout会调用onSizeChanged方法, 这里可以监听 由 记录界面的显示状态 -->
    // 记录界面不显示状态
    XCKeyBoardLayout xc_id_fragment_search_record_keyboard_layout;

    XCIDao<XCSearchRecordModel> dao;
    SearchRecordAdapter adapter;
    TextView xc_id_fragment_search_record_close;

    int item_layout_id;

    public void setItem_layout_id(int item_layout_id) {
        this.item_layout_id = item_layout_id;
    }

    // 这个监听器 会在 KeyBoardLayout 的 onSizeChanged的方法中调用
    OnKeyBoardStatusListener keyBoardStatusListener;

    public interface OnKeyBoardStatusListener {
        void onStatusChange(boolean is_key_board_show);
    }

    // 该方法是给activity中控制该fragment用的
    public void setOnKeyBoardStatusListener(OnKeyBoardStatusListener keyBoardStatusListener) {
        this.keyBoardStatusListener = keyBoardStatusListener;
    }

    OnRecordItemClickListener onRecordItemClickListener;

    public interface OnRecordItemClickListener {
        void onRecordItemClickListener(XCSearchRecordModel model, String key_word, int position);
    }

    public void setOnRecordItemClickListener(OnRecordItemClickListener onRecordItemClickListener) {
        this.onRecordItemClickListener = onRecordItemClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (item_layout_id != 0) {
            return init(inflater, item_layout_id);
        } else {
            return init(inflater, R.layout.xc_l_fragment_search_record);
        }
    }

    public interface OnCloseClickListener {
        void close();
    }

    OnCloseClickListener OnCloseClickListener;

    public void setOnCloseClickListener(OnCloseClickListener OnCloseClickListener) {
        this.OnCloseClickListener = OnCloseClickListener;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xc_id_fragment_search_record_clear_button) {
            if ("清空所有历史记录".equals(xc_id_fragment_search_record_clear_button.getText())) {
                dao.deleteAll();
                // 查询数据库记录
                List<XCSearchRecordModel> searchRecordBeans = dao.queryAll();
                if (searchRecordBeans != null && searchRecordBeans.size() > 0) {
                    xc_id_fragment_search_record_clear_button.setText("清空所有历史记录");
                } else {
                    xc_id_fragment_search_record_clear_button.setText("暂无历史记录");
                }
                adapter.update(searchRecordBeans);
                adapter.notifyDataSetChanged();
            }
        } else if (id == R.id.xc_id_fragment_search_record_close) {
            if (OnCloseClickListener != null) {
                OnCloseClickListener.close();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        XCSearchRecordModel bean = (XCSearchRecordModel) parent.getItemAtPosition(position);
        if (onRecordItemClickListener != null) {
            onRecordItemClickListener.onRecordItemClickListener(bean, bean.getKey_word(), position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    public void update() {
        // 查询数据库记录 , 恢复查询历史记录
        List<XCSearchRecordModel> searchRecordBeans = dao.queryAll();

        if (searchRecordBeans != null && searchRecordBeans.size() > 0) {
            xc_id_fragment_search_record_clear_button.setText("清空所有历史记录");
        } else {
            xc_id_fragment_search_record_clear_button.setText("暂无历史记录");
        }

        adapter.update(searchRecordBeans);
        adapter.notifyDataSetChanged();
        xc_id_fragment_search_record_listview.setSelection(0);
    }

    class SearchRecordAdapter extends XCBaseAdapter<XCSearchRecordModel> implements View.OnClickListener {

        public View getView(int position, View convertView, ViewGroup parent) {

            bean = list.get(position);

            SearchRecoderViewHolder holder = null;

            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.xc_l_adapter_fragment_search_recoder_item, null);
                holder = new SearchRecoderViewHolder();
                holder.xc_id_adapter_search_recoder_item_textview = (TextView) convertView.findViewById(R.id.xc_id_adapter_search_recoder_item_textview);
                holder.xc_id_adapter_search_recoder_item_delete = (ImageView) convertView.findViewById(R.id.xc_id_adapter_search_recoder_item_delete);
                holder.xc_id_adapter_search_recoder_item_delete.setOnClickListener(SearchRecordAdapter.this);
                convertView.setTag(holder);

            } else {
                holder = (SearchRecoderViewHolder) convertView.getTag();
            }
            holder.xc_id_adapter_search_recoder_item_textview.setText(bean.getKey_word());
            holder.xc_id_adapter_search_recoder_item_delete.setTag(position);
            // 获取和设置控件的显示值

            return convertView;

        }

        public SearchRecordAdapter(Context context, List<XCSearchRecordModel> list) {
            super(context, list);
        }

        class SearchRecoderViewHolder {
            // 添加字段
            TextView xc_id_adapter_search_recoder_item_textview;
            ImageView xc_id_adapter_search_recoder_item_delete;
        }

        @Override
        public void onClick(View view) {
            Integer position = (Integer) view.getTag();
            dShortToast(position + "");
            dao.delete_unique(list.get(position).getTime());
            XCSearchRecordFragment.this.update();
        }
    }

    @Override
    public void initWidgets() {
        xc_id_fragment_search_record_clear_button = getViewById(R.id.xc_id_fragment_search_record_clear_button);
        xc_id_fragment_search_record_listview = getViewById(R.id.xc_id_fragment_search_record_listview);
        xc_id_fragment_search_record_keyboard_layout = getViewById(R.id.xc_id_fragment_search_record_keyboard_layout);
        xc_id_fragment_search_record_close = getViewById(R.id.xc_id_fragment_search_record_close);

        dao = XCDaoFactory.getDaoInstance(getActivity(), XCConfig.XC_SEARCH_RECODER_DAO_CLASS);
        adapter = new SearchRecordAdapter(getActivity(), null);
        UtilAbsListStyle.setListViewStyle(xc_id_fragment_search_record_listview, null, 0, false);
        xc_id_fragment_search_record_listview.setAdapter(adapter);
    }

    @Override
    public void listeners() {
        xc_id_fragment_search_record_clear_button.setOnClickListener(this);
        xc_id_fragment_search_record_listview.setOnItemClickListener(this);
        xc_id_fragment_search_record_close.setOnClickListener(this);
        xc_id_fragment_search_record_keyboard_layout.setOnResizeListener(new XCKeyBoardLayout.OnResizeListener() {
            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                if (keyBoardStatusListener != null && 0 != oldw && 0 != oldh) {
                    if (h < oldh) {
                        keyBoardStatusListener.onStatusChange(true);
                    } else {
                        keyBoardStatusListener.onStatusChange(false);
                    }
                }
            }
        });
    }
}
