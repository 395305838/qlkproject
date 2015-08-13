package com.xiaocoder.buffer.function;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.view.XCImageView;
import com.xiaocoder.android_fw_general.R;

import java.util.ArrayList;

/*
 * app启动时的页面
 */
public abstract class QlkLoadActivity extends XCBaseActivity implements OnClickListener {

    public ImageView load_image;
    private ViewPager mViewPager;
    private ImageView imageView;
    public ArrayList<Integer> welcomeImgList;
    public ArrayList<Integer> bgColorList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (spGet("isInstall", true)) {
            spPut("isInstall", false);
            initWelCom();
        } else {
            initLoad();
        }

        super.onCreate(savedInstanceState);

        //更新后台请求地址
        reviewConnectApiUrl();

    }

    private void initLoad() {
        setContentView(R.layout.sk_l_activity_load);
        load_image = getViewById(R.id.sk_id_load_image);
        Animation alpAnimation = new AlphaAnimation(0.1f, 1.0f);
        // 设置动画时间
        alpAnimation.setDuration(1000);
        load_image.setOnClickListener(this);
        load_image.setAnimation(alpAnimation);
        setLoad_Bg();
        alpAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override

            public void onAnimationEnd(Animation arg0) {

                go2MainActivity();

            }
        });

    }

    public abstract void setLoad_Bg();

    public abstract void go2MainActivity();

    public abstract void setWelcomeImgList();

    @Override
    public void onClick(View v) {

    }

    /**
     * 更新后台请求地址
     */
    public void reviewConnectApiUrl(){

    }

    private void initWelCom() {
        setContentView(R.layout.sk_l_activity_welcome);
        mViewPager = getViewById(R.id.sk_id_welcome_viewpager);
        setWelcomeImgList();
        LayoutInflater mLi = LayoutInflater.from(this);
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapOptions.inPurgeable = true;
        bitmapOptions.inInputShareable = true;

        ArrayList<View> views = new ArrayList<View>();

        for(int i = 0; i < welcomeImgList.size() ; i++){
            View view = mLi.inflate(R.layout.sk_l_item_welcome, null);
            XCImageView imageView = (XCImageView) view.findViewById(R.id.sk_id_welcome_img);
            imageView.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), welcomeImgList.get(i), bitmapOptions)));
            view.setBackgroundColor(bgColorList.get(i));
            views.add(view);
        }


        views.get(welcomeImgList.size()-1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                go2MainActivity();
                finish();
            }
        });

        //填充ViewPager的数据适配器
        pagerAdapter mPagerAdapter = new pagerAdapter(views);
        mViewPager.setAdapter(mPagerAdapter);


    }


    class pagerAdapter extends PagerAdapter {

        private ArrayList<View> views;

        public pagerAdapter(ArrayList<View> views) {

            this.views = views;
        }

        @Override
        public int getCount() {
            return this.views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        //页面view
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }

    }

    @Override
    public void listeners() {

    }

    @Override
    public void initWidgets() {

    }

    @Override
    public void onNetRefresh() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        welcomeImgList = null;
        bgColorList = null;
    }
}
