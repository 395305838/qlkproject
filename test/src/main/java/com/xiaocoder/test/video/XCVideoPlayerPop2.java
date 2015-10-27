package com.xiaocoder.test.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.im.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 该类只暴露出 release  lanuch   savaState   recoverState  closeThreadPool
 * 必须等到activity中的surface创建好了之后才可以创建该类, 在activity中创建的时候,传入context  surfaceview  uri
 * 在onPause和onResume方法中分别调用 savaState recoverState
 * surface销毁的时候调用release,surface重建好了后嗲用launch
 * activity销毁的时候closeThreadPool
 *
 * @author honor
 */
public class XCVideoPlayerPop2 implements OnErrorListener, OnCompletionListener, OnBufferingUpdateListener, OnVideoSizeChangedListener, OnPreparedListener, Serializable {
    private Context context;
    private TextView move_time;
    private SeekBar time_seekbar;
    private TextView duration_time;
    private ImageButton options;
    private ImageButton last;
    private ImageButton pause_or_start;
    private ImageButton next;
    private ImageButton volumn;
    // 以上是popupWindow上的控件
    private Window window;
    private Vibrator vibrator;
    private AudioManager audioManager;
    private View controlView;
    private SurfaceView surface_view;
    private ViewGroup parent_of_surface_view; // pop固定在此view上,maskview也固定在此view上
    private LinearLayout volumn_or_brightness_changed;
    private ImageView volumn_or_brightness_imageview;
    private TextView volumn_or_brightness_textview;
    private SurfaceHolder surface_holder;
    private PopupWindow popController;
    private PopupWindow pop_volumn_or_brightness_changed;
    private MediaPlayer player;
    private Uri uri;
    private static final int FULL_SCREEN = 1;
    private static final int SCALE_TO_FIFTY = 2;
    private static final int REAL_OF_VIDEO = 3;
    private static final int UPDATE_SEEK_BAR = 4;
    private int screenWidth;
    private int screenHeight;
    private int popWidth;
    private int popHeight;
    private int videoHeight; // 这个是video的真实大小,由video自身决定,与surface_view的宽高无关
    private int videoWidth;
    private int currentBufferingPercentage;
    private int duration;
    private int seekToWhenPreparedFinished; // 当缓冲完成后,应该跳到指定的位置
    private int maxVolumn;
    private int currentVolumn;
    private boolean isPrepared;
    private boolean isPause;
    private boolean isLoop;
    private boolean isVibrate;
    private boolean isVideoSlient;

