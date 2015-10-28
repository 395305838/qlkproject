package com.xiaocoder.test.http2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XLBaseAdapterExpand;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.fragment.XCTitleCommonFragment;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.view.XCSlideBar;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class ExpandListActivity extends MActivity {

    ExpandableListView test_expandablelistview;
    MyAdapter adapter;
    List<String> listParaent;
    List<List<String>> listChild;
    XCTitleCommonFragment title;

    TextView xc_id_search_slide_dialog;
    XCSlideBar xc_id_search_slide_slidebar;


    public TextView getTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, UtilScreen.dip2px(this, 60));
        TextView textView = new TextView(this);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(36, 0, 0, 0);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_expand_list);
        super.onCreate(savedInstanceState);

        initData();

    }


    public void initData() {

        listParaent = new ArrayList<String>();
        listParaent.add("A");
        listParaent.add("F");
        listParaent.add("I");
        listParaent.add("N");
        listParaent.add("Q");
        listParaent.add("V");


        listChild = new ArrayList<List<String>>();

        List<String> list1 = new ArrayList<String>();
        list1.add("苹果");
        list1.add("猕猴桃");
        list1.add("西瓜");
        list1.add("哈密瓜");
        list1.add("葡萄");
        List<String> list2 = new ArrayList<String>();
        list2.add("thinkpad");
        list2.add("联想");
        list2.add("苹果");
        list2.add("戴尔");
        list2.add("华硕");
        List<String> list3 = new ArrayList<String>();
        list3.add("篮球");
        list3.add("足球");
        list3.add("乒乓球");
        list3.add("羽毛球");
        list3.add("排球");
        List<String> list4 = new ArrayList<String>();
        list4.add("中国");
        list4.add("瑞士");
        list4.add("芬兰");
        list4.add("德国");
        list4.add("法国");
        List<String> list5 = new ArrayList<String>();
        list5.add("华为");
        list5.add("小米");
        list5.add("联想");
        list5.add("酷派");
        list5.add("三星");
        List<String> list6 = new ArrayList<String>();
        list6.add("格力");
        list6.add("美的");
        list6.add("九阳");
        list6.add("苏泊尔");
        list6.add("格兰仕");

        listChild.add(list1);
        listChild.add(list2);
        listChild.add(list3);
        listChild.add(list4);
        listChild.add(list5);
        listChild.add(list6);

        adapter = new MyAdapter(this, listChild, listParaent);
        test_expandablelistview.setAdapter(adapter);

        allExpand(test_expandablelistview);

    }

    /**
     * 全部展开
     *
     * @param view
     */
    public void allExpand(ExpandableListView view) {
        // 如果是用view.getViewCount(),则在有headView与footView  会越界异常
        int count = view.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < count; i++) {
            view.expandGroup(i);
        }
    }

    /**
     * 返回true，不会收缩(点击效果还在)，即在调用allExpand后， 再设置该监听，即可达到全部展示不会收缩的效果
     */
    public void expandAllTime() {
        test_expandablelistview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                XCApp.shortToast(groupPosition + "");
                return true;
            }
        });
    }

    /**
     * 一个点击展开，别的都关闭
     */
    public void closeOther(final ExpandableListView view) {
        view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < view.getCount(); i++) {
                    if (groupPosition != i) {
                        view.collapseGroup(i);
                    }
                }
            }
        });
    }


    @Override
    public void initWidgets() {

        test_expandablelistview = getViewById(R.id.test_expandablelistview);
        xc_id_search_slide_dialog = getViewById(R.id.xc_id_search_slide_dialog);
        xc_id_search_slide_slidebar = getViewById(R.id.xc_id_search_slide_slidebar);

        xc_id_search_slide_slidebar.setTextView(xc_id_search_slide_dialog);


        test_expandablelistview.setGroupIndicator(null); // 删除箭头

        title = new XCTitleCommonFragment();
        title.setTitleCenter(true, "haha");
        title.setColorLayout(0xffff0000);
        addFragment(R.id.xc_id_model_titlebar, title);
    }

    @Override
    public void listeners() {
        expandAllTime();

        xc_id_search_slide_slidebar.setOnTouchingLetterChangedListener(new XCSlideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                Integer position = adapter.getPositionFromLetter(s);
                XCApp.dShortToast(position + "");
                if (position != null) {
                    test_expandablelistview.setSelection(position);
                }
            }
        });
    }

    public class MyAdapter extends XLBaseAdapterExpand<String, String> {

        LinkedHashMap<String, Integer> sava_letter_position_map = new LinkedHashMap<String, Integer>();

        @Override
        public void update(List<List<String>> listChild, List<String> listParaent) {
            super.update(listChild, listParaent);
            sava_letter_position_map.clear();
            if (listParaent != null) {
                initLettersPosition(listParaent, listChild);
            }
        }

        // 获取字母的位置
        public Integer getPositionFromLetter(String letter) {
            return sava_letter_position_map.get(letter);
        }

        // 保存字母的位置
        public LinkedHashMap<String, Integer> initLettersPosition(List<String> listParent, List<List<String>> listChild) {
            String record_last_letter = null;
            for (int i = 0; i < listParent.size(); i++) {
                String letter = listParent.get(i);
                if (!letter.equals(record_last_letter)) {
                    if (listChild.get(i) != null) {
                        if (i == 0) {
                            sava_letter_position_map.put(letter, 0);
                        } else {
                            // position等于上一个字母的位置+改字母的子项的数量
                            int position = 1 + sava_letter_position_map.get(record_last_letter) + listChild.get(i - 1).size();
                            sava_letter_position_map.put(letter, position);
                        }
                    }
                }
                record_last_letter = letter;
            }
            return sava_letter_position_map;
        }

        public MyAdapter(Context context, List listChild, List listParaent) {
            super(context, listChild, listParaent);
            update(listChild, listParaent);
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getTextView();
            }
            if (viewGroup != null) {
                XCApp.i(XCConfig.TAG_TEST, "getGroupView()---" + viewGroup.toString());
            }
            ((TextView) view).setText(getGroup(i).toString());
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getTextView();
            }
            if (viewGroup != null) {
                XCApp.i(XCConfig.TAG_TEST, "getChildView()---" + viewGroup.toString());
            }
            ((TextView) view).setText(getChild(groupPosition, childPosition).toString());
            return view;
        }

    }

    @Override
    public void onNetRefresh() {

    }
}
