package com.xiaocoder.android.fw.general.io;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
    public static int TOAST_SHORT_TIME_GAP = 2000;
    public static int TOAST_LONG_TIME_GAP = 3000;

    public boolean is_dtoast;
    public boolean is_printlog;
    public boolean is_output;

    public boolean is_dtoast() {
        return is_dtoast;
    }

    public void setIs_dtoast(boolean is_dtoast) {
        this.is_dtoast = is_dtoast;
    }

    public boolean is_printlog() {
        return is_printlog;
    }

    public void setIs_printlog(boolean is_printlog) {
        this.is_printlog = is_printlog;
    }

    public boolean is_OutPut() {
        return is_output;
    }

    public void setIs_output(boolean is_output) {
        this.is_output = is_output;
    }

    public XCLog(Context context, boolean is_dtoast, boolean is_output, boolean is_printlog,
                 String app_root_dir, String app_log_file, String app_temp_file, String encoding) {

        this.context = context;
        this.app_root_dir = app_root_dir;
        this.app_log_file = app_log_file;
        this.app_temp_file = app_temp_file;
        this.encoding = encoding;

        this.is_dtoast = is_dtoast;
        this.is_output = is_output;
        this.is_printlog = is_printlog;
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public void longToast(Object msg) {
        if (System.currentTimeMillis() - last_time > TOAST_LONG_TIME_GAP) {
            Toast.makeText(context, msg+"", Toast.LENGTH_LONG).show();
            last_time = System.currentTimeMillis();
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public void longToast(boolean showImmediately, Object msg) {
        if (showImmediately) {
            Toast.makeText(context, msg+"", Toast.LENGTH_LONG).show();
            last_time = System.currentTimeMillis();
        } else {
            longToast(msg);
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public void shortToast(Object msg) {
        if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
            Toast.makeText(context, msg+"", Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public void shortToast(boolean showImmediately, Object msg) {
        if (showImmediately) {
            Toast.makeText(context, msg+"", Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        } else {
            shortToast(msg);
        }
    }


    /**
     * 调试的toast , 上线前开关关闭
     */
    public void debugShortToast(Object msg) {
        if (is_dtoast) {
            if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
                Toast.makeText(context, msg+"", Toast.LENGTH_SHORT).show();
                last_time = System.currentTimeMillis();
            }
        }
    }

    /**
     * 调试的toast , 上线前开关关闭
     */
    public void debugLongToast(Object msg) {
        if (is_dtoast) {
            if (System.currentTimeMillis() - last_time > TOAST_LONG_TIME_GAP) {
                Toast.makeText(context, msg+"", Toast.LENGTH_LONG).show();
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
    public String app_root_dir;
    public String app_log_file;
    public String app_temp_file;
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

                // sd中，在app_root文件夹下创建log文件
                file = XCIOAndroid.createFileInSDCard(app_root_dir, app_log_file);

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
            // 这里不要调用e(),可能相互调用，异常
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
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
                    // 在app_root目录下创建temp_print文件，如果没有sd卡，则写到内部存储中
                    fos = new FileOutputStream(XCIOAndroid.createFileInAndroid(context, app_root_dir, app_temp_file));
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

    /**
     * 缓存文件达到70M就会清空
     */
    public static long LOG_FILE_LIMIT_SIZE = 73400320; // 70M
}