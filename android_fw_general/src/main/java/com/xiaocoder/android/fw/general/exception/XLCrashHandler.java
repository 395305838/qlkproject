package com.xiaocoder.android.fw.general.exception;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.xiaocoder.android.fw.general.application.XCApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 记得在清单文件中注册啊
 * <p/>
 * 有一个bug：如果实在显示打印的异常类的环境下，且mainactivity出现异常了，则会启动多次--待改
 */
public class XLCrashHandler implements UncaughtExceptionHandler {

    // CrashHandler 实例
    private static XLCrashHandler INSTANCE = new XLCrashHandler();

    // 程序的 Context 对象
    private Context mContext;

    private Application application;

    // 系统默认的 UncaughtException 处理类
    private UncaughtExceptionHandler mDefaultHandler;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private String path = Environment.getExternalStorageDirectory().getPath() + File.separator;

    /**
     * 保证只有一个 CrashHandler 实例
     */
    private XLCrashHandler() {
    }

    /**
     * 获取 CrashHandler 实例 ,单例模式
     */
    public static XLCrashHandler getInstance() {
        return INSTANCE;
    }

    private boolean mIsShowExceptionActivity;

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, String crash_path, boolean isShowExceptionActivity) {
        if (context instanceof Application) {
            application = (Application) context;
        }
        mContext = context;
        mIsShowExceptionActivity = isShowExceptionActivity;

        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        path = path + crash_path;
        XCApplication.printi("path:" + path);
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        XCApplication.printe(ex.toString());
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (application != null && application instanceof XCApplication) {
                ((XCApplication) application).finishAllActivity();
            }
            // 退出程序,注释下面的重启启动程序代码
            System.exit(1);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        ex.printStackTrace();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            XCApplication.printe("an error occured when collect package info--", e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                XCApplication.printi(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                XCApplication.printe("an error occured when collect crash info--", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中 *
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);

        if (mIsShowExceptionActivity) {
            Intent intent = new Intent(mContext, XLShowExceptionsActivity.class);
            intent.putExtra("text", sb.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // path = "/sdcard/crash/" + app_name;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }

            return fileName;
        } catch (Exception e) {
            XCApplication.printe("an error occured while writing file--", e);
        }

        return null;
    }
}
