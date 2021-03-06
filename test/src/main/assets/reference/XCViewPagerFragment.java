package reference;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.xiaocoder.android.fw.general.function.adapter.XCAdapterViewPager;
import com.xiaocoder.android.fw.general.application.XCBaseFragment;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android_fw_general.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaocoder
 * @Description: 封装可以自动滑动的viewpager, 该viewpager加入了线程打断功能, 即当切换到一张图片时,
 * 会重新开始计时, 提升用户体验; 用法添加该fragment后, 调用setData和setAllowSlide方法即可
 * @date 2015-1-6 下午10:50:22
 */
@Deprecated
public class XCViewPagerFragment extends XCBaseFragment {

    ViewPager qlk_id_viewpager;
    LinearLayout qlk_id_dots;
    List<ImageView> imageviews;
    List<View> dots;
    List<String> urls;
    XCAdapterViewPager adapter;

    int last_dot_position = 0;// 记录上一次点的位置
    int currentItem; // 当前页面
    boolean viewpagerisRunning;
    int total_images;
    boolean is_allow_running; // 从外部设置是否可以自动滑动

    Thread time_thread; // 打断线程用的 -->否则会有一个图片切换时在计时上的bug

    public interface OnLoadImage {
        void onLoadImage(ImageView imageview, String url);
    }

    public interface OnImageClickListener {
        void onImageClickListener(int position);
    }

    OnImageClickListener listener;

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    OnLoadImage on_load_image_listener;

    public void setOnLoadImageListener(OnLoadImage on_load_image_listener) {
        this.on_load_image_listener = on_load_image_listener;
    }

    // 会等父fragment的onActivityCreated完成后才会调用子fragment的onCreate()方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_viewpager);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onImageClickListener((Integer) (v.getTag()));
        }
    }

    public void setData(List<String> urls) {
        this.urls = urls;

    }

    long time_gap = 2000;

    public void setAllowAutoSlide(boolean is_allow_running, long time_gap) {
        this.is_allow_running = is_allow_running;
        this.time_gap = time_gap;
    }

    private void createImageViewsAndDots() {
        // 创建images
        imageviews = new ArrayList<ImageView>();
        // 创建dots
        dots = new ArrayList<View>();
        total_images = urls.size();

        for (int i = 0; i < total_images; i++) {

            if (createImages(i)) {
                return;
            }
            createDots(i);
        }
    }

    private void createDots(int i) {
        // 创建dots
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.xc_l_view_viewpager_dot, null);
        LayoutParams ll = new LayoutParams(UtilScreen.dip2px(getActivity(), 7), UtilScreen.dip2px(getActivity(), 7));
        ll.setMargins(UtilScreen.dip2px(getActivity(), 3), 0, UtilScreen.dip2px(getActivity(), 3), 0);
        view.setLayoutParams(ll);
        dots.add(view);
        if (i == 0) {
            view.setBackgroundResource(R.drawable.xc_dd_point_blue_471c87);
            last_dot_position = 0;
        } else {
            view.setBackgroundResource(R.drawable.xc_dd_point_gray_55000000);
        }
        qlk_id_dots.addView(view);
    }

    private boolean createImages(int i) {
        // 创建images
        ImageView imageview = new ImageView(getActivity());
        // ---------------------------加载图片-----------------------------
        if (on_load_image_listener != null) {
            on_load_image_listener.onLoadImage(imageview, urls.get(i));

        } else {
            return true;
        }
        imageview.setOnClickListener(this);
        imageview.setTag(i);
        imageviews.add(imageview);
        return false;
    }

    protected void createViewPager() {
        adapter = new XCAdapterViewPager(imageviews);
        qlk_id_viewpager.setAdapter(adapter);
        qlk_id_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dots.get(last_dot_position).setBackgroundResource(R.drawable.xc_dd_point_gray_55000000);
                dots.get(position).setBackgroundResource(R.drawable.xc_dd_point_blue_471c87);
                last_dot_position = position;
                currentItem = position;

                if (time_thread != null) {
                    time_thread.interrupt();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        viewpagerisRunning = is_allow_running;
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (time_thread == null) {
                    time_thread = Thread.currentThread();
                }
                while (viewpagerisRunning) {
                    try {
                        publishProgress(currentItem);
                        Thread.sleep(time_gap);
                        if (currentItem == total_images - 1) {
                            currentItem = 0;
                        } else {
                            currentItem += 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                qlk_id_viewpager.setCurrentItem(currentItem);
            }
        }.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewpagerisRunning = false;
        if (time_thread != null) {
            time_thread.interrupt();
            time_thread = null;
        }
    }

    @Override
    public void initWidgets() {
        qlk_id_viewpager = getViewById(R.id.xc_id_fragment_viewpager);
        qlk_id_dots = getViewById(R.id.xc_id_fragment_viewpager_dots);

        if (urls != null) {
            createImageViewsAndDots();
            createViewPager();
        }
    }

    @Override
    public void listeners() {

    }

}
