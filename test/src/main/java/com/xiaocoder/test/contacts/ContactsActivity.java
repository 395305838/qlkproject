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
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.base.XCBaseConfig;
import com.xiaocoder.android.fw.general.fragment.XCTitleJustFragment;
import com.xiaocoder.android.fw.general.helper.XCContactHelper;
import com.xiaocoder.android.fw.general.model.XCContactModel;
import com.xiaocoder.test.R;
import com.xiaocoder.test.buffer.QlkBaseActivity;

import java.util.List;

/*
 复制这里：activity在注册清单文件的配置 
 <activity
 android:name="com.xiaocoder.android.ContactsActivity"
 android:screenOrientation="portrait"
 android:windowSoftInputMode="adjustResize|stateHidden" >
 </activity>*/
public class ContactsActivity extends QlkBaseActivity {
    ListView contacts_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_contacts);
        super.onCreate(savedInstanceState);

    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
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
            displayImage("http://www.baidu.com/img/bdlogo.png", holder.imageview);

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
        XCTitleJustFragment title = new XCTitleJustFragment();
        title.setTitle("通讯录");
        addFragment(R.id.xc_id_model_titlebar, title);
        // listview
        contacts_listview = getViewById(R.id.contacts_list);
        setListViewStyle(contacts_listview, null, 1, false);

        // 获取联系人
        XCContactHelper contact_helper = new XCContactHelper(this);
        List<XCContactModel> list = contact_helper.getContacts();
        XCApplication.printi(list.toString());

        // 创建adapter
        ContactsAdapter adpater = new ContactsAdapter(this, list);
        // 设置adapter
        contacts_listview.setAdapter(adpater);
    }

    @Override
    public void listeners() {

    }

}
