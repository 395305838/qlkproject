package com.xiaocoder.android.fw.general.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;

import com.xiaocoder.android.fw.general.util.UtilString;

/**
 * android.permission.MOUNT_UNMOUNT_FILESYSTEMS
 * <p/>
 * android.permission.WRITE_EXTERNAL_STORAGE
 *
 * @author xiaocoder
 */
public class XCIOAndroid {

    private Context context;

    public XCIOAndroid(Context context) {
        this.context = context;
    }

    public static boolean isSDcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static InputStream readSdCardFile(String url) {
        File file = new File(url);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException f) {

        }
        return in;
    }

    /**
     * 写文本到内部存储 ,默认为Ecplise的工程编码, 指定编码 在Android系统中，文件保存在
     * /data/data/"PACKAGE_NAME"/files/ 目录下
     *
     * @param fileName 如 "android.txt" ,不可以是"aa/bb.txt"
     * @param content
     * @param mode     模式
     * @throws java.io.IOException
     * @charset "gbk" "utf-8"
     */
    public void write2Inside(String fileName, String content, String charset, int mode) throws IOException {
        if (content == null) {
            content = "";
        }
        FileOutputStream fos = context.openFileOutput(fileName, mode);
        fos.write(content.getBytes(charset));
        fos.close();
    }

    // ----------------------------------------------------------------------------------------------------------------

    /**
     * 写文本到SD卡,默认为Ecplise的工程编码
     *
     * @param dirName  如"aa/bb" 在SDCard下建立/mnt/sdcard/aa/bb的目录,并在该目录下建立文件 如null 或 ""
     *                 在/mnt/sdcard的目录下建立文件
     * @param fileName 如"cc.txt", 不可以是"ee/cc.txt"
     * @param content  如为null,则写入""
     * @throws java.io.IOException
     */
    public File write2SDCard(String dirName, String fileName, String content) throws IOException {
        if (content == null) {
            content = "";
        }
        File file = createFileInSDCard(dirName, fileName);
        if (file == null) {
            throw new RuntimeException("未检测到sd卡");
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
        return file;
    }

    /**
     * 指定编码,写文本到SD卡,默认为Ecplise的工程编码
     *
     * @param dirName  如"aa/bb" 在SDCard下建立/mnt/sdcard/aa/bb的目录,并在该目录下建立文件 如null 或 ""
     *                 在/mnt/sdcard的目录下建立文件
     * @param fileName 如"cc.txt", 不可以是"ee/cc.txt"
     * @param content  如为null,则写入""
     * @param charset  "utf-8" "gbk"
     * @throws java.io.IOException
     */
    public void write2SDCard(String dirName, String fileName, String content, String charset) throws IOException {
        if (content == null) {
            content = "";
        }
        File file = createFileInSDCard(dirName, fileName);
        if (file == null) {
            throw new RuntimeException("未检测到sd卡");
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes(charset));
        fos.close();
    }

    /**
     * 从内部存储读取文本文件,默认平台编码
     *
     * @param fileName 文件名 如 "android.txt" 不可以是"aa/bb.txt",系统只提供了"android.txt"方式的api
     * @return
     * @throws java.io.IOException
     */
    public String readFromInside(String fileName) throws IOException {
        FileInputStream in = context.openFileInput(fileName);
        return new String(XCIO.toBytesByInputStream(in));
    }

    /**
     * 写文本到内部存储 ,默认为Ecplise的工程编码 --> windows记事本会自动判断编码的类型从而启动相应的编码打开文件
     * 在Android系统中，文件保存在 /data/data/"PACKAGE_NAME"/files/ 目录下
     *
     * @param fileName 文件名 如 "android.txt" ,不可以是"aa/bb.txt"
     * @param mode     模式
     * @param content
     * @throws java.io.IOException
     */
    public void write2Inside(String fileName, String content, int mode) throws IOException {
        FileOutputStream fos = context.openFileOutput(fileName, mode);
        if (content == null) {
            content = "";
        }
        fos.write(content.getBytes());
        fos.close();
    }

    /**
     * 从内部存储读取文本文件,指定编码
     *
     * @param fileName 文件名 如 "android.txt" 不可以是"aa/bb.txt",系统只提供了"android.txt"方式的api
     * @param charset
     * @return
     * @throws java.io.IOException
     */
    public String readFromInside(String fileName, String charset) throws IOException {
        FileInputStream in = context.openFileInput(fileName);
        return new String(XCIO.toBytesByInputStream(in), charset);
    }

    /**
     * 从sd卡中读取数据,默认ecplise工程编码
     *
     * @param dirName  如果是"" 或null 则从Environment.getExternalStorageState()目录读取文件
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public String readFromSDCard(String dirName, String fileName) throws IOException {

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
     * 从sd卡中读取数据,默认ecplise工程编码
     *
     * @param dirName  如果是"" 或null 则从Environment.getExternalStorageState()目录读取文件
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public String readFromSDCard(String dirName, String fileName, String charset) throws IOException {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new RuntimeException("未检测到SD卡");
        }
        if (dirName == null || dirName.trim().length() == 0) {
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            if (!file.exists()) {
                throw new RuntimeException("该文件不存在");
            }
            FileInputStream in = new FileInputStream(file);
            return new String(XCIO.toBytesByInputStream(in), charset);
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
        return new String(XCIO.toBytesByInputStream(in), charset);
    }

    // -----------------------------------------------------------------------------------------

    /**
     * 现在sd卡中创建，如果没有sd卡， 则在内部存储中创建
     *
     * @param dirName
     * @return
     */
    public File createDirInAndroid(String dirName) {
        try {
            if (isSDcardExist()) {
                return createDirInSDCard(dirName);
            } else {
                return createDirInside(dirName);
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
    public File createDirInSDCard(String dirName) {
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
    public File createDirInside(String dirName) {
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
    public File createFileInAndroid(String dirName, String fileName) {
        try {
            if (isSDcardExist()) {
                return createFileInSDCard(dirName, fileName);
            } else {
                return createFileInside(dirName, fileName);
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
    public File createFileInSDCard(String dirName, String fileName) throws IOException {
        File dir = createDirInSDCard(dirName);
        if (dir == null) {
            return null;
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 在内部存储中创建文件
     *
     * @param dirName  "aa/bb","aa"都可,如果是null 或""默认为在context.getCacheDir()目录下创建文件
     * @param fileName 文件名-->"abc.txt"格式, 不可写成"abc/ed.txt"
     * @return 返回一个创建了文件夹和文件的目录
     * @throws java.io.IOException
     */
    public File createFileInside(String dirName, String fileName) throws IOException {
        File file = new File(createDirInside(dirName), fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public String getFileFromAssets(String fileName) throws IOException {
        if (context == null || UtilString.isBlank(fileName)) {
            return null;
        }
        StringBuilder s = new StringBuilder("");
        InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
        BufferedReader br = new BufferedReader(in);
        String line;
        while ((line = br.readLine()) != null) {
            s.append(line).append(XCIO.LINE_SEPARATOR);
        }
        return s.toString();
    }

    public InputStream getInputStreamFromAssets(String fileName) throws IOException {
        if (context == null || UtilString.isBlank(fileName)) {
            return null;
        }
        return context.getResources().getAssets().open(fileName);
    }

    public String getFileFromRaw(int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
