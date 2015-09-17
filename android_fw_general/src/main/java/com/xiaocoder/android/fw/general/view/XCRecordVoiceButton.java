package com.xiaocoder.android.fw.general.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.util.UtilDate;
import com.xiaocoder.android_fw_general.R;

// 可以录音的button
public class XCRecordVoiceButton extends Button implements OnTouchListener {

    private MediaRecorder media_recorder;
    private File save_file;

    public static final int MIN_TIME = 3000; // 录音最短时间，毫秒
    public static final int MAX_TIME = 45000; // 录音最长时间,毫秒
    public static final int FAKE_TIME = 60; // 假的录音最长时间，秒

    private long start_time; // 记录录音的开始时间
    private long end_time; // 记录录音的结束时间

    private Dialog dialog;
    private TextView hint;
    private TextView time;

    public XCRecordVoiceButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public XCRecordVoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        createDialog(context);
        hint = (TextView) dialog.findViewById(R.id.xc_id_voice_recoder_hint_textview);
        time = (TextView) dialog.findViewById(R.id.xc_id_voice_recoder_time);
        timeRunnable = new TimeRunnable();
        boundary_flag = false;
    }

    public class TimeRunnable implements Runnable {

        public void update(int time, TextView textview) {
            this.time = time;
            this.textview = textview;
        }

        int time;
        TextView textview;

        @Override
        public void run() {
            textview.setText(time + "");
        }
    }

    TimeRunnable timeRunnable;

    public class UpdateRunnable implements Runnable {

        public boolean isQuitNow = false;

        @Override
        public void run() {
            int i = FAKE_TIME;
            while (!isQuitNow && i >= 0) {
                try {
                    timeRunnable.update(i--, time);
                    XCApp.getBase_handler().post(timeRunnable);
                    Thread.sleep(750);
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

    public void createDialog(Context context) {
        dialog = new Dialog(context, R.style.xc_s_dialog);
        dialog.setContentView(R.layout.xc_l_view_voice_recoder_hint);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
    }

    public XCRecordVoiceButton(Context context) {
        super(context);
    }

    {
        super.setOnTouchListener(this);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        throw new RuntimeException("the listener has been setted in the class");
    }

    // 默认没有出界
    boolean boundary_flag;

    long record_last_time;

    private boolean isDialogShow;

    public void showDialog(String hint_text) {
        setBackgroundResource(R.drawable.xc_dd_line_gray_e1e1e1_bg_gray_cccccc_8);
        hint.setText(hint_text);
        startTime();
        dialog.show();
        isDialogShow = true;
    }

    public void closetDialog() {
        endTime();//该方法放前面
        if (dialog != null) {
            dialog.dismiss();
        }
        isDialogShow = false;
        setBackgroundResource(R.drawable.xc_dd_line_gray_e1e1e1_bg_white_ffffff_8);
    }

    public boolean isDialogShow() {
        return isDialogShow;
    }

    public interface OnTouchRecoderListener {
        void onTouchRecoderListener();
    }

    public void setOnTouchRecoderListener(OnTouchRecoderListener onTouchRecoderListener) {
        this.onTouchRecoderListener = onTouchRecoderListener;
    }

    OnTouchRecoderListener onTouchRecoderListener;

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        // 防止连续快速的点击
        if (System.currentTimeMillis() - record_last_time < 250) {
            record_last_time = System.currentTimeMillis();
            return false;
        }

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (onTouchRecoderListener != null) {
                    onTouchRecoderListener.onTouchRecoderListener();
                }
                boundary_flag = false;
                XCApp.i("down");
                forceStop();// 确保停止了
                showDialog("松开发送");
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
                boundary_flag = false;
                record_last_time = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOutSide(event.getX(), event.getY())) {
                    if (!boundary_flag) {
                        hint.setText("取消发送");
                        boundary_flag = true;
                    }
                } else {
                    if (boundary_flag) {
                        hint.setText("松开发送");
                        boundary_flag = false;
                    }
                }
                break;
            default:
                return false;
        }
        return false;
    }

    // 是否touch出了控件的边界
    public boolean isOutSide(float x, float y) {
        if (x > getWidth() || x < 0 || y < 0 || y > getHeight()) {
            return true;
        }
        return false;
    }

    // 确保停止， 比如正在播放时，又按了录音键
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
        boundary_flag = false;
        closetDialog();
    }

    // 取消录制， 并删除本地文件
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
        boundary_flag = false;
        closetDialog();
    }

    private void lessTimeOrExceptionDialog() {
        hint.setText("说话时间太短!");
        XCApp.getBase_handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closetDialog();
            }
        }, 500);
    }

    // 取消录制 ， 根据文件时间的长度决定是否上传
    private void stopRecording() {
        // 是否正常停止
        boolean normal = true;
        if (media_recorder != null) {
            // 停止并释放资源
            try {
                // 点击录制后快速松开，回报异常
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
                lessTimeOrExceptionDialog();
                return;
            }
        }
        // 记录录制的结束时间
        end_time = System.currentTimeMillis();
        float gap = end_time - start_time; // 真实时间
        // 判断文件的时间是否符合要求
        if (gap > MAX_TIME) {
            XCApp.i("时间不符合 ，删除文件");
            XCApp.shortToast("发送失败，每段录音的最大时间限制为" + FAKE_TIME + "秒");
            deleteFile("up max time");
        } else if (gap < MIN_TIME) {
            deleteFile("down min time");
            lessTimeOrExceptionDialog();
            return;
        } else {
            XCApp.i("时间符合 ，准备上传文件");
            if (file_listener != null) {
                XCApp.i(gap + "---原始录音的buttonview中记录的时间");
                file_listener.onRecordVoiceSuccessListener(save_file, (long) (gap / COMPRESS_RATIO));
                save_file = null;
            }
        }
        closetDialog();
    }

    public static double COMPRESS_RATIO = 0.75;

    public void deleteFile(String debug) {
        if (save_file != null && save_file.exists()) {
            save_file.delete();
            XCApp.i("TAG_CHAT", "delete---" + debug + save_file.getAbsolutePath());
            save_file = null;
        }
    }

    public String save_dir; //XCConfig.CHAT_VIDEO_FILE,

    public void setSave_dir(String save_dir) {
        this.save_dir = save_dir;
    }

    private void startRecording() {
        save_file = XCIOAndroid.createFileInAndroid(getContext(), save_dir, "voice" + UtilDate.format(new Date(), UtilDate.FORMAT_FULL_S));
        media_recorder = getPreparedRecorder(save_file);
        start_time = System.currentTimeMillis();
        media_recorder.start();
    }

    // 该方法需要在activity的onpaused方法中调用
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
            XCApp.i("prepare() failed");
        }
        return recorder;
    }

    public interface OnRecordVoiceSuccessListener {
        void onRecordVoiceSuccessListener(File file, float duration);
    }

    public void setOnRecordVoiceSuccessListener(OnRecordVoiceSuccessListener listener) {
        this.file_listener = listener;
    }

    OnRecordVoiceSuccessListener file_listener;

}
