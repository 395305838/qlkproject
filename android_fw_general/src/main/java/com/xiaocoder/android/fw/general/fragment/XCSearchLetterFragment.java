/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.base.XCBaseFragment;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.util.UtilAbsListStyle;
import com.xiaocoder.android.fw.general.util.UtilImage;
import com.xiaocoder.android.fw.general.view.XCNoScrollGridView;
import com.xiaocoder.android.fw.general.view.XCSlideBar;
import com.xiaocoder.android.fw.general.view.XCSlideBar.OnTouchingLetterChangedListener;
import com.xiaocoder.android_fw_general.R;

/**
 * @author xiaocoder
 * @Description:带有字母的滑动的两层absistview的效果
 * @date 2015-1-16 下午1:50:46
 */
// 以 以下格式为例子 , 复用时 , 得重写json格式的解析字段
// {
// "sections": [
// {
// "index":"A",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// }
// ]
// }
/*
 * 用法 1 setOutItemLayout() 2 setInnerItemLayout() 3 setInnerOnItemListener()
 * 4重写setData()里的解析json的方法 5 重写yourholder() yourholder2() yourlogic()
 * yourlogic2();
 */

public class XCSearchLetterFragment extends XCBaseFragment {

    XCSlideBar xc_id_fragment_search_slide_slidebar;
    TextView xc_id_fragment_search_slide_dialog;
    ListView xc_id_fragment_search_slide_listview;
    OutSearchLetterAdapter out_adapter;

    ArrayList<XCJsonBean> out_jsons;

    // 第二个girdview的监听
    public interface OnInnerItemClickListener {
        void onInnerItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    OnInnerItemClickListener inner_listener;

    public void setOnInnerItemClickListener(OnInnerItemClickListener listener) {
        this.inner_listener = listener;
    }

    public interface OnOutTitleClickListene {
        void onOutTitleClickListener(int position);
    }

    OnOutTitleClickListene out_title_listnener;

    public void setOnOutTitleClickListene(OnOutTitleClickListene out_title_listnener) {
        this.out_title_listnener = out_title_listnener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_search_letter);
    }

    // 设置数据
    public void setData(XCJsonBean origin_bean) {
        out_jsons = (ArrayList<XCJsonBean>) origin_bean.obtList("sections" , new ArrayList<XCJsonBean>());
        // 解析第一层的数据
        out_adapter.update(out_jsons);
        out_adapter.notifyDataSetChanged();
    }

    // 第二层gridview的adapter
    class InnerSearchLetterAdapter extends XCBaseAdapter<XCJsonBean> {

