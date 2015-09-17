package com.xiaocoder.android.fw.general.io;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.util.Date;

/**
 * 1 可以控制频率的吐司
 * 2 输出log到控制台
 * 3 输出log到文件
 * 4 日志的清空，日志的大小控制
 */
public class XCLog {

    public Context context;
    public long last_time;
    public File file;
    public int TOAST_SHORT_TIME_GAP;
    public int TOAST_LONG_TIME_GAP;

    public boolean is_dtoast;
    public boolean is_printlog;
    public boolean is_output;

    public boolean is_OutPut() {
        return is_output;
    }

    public XCLog(Context context, boolean is_dtoast, boolean is_output, boolean is_printlog,
                 String app_root_dir_name, String app_log_file_name, String app_temp_file_name, String encoding) {

        this.context = context;
        this.app_root_dir_name = app_root_dir_name;
        this.app_log_file_name = app_log_file_name;
        this.app_temp_file_name = app_temp_file_name;
        this.encoding = encoding;

        TOAST_SHORT_TIME_GAP = 1000;
        TOAST_LONG_TIME_GAP = 3000;

        this.is_dtoast = is_dtoast;
        this.is_output = is_output;
        this.is_printlog = is_printlog;

    }

    // --------------------------控制频率的吐司---------------------------------
    /**
     * 防止点击频繁, 不断的弹出
     */
    public void longToast(String msg) {
        if (System.currentTimeMillis() - last_time > TOAST_LONG_TIME_GAP) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            last_time = System.currentTimeMillis();
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public void longToast(boolean showImmediately, String msg) {
        if (showImmediately) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            last_time = System.currentTimeMillis();
        } else {
            longToast(msg);
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public void shortToast(String msg) {
        if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public void shortToast(boolean showImmediately, String msg) {
        if (showImmediately) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        } else {
            shortToast(msg);
        }
    }


    /**
     * 调试的toast , 上线前开关关闭
     */
    public void debugShortToast(String msg) {
        if (is_dtoast) {
            if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                last_time = System.currentTimeMillis();
            }
        }
    }

    /**
     * 调试的toast , 上线前开关关闭
     */
    public void debugLongToast(String msg) {
        if (is_dtoast) {
            if (System.currentTimeMillis() - last_time > TOAST_LONG_TIME_GAP) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                last_time = System.currentTimeMillis();
            }
        }
    }

    /**
     * 以tag打印到控制台 和 文件
     * <p/>
     * 上线前is_output 与 is_printlog关闭
     */
    public void i(Context context, Object msg) {
        if (is_output) {
            Log.i(context.getClass().getSimpleName(), msg + "");
        }
        if (is_printlog) {
            writeLog2File(context.getClass().getSimpleName() + "---" + msg, true);
        }
    }

    public void i(String tag, Object msg) {
        if (is_output) {
            Log.i(tag, msg + "");
        }
        if (is_printlog) {
            writeLog2File(msg + "", true);
        }
    }

    public void i(Object msg) {
        if (is_output) {
            Log.i(XCConfig.TAG_SYSTEM_OUT, msg + "");
        }
        if (is_printlog) {
            writeLog2File(msg + "", true);
        }
    }

    /**
     * 不管是否上线，都打印日志到本地，并输出到控制台
     * 注：e的日志颜色不同
     */
    public void e(String hint) {
        Log.e(XCConfig.TAG_ALOG, hint);
        writeLog2File(hint, true);
    }

    public void e(Context context, String hint) {
        Log.e(XCConfig.TAG_ALOG, context.getClass().getSimpleName() + "--" + hint);
        writeLog2File(context.getClass().getSimpleName() + "--" + hint, true);
    }

    public void e(String hint, Exception e) {
        e.printStackTrace();
        Log.e(XCConfig.TAG_ALOG, hint + "--" + "Exception-->" + e.toString() + "--" + e.getMessage());
        writeLog2File("Exception-->" + hint + "-->" + e.toString() + "--" + e.getMessage(), true);
    }

    public void e(Context context, String hint, Exception e) {
        e.printStackTrace();
        Log.e(XCConfig.TAG_ALOG, "Exception-->" + context.getClass().getSimpleName() + "--" + hint + "--" + e.toString() + "--" + e.getMessage());
        writeLog2File("Exception-->" + context.getClass().getSimpleName() + "--" + hint + "--" + e.toString() + "--" + e.getMessage(), true);
    }

    /**
     * 删除日志文件
     */
    public synchronized void clearLog() {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    // -----------------------------------打印到文件中，保存日志----------------------------------------------------------------

    public String app_root_dir_name;
    public String app_log_file_name;
    public String app_temp_file_name;
    public String encoding;

    /**
     * 只在有sd卡的时候，才会打印日志
     */
    private synchronized void writeLog2File(String content, boolean is_append) {

        if (TextUtils.isEmpty(content) || !XCIOAndroid.isSDcardExist()) {
            return;
        }

        RandomAccessFile raf = null;

        try {
            if (file == null || !file.exists()) {

                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + System.getProperty("file.separator") + app_root_dir_name);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + System.getProperty("file.separator") + app_log_file_name);
                file.createNewFile();
            }

            // 日志满了的处理
            logFull();

            // 已存在文件
            if (!is_append) {
                // 假如不允许追加写入，则删除 后 重新创建
                file.delete();
                file.createNewFile();
            }

            raf = new RandomAccessFile(file, "rw");
            long len = raf.length();
            raf.seek(len);
            raf.write((content + "-->" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG).format(new Date()) + "  end  " + System.getProperty("line.separator"))
                    .getBytes(encoding));

        } catch (Exception e) {
            e.printStackTrace();
            // 这里不要调用e(),可能相互调用
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    raf = null;
                }
            }
        }
    }

    /**
     * 打印到XCConfig.TEMP_PRINT_FILE文件中，会覆盖之前的打印信息
     * <p/>
     * 场景：如果json很长，有时控制台未必会全部打印出来，则可以去app的目录下找到这个临时文件查看
     */
    public void tempPrint(String str) {
        if (is_output) {
            synchronized (this) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(XCIOAndroid.createFileInAndroid(context, null, app_temp_file_name));
                    fos.write(str.getBytes());
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        } finally {
                            fos = null;
                        }
                    }
                }
            }
        }
    }

    public void logFull() throws Exception {
        if (file != null && file.exists() && file.length() > LOG_FILE_LIMIT_SIZE) {
            file.delete();
            file.createNewFile();
        }
    }

    public static long LOG_FILE_LIMIT_SIZE = 73400320; // 70M
}