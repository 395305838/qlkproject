/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.xiaocoder.android.fw.general.adapter.XCAdapterViewPager;
import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBottomFragment;
import com.xiaocoder.android.fw.general.listener.XCViewPagerListener;
import com.xiaocoder.android.fw.general.util.UtilAbsListStyle;
import com.xiaocoder.android.fw.general.util.UtilImage;
import com.xiaocoder.android.fw.general.util.UtilInputMethod;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.android.fw.general.view.XCRecordVoiceButton;
import com.xiaocoder.android.fw.general.view.XCRecordVoiceButton.OnRecordVoiceSuccessListener;
import com.xiaocoder.android_fw_general.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaocoder
 */
public class XCBottomChatFragment2 extends XCBottomFragment {

    // 最左边的进入录音布局的按钮--喇叭
    ImageView xc_id_fragment_bottom_show_recoder;
    // 输入框
    EditText xc_id_fragment_bottom_edit;

    public XCRecordVoiceButton getXc_id_fragment_bottom_recoder_button() {
        return xc_id_fragment_bottom_recoder_button;
    }

    // 按住录音的按钮
    XCRecordVoiceButton xc_id_fragment_bottom_recoder_button;
    // 发送按钮
    Button xc_id_fragment_bottom_right_send;
    // 选择图片按钮
    ImageView xc_id_fragment_bottom_right_photo;
    // 表情和图片的选择布局
    RelativeLayout xc_id_fragment_bottom_face_photo_layout;
    // 表情布局
    ViewPager xc_id_fragment_bottom_face_viewpager;
    // 图片布局
    LinearLayout xc_id_fragment_bottom_photo_layout;

    ImageView xc_id_chat_bottom_album;
    ImageView xc_id_chat_bottom_camera;
    ImageView xc_id_chat_bottom_reply;
    ImageView xc_id_chat_bottom_time;
    ImageView xc_id_chat_bottom_medicine;
    ImageView xc_id_chat_bottom_back;

    public interface OnTouchRecoderButtonListener {
        void onTouchRecoderButtonListener();
    }

    public void setOnTouchRecoderListener(OnTouchRecoderButtonListener onTouchRecoderButtonListener) {
        this.onTouchRecoderButtonListener = onTouchRecoderButtonListener;
    }

