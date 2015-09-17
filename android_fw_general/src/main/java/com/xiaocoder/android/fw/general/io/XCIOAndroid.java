package com.xiaocoder.android.fw.general.io;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * android.permission.MOUNT_UNMOUNT_FILESYSTEMS
 * <p/>
 * android.permission.WRITE_EXTERNAL_STORAGE
 */
public class XCIOAndroid {

    public static boolean isSDcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static InputStream getInputStreamFromUri(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getInputStreamFromRaw(Context context, int drawable_id) {
        try {
            return context.getResources().openRawResource(drawable_id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getInputStreamFromAsserts(Context context, String fileName) {
        try {
            return context.getAssets().open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringFromAssets(Context context, String fileName) {

        if (context == null) {
            return null;
        }

        return XCIO.toStringByInputStream(getInputStreamFromAsserts(context, fileName));

    }

    public static String getStringFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        return XCIO.toStringByInputStream(getInputStreamFromRaw(context, resId));
    }

    /**
     * 写文本到内部存储 ,默认为Ecplise的工程编码, 指定编码 在Android系统中，文件保存在
     * /data/data/"PACKAGE_NAME"/files/ 目录下
     *
     * @param fileName 如 "android.txt" ,不可以是"aa/bb.txt"
     * @param content
     * @param mode     模式 Context.MODE_APPEND等
     * @throws java.io.IOException
     * @charset "gbk" "utf-8"
     */
    public static void write2Inside(Context context, String fileName, String content, int mode) {

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, mode);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写文本到SD卡,默认为Ecplise的工程编码
     *
     * @param dirName  如"aa/bb" 在SDCard下建立/mnt/sdcard/aa/bb的目录,
     *                 如null 或 "" 在/mnt/sdcard的目录下建立文件
     * @param fileName 如"cc.txt", 不可以是"ee/cc.txt"
     * @param content
     * @throws java.io.IOException
     */
    public static File write2SDCard(String dirName, String fileName, String content) {
        FileOutputStream fos = null;
        try {
            File file = createFileInSDCard(dirName, fileName);
            if (file == null) {
                throw new RuntimeException("未检测到sd卡");
            }
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从内部存储读取文本文件,默认平台编码
     *
     * @param fileName 文件名 如 "android.txt" 不可以是"aa/bb.txt",系统只提供了"android.txt"方式的api
     * @return
     * @throws java.io.IOException
     */
    public static String readFromInside(Context context, String fileName) {
        FileInputStream in = null;
        try {
            in = context.openFileInput(fileName);
            return new String(XCIO.toBytesByInputStream(in));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从sd卡中读取数据,默认ecplise工程编码
     *
     * @param dirName  如果是"" 或null 则从Environment.getExternalStorageState()目录读取文件
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public static String readFromSDCard(String dirName, String fileName) throws IOException {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new RuntimeException("未检测到SD卡");
        }
        if (dirName == null || dirName.trim().length() == 0) {
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            if (!file.exists()) {
                throw new RuntimeException("该文件不存在");
            }
            FileInputStream in = new FileInputStream(file);
            return new String(XCIO.toBytesByInputStream(in));
        }
        File dir = new File(Environment.getExternalStorageDirectory() + XCIO.FILE_SEPARATOR + dirName);
        if (!dir.exists()) {
            throw new RuntimeException("该目录不存在");
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            throw new RuntimeException("该文件不存在");
        }
        FileInputStream in = new FileInputStream(file);
        return new String(XCIO.toBytesByInputStream(in));
    }

    /**
     * 在sd卡中创建，如果没有sd卡， 则在内部存储中创建
     */
    public static File createDirInAndroid(Context context, String dirName) {
        try {
            if (isSDcardExist()) {
                return createDirInSDCard(dirName);
            } else {
                return createDirInside(context, dirName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在android环境下的SDCard中创建文件夹,如果没有SDCard,则返回null
     *
     * @param dirName 如果传入的为 null或""或"   ",
     *                则返回的file为Environment.getExternalStorageState()目录 如果传入的为
     *                "aa/bb" 或"aa"
     *                则返回的是在Environment.getExternalStorageState()目录下创建aa/bb 或aa文件夹
     * @return
     */
    public static File createDirInSDCard(String dirName) {
        File dir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 传入"",或"     ",则返回file =
            // Environment.getExternalStorageDirectory();
            if (dirName == null || dirName.trim().length() == 0) {
                return Environment.getExternalStorageDirectory();// mnt/sdcard
            }
            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + XCIO.FILE_SEPARATOR + dirName;
            dir = new File(dirPath);

            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return dir;
    }

    /**
     * 在android环境下的内部存储中的Cache里创建文件夹
     *
     * @param dirName 如果传入的为 null或""或"   ", 则返回的file为context.getCacheDir()目录 如果传入的为
     *                "aa/bb" 或"aa" 则在context.getCacheDir()目录下创建aa/bb或aa文件夹
     * @return
     */
    public static File createDirInside(Context context, String dirName) {
        File dir = null;
        if (dirName == null || dirName.trim().length() == 0) {
            return context.getCacheDir();// 内部存储下的/data/data/<package name>/cache
        }
        String dirPath = context.getCacheDir() + XCIO.FILE_SEPARATOR + dirName;
        dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 先在sd卡中创建，如果没有sd卡， 则在内部存储中创建
     *
     * @param dirName
     * @param fileName
     * @return
     */
    public static File createFileInAndroid(Context context, String dirName, String fileName) {
        try {
            if (isSDcardExist()) {
                return createFileInSDCard(dirName, fileName);
            } else {
                return createFileInside(context, dirName, fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在SDCard中创建文件
     *
     * @param dirName  "aa/bb" "aa"都可,如果是null
     *                 或""默认为在Environment.getExternalStorageState()目录下创建文件
     * @param fileName 文件名-->"abc.txt"格式, 不可写成"abc/ed.txt"
     * @return 返回一个创建了文件夹和文件的目录, 如果没有SD卡, 返回null
     * @throws java.io.IOException
     */
    public static File createFileInSDCard(String dirName, String fileName) {

        try {
            File dir = createDirInSDCard(dirName);
            if (dir == null) {
                return null;
            }
            File file = new File(dir, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在内部存储中创建文件
     *
     * @param dirName  "aa/bb","aa"都可,如果是null 或""默认为在context.getCacheDir()目录下创建文件
     * @param fileName 文件名-->"abc.txt"格式, 不可写成"abc/ed.txt"
     * @return 返回一个创建了文件夹和文件的目录
     * @throws java.io.IOException
     */
    public static File createFileInside(Context context, String dirName, String fileName) {
        try {
            File file = new File(createDirInside(context, dirName), fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
