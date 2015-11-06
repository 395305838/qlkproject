package com.xiaocoder.test.contacts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.views.fragment.XCTitleCommonFragment;
import com.xiaocoder.android.fw.general.model.XCContactModel;
import com.xiaocoder.android.fw.general.util.UtilAbsListStyle;
import com.xiaocoder.android.fw.general.util.UtilContacts;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.util.List;

public class ContactsActivity extends MActivity {
    ListView contacts_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_contacts);
        super.onCreate(savedInstanceState);

    }

    class ContactsAdapter extends XCBaseAdapter<XCContactModel> {

        public ContactsAdapter(Context context, List<XCContactModel> list) {
            super(context, list);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            bean = list.get(position);

            ContactViewHolder holder = null;

            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_contact_item, null);
                holder = new ContactViewHolder();
                holder.textview = (TextView) convertView.findViewById(R.id.contact_item);
                holder.imageview = (ImageView) convertView.findViewById(R.id.contact_imageview);
                convertView.setTag(holder);

            } else {
                holder = (ContactViewHolder) convertView.getTag();
            }

            // 获取和设置控件的显示值
            holder.textview.setText(bean.name + "--" + bean.email + "--" + bean.phone_number);
            XCApp.displayImage("http://www.baidu.com/img/bdlogo.png", holder.imageview);

            return convertView;

        }

        class ContactViewHolder {
            TextView textview;
            ImageView imageview;
        }
    }

    @Override
    public void initWidgets() {
        // 初始化控件

        XCTitleCommonFragment title = new XCTitleCommonFragment();
        title.setTitleLeft(false, "");
        title.setTitleCenter(true, "通讯录");
        title.setColorLayout(0xff00cccc);
        addFragment(R.id.xc_id_model_titlebar, title);
        // listview
        contacts_listview = getViewById(R.id.contacts_list);
        UtilAbsListStyle.setListViewStyle(contacts_listview, null, 1, false);

        // 获取联系人
        List<XCContactModel> list = UtilContacts.getContacts(this);
        XCApp.i(list.toString());

        // 创建adapter
        ContactsAdapter adpater = new ContactsAdapter(this, list);
        // 设置adapter
        contacts_listview.setAdapter(adpater);
    }

    @Override
    public void listeners() {

    }

}
