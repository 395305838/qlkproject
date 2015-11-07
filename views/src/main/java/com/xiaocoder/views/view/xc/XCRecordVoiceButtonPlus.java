package com.xiaocoder.views.view.xc;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.util.UtilDate;
import com.xiaocoder.views.R;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 可以录音的button，可扩展
 */
public class XCRecordVoiceButtonPlus extends Button implements OnTouchListener {

    private MediaRecorder media_recorder;
    /**
     * 录音成功返回的文件存储在哪个目录里
     */
    public String save_dir;
    /**
     * 录音成功返回的文件
     */
    private File save_file;
    /**
     * 时间的压缩参数
     */
    public static double COMPRESS_RATIO = 0.75;
    /**
     * 录音最短时间的限制，毫秒
     * 会用真实的录音时间（录音开始时间-结束时间）与 MIN_TIME 比较，是否小于了录音时间的限制
     */
    public static int MIN_TIME = 3000;
    /**
     * 录音最长时间的限制，毫秒.
     * 会用真实的录音时间（录音开始时间-结束时间）与 MAX_TIME 比较，是否超过了录音时间的限制
     */
    public static int MAX_TIME = 30000;
    /**
     * 每隔多少时间，time减减一次，毫秒
     */
    public static final int SLEEP_TIME = (int) (1000 * COMPRESS_RATIO);
    /**
     * 从哪一个时间开始倒计时（线程里的time--的初始值用的就是这个），秒。
     * <p/>
     * 有的时候可能显示的是60秒，但是真实可以录制的时间只给45秒，比如wx
     */
    public static final int FAKE_TIME = (int) (MAX_TIME / COMPRESS_RATIO / 1000);
    /**
     * 记录每次录音的开始时间
     */
    private long start_time;
    /**
     * 记录每次录音的结束时间
     */
    private long end_time;
    /**
     * 每一次按下button时，默认没有移出边界
     */
    boolean boundaryOut;
    /**
     * 防止快速点击，做了一个时间间隔的限制，及上次点击与这次点击的时间间隔必须大于 CLICK_LIMIT 毫秒，才有效
     */
    long record_last_time;
    /**
     * 毫秒
     */
    public static int CLICK_LIMIT = 200;

    public interface OnButtonStatus {

        /**
         * 当点击button时，最先调用该方法
         */
        void onBeforeRecoder();

        /**
         * 每隔SLEEP_TIME秒，回调该方法一次，time初始值是FAKE_TIME
         */
        void onUpdateTime(int time);

        /**
         * 刚刚触摸，这里建议 ：show你的dialog，可以给“松开发送”的提示
         */
        void onStartTouch();

        /**
         * 移出button范围，但没有up,这里建议：可以给“松开取消发送”的提示
         */
        void onMoveOut();

        /**
         * 移出button范围，但没有up，这时又移入button，没有up，这里建议：可以给“松开发送”的提示
         */
        void onMoveIn();

        /**
         * 结束触摸，这里建议：close你的dialog
         * <p/>
         * FORCE_STOP -- 每一次调用startTouch（）之前都会调用forceStop（），确保上一次的停止了
         * CANCLE_STOP -- 在onPause（） 和 up 手势触摸移出了button之外时
         * LESS_TIME_STOP -- 正常up手势。声音时长太短，不符合要求；可以给录音时间太短的提示
         * OUT_TIME_STOP -- 正常up手势。声音时长太长，不符合要求；可以给录音时间超过最大时间限制的提示
         * EXCEPTION_STOP -- 异常停止
         * NORMAL_STOP -- 正常up手势停止，符合规则，传回file
         */
        void onEndTouch(RecoderStop stop);

        /**
         * 录音成功时回调，传回文件 和 时间
         */
        void onRecoderSuccess(File file, double time);