    OnTouchRecoderButtonListener onTouchRecoderButtonListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_bar_bottom_chat2);
    }

    // 这里重写了
    @Override
    public View init(LayoutInflater inflater, int layout_id) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer = (ViewGroup) inflater.inflate(layout_id, null);
        mContainer.setLayoutParams(lp);
        return mContainer;
    }

    public interface OnItemImageViewClikedListener {
        void onItemImageViewClikedListener(ImageView imageview);
    }

    OnItemImageViewClikedListener item_imageview_listener;

    public void setOnItemImageViewClikedListener(OnItemImageViewClikedListener listener) {
        item_imageview_listener = listener;
    }

    @Override
    public void initWidgets() {
        xc_id_fragment_bottom_show_recoder = getViewById(R.id.xc_id_fragment_bottom_show_recoder);
        xc_id_fragment_bottom_edit = getViewById(R.id.xc_id_fragment_bottom_edit);
        xc_id_fragment_bottom_recoder_button = getViewById(R.id.xc_id_fragment_bottom_recoder_button);
        xc_id_fragment_bottom_right_send = getViewById(R.id.xc_id_fragment_bottom_right_send);
        xc_id_fragment_bottom_right_photo = getViewById(R.id.xc_id_fragment_bottom_right_photo);
        xc_id_fragment_bottom_face_photo_layout = getViewById(R.id.xc_id_fragment_bottom_face_photo_layout);
        xc_id_fragment_bottom_photo_layout = getViewById(R.id.xc_id_fragment_bottom_photo_layout);
        xc_id_fragment_bottom_face_viewpager = getViewById(R.id.xc_id_fragment_bottom_face_viewpager);
        face_viewpager_dots = getViewById(R.id.xc_id_fragment_bottom_viewpager_dots);

        xc_id_chat_bottom_album = getViewById(R.id.xc_id_chat_bottom_album);
        xc_id_chat_bottom_camera = getViewById(R.id.xc_id_chat_bottom_camera);
        xc_id_chat_bottom_reply = getViewById(R.id.xc_id_chat_bottom_reply);
        xc_id_chat_bottom_time = getViewById(R.id.xc_id_chat_bottom_time);
        xc_id_chat_bottom_medicine = getViewById(R.id.xc_id_chat_bottom_medicine);
        xc_id_chat_bottom_back = getViewById(R.id.xc_id_chat_bottom_back);

        // 创建表情布局
        createFaceViewsAndDots();
        createViewPager();
    }

    @Override
    public void listeners() {
        xc_id_fragment_bottom_show_recoder.setOnClickListener(this);
        xc_id_fragment_bottom_edit.setOnClickListener(this);
        xc_id_fragment_bottom_recoder_button.setOnClickListener(this);

        xc_id_fragment_bottom_right_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_content = xc_id_fragment_bottom_edit.getText().toString().trim();
                if (!UtilString.isBlank(text_content)) {
                    xc_id_fragment_bottom_edit.setText("");
                    onButtonSendTextMessageListener.onButtonSendTextMessageListener(text_content);
                }
            }
        });
        xc_id_fragment_bottom_right_photo.setOnClickListener(this);


        xc_id_fragment_bottom_recoder_button.setOnTouchRecoderListener(new XCRecordVoiceButton.OnTouchRecoderListener() {
            @Override
            public void onTouchRecoderListener() {
                if (onTouchRecoderButtonListener != null) {
                    onTouchRecoderButtonListener.onTouchRecoderButtonListener();
                }
            }
        });

        xc_id_chat_bottom_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_imageview_listener != null) {
                    item_imageview_listener.onItemImageViewClikedListener((ImageView) view);
                }
            }
        });

        xc_id_chat_bottom_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_imageview_listener != null) {
                    item_imageview_listener.onItemImageViewClikedListener((ImageView) view);
                }
            }
        });
        xc_id_chat_bottom_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_imageview_listener != null) {
                    item_imageview_listener.onItemImageViewClikedListener((ImageView) view);
                }
            }
        });
        xc_id_chat_bottom_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_imageview_listener != null) {
                    item_imageview_listener.onItemImageViewClikedListener((ImageView) view);
                }
            }
        });
        xc_id_chat_bottom_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_imageview_listener != null) {
                    item_imageview_listener.onItemImageViewClikedListener((ImageView) view);
                }
            }
        });
        xc_id_chat_bottom_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_imageview_listener != null) {
                    item_imageview_listener.onItemImageViewClikedListener((ImageView) view);
                }
            }
        });


        // 当点击发送时， 可以获取到文本
        xc_id_fragment_bottom_edit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String text_content = xc_id_fragment_bottom_edit.getText().toString().trim();
                    if (!UtilString.isBlank(text_content)) {
                        if (onSendTextMessageListener != null) {
                            onSendTextMessageListener.onSendTextMessageListener(text_content);
                            xc_id_fragment_bottom_edit.setText("");
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        // 当录音按钮释放时， 可以获取到保存在目录里的语音文件的路径
        xc_id_fragment_bottom_recoder_button.setOnRecordVoiceSuccessListener(new OnRecordVoiceSuccessListener() {

            @Override
            public void onRecordVoiceSuccessListener(File file, float gap) {
                if (onSendVoiceMessageListener != null) {
                    onSendVoiceMessageListener.onSendVoiceMessageListener(file, gap);
                }
            }
        });

        //xc_id_fragment_bottom_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null) {
//                    listener.onBottomClickListener();
//                }
//            }
//        });
    }


    public void whenKeyBoardShow() {
        XCApplication.getBase_handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getBaseActivity().setViewGone(false, xc_id_fragment_bottom_face_photo_layout);
                getBaseActivity().setViewGone(false, xc_id_fragment_bottom_right_photo);
                getBaseActivity().setViewGone(true, xc_id_fragment_bottom_right_send);
            }
        }, 20);
        printi("显示发送");

    }

    public void whenKeyBoardHidden() {
        printi("显示照片");
        getBaseActivity().setViewGone(false, xc_id_fragment_bottom_face_photo_layout);
        getBaseActivity().setViewGone(true, xc_id_fragment_bottom_right_photo);
        getBaseActivity().setViewGone(false, xc_id_fragment_bottom_right_send);
    }

    // 获取到聊天的输入框
    public EditText getEditText() {
        return xc_id_fragment_bottom_edit;
    }

    // 记录上次点击的位置
    int recoder_clicked;

    @Override
    public void onClick(View v) {
        super.onClick(v);

        final int id = v.getId();
        if (id == R.id.xc_id_fragment_bottom_show_recoder) {
            // 点击喇叭
            if (xc_id_fragment_bottom_edit.isShown()) {
                UtilInputMethod.hiddenInputMethod(getActivity());
                getBaseActivity().setViewGone(false, xc_id_fragment_bottom_edit);
                getBaseActivity().setViewGone(false, xc_id_fragment_bottom_face_photo_layout);
                getBaseActivity().setViewGone(true, xc_id_fragment_bottom_recoder_button);
            } else {
                getBaseActivity().setViewGone(true, xc_id_fragment_bottom_edit);
                getBaseActivity().setViewGone(false, xc_id_fragment_bottom_face_photo_layout);
                getBaseActivity().setViewGone(false, xc_id_fragment_bottom_recoder_button);
            }
        } else if (id == R.id.xc_id_fragment_bottom_right_photo) {
            // 点击进入加号获取图片的按钮
            printi("temp", "click photo");
            UtilInputMethod.hiddenInputMethod(getActivity());

            // 如果取消延迟发送效果不对
            XCApplication.getBase_handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (getBaseActivity() != null) {
                        getBaseActivity().setViewGone(false, xc_id_fragment_bottom_face_viewpager);
                        getBaseActivity().setViewGone(false, face_viewpager_dots);
                        getBaseActivity().setViewGone(true, xc_id_fragment_bottom_photo_layout);

                        printi("temp", recoder_clicked + "-->recoder_clicked");

                        if (recoder_clicked == R.id.xc_id_fragment_bottom_right_face) {
                            recoder_clicked = id;
                            getBaseActivity().setViewGone(true, xc_id_fragment_bottom_face_photo_layout);
                            if (listener != null) {
                                listener.onBottomClickListener();
                            }
                            return;
                        }
                        common();
                        recoder_clicked = id;
                        if (listener != null) {
                            listener.onBottomClickListener();
                        }
                    }
                }
            }, 100);
            return;
        } else if (id == R.id.xc_id_fragment_bottom_edit) {
            // 点击eidttext
            // 在布局的onSizeChange中监听


        }
