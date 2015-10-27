/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCBaseFragment;
import com.xiaocoder.android_fw_general.R;

/**
 * @author xiaocoder
 * @Description:该类是一个通用的title , 结构为 左边一个imageview + textview
 * ,中间一个textview(可以设置最右边的drawable)
 * 右边一个textview+imageview
 * @date 2014-12-31 上午11:31:25
 */

@Deprecated
public class XCTitleCommonFragment extends XCBaseFragment {

    LinearLayout xc_id_titlebar_left_layout;
    ImageView xc_id_titlebar_left_imageview;
    TextView xc_id_titlebar_left_textview;
    TextView xc_id_titlebar_center_textview;

    LinearLayout xc_id_titlebar_right_layout;
    ImageView xc_id_titlebar_right_imageview;

    LinearLayout xc_id_titlebar_right2_layout;
    TextView xc_id_titlebar_right2_textview;
    ImageView xc_id_titlebar_right2_imageview;
    LinearLayout xc_id_titlebar_right2_imageview_layout;
    RelativeLayout xc_id_titlebar_right3_layout;
    TextView xc_id_titlebar_right3_textview_number;
    ImageView xc_id_titlebar_right3_imageView;

    RelativeLayout xc_id_titlebar_common_layout;

    Bundle init_bundle; // 在activity的oncreate方法中调用如setTitleCenter,会null指针,即控件还没有找到引用,所以先保存,在onActvitiyCreated中再初始化

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_bar_title_common);
    }

    CenterListener center_listener;

    public void setCenter_listener(CenterListener center_listener) {
        this.center_listener = center_listener;
    }

    public interface CenterListener {
        void centerClick();
    }

    public interface LeftListener {
        void leftClick();
    }

    LeftListener left_listener;

    public void setLeft_listener(LeftListener left_listener) {
        this.left_listener = left_listener;
    }

    public interface RightListener {
        void rightClick();
    }

    RightListener right_listener;

    public void setRight_listener(RightListener right_listener) {
        this.right_listener = right_listener;
    }

    public TextView getXC_id_titlebar_center_textview() {
        return xc_id_titlebar_center_textview;
    }

    public void setQlk_id_titlebar_center_textview(TextView xc_id_titlebar_center_textview) {
        this.xc_id_titlebar_center_textview = xc_id_titlebar_center_textview;
    }

    Right3LayoutClickListener right3LayoutClickListener;

    public interface Right3LayoutClickListener {
        void onRight3LayoutClick();
    }

    public void setRight3LayoutClickListener(Right3LayoutClickListener right3LayoutClickListener) {
        this.right3LayoutClickListener = right3LayoutClickListener;
    }

    Right2TextViewClickListener right2TextViewClickListener;

    public void setOnRight2TextViewClickListener(Right2TextViewClickListener right2TextViewClickListener) {
        this.right2TextViewClickListener = right2TextViewClickListener;
    }

    public interface Right2TextViewClickListener {
        void onRight2TextViewClick();
    }

    RightClickListener rightClickListener;

    public void setOnRightClickListener(RightClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    public interface RightClickListener {
        void onRightClickListener();
    }

    public TextView getXc_id_titlebar_center_textview() {
        return xc_id_titlebar_center_textview;
    }


    // 设置title的中心的标题
    public void setTitleCenter(boolean isCenterShow, String title) {
        if (xc_id_titlebar_center_textview == null) {
            if (init_bundle == null) {
                init_bundle = new Bundle();
            }
            init_bundle.putBoolean("isCenterShow", isCenterShow);
            init_bundle.putString("title", title);
        } else {
            getBaseActivity().setViewGone(isCenterShow, xc_id_titlebar_center_textview);
            xc_id_titlebar_center_textview.setText(title);
        }
    }

    // 设置左边的文本, 设置true , 如果是"" 则显示的就是一个返回的图标, 如果是"返回" ,则为一个返回的图标+"返回"的字样
    public void setTitleLeft(boolean isLeftShow, String left_text) {
        if (xc_id_titlebar_left_layout == null) {
            if (init_bundle == null) {
                init_bundle = new Bundle();
            }
            init_bundle.putBoolean("isLeftShow", isLeftShow);
            init_bundle.putString("left_text", left_text);
        } else {
            getBaseActivity().setViewGone(isLeftShow, xc_id_titlebar_left_layout);
            xc_id_titlebar_left_textview.setText(left_text);
        }
    }


    // title中间的textview也可以设置drawable , 这里默认的是textview的右边的drawable
    public void setTitleCenterRightDrawable(boolean isCenterShow, int textview_drawable_id) {
        if (xc_id_titlebar_center_textview == null) {
            if (init_bundle == null) {
                init_bundle = new Bundle();
            }
            init_bundle.putBoolean("isCenterShow", isCenterShow);
            init_bundle.putInt("textview_drawable_id", textview_drawable_id);
        } else {
            getBaseActivity().setViewGone(isCenterShow, xc_id_titlebar_center_textview);
            Drawable drawable = getResources().getDrawable(R.drawable.xc_d_arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            xc_id_titlebar_center_textview.setCompoundDrawables(null, null, drawable, null);
        }
    }

    // 如果这个显示, 那么TitleRight2就不必要显示了, 这个最右边的布局只有一个图片
    public void setTitleRight(boolean isRightShow, int right_drawable_id) {
        if (xc_id_titlebar_right_imageview == null) {
            if (init_bundle == null) {
                init_bundle = new Bundle();
            }
            init_bundle.putBoolean("isRightShow", isRightShow);
            init_bundle.putInt("right_drawable_id", right_drawable_id);
        } else {
            getBaseActivity().setViewGone(isRightShow, xc_id_titlebar_right_layout);
            xc_id_titlebar_right_imageview.setImageResource(right_drawable_id);
        }
    }

    /**
     * 这个布局为 textview + iamgeview , isRight2Show如果为false,则该right2_layout不显示,
     * right2_drawable_id如果小于0 ,则imageview不显示 ,text如果为null,则textview不显示
     */
    public void setTitleRight2(boolean isRight2Show, int right2_drawable_id, String text) {
        if (xc_id_titlebar_right2_textview == null) {
            if (init_bundle == null) {
                init_bundle = new Bundle();
            }
            init_bundle.putBoolean("isRight2Show", isRight2Show);
            init_bundle.putInt("right2_drawable_id", right2_drawable_id);
            init_bundle.putString("text", text);
        } else {
            getBaseActivity().setViewGone(isRight2Show, xc_id_titlebar_right2_layout);

            if (right2_drawable_id > 0) {
                xc_id_titlebar_right2_imageview.setBackgroundResource(right2_drawable_id);
                getBaseActivity().setViewGone(true, xc_id_titlebar_right2_imageview);
            } else {
                getBaseActivity().setViewGone(false, xc_id_titlebar_right2_imageview);
                getBaseActivity().setViewGone(false, xc_id_titlebar_right2_imageview_layout);
            }

            if (text != null) {
                getBaseActivity().setViewGone(true, xc_id_titlebar_right2_textview);
                xc_id_titlebar_right2_textview.setText(text);
            } else {
                getBaseActivity().setViewGone(false, xc_id_titlebar_right2_textview);
            }

        }
    }

    /**
     * 这个布局为 textview + iamgeview , isRight2Show如果为false,则该right2_layout不显示,
     * right2_drawable_id如果小于0 ,则imageview不显示 ,text如果为null,则textview不显示
     */
    public void setTitleRight3(boolean isRight3Show, int right3_drawable_id, String text) {
        if (xc_id_titlebar_right3_textview_number == null) {
            if (init_bundle == null) {
                init_bundle = new Bundle();
            }
            init_bundle.putBoolean("isRight3Show", isRight3Show);
            init_bundle.putInt("right3_drawable_id", right3_drawable_id);
            init_bundle.putString("text3", text);
        } else {
            if (getBaseActivity() != null) {

                getBaseActivity().setViewGone(isRight3Show, xc_id_titlebar_right3_layout);

                if (right3_drawable_id > 0) {
                    xc_id_titlebar_right3_imageView.setBackgroundResource(right3_drawable_id);
                    getBaseActivity().setViewGone(true, xc_id_titlebar_right3_imageView);
                } else {
                    getBaseActivity().setViewGone(false, xc_id_titlebar_right3_imageView);
                }

                if (text != null) {
                    xc_id_titlebar_right3_textview_number.setText(text);
                    getBaseActivity().setViewGone(true, xc_id_titlebar_right3_textview_number);
                    Animation animation = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.xl_anim_push_down_in);
                    xc_id_titlebar_right3_textview_number.startAnimation(animation);

                } else {
                    getBaseActivity().setViewGone(false, xc_id_titlebar_right3_textview_number);

                }
            }
        }
    }

    public String getTitleRightText() {

        return xc_id_titlebar_right2_textview.getText().toString().trim();

    }

    public TextView getTitleCenterText() {
        return xc_id_titlebar_center_textview;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xc_id_titlebar_left_layout) {
            if (left_listener != null) {
                // 自定义逻辑
                left_listener.leftClick();
            } else {
                // 默认的
                myFinish();
            }
        } else if (id == R.id.xc_id_titlebar_right_layout) {
            if (right_listener != null) {
                right_listener.rightClick();
            }
        } else if (id == R.id.xc_id_titlebar_center_textview) {
            if (center_listener != null) {
                center_listener.centerClick();
            }
        } else if (id == R.id.xc_id_titlebar_right2_textview) {
            if (right2TextViewClickListener != null) {
                right2TextViewClickListener.onRight2TextViewClick();
            }
        } else if (id == R.id.xc_id_titlebar_right_imageview) {
            if (rightClickListener != null) {
                rightClickListener.onRightClickListener();
            }
        } else if (id == R.id.xc_id_titlebar_right3_layout) {
            if (right3LayoutClickListener != null) {
                right3LayoutClickListener.onRight3LayoutClick();
            }
        }
    }

    public int colorLayout;

    public void setColorLayout(int color) {
        colorLayout = color;
    }


    @Override
    public void initWidgets() {
        xc_id_titlebar_left_layout = getViewById(R.id.xc_id_titlebar_left_layout);
        xc_id_titlebar_left_imageview = getViewById(R.id.xc_id_titlebar_left_imageview);
        xc_id_titlebar_left_textview = getViewById(R.id.xc_id_titlebar_left_textview);
        xc_id_titlebar_center_textview = getViewById(R.id.xc_id_titlebar_center_textview);
        xc_id_titlebar_right_layout = getViewById(R.id.xc_id_titlebar_right_layout);
        xc_id_titlebar_right_imageview = getViewById(R.id.xc_id_titlebar_right_imageview);
        xc_id_titlebar_right2_layout = getViewById(R.id.xc_id_titlebar_right2_layout);
        xc_id_titlebar_right2_textview = getViewById(R.id.xc_id_titlebar_right2_textview);
        xc_id_titlebar_right2_imageview = getViewById(R.id.xc_id_titlebar_right2_imageview);
        xc_id_titlebar_right2_imageview_layout = getViewById(R.id.xc_id_titlebar_right2_imageview_layout);
        xc_id_titlebar_right3_textview_number = getViewById(R.id.xc_id_titlebar_right3_textview_number);
        xc_id_titlebar_right3_imageView = getViewById(R.id.xc_id_titlebar_right3_imageView);
        xc_id_titlebar_right3_layout = getViewById(R.id.xc_id_titlebar_right3_layout);
        xc_id_titlebar_common_layout = getViewById(R.id.xc_id_titlebar_common_layout);

        if (init_bundle == null) {
            init_bundle = new Bundle();
        }

        if (init_bundle != null) {
            // 设置是否可见
            if (init_bundle.getBoolean("isCenterShow", false)) {
                getBaseActivity().setViewGone(true, xc_id_titlebar_center_textview);
            }
            if (init_bundle.getBoolean("isRightShow", false)) {
                getBaseActivity().setViewGone(true, xc_id_titlebar_right_layout);
            }
            if (init_bundle.getBoolean("isRight2Show", false)) {
                getBaseActivity().setViewGone(true, xc_id_titlebar_right2_layout);
            }
            if (init_bundle.getBoolean("isRight3Show", false)) {
                getBaseActivity().setViewGone(true, xc_id_titlebar_right3_layout);
            }
            // 只有左边是默认可见的
            if (init_bundle.getBoolean("isLeftShow", false)) {
                getBaseActivity().setViewVisible(true, xc_id_titlebar_left_layout);
            } else {
                getBaseActivity().setViewVisible(false, xc_id_titlebar_left_layout);
            }
            // 设置内容
            xc_id_titlebar_center_textview.setText(init_bundle.getString("title"));
            xc_id_titlebar_right_imageview.setImageResource(init_bundle.getInt("right_drawable_id"));
            xc_id_titlebar_left_textview.setText(init_bundle.getString("left_text"));
            if (init_bundle.getInt("right2_drawable_id") > 0) {
                xc_id_titlebar_right2_imageview.setBackgroundResource(init_bundle.getInt("right2_drawable_id"));
                getBaseActivity().setViewGone(true, xc_id_titlebar_right2_imageview);
            } else {
                getBaseActivity().setViewGone(false, xc_id_titlebar_right2_imageview);
                getBaseActivity().setViewGone(false, xc_id_titlebar_right2_imageview_layout);
            }
            if (init_bundle.getInt("right3_drawable_id") > 0) {
                xc_id_titlebar_right3_imageView.setBackgroundResource(init_bundle.getInt("right3_drawable_id"));
                getBaseActivity().setViewGone(true, xc_id_titlebar_right3_imageView);
            } else {
                getBaseActivity().setViewGone(false, xc_id_titlebar_right3_imageView);
            }

            if (init_bundle.getInt("textview_drawable_id") > 0) {
                Drawable drawable = getResources().getDrawable(R.drawable.xc_d_arrow_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                xc_id_titlebar_center_textview.setCompoundDrawables(null, null, drawable, null);
                getBaseActivity().setViewGone(true, xc_id_titlebar_center_textview);
            }

            if (init_bundle.getString("text") != null) {
                getBaseActivity().setViewGone(true, xc_id_titlebar_right2_textview);
                xc_id_titlebar_right2_textview.setText(init_bundle.getString("text"));
            } else {
                getBaseActivity().setViewGone(false, xc_id_titlebar_right2_textview);
            }

            if (init_bundle.getString("text3") != null) {
                getBaseActivity().setViewGone(true, xc_id_titlebar_right3_textview_number);
                xc_id_titlebar_right3_textview_number.setText(init_bundle.getString("text3"));
                Animation animation = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.xl_anim_push_down_in);
                xc_id_titlebar_right3_textview_number.startAnimation(animation);
            } else {
                getBaseActivity().setViewGone(false, xc_id_titlebar_right3_textview_number);
            }

            xc_id_titlebar_common_layout.setBackgroundColor(colorLayout);

            init_bundle = null;
        }
    }

    @Override
    public void listeners() {
        xc_id_titlebar_left_layout.setOnClickListener(this);
        xc_id_titlebar_right_layout.setOnClickListener(this);
        xc_id_titlebar_right2_layout.setOnClickListener(this);
        xc_id_titlebar_center_textview.setOnClickListener(this);
        xc_id_titlebar_right2_imageview_layout.setOnClickListener(this);
        xc_id_titlebar_right2_textview.setOnClickListener(this);
        xc_id_titlebar_right_imageview.setOnClickListener(this);
        xc_id_titlebar_right3_layout.setOnClickListener(this);
    }
}
