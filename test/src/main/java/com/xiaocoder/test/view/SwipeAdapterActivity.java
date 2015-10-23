package com.xiaocoder.test.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaocoder.android.fw.general.view.swipelayoutadapter.SwipeLayoutAdapter;
import com.xiaocoder.middle.QlkActivity;
import com.xiaocoder.test.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeAdapterActivity extends QlkActivity {

    private SwipeLayoutAdapter mAdapter;
    private ListView mListView;
    private List<String> mData;
    private static int mCurrentIndex;
    private GridView mGridView;

    //插入数据
    public void insertData(int n) {
        for (int i = 0; i < n; i++) {
            mData.add("hello " + mCurrentIndex++);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_swipe_adapter);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {
        mData = new ArrayList<String>();
        insertData(30);
        mCurrentIndex = 0;
        mListView = (ListView) findViewById(R.id.listView);
        mGridView = (GridView) findViewById(R.id.gridview);
        mAdapter = new MyAdapater(this, R.layout.item_content, R.layout.item_action, mData);
        //mGridView.setAdapter(mAdapter);
        //如果要使用GridView，只需要注释掉mListView.setAdapter(mAdapter);去掉上方的注释
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void listeners() {

    }

    @Override
    public void onNetRefresh() {

    }

    //适配器
    class MyAdapater extends SwipeLayoutAdapter<String> {
        private List<String> _data;

        public MyAdapater(Activity context, int contentViewResourceId, int actionViewResourceId, List<String> objects) {
            super(context, contentViewResourceId, actionViewResourceId, objects);
            _data = objects;
        }

        //实现setContentView方法
        @Override
        public void setContentView(View contentView, int position, HorizontalScrollView parent) {
            TextView tv = (TextView) contentView.findViewById(R.id.tv);
            tv.setText(_data.get(position));
        }

        //实现setActionView方法
        @Override
        public void setActionView(View actionView, final int position, final HorizontalScrollView parent) {

            actionView.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.scrollTo(0, 0);
                    _data.remove(position);
                    notifyDataSetChanged();
                }
            });

            actionView.findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SwipeAdapterActivity.this, "star item - " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