        public InnerSearchLetterAdapter(Context context, List<XCJsonBean> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            bean = list.get(position);
            InnerSearchLetterViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.xc_l_adapter_search_letter_inner_item, null);
                holder = new InnerSearchLetterViewHolder();
                holder.xc_id_fragment_search_letter_content2 = (TextView) convertView.findViewById(R.id.xc_id_fragment_search_letter_content2);
                convertView.setTag(holder);
            } else {
                holder = (InnerSearchLetterViewHolder) convertView.getTag();
            }
            // 获取和设置控件的显示值
            holder.xc_id_fragment_search_letter_content2.setText(bean.obtString("brand_name", ""));
            // 加载图片
            return convertView;
        }

        class InnerSearchLetterViewHolder {
            // 添加字段
            TextView xc_id_fragment_search_letter_content2;
        }
    }

    // 第一层listview的adapter
    class OutSearchLetterAdapter extends XCBaseAdapter<XCJsonBean> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            bean = list.get(position);

            OutSearchLetterViewHolder holder = null;

            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.xc_l_adapter_search_letter_out_item, null);
                holder = new OutSearchLetterViewHolder();

                holder.xc_id_fragment_search_content_gridview = (XCNoScrollGridView) convertView.findViewById(R.id.xc_id_fragment_search_content_gridview);
                holder.xc_id_fragment_search_letter_view = (TextView) convertView.findViewById(R.id.xc_id_fragment_search_letter_view);
                holder.xc_id_fragment_search_letter_view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (out_title_listnener != null) {
                            out_title_listnener.onOutTitleClickListener((Integer) v.getTag());
                        }
                    }
                });

                convertView.setTag(holder);

            } else {
                holder = (OutSearchLetterViewHolder) convertView.getTag();
            }

            // 获取和设置控件的显示值

            holder.xc_id_fragment_search_letter_view.setText(bean.obtString("index", ""));
            holder.xc_id_fragment_search_letter_view.setTag(position);

            // 解析第二层的数据
            ArrayList<XCJsonBean> inner_jsons = (ArrayList<XCJsonBean>) bean.obtList("brands", new ArrayList<XCJsonBean>());
            UtilAbsListStyle.setGridViewStyle(holder.xc_id_fragment_search_content_gridview, false, 2, UtilImage.dip2px(context, 1), UtilImage.dip2px(context, 1));
            holder.xc_id_fragment_search_content_gridview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (XCSearchLetterFragment.this.inner_listener != null) {
                        XCSearchLetterFragment.this.inner_listener.onInnerItemClick(parent, view, position, id);
                    }
                }
            });
            // 添加第二层的item布局
            holder.xc_id_fragment_search_content_gridview.setAdapter(new InnerSearchLetterAdapter(getActivity(), inner_jsons));
            holder.xc_id_fragment_search_content_gridview.setVisibility(View.VISIBLE);

            // 加载图片

            return convertView;

        }

        public OutSearchLetterAdapter(Context context, List<XCJsonBean> list) {
            super(context, list);
            sava_letter_position_map = new LinkedHashMap<String, Integer>();
        }

        LinkedHashMap<String, Integer> sava_letter_position_map;

        // 重写了
        @Override
        public void update(List<XCJsonBean> list) {
            this.list = list;
            sava_letter_position_map.clear();
            if (list != null) {
                initLettersPosition(list);
            }
        }

        // 获取字母的位置
        public Integer getPositionFromLetter(String letter) {
            return sava_letter_position_map.get(letter);
        }

        // 保存字母的位置 , 根据业务逻辑重写
        public LinkedHashMap<String, Integer> initLettersPosition(List<XCJsonBean> beans) {
            String record_last_letter = null;
            // 获取每个字母首次出现的位置
            for (int i = 0; i < list.size(); i++) {
                XCJsonBean sort_bean = list.get(i);
                String temp = sort_bean.obtString("index","");
                // 可能接口是 [{index:A , 品牌1} , {index:A, 品牌2} , {index:B,品牌1}]
                // 可能接口是[{index:A , brands:[品牌1 , 品牌2]}]
                if (!temp.equals(record_last_letter)) {
                    sava_letter_position_map.put(temp, i);
                }
                record_last_letter = temp;
            }
            return sava_letter_position_map;
        }

        class OutSearchLetterViewHolder {
            // 添加字段
            TextView xc_id_fragment_search_letter_view;
            XCNoScrollGridView xc_id_fragment_search_content_gridview;
        }
    }

    @Override
    public void initWidgets() {
        xc_id_fragment_search_slide_slidebar = getViewById(R.id.xc_id_fragment_search_slide_slidebar);
        xc_id_fragment_search_slide_dialog = getViewById(R.id.xc_id_fragment_search_slide_dialog);
        xc_id_fragment_search_slide_listview = getViewById(R.id.xc_id_fragment_search_slide_listview);
        xc_id_fragment_search_slide_slidebar.setTextView(xc_id_fragment_search_slide_dialog);

        // 外层的apdater 和 item布局
        out_adapter = new OutSearchLetterAdapter(getActivity(), out_jsons);
        UtilAbsListStyle.setListViewStyle(xc_id_fragment_search_slide_listview, null, 0, false);

        xc_id_fragment_search_slide_listview.setAdapter(out_adapter);
    }

    @Override
    public void listeners() {
        // 字母的滑动监听
        xc_id_fragment_search_slide_slidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                Integer position = out_adapter.getPositionFromLetter(s);
                if (position != null) {
                    xc_id_fragment_search_slide_listview.setSelection(position);
                }
            }
        });
    }

}
// {
// sections": [
// {
// "index":"A",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"B",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "B1",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// },{
// "brand_id": "281",
// "brand_name": "B2",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"C",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "C1",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// },{
// "brand_id": "281",
// "brand_name": "C2",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// },{
// "brand_id": "281",
// "brand_name": "C3",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// },{
// "brand_id": "281",
// "brand_name": "C4",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"D",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"E",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"F",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"G",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"H",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"I",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"J",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "J1",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// },{
// "brand_id": "281",
// "brand_name": "J2",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// },{
// "brand_id": "281",
// "brand_name": "J3",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// },{
// "brand_id": "281",
// "brand_name": "J4",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"K",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"L",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"M",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"N",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"O",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"P",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"Q",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"R",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"S",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"T",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"U",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"V",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"W",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"X",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"Y",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// },
// {
// "index":"Z",
// "brands":[
// {
// "brand_id": "281",
// "brand_name": "\u8fbe\u8299\u59ae",
// "logo": "1419448883127936891.jpg",
// "differ_time": 173341
// }
// ]
// }
// ]
// }
