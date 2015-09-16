package com.xiaocoder.android.fw.general.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.attention.StandUpAnimator;
import com.daimajia.slider.library.Animations.BaseAnimationInterface;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseFragment;
import com.xiaocoder.android_fw_general.R;

import java.util.Map;

public class XCViewPagerFragment extends XCBaseFragment implements BaseSliderView.OnSliderClickListener {

    private static final String ARG_PARAM1 = "extra";
    private static final String ARG_PARAM2 = "position";

    private SliderLayout mDemoSlider;
    private long time_gap = 2000;
    private SliderLayout.Transformer st = SliderLayout.Transformer.Accordion;
    private SliderLayout.PresetIndicators sp = SliderLayout.PresetIndicators.Center_Bottom;
    private BaseAnimationInterface anim = new DescriptionAnimation();
    private BaseSliderView.ScaleType scale = BaseSliderView.ScaleType.Fit;
    private boolean isNeedDes = false;
    private boolean isAutoSlider = true;

    /**
     * int File String类型
     */
    private Map<String, Object> map;

    public SliderLayout getmDemoSlider() {
        return mDemoSlider;
    }

    public void setTime_gap(long time_gap) {

        this.time_gap = time_gap;
    }

    public void setTf(SliderLayout.Transformer st) {
        this.st = st;
    }

    public void setTp(SliderLayout.PresetIndicators sp) {
        this.sp = sp;
    }

    /**
     * 设置指示器的动画效果
     */
    public void setAnim(BaseAnimationInterface anim) {
        this.anim = anim;
    }

    public void setScale(BaseSliderView.ScaleType scale) {
        this.scale = scale;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void setIsNeedDes(boolean isNeedDes) {
        this.isNeedDes = isNeedDes;
    }

    public void setIsAutoSlider(boolean isAutoSlider) {
        this.isAutoSlider = isAutoSlider;
    }

    public interface OnImageClickListener {
        void onImageClickListener(int position);
    }

    OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_view_pager_slider);
    }

    @Override
    public void initWidgets() {

        mDemoSlider = getViewById(R.id.slider);

        if (!isAutoSlider) {
            mDemoSlider.stopAutoCycle();
        }

        int index = 0;
        for (String key : map.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            String des = "";
            if (isNeedDes) {
                des = key;
            }
            textSliderView.description(des)
                    .image(map.get(key))
                    .setScaleType(scale)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString(ARG_PARAM1, key);
            textSliderView.getBundle().putInt(ARG_PARAM2, index++);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(st);
        mDemoSlider.setPresetIndicator(sp);
        mDemoSlider.setCustomAnimation(anim);
        // mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        // mDemoSlider.setCustomIndicator((PagerIndicator) getViewById(R.id.custom_indicator));
        mDemoSlider.setDuration(time_gap);
    }

    @Override
    public void listeners() {

    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        if (mDemoSlider != null && isAutoSlider) {
            mDemoSlider.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mDemoSlider != null && isAutoSlider) {
            mDemoSlider.startAutoCycle();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if (onImageClickListener != null) {
            onImageClickListener.onImageClickListener(slider.getBundle().getInt(ARG_PARAM2, -1));
        }
    }

    public class ChildAnimationExample implements BaseAnimationInterface {

        private final static String TAG = "ChildAnimationExample";

        @Override
        public void onPrepareCurrentItemLeaveScreen(View current) {
            View descriptionLayout = current.findViewById(R.id.description_layout);
            if (descriptionLayout != null) {
                current.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
            }
            XCApplication.printi(TAG, "onPrepareCurrentItemLeaveScreen called");
        }

        @Override
        public void onPrepareNextItemShowInScreen(View next) {
            View descriptionLayout = next.findViewById(R.id.description_layout);
            if (descriptionLayout != null) {
                next.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
            }
            XCApplication.printi(TAG, "onPrepareNextItemShowInScreen called");
        }

        @Override
        public void onCurrentItemDisappear(View view) {
            XCApplication.printi(TAG, "onCurrentItemDisappear called");
        }

        @Override
        public void onNextItemAppear(View view) {

            View descriptionLayout = view.findViewById(R.id.description_layout);
            if (descriptionLayout != null) {
                view.findViewById(R.id.description_layout).setVisibility(View.VISIBLE);
//            ValueAnimator animator = ObjectAnimator.ofFloat(
//                    descriptionLayout, "y", -descriptionLayout.getHeight(),
//                    0).setDuration(500);
//            animator.start();
//            new BounceInAnimator().animate(descriptionLayout);
                new StandUpAnimator().animate(descriptionLayout);
            }
            XCApplication.printi(TAG, "onCurrentItemDisappear called");
        }
    }

}


