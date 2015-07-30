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

/*
 * 1 可以控制频率的吐司
 * 2 输出log到控制台 
 * 3 输出log到文件        
 */
public class XCLog {

    public Context context;
    public long last_time;
    public File file;
    public int TOAST_SHORT_TIME_GAP;
    public int TOAST_LONG_TIME_GAP;

    public boolean IS_DTOAST;
    public boolean IS_PRINTLOG;
    public boolean IS_OUTPUT;


    public boolean is_OutPut() {
        return IS_OUTPUT;
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

        IS_DTOAST = is_dtoast;
        IS_OUTPUT = is_output;
        IS_PRINTLOG = is_printlog;


    }

    // --------------------------控制频率的吐司---------------------------------
    // 防止点击频繁, 不断的弹出
    public void longToast(String msg) {
        if (System.currentTimeMillis() - last_time > TOAST_LONG_TIME_GAP) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            last_time = System.currentTimeMillis();
        }
    }

    // 防止点击频繁, 不断的弹出
    public void shortToast(String msg) {
        if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        }
    }

    // 防止点击频繁, 不断的弹出
    public void shortToast(boolean showImmediately, String msg) {
        if (showImmediately) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        } else {
            if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                last_time = System.currentTimeMillis();
            }
        }
    }

    // 防止点击频繁, 不断的弹出
    public void shortToast(boolean showImmediately, int stringId) {
        if (showImmediately) {
            Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        } else {
            if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
                Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
                last_time = System.currentTimeMillis();
            }
        }
    }

    // 防止点击频繁, 不断的弹出
    public void shortToast(int stringId) {
        if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
            Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
            last_time = System.currentTimeMillis();
        }
    }

    // 调试的toast
    public void debugShortToast(String msg) {
        if (IS_DTOAST) {
            if (System.currentTimeMillis() - last_time > TOAST_SHORT_TIME_GAP) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                last_time = System.currentTimeMillis();
            }
        }
    }

    // 调试的toast
    public void debugLongToast(String msg) {
        if (IS_DTOAST) {
            if (System.currentTimeMillis() - last_time > TOAST_LONG_TIME_GAP) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                last_time = System.currentTimeMillis();
            }
        }
    }

    // 以tag打印到控制台 和 文件 该tag为activity的名字
    public void i(Context context, String msg) {
        if (IS_OUTPUT) {
            Log.i(context.getClass().getSimpleName(), msg);
        }
        if (IS_PRINTLOG) {
            writeLog2File(msg, true);
        }
    }

    // 以tag打印到控制台 和 文件， 该tag为指定
    public void i(String tag, String msg) {
        if (IS_OUTPUT) {
            Log.i(tag, msg);
        }
        if (IS_PRINTLOG) {
            writeLog2File(msg, true);
        }
    }

    // 以tag打印到控制台 和 文件， 该tag为指定
    public void i(String msg) {
        if (IS_OUTPUT) {
            Log.i(XCConfig.TAG_SYSTEM_OUT, msg);
        }
        if (IS_PRINTLOG) {
            writeLog2File(msg, true);
        }
    }

    public void e(String hint) {
        i(XCConfig.TAG_LOG, hint);
    }

    public void e(Context context, String hint) {
        i(XCConfig.TAG_LOG, context.getClass().getSimpleName() + "--" + hint);
    }

    public void e(String hint, Exception e) {
        e.printStackTrace();
        Log.e(XCConfig.TAG_ANDROID_RUNTIME, hint + "--" + "Exception-->" + e.toString() + "--" + e.getMessage());
        if (IS_PRINTLOG) {
            writeLog2File("Exception-->" + hint + "-->" + e.toString() + "--" + e.getMessage(), true);
        }
    }

    public void e(Context context, String hint, Exception e) {
        e.printStackTrace();
        Log.e(XCConfig.TAG_ANDROID_RUNTIME, "Exception-->" + context.getClass().getSimpleName() + "--" + hint + "--" + e.toString() + "--" + e.getMessage());
        if (IS_PRINTLOG) {
            writeLog2File("Exception-->" + context.getClass().getSimpleName() + "--" + hint + "--" + e.toString() + "--" + e.getMessage(), true);
        }
    }

    // -----------------------------------打印到文件中，保存日志----------------------------------------------------------------

    public String app_root_dir_name;
    public String app_log_file_name;
    public String app_temp_file_name;
    public String encoding;

    /**
     * <uses-permission
     * android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
     * <uses-permission
     * android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     */
    public synchronized void writeLog2File(String content, boolean is_append) {

        if (TextUtils.isEmpty(content) || !XCIOAndroid.isSDcardExist()) {
            return;
        }

        try {
            if (file == null || !file.exists()) {

                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + System.getProperty("file.separator") + app_root_dir_name);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + System.getProperty("file.separator") + app_log_file_name);
                file.createNewFile();
            }

            // 已存在文件
            if (!is_append) {
                // 假如不允许追加写入，则删除 后 重新创建
                file.delete();
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            long len = raf.length();
            raf.seek(len);
            raf.write((content + "-->" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG).format(new Date()) + "  end  " + System.getProperty("line.separator"))
                    .getBytes(encoding));

            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------测试打印字符串到XCConfig.TEMP_PRINT_FILE文件中，会覆盖之前的---------------------------
    public void tempPrint(String str) {
        if (IS_OUTPUT) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(XCApplication.getBase_io().createFileInSDCard(null, app_temp_file_name));
                fos.write(str.getBytes());
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