    // 以下是当切换视频屏幕时,屏幕会变形,所以在屏幕变形的时候设置屏幕变黑,正常后再显示surfaceView,所以需要线程定时
    // 需要handler记录时间textview和handler的更新
    private ExecutorService threadpool;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SEEK_BAR:
                    // Logs.i(MyConfig.DEBUG_TAG, "update_seek");
                    int current = getCurrentPosition();
                    int second = current / 1000;
                    int minute = second / 60;
                    int hour = minute / 60; // -->获得到hour
                    minute = minute % 60;
                    second = second % 60;
                    move_time.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                    time_seekbar.setProgress(current);
                    if (!isPause) {
                        sendEmptyMessageDelayed(UPDATE_SEEK_BAR, 1000);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 在onDestroy中调用,如果是在释放中关闭,可能关闭的时候,线程里的内容还没运行完,会抛RejectedExecutionException
     */
    public void closeThreadPool() {
        if (threadpool != null) {
            try {
                threadpool.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public void setVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    public XCVideoPlayerPop2(Context context, Uri uri, SurfaceView surface_view) {
        if (surface_view == null || context == null) {
            return;
        }
        this.context = context;
        this.surface_view = surface_view;
        this.uri = uri;
        init();
    }

    private void init() {
        // 获取播放相关的高宽
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = manager.getDefaultDisplay().getWidth();
        screenHeight = manager.getDefaultDisplay().getHeight();

        popWidth = screenWidth;
        popHeight = UtilScreen.dip2px(context, 70);
        // 如果是播放视频才需要该字段
        threadpool = Executors.newSingleThreadExecutor();
        parent_of_surface_view = (ViewGroup) surface_view.getParent();
        // 创建音量调节和屏幕亮度调节的视图 ,该视图有imageview和textview组成
        volumn_or_brightness_changed = new LinearLayout(context);
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        volumn_or_brightness_changed.setLayoutParams(param);
        volumn_or_brightness_changed.setOrientation(LinearLayout.HORIZONTAL);
        volumn_or_brightness_changed.setBackgroundResource(R.drawable.xc_dd_chat_volumn_or_brightness_background_shape);
        // 创建调节音量pop中的imageview
        volumn_or_brightness_imageview = new ImageView(context);
        LinearLayout.LayoutParams param_imageview = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        param_imageview.gravity = Gravity.CENTER;
        volumn_or_brightness_imageview.setScaleType(ScaleType.CENTER_CROP);
        volumn_or_brightness_imageview.setLayoutParams(param_imageview);
        volumn_or_brightness_changed.addView(volumn_or_brightness_imageview);
        // 创建创建调节音量pop中的textview
        volumn_or_brightness_textview = new TextView(context);
        volumn_or_brightness_textview.setBackgroundColor(0x00aa0000);
        volumn_or_brightness_textview.setTextColor(0xffffffff);
        LinearLayout.LayoutParams param_textview = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        param_textview.gravity = Gravity.CENTER;
        volumn_or_brightness_textview.setGravity(Gravity.CENTER);
        volumn_or_brightness_textview.setLayoutParams(param_textview);
        volumn_or_brightness_changed.addView(volumn_or_brightness_textview);
        //创建该pop
        pop_volumn_or_brightness_changed = new PopupWindow(volumn_or_brightness_changed, screenHeight / 5, screenWidth / 6);
        //控制器
        controlView = LayoutInflater.from(context).inflate(R.layout.xc_l_view_chat_video_play_bottom_pop, null);
        options = (ImageButton) controlView.findViewById(R.id.options);
        last = (ImageButton) controlView.findViewById(R.id.last);
        pause_or_start = (ImageButton) controlView.findViewById(R.id.pause_or_start);
        next = (ImageButton) controlView.findViewById(R.id.next);
        volumn = (ImageButton) controlView.findViewById(R.id.volumn);
        move_time = (TextView) controlView.findViewById(R.id.move_time);
        duration_time = (TextView) controlView.findViewById(R.id.duration_time);
        time_seekbar = (SeekBar) controlView.findViewById(R.id.time_seekbar);
        // 添加popwindow上的按钮的监听
        addControlViewListeners();
        // 获取震动服务和声音服务
        window = ((Activity) context).getWindow();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 创建popwindow
        popController = new PopupWindow(controlView, popWidth, popHeight);// 构建popupWindow,传入的是需要显示的view
//        popController.setBackgroundDrawable(new ColorDrawable(0x550000ff));// 这个设置pop的背景,如果设置了FILL_PARENT,那么整个屏幕都是背景
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surface_view.getLayoutParams();
//        layoutParams.setMargins(0, popHeight, 0, popHeight);
//        surface_view.setLayoutParams(layoutParams);
        popController.setAnimationStyle(R.style.pop);

        //howPop();
        // 初始化播放器相关的控制参数
        initPlayerFlags();
        // 创建和启动播放器
        launchMediaPlayer(uri);
    }

    private void addControlViewListeners() {

        options.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                surface_view.setBackgroundColor(0xff000000);
                threadpool.execute(enterBlackRunnable);
            }
        });
        pause_or_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    start();
                } else {
                    pause();
                }
            }
        });

        time_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekToWhenPreparedFinished = progress;
                    if (!isPause) {
                        start();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        volumn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVideoSlient) {
                    currentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    isVideoSlient = true;
                    volumn.setImageResource(R.drawable.xc_dd_chat_soundmute);
                    return;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, 0);
                isVideoSlient = false;
                volumn.setImageResource(R.drawable.xc_dd_chat_soundcontrol);
            }
        });

        // 控制音量与暗度的监听
        parent_of_surface_view.setOnTouchListener(new OnTouchListener() {
            long down_time;
            long up_time;
            float y1; // 记录起点或者是拐点
            float y2;
            float y2_temp;// 用来保存y2上一次滑到了哪里
            boolean first_move;
            int count = 1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        XCApp.i("down");
                        down_time = System.currentTimeMillis();
                        first_move = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        up_time = System.currentTimeMillis();
                        if (up_time - down_time < 350) {
                            if (isPopShowing()) {
                                dismissControlPop();
                            } else {
                                showControlPop();
                            }
                        }
                        count = 1;
                        y2_temp = 0;
                        if (pop_volumn_or_brightness_changed != null) {
                            pop_volumn_or_brightness_changed.dismiss();
                        }
                        XCApp.i("up" + down_time);
                        break;
                    case MotionEvent.ACTION_MOVE: //  -->这里是可以双向滑动的
                        if (first_move) {
                            y1 = event.getRawY();
                            first_move = false;
                        }
                        y2 = event.getRawY();

                        XCApp.i("move_y1 is " + y1); // 每一个滑动过程中
                        XCApp.i("move_y2 is " + y2); // y2是变化的

                        float distance = y1 - y2;
                        if (distance > 0) {
                            // 为向上滑动,y2不断减小,y2_temp即y2的前一次值应该是大于y2的
                            if (y2_temp < y2) {
                                y1 = y2_temp;// 记录拐点
                                count = 1;
                            }
                        } else if (distance < 0) {
                            if (y2_temp > y2) {
                                y1 = y2_temp;
                                count = 1;
                            }
                        }
                        y2_temp = y2;
                        distance = y1 - y2;
                        if (event.getRawX() < screenWidth / 3.0) {

                            if (distance > 25 * count) { // 每滑动到40点像素的整倍数的时候,就调节亮度一次,亮度在0.1-1之间,每次调节0.02
//                                setBrightness(1);
                                count++;
                            } else if (distance < -40 * count) {
//                                setBrightness(-1);
                                count++;
                            }
                            XCApp.i("left");
                        }

                        if (event.getRawX() > screenWidth * 2 / 3.0) {
                            if (distance > 60 * count) { // 每滑动到60点像素的整倍数的时候,就调节声音一次,声音在0-15之间,每次调节1
//                                setVolumn(1);
                                count++;
                            } else if (distance < -90 * count) {
//                                setVolumn(-1);
                                count++;
                            }
                            XCApp.i("right");
                        }
                    default:
                        break;
                }
                return true; // 这里一定要返回true,如果是false的话,那么监听不到move和up
            }
        });
    }

	/*
     * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮
	 */

    private void setBrightness(int direction) {
        WindowManager.LayoutParams lp = window.getAttributes();
        // 设置屏幕亮度属性 范围 0.0 --- 1.0 0.0全黑（切记不能设置成0.0） 1.0最亮;如果设置成了 0.0无论怎么滑都是黑的了
        lp.screenBrightness = lp.screenBrightness + direction * 0.02f;
        // 1.1
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if (lp.screenBrightness < 0.1) {
            lp.screenBrightness = (float) 0.1;
        }
        // 通过pattern指定方式的震动时间也是最多只有2秒左右，比如{100, 5000, 100, 5000}
        // 表示暂停0.01秒，震动0.2秒
        if (isVibrate) {
            long[] pattern = {100, 200}; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1); // -1不重复，非-1为从pattern的指定下标开始重复
        }
        window.setAttributes(lp);
        volumn_or_brightness_imageview.setImageResource(R.drawable.xc_d_chat_player_icon_bright);
        volumn_or_brightness_textview.setText((int) ((lp.screenBrightness - 0.1) / (1 - 0.1) * 100) + "%");

        if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            pop_volumn_or_brightness_changed.showAsDropDown(parent_of_surface_view, (screenHeight - pop_volumn_or_brightness_changed.getWidth()) / 2,
                    -(screenWidth + pop_volumn_or_brightness_changed.getHeight()) / 2);
        } else {
            pop_volumn_or_brightness_changed.showAsDropDown(parent_of_surface_view, (screenWidth - pop_volumn_or_brightness_changed.getWidth()) / 2,
                    -(screenHeight + pop_volumn_or_brightness_changed.getHeight()) / 2);
        }
    }

    private void setVolumn(int direction) {
        if (audioManager != null) {
            if (isVideoSlient) {// 是否静音
                // 设置当前音量
                volumn_or_brightness_imageview.setImageResource(R.drawable.xc_d_chat_soundmute_normal);
                volumn_or_brightness_textview.setText("静音模式");
                if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    pop_volumn_or_brightness_changed.showAsDropDown(parent_of_surface_view, (screenHeight - pop_volumn_or_brightness_changed.getWidth()) / 2,
                            -(screenWidth + pop_volumn_or_brightness_changed.getHeight()) / 2);
                } else {
                    pop_volumn_or_brightness_changed.showAsDropDown(parent_of_surface_view, (screenWidth - pop_volumn_or_brightness_changed.getWidth()) / 2,
                            -(screenHeight + pop_volumn_or_brightness_changed.getHeight()) / 2);
                }
                return;
            }
            // 设置当前音量到 index
            currentVolumn = currentVolumn + direction * 1;

            if (currentVolumn <= maxVolumn && currentVolumn >= 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, 0);
            } else if (currentVolumn > maxVolumn) {
                currentVolumn = maxVolumn;
            } else {
                currentVolumn = 0;
            }
            XCApp.i("current--" + currentVolumn);
            XCApp.i("maxVolumn--" + maxVolumn);
            volumn_or_brightness_imageview.setImageResource(R.drawable.xc_d_chat_soundcontrol_normal);
            volumn_or_brightness_textview.setText((int) ((currentVolumn * 1.0 / maxVolumn) * 100) + "%");

            if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                pop_volumn_or_brightness_changed.showAsDropDown(parent_of_surface_view, (screenHeight - pop_volumn_or_brightness_changed.getWidth()) / 2,
                        -(screenWidth + pop_volumn_or_brightness_changed.getHeight()) / 2);
            } else {
                pop_volumn_or_brightness_changed.showAsDropDown(parent_of_surface_view, (screenWidth - pop_volumn_or_brightness_changed.getWidth()) / 2,
                        -(screenHeight + pop_volumn_or_brightness_changed.getHeight()) / 2);
            }
        }
    }

    // openMediaPlayer该方法一定要在surface(不是surfaceview)创建完了之后才可以使用,即在callback中调用,否则报surface
    // has been relesed 异常,因为setDisplay的时候,surface还没有创建好
    public void launchMediaPlayer(Uri uri) {
        try {
            if (uri == null) {
                return;
            }

            if (player == null) {
                player = new MediaPlayer();
            }
            player.reset();
            player.setDataSource(context, uri);
            surface_holder = surface_view.getHolder();
            player.setDisplay(surface_holder);
            player.setScreenOnWhilePlaying(true);
            player.setLooping(isLoop); // 这貌似是循环播放的唯一方式,如果自己定义一个flag没用
            // player.seekTo(position); //不能写在这里,报错,还没有缓冲好
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			((Activity)context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            addMediaPlayerListener();
            player.prepareAsync();
            XCApp.i("player has been created");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPlayerFlags() {
        maxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentBufferingPercentage = 0;// 默认缓冲了0
        duration = 0; // 默认总时长为0
        seekToWhenPreparedFinished = 0; // 当缓冲完成的时候,应该跳到指定的位置
        isPrepared = false;
        isPause = false;
        isLoop = false;
        isVibrate = false;
        isVideoSlient = false;
    }

    private void addMediaPlayerListener() {
        if (player != null) {
            // 这个是视频加载时,回调的监听方法
            player.setOnPreparedListener(this);
            player.setOnBufferingUpdateListener(this);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            // 这个是video的宽高,只有在播放视频的时候才设置该监听器
            player.setOnVideoSizeChangedListener(this);
        }
    }

    /**
     * 设置播放视频的尺寸大小
     */
    private void setSurfaceViewScale(int type) {
        switch (type) {
            case FULL_SCREEN:
                setSurfaceViewScaleParam(screenWidth, screenHeight);
                break;
            case SCALE_TO_FIFTY:
                setSurfaceViewScaleParam((int) (screenWidth / 1.5), (int) (screenHeight / 1.5));
                break;
            case REAL_OF_VIDEO:
                setSurfaceViewScaleParam(videoWidth, videoHeight);
                break;
            default:
                setSurfaceViewScaleParam(screenWidth, screenHeight);
                break;
        }
    }

    private void setSurfaceViewScaleParam(int width, int height) {
        LayoutParams layoutParams = surface_view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        surface_view.setLayoutParams(layoutParams);
        threadpool.execute(quitBlackRunnable);
    }

    Runnable enterBlack = new Runnable() {
        @Override
        public void run() {
            dismissControlPop();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {

                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                // 更改popcontroll的宽高

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surface_view.getLayoutParams();
                layoutParams.setMargins(0, popHeight, 0, popHeight);
                surface_view.setLayoutParams(layoutParams);

                popController.setHeight(popHeight);
                popController.setWidth(screenWidth);

            } else {

                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surface_view.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                surface_view.setLayoutParams(layoutParams);

                popController.setHeight(popHeight);
                popController.setWidth(screenHeight);
            }
            threadpool.execute(quitBlackRunnable);
        }
    };

    Runnable enterBlackRunnable = new Runnable() {
        @Override
        public void run() {
            handler.post(enterBlack);
        }
    };

    Runnable quitBlack = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            surface_view.setBackgroundColor(0x00000000);
        }
    };

    Runnable quitBlackRunnable = new Runnable() {
        @Override
        public void run() {
            handler.post(quitBlack);
        }
    };

    private void showControlPop() {
//        if (popController != null && surface_view.isShown()) {
//            popController.showAsDropDown(parent_of_surface_view, 0, -popHeight);
//        }
//        XCApp.base_log.i(XCConfig.TAG_SYSTEM_OUT, "showController");
    }

    private void dismissControlPop() {
//        if (popController != null && surface_view.isShown()) {
//            popController.dismiss();
//        }
//        XCApp.base_log.i(XCConfig.TAG_SYSTEM_OUT, "dismissController");
    }

    private boolean isPopShowing() {
//        return popController.isShowing();
        return false;
    }

    /**
     * 获得当前视频总时长
     */
    private int getDuration() {
        if (player != null && isPrepared) {
            return (int) player.getDuration();
        }
        return -1;
    }

    /**
     * 获得视频在线缓存百分比,在线播放的时候
     */
    private int getBufferPercentage() {
        return currentBufferingPercentage;
    }

    /**
     * 获得当前视频播放位置
     */
    private int getCurrentPosition() {
        if (player != null && isPrepared) {
            XCApp.i("getCurrentPosition");
            return (int) player.getCurrentPosition();
        }
        return 0;
    }

    // ------------------------------------------以下是mediaplayer的监听方法-----------------------------------------------------
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //查看what extra 切换到软解码

        XCApp.i("onError");
        release();
        new AlertDialog.Builder(context).setTitle("对不起")// 标题
                .setMessage("您所播的视频格式不正确，播放已停止。")// 提示消息
                .setPositiveButton("知道了",// 确定按钮
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                release();
                            }
                        }).setCancelable(false)// 设置撤销键 不好用
                .show();
        return false;
    }

    // 当播放完成后,如本地视频,设置了循环播放,那么再次播放的时候,不会经过onPrepared方法
    // 当调用了onCompletion方法后,不会再一直保持屏幕的高亮的显示,这里没看源码,估计源码里取消该设置
    // 如果要循环播放,值有通过setLooping方法来实现.如果是如下
    /*
     * if (isloop == true) { time_seekbar.setProgress(0); seekTo(0); start();
	 * },这种方式会莫名其妙的错误,而且屏幕会关
	 */
    // setLooping方式会自动把时间和seekbar调零,原理是循环播放时getCurrentDuration为0
    // 循环播放的时候,当结束时,然后开始下一次播放,这期间不会调用onCompletion监听
    // 按home键或返回键退出界面,不会触发该方法,该方法只有在真实的播放完全后才会触发
    // 小结:感觉onCompletion是播放全部结束标志,不管之前设置了什么都没用,只有真正播放结束了才会调用该监听方法
    @Override
    public void onCompletion(MediaPlayer mp) {
        XCApp.i("onCompletion" + "-isLoop" + isLoop);
        // 这里可以判断播放列表的下一个播放的视频是什么
        pause_or_start.setImageResource(R.drawable.xc_dd_chat_play);// 按暂停后显示的图片
        isPause = true;//如果不设置,还会不断的发送更新消息
        seekToWhenPreparedFinished = 0;

        //重新播放


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, 700);



        /*
         */// seekTo(0);
        // //当播放完了后,即使不写这句,也会默认从0开始播放;但是如果写了这句停止的画面会停留在开始的第一个图片,如果不写这句,会停留在结束的最后一张图片

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        XCApp.i("onBufferingUpdate---" + percent);
//		isPrepared = false;
        currentBufferingPercentage = percent; // 记录缓冲的进度条
    }

    /**
     * 初始化时候的顺序 surfacechanged -->onVideoSizeChanged -->onPrepared
     * 注:videosizechanged仅仅在初始化的时候会被执行一次,得到的值是视频的原始宽高
     * surfacechanged方法在surface_view的宽高每次改变的时候都会被调用
     * ,但是videosizechanged在surface_view改变的时候不会被调用
     * player.getVideoHeight/Width也是video的真实值,不是surface_view的宽高
     */
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        XCApp.i("onVideoSizeChangedListener" + "--width" + width + "/height" + height);
        videoHeight = height;
        videoWidth = width;
    }

    // 当播放完成后,如本地视频,设置了循环播放,那么再次播放的时候,不会经过onPrepared方法
    @Override
    public void onPrepared(MediaPlayer mp) {
        XCApp.i("OnPreparedListener" + "--the width of mediaplayer is " + player.getVideoWidth() + "--the height of mediaplayer is" + player.getVideoHeight());
        // 缓冲好了后,设置seekbar的最大值
        int duration = (int) player.getDuration();
        time_seekbar.setMax(duration);
        isPrepared = true;
        int totalSecond = duration / 1000;// 共多少秒
        int minute = totalSecond / 60;// 共多少分
        int hour = minute / 60;// 共多少小时 -->得到了hour
        int second = totalSecond % 60;// 得到余下的秒数 -->得到了second
        minute = minute % 60;// 得到余下的分钟数 -->得到了minute
        duration_time.setText(String.format("%02d:%02d:%02d", hour, minute, second));
        player.seekTo(seekToWhenPreparedFinished);
        if (!isPause) {
            start();// 继续播放
        }
        XCApp.i("seekToWhenPreparedFinished" + seekToWhenPreparedFinished);
    }

    public void saveState() {
        if (player != null) {
            player.pause();
            if (seekToWhenPreparedFinished != 0) {
                seekToWhenPreparedFinished = (int) player.getCurrentPosition();
            }
        }
    }

    public void recoverState() {
        if (player != null && isPrepared) {
            if (!isPause) {
                player.seekTo(seekToWhenPreparedFinished);
                player.start();
            }
        }
    }

    public void release() {
        if (isPopShowing()) {
            dismissControlPop();
        }
        if (player != null) {
            player.stop(); // 调用了这个方法屏幕就不会再持续显示onwhile
            player.reset();
            player.release();
            player = null; // 没这句就报错,why?
            pause_or_start.setImageResource(R.drawable.xc_dd_chat_play);// 按暂停后显示的图片
        }
        XCApp.i("release");
    }

    private void pause() {
        if (player != null && isPrepared) {
            player.pause();
            pause_or_start.setImageResource(R.drawable.xc_dd_chat_play);// 按暂停后显示的图片
            isPause = true;
            seekToWhenPreparedFinished = (int) player.getCurrentPosition();
        }
    }

    private void start() {
        if (player != null && isPrepared) {
            player.seekTo(seekToWhenPreparedFinished); // 继续播放,当播放完了后即onCompletion执行了后,画面会停留在最后一张图片(如果设置了seekto(0),则停留在第一张图片)
            player.start();
            pause_or_start.setImageResource(R.drawable.xc_dd_chat_pause);
            handler.sendEmptyMessage(UPDATE_SEEK_BAR);
            isPause = false;
        }
    }
}
