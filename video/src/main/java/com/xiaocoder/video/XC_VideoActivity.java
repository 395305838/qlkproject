package com.xiaocoder.video;


import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;


public class XC_VideoActivity extends XCBaseActivity {
    private SurfaceView surface_view;
    private SurfaceHolder surface_holder;
    private Uri uri;
    private XCVideoPlayerPop player_controller_pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.xc_l_activity_video);
        super.onCreate(savedInstanceState);
    }

    /**
     * urfaceHolder还提供了很多重要的方法，其中最重要的就是：
     * <p/>
     * 1.        abstract void addCallback(SurfaceHolder.Callbackcallback)
     * <p/>
     * 为SurfaceHolder添加一个SurfaceHolder.Callback回调接口。
     * <p/>
     * 2.        abstract Canvas lockCanvas()
     * <p/>
     * 获取一个Canvas对象，并锁定之。所得到的Canvas对象，其实就是Surface中一个成员。
     * <p/>
     * 3.        abstract Canvas lockCanvas(Rectdirty)
     * <p/>
     * 同上。但只锁定dirty所指定的矩形区域，因此效率更高。
     * <p/>
     * 4.        abstract void unlockCanvasAndPost(Canvascanvas)
     * <p/>
     * 当修改Surface中的数据完成后，释放同步锁，并提交改变，然后将新的数据进行展示，同时Surface中相关数据会被丢失。
     * <p/>
     * 5.      public abstract void setType (int type)
     * <p/>
     * 设置Surface的类型，接收如下的参数：
     * <p/>
     * SURFACE_TYPE_NORMAL：用RAM缓存原生数据的普通Surface
     * <p/>
     * SURFACE_TYPE_HARDWARE：适用于DMA(Direct memory access )引擎和硬件加速的Surface
     * <p/>
     * SURFACE_TYPE_GPU：适用于GPU加速的Surface
     * <p/>
     * SURFACE_TYPE_PUSH_BUFFERS：表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，在Camera图像预览中就使用该类型的Surface，有Camera负责提供给预览Surface数据，这样图像预览会比较流畅。如果设置这种类型则就不能调用lockCanvas来获取Canvas对象了。需要注意的是，在高版本的Android SDK中，setType这个方法已经被depreciated了。
     */
    private void initSurfaceView() {
        surface_view = (SurfaceView) findViewById(R.id.surface_view);
        surface_view.setFocusable(true);
        surface_view.setFocusableInTouchMode(true);
        surface_holder = surface_view.getHolder();
        // holder.setFormat(PixelFormat.OPAQUE);//这是默认值
        surface_holder.setFormat(PixelFormat.RGBA_8888); //否则没图像
        // holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//默认就是这个,设置SurfaceView不维护自己的缓冲区,而是等待屏幕的渲染引擎将内容推送到用户面前
        // 给SurfaceView当前的持有者一个回调对象,必须的
        surface_holder.addCallback(new Callback() {
            // 当activity退出,或进入下一个activity时,surface会销毁
            // 当手动的按锁屏键时,pause()-->stop() 不会调用surface的销毁方法
            // 当手动的按home键时,pause()-->surface销毁-->stop(),会调用surface的销毁方法
            // 当自动锁屏时,pause()-->stop() 不会调用surface的销毁方法
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                XCApp.i("surfaceDestroyed");
                player_controller_pop.release();
            }

            // 该方法调用的时候surface还没有真正的创建,只有当调用了srufaceChanged的时候.才算创建了 ??
            // 当activity从暂停状态重新到运行状态的时候,会调用oncreate方法
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                XCApp.i("surfaceCreated");
                if (player_controller_pop == null) {
                    player_controller_pop = new XCVideoPlayerPop(XC_VideoActivity.this, uri, surface_view);//这里是创建播放器,非得放这里,因为只有surface创建好了后,才可以player.setDisplay(surface_holder);,否则报错 surface has beeb release
                    XCApp.i("player_controller_pop created");
                } else {
                    player_controller_pop.launchMediaPlayer(uri);
                    XCApp.i("player_controller_pop re_init");
                }
            }

            // surfaceview的大小改变的时候 或者说surfaceView刷新的时候 都会调用该方法
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                XCApp.i("surfaceChanged" + "--width" + width + "/height" + height);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        XCApp.i("videoActivity--onStart");
    }

    //如果surface没销毁就从这里开始进入播放,如果销毁了就从surfaceChanged方法里面进入播放
    @Override
    protected void onResume() {
        super.onResume();
        XCApp.i("videoActivity--onResume");
        if (player_controller_pop != null) {
            player_controller_pop.recoverState();
        }
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {//设置屏幕横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        XCApp.i("videoActivity--onPause");
        if (player_controller_pop != null) {
            player_controller_pop.saveState();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        XCApp.i("videoActivity--onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XCApp.i("videoActivity--onDestroy");
        if (player_controller_pop != null) {
            player_controller_pop.closeThreadPool();
        }
    }


    @Override
    public void initWidgets() {
        uri = interceptUri();
        initSurfaceView();
    }

    @Override
    public void listeners() {

    }

}