        enum RecoderStop {
            FORCE_STOP,
            CANCLE_STOP,
            LESS_TIME_STOP,
            OUT_TIME_STOP,
            EXCEPTION_STOP,
            NORMAL_STOP
        }
    }

    public OnButtonStatus listener;

    public void setOnButtonStatus(OnButtonStatus listener) {
        this.listener = listener;
    }

    public XCRecordVoiceButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        timeRunnable = new TimeRunnable();
    }

    public XCRecordVoiceButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        timeRunnable = new TimeRunnable();
    }

    /**
     * 子线程中更新textview用的
     */
    public class TimeRunnable implements Runnable {

        public void update(int time) {
            this.time = time;
        }

        int time;

        @Override
        public void run() {
            if (listener != null) {
                listener.onUpdateTime(time);
            }
        }
    }

    TimeRunnable timeRunnable;

    /**
     * 子线程中运行的time--代码
     */
    public class UpdateRunnable implements Runnable {

        public boolean isQuitNow = false;

        @Override
        public void run() {
            int i = FAKE_TIME;
            while (!isQuitNow && i >= 0) {
                try {
                    timeRunnable.update(i--);
                    XCApp.getBase_handler().post(timeRunnable);
                    Thread.sleep(SLEEP_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                    isQuitNow = true;
                }
            }
        }
    }

    public UpdateRunnable current_time_runnable;

    public void startTime() {
        XCApp.getBase_cache_threadpool().execute(current_time_runnable = new UpdateRunnable());
    }

    public void endTime() {
        if (current_time_runnable != null) {
            current_time_runnable.isQuitNow = true;
        }
    }

    public XCRecordVoiceButtonPlus(Context context) {
        super(context);
    }

    {
        super.setOnTouchListener(this);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        throw new RuntimeException(this + "-- touchlistener 已经设置了");
    }

    /**
     * dialog是否正在显示
     */
    private boolean isDialogShow;

    public void showDialog() {
        setBackgroundResource(R.drawable.xc_dd_line_gray_e1e1e1_bg_gray_cccccc_8);
        startTime();
        if (listener != null) {
            listener.onStartTouch();
        }
        isDialogShow = true;
    }

    public void closetDialog(OnButtonStatus.RecoderStop stop) {
        endTime();//该方法放前面
        setBackgroundResource(R.drawable.xc_dd_line_gray_e1e1e1_bg_white_ffffff_8);
        if (listener != null) {
            listener.onEndTouch(stop);
        }
        isDialogShow = false;
    }

    public boolean isDialogShow() {
        return isDialogShow;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        // 防止连续快速的点击
        if (System.currentTimeMillis() - record_last_time < CLICK_LIMIT) {
            record_last_time = System.currentTimeMillis();
            return false;
        }

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null) {
                    listener.onBeforeRecoder();
                }
                boundaryOut = false;
                XCApp.i("down");
                forceStop();// 确保停止了
                showDialog();
                startRecording();
                break;
            case MotionEvent.ACTION_UP:
                XCApp.i("up");
                if (isOutSide(event.getX(), event.getY())) {
                    // touch出界了
                    cancelRecord();
                    XCApp.i("出界了， 删除文件");
                } else {
                    XCApp.i("未出界");
                    stopRecording();
                }
                record_last_time = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOutSide(event.getX(), event.getY())) {
                    if (!boundaryOut) {
                        boundaryOut = true;
                        if (listener != null) {
                            listener.onMoveOut();
                        }
                    }
                } else {
                    if (boundaryOut) {
                        boundaryOut = false;
                        if (listener != null) {
                            listener.onMoveIn();
                        }
                    }
                }
                break;
            default:
                return false;
        }
        return false;
    }

    /**
     * 是否touch出了控件的边界
     */
    public boolean isOutSide(float x, float y) {
        if (x > getWidth() || x < 0 || y < 0 || y > getHeight()) {
            return true;
        }
        return false;
    }

    /**
     * 确保停止， 比如正在播放录音时，又按了录音键
     */
    public void forceStop() {
        if (media_recorder != null) {
            try {
                media_recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            media_recorder.release();
            media_recorder = null;
        }
        boundaryOut = false;
        closetDialog(OnButtonStatus.RecoderStop.FORCE_STOP);
    }

    /**
     * onPause（），以及出界了 的 取消录制， 并删除本地文件
     */
    private void cancelRecord() {
        if (media_recorder != null) {
            try {
                media_recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            media_recorder.release();
            media_recorder = null;
            deleteFile("cancle");
        }
        closetDialog(OnButtonStatus.RecoderStop.CANCLE_STOP);
    }

    private void quit(final OnButtonStatus.RecoderStop stop, long time) {
        XCApp.getBase_handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closetDialog(stop);
            }
        }, time);
    }

    /**
     * 停止录制，根据文件时间的长度决定是否上传
     */
    private void stopRecording() {
        // 是否正常停止
        boolean normal = true;
        if (media_recorder != null) {
            // 停止并释放资源
            try {
                // 点击录制后快速松开，会报异常
                media_recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
                XCApp.i("时间太短，停止时抛异常了 ，删除文件");
                normal = false;
            }
            media_recorder.release();
            media_recorder = null;
            if (!normal) {
                // 如果是异常停止
                deleteFile("exception");
                quit(OnButtonStatus.RecoderStop.EXCEPTION_STOP, 500);
                return;
            }
        }
        // 记录录制的结束时间
        end_time = System.currentTimeMillis();
        float gap = end_time - start_time; // 真实时间
        // 判断文件的时间是否符合要求
        if (gap > MAX_TIME) {
            XCApp.i("时间超时 ，删除文件");
            // XCApp.shortToast("发送失败，每段录音的最大时间限制为" + FAKE_TIME + "秒");
            deleteFile("up max time");
            quit(OnButtonStatus.RecoderStop.OUT_TIME_STOP, 100);
        } else if (gap < MIN_TIME) {
            XCApp.i("时间太短 ，删除文件");
            deleteFile("down min time");
            quit(OnButtonStatus.RecoderStop.LESS_TIME_STOP, 500);
        } else {
            XCApp.i("时间符合 ，准备上传文件");
            if (listener != null) {
                XCApp.i(gap + "---原始录音的buttonview中记录的时间");
                listener.onRecoderSuccess(save_file, gap / COMPRESS_RATIO);
                save_file = null;
            }
            closetDialog(OnButtonStatus.RecoderStop.NORMAL_STOP);
        }
    }

    public void deleteFile(String debug) {
        if (save_file != null && save_file.exists()) {
            save_file.delete();
            XCApp.i("TAG_CHAT", "delete---" + debug + save_file.getAbsolutePath());
            save_file = null;
        }
    }

    public void setSave_dir(String save_dir) {
        this.save_dir = save_dir;
    }

    private void startRecording() {
        save_file = XCIOAndroid.createFileInAndroid(getContext(), save_dir, "voice" + UtilDate.format(new Date(), UtilDate.FORMAT_FULL_S));
        media_recorder = getPreparedRecorder(save_file);
        start_time = System.currentTimeMillis();
        media_recorder.start();
    }

    /**
     * 该方法需要在activity的onpaused方法中调用
     */
    public void onActivityPaused() {
        cancelRecord();
    }

    private MediaRecorder getPreparedRecorder(File file) {
        MediaRecorder recorder = new MediaRecorder();
        // 设置为初始状态
        recorder.reset();
        // 设置声音来源 , 需要加权限
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 指定音频文件输出的格式
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        // 指定音频的编码格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        // recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 指定文件输出路径
        recorder.setOutputFile(file.getAbsolutePath());

        try {
            // 开始缓冲
            recorder.prepare();
        } catch (IOException e) {
            XCApp.i(this + "--prepare() failed");
        }
        return recorder;
    }
}