//        else if (id == R.id.xc_id_fragment_bottom_recoder_button) {
//             点击录音按钮， 自定义的XCRecoderButton里面已经做了处理
//        }
//      else if (id == R.id.xc_id_fragment_bottom_right_face) {
//            // 点击表情
//            // 关闭键盘
//            UtilInputMethod.hiddenInputMethod(getActivity());
//            // 这里得发送延迟消息，否则界面的效果有问题
//            XCApplication.base_handler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (getBaseActivity() != null) {
//                        getBaseActivity().setViewGone(true, xc_id_fragment_bottom_face_viewpager);
//                        getBaseActivity().setViewGone(false, xc_id_fragment_bottom_photo_layout);
//                        getBaseActivity().setViewGone(true, face_viewpager_dots);
//
//                        if (recoder_clicked == R.id.xc_id_fragment_bottom_right_photo) {
//                            recoder_clicked = id;
//                            getBaseActivity().setViewGone(true, xc_id_fragment_bottom_face_photo_layout);
//                            if (listener != null) {
//                                listener.onBottomClickListener();
//                            }
//                            return;
//                        }
//                        common();
//                        recoder_clicked = id;
//                        if (listener != null) {
//                            listener.onBottomClickListener();
//                        }
//                    }
//                }
//            }, 300);
//            return;
//
//        }


        recoder_clicked = id;
    }

    // 点击表情的布局和加号按钮可能会调用到的方法
    private void common() {

        if (!xc_id_fragment_bottom_edit.isShown()) {
            getBaseActivity().setViewGone(false, xc_id_fragment_bottom_recoder_button);
            getBaseActivity().setViewGone(true, xc_id_fragment_bottom_edit);
        }

        if (xc_id_fragment_bottom_face_photo_layout.isShown()) {
            getBaseActivity().setViewGone(false, xc_id_fragment_bottom_face_photo_layout);
        } else {
            getBaseActivity().setViewGone(true, xc_id_fragment_bottom_face_photo_layout);
        }

    }

    // -----文本信息
    public interface OnButtonSendTextMessageListener {
        void onButtonSendTextMessageListener(String text_content);
    }

    OnButtonSendTextMessageListener onButtonSendTextMessageListener;

    public void setOnButtonSendTextMessageListener(OnButtonSendTextMessageListener onButtonSendTextMessageListener) {
        this.onButtonSendTextMessageListener = onButtonSendTextMessageListener;
    }


    // -----文本信息
    public interface OnSendTextMessageListener {
        void onSendTextMessageListener(String text_content);
    }

    OnSendTextMessageListener onSendTextMessageListener;

    public void setOnSendTextMessageListener(OnSendTextMessageListener onSendTextMessageListener) {
        this.onSendTextMessageListener = onSendTextMessageListener;
    }

    // -----声音信息
    public interface OnSendVoiceMessageListener {
        void onSendVoiceMessageListener(File voice_content, float gap);
    }

    OnSendVoiceMessageListener onSendVoiceMessageListener;

    public void setOnSendVoiceMessageListener(OnSendVoiceMessageListener onSendVoiceMessageListener) {
        this.onSendVoiceMessageListener = onSendVoiceMessageListener;
    }

    // ----摄像头信息
    public interface OnCaremaSelectedFileListener {
        void onCaremaSelectedFileListener(File file);
    }

    OnCaremaSelectedFileListener onCaremaSelectedFileListener;

    public void setOnCaremaSelectedFileListener(OnCaremaSelectedFileListener onCaremaSelectedFileListener) {
        this.onCaremaSelectedFileListener = onCaremaSelectedFileListener;
    }

    // ---- 本地相册
    public interface OnLocalSelectedFileListener {
        void onLocalSelectedFileListener(File file);
    }

    OnLocalSelectedFileListener onLocalSelectedFileListener;

    public void setOnLocalSelectedFileListener(OnLocalSelectedFileListener onLocalSelectedFileListener) {
        this.onLocalSelectedFileListener = onLocalSelectedFileListener;
    }

    // 录音进行中时， 如果有来电话等，就删除
    @Override
    public void onPause() {
        super.onPause();
        // 录音删除
        xc_id_fragment_bottom_recoder_button.onActivityPaused();
    }

    // 创建表情布局， 在viewpager中可以滑动
    ArrayList<LinearLayout> face_view_layout;
    ArrayList<GridView> face_view_gridview;
    List<View> dots;
    int total_images;
    int last_dot_position = 0;// 记录上一次点的位置
    int currentItem; // 当前页面
    LinearLayout face_viewpager_dots;
    XCAdapterViewPager viewpager_adapter;

    // 创建viewpager中的gridview 和 dots
    private void createFaceViewsAndDots() {
        // 创建gridview
        face_view_layout = new ArrayList<LinearLayout>();
        face_view_gridview = new ArrayList<GridView>();
        int gap = UtilImage.dip2px(getActivity(), 6);
        int gap2 = UtilImage.dip2px(getActivity(), 12);
        // 创建dots
        dots = new ArrayList<View>();
        total_images = TOTAL_FACE_NUM / PAGE_NUM + 1; // 85/20 + 1
        for (int i = 0; i < total_images; i++) {
            // 创建gridview
            LinearLayout include_gridview_layout = (LinearLayout) getBaseActivity().base_inflater.inflate(R.layout.xc_l_view_face_gridview, null);
            GridView gridview = (GridView) include_gridview_layout.findViewById(R.id.xc_id_fragment_face_gridview);
            LinearLayout.LayoutParams ll_gridview = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UtilImage.dip2px(getActivity(), 150));
            gridview.setLayoutParams(ll_gridview);
            gridview.setPadding(gap2, gap2, gap2, gap2);
            UtilAbsListStyle.setGridViewStyle(gridview, false, gap, gap, 7);
            // 设置表情点击的监听
            FaceAdapter face_adapter = new FaceAdapter(getActivity(), getFaceUrl(i + 1));
            gridview.setAdapter(face_adapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String url = (String) (view.findViewById(R.id.xc_id_face_item_imageview).getTag() + "");
                    String name = url.substring(url.lastIndexOf("/") + 1, url.length());
                    // shortToast(name + "--" + url);
                    updateEditText(name);
                }
            });
            face_view_gridview.add(gridview);
            face_view_layout.add(include_gridview_layout);
            // 创建dots
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.xc_l_view_viewpager_dot, null);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(UtilImage.dip2px(getActivity(), 7), UtilImage.dip2px(getActivity(), 7));
            ll.setMargins(UtilImage.dip2px(getActivity(), 3), 0, UtilImage.dip2px(getActivity(), 3), 0);
            view.setLayoutParams(ll);
            dots.add(view);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.xc_dd_fragment_viewpager_dot_focused);
                last_dot_position = 0;
            } else {
                view.setBackgroundResource(R.drawable.xc_dd_fragment_viewpager_dot_normal);
            }
            face_viewpager_dots.addView(view);
        }
    }

    // 设置viewpager的监听和adapter
    protected void createViewPager() {
        viewpager_adapter = new XCAdapterViewPager(face_view_layout);
        xc_id_fragment_bottom_face_viewpager.setAdapter(viewpager_adapter);
        xc_id_fragment_bottom_face_viewpager.setOnPageChangeListener(new XCViewPagerListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                dots.get(last_dot_position).setBackgroundResource(R.drawable.xc_dd_fragment_viewpager_dot_normal);
                dots.get(position).setBackgroundResource(R.drawable.xc_dd_fragment_viewpager_dot_focused);
                last_dot_position = position;
                currentItem = position;
            }
        });
    }

    public static final String FACE_PATH_DIR = "face/";

    // 当选中一个表情后， 更新edittext中的文本 +表情
    private void updateEditText(String name) {
        try {
            InputStream inputStream = getActivity().getAssets().open(FACE_PATH_DIR + name);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            bitmap = Bitmap.createScaledBitmap(bitmap, UtilImage.dip2px(getActivity(), 24), UtilImage.dip2px(getActivity(), 24), true);

            name = "[" + name + "]";
            ImageSpan image_span = new ImageSpan(getActivity(), bitmap);
            SpannableString spannable_string = new SpannableString(name);
            spannable_string.setSpan(image_span, name.indexOf('['), name.indexOf(']') + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            xc_id_fragment_bottom_edit.append(spannable_string);

            shortToast(xc_id_fragment_bottom_edit.getText().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class FaceAdapter extends XCBaseAdapter<String> {

        public FaceAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            bean = list.get(position);
            FaceHolder holder = null;
            if (convertView == null) {
                holder = new FaceHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.xc_l_view_face_gridview_item, null);
                holder.xc_jzh_face_item_imageview = (ImageView) convertView.findViewById(R.id.xc_id_face_item_imageview);
                // 设置每个表情的大小， 这里可以根据不同手机屏幕做判断后再设置值
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(UtilImage.dip2px(context, 30), UtilImage.dip2px(context, 30));
                holder.xc_jzh_face_item_imageview.setLayoutParams(ll);
                convertView.setTag(holder);
            } else {
                holder = (FaceHolder) convertView.getTag();
            }

            holder.xc_jzh_face_item_imageview.setTag(bean);
            imageloader.displayImage(bean, holder.xc_jzh_face_item_imageview, options);
            return convertView;
        }

        class FaceHolder {
            ImageView xc_jzh_face_item_imageview;
        }
    }

    // 获取资产目录下的所有表情图片的路径
    public List<String> getFaceUrl(int page) {
        ArrayList<String> list = new ArrayList<String>(20);
        for (int i = (page - 1) * PAGE_NUM + 1; i <= page * PAGE_NUM; i++) {
            if (i < 10) {
                list.add("assets://face/00" + i + ".png");
            } else {
                if (i > TOTAL_FACE_NUM) {
                    return list;
                }
                list.add("assets://face/0" + i + ".png");
            }
        }
        return list;
    }

    // 表情的总数量
    public static int TOTAL_FACE_NUM = 85;
    // 每页表情的数量
    public static int PAGE_NUM = 21;

    // 这个监听一般用不到，可以不管
    OnBottomClickListener listener;

    // 这个监听一般用不到，可以不管
    public interface OnBottomClickListener {
        void onBottomClickListener();
    }

    // 这个监听一般用不到，可以不管
    public void setOnBottomClickListener(OnBottomClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (xc_id_fragment_bottom_recoder_button != null) {
            xc_id_fragment_bottom_recoder_button.closetDialog();
        }
    }
}
