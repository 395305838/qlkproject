package com.xiaocoder.android.fw.general.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.util.UtilEncrypt;

public class XCIO {

    /**
     * 为了避免总是调用System.getProperties("系统属性中的分隔符号")
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");// 换行符
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");// 如环境变量的路径分隔符
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");// 如c:/

    /**
     * 读文件
     *
     * @param file
     * @return
     */
    public static String readFrom(File file) {
        if (!file.exists()) {
            return "";
        }
        BufferedReader br = null;
        try {
            FileReader reader = new FileReader(file);
            br = new BufferedReader(reader);
            StringBuffer buffer = new StringBuffer();
            String s;
            while ((s = br.readLine()) != null) {
                buffer.append(s);
            }
            return buffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * createFile("e:","good.txt")-->在e盘下建一个good.txt文件
     * createFile("e:/learn/chinese"
     * ,"englis.txt")-->在e盘下建立一个learn/chinese的二级文件夹,并建立englis.txt的文件
     *
     * @param dirPath  文件夹的绝对路径
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public static File createFile(String dirPath, String fileName) throws IOException {
        File destDir = new File(dirPath); // 这句不会抛异常
        if (!destDir.exists()) {
            destDir.mkdirs();// 这句也不会抛异常
        }
        File file = new File(destDir, fileName);
        if (!file.exists()) {
            file.createNewFile(); // 这句可能创建文件异常
        }
        return file;
    }

    /**
     * IOUtils.createDir("e:/haha/enen.o/hexx.&...we/android.txt"),没错,这创建出来的是文件夹
     *
     * @param dirPath
     * @return
     */
    public static File createDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取url的文件名
     *
     * @param urlStr
     * @return
     */
    public static String getSaveFileName(String urlStr) {
        urlStr = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
        return urlStr;
    }

    /**
     * 获取真实存在的文件的后缀名
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            String filename = file.getName();
            if (filename.lastIndexOf(".") < 0) {
                return null;
            }
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 获取真实存在的文件的文件名(不包括后缀名)
     *
     * @param file
     * @return
     */
    public static String getFileNameNoSuffix(File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            String filename = file.getName();
            if (filename.lastIndexOf(".") < 0) {
                return filename;
            }
            return filename.substring(0, filename.lastIndexOf("."));
        }
        return null;
    }

    /**
     * 获取文件大小,单位字节
     *
     * @param filePath 传入文件的绝对路径
     * @return
     */
    public static long getFileSizeByByte(String filePath) {
        long size = 0;
        File file = new File(filePath);
        if (file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 换算文件大小含单位
     *
     * @param size
     * @return
     */
    public static String getFileSizeByUnit(long size) {
        if (size <= 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "未知大小";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取文件大小的 M/K/B表示方式
     *
     * @param contentLenght
     * @return
     */
    public static String getLengthText(long contentLenght) {
        // b/k/M

        DecimalFormat format = new DecimalFormat("###.00");
        float result = 0.0f;
        String resultText = "";
        if (contentLenght > 1024 * 1024)// 1M
        {
            result = (float) (contentLenght / (1024 * 1024.00));
            resultText = format.format(result) + "M";
        } else if (contentLenght > 1024)// 1K
        {
            result = (float) (contentLenght / (1024.00));
            resultText = format.format(result) + "K";
        } else // 1B
        {
            result = (float) (contentLenght / (1.00));
            resultText = format.format(result) + "B";
        }
        return resultText;
    }

    /**
     * 把输入流转为字节数组
     *
     * @param in ：InputStream
     * @return byte[] :字节数组
     */
    public static byte[] toBytesByInputStream(InputStream in) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();// 字节数组输出流
            byte[] buffer = new byte[1024];

            for (int len = -1; (len = in.read(buffer)) != -1; ) {
                baos.write(buffer, 0, len);
            }
            in.close();
            baos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 把输入流转为文件
     *
     * @param in
     * @param file
     * @throws java.io.IOException
     */
    public static void toFileByInputStream(InputStream in, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);// 该句只会创建文件,不会创建文件夹,否则异常
            byte[] buffer = new byte[10240];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            in.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 从网络获取文件,检测本地是否有缓存 如果url乱写,这里可能会有空指针或字符串等方面的异常 如 IOUtils.getCacheFile(new
     * File("e:/hello/cache"), "http://localhost/androidtest/image/1.gif");//可以
     * 如 IOUtils.getCacheFile(new File("e:/hello/cache"),
     * "localhost/androidtest/image/1.gif");//抛MalformedURLException 如
     * IOUtils.getCacheFile(new File("e:/hello/cache"),
     * "http://192.168.0.107/androidtest/image/2.gif");//可以 如
     * IOUtils.getCacheFile(new File("e:/hello/cache"),
     * "192.168.0.107/androidtest/image/2.gif");//抛MalformedURLException
     * <p/>
     * 缓存到哪个文件夹-->这个是文件夹
     * 请求资源的url-->用已加密后的字符串创建文件
     */
    public static File getCacheFile(File cacheDir, String urlPath) throws MalformedURLException, IOException {
        String filename = UtilEncrypt.MD5(urlPath) + urlPath.substring(urlPath.lastIndexOf("."));
        File file = new File(cacheDir, filename);
        if (file.exists()) {
            System.out.println("读缓存了");
            return file;
        } else {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlPath).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                // 创建文件
                file = createFile(cacheDir.getAbsolutePath(), filename);
                // 开辟一个文件输出流，把网络中的输入流写入文件中
                toFileByInputStream(conn.getInputStream(), file);
                System.out.println("该文件没有缓存,创建缓存成功");
                return file;
            } else {
                return null;
            }
        }
    }

    /**
     * 复制文件夹
     *
     * @param fromPath
     * @param toPath
     * @return
     * @throws java.io.IOException
     */
    public static void copyDirAndFile(String fromPath, String toPath) throws IOException {
        // 1.将原始路径和目标路径转为File
        File fromFile = new File(fromPath);
        if (!fromFile.exists()) {
            return;
        }
        File toFile = new File(toPath);
        // 2.如果目标路径不存在，则创建该路径
        if (!toFile.exists()) {
            toFile.mkdir();
        }
        // 3.判断原始路径是一个目录还是一个文件，如果是一个文件，直接复制，
        // 递归遍历

        if (fromFile.isFile()) {
            copyFile(fromFile.getAbsolutePath(), toPath + fromFile.getName());
        } else if (fromFile.isDirectory()) {
            // fromFile = new File(fromFile.getAbsoluteFile() + "/");
            File[] listFiles = fromFile.listFiles();
            if (listFiles == null) {
                return;
            }
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    // 4.如果是一个文件夹，得到该文件夹下的所有文件，需要将该文件夹的所有文件都复制，文件夹需要
                    // 递归遍历,因为是文件夹，所以最后都要加上"/"
                    copyFile(file.getAbsolutePath() + "/", toPath + file.getName() + "/");
                } else {
                    // 5.是一个目录还是一个文件，如果是一个文件，直接复制，
                    copyFile(file.getAbsolutePath(), toPath + file.getName());
                }
            }
        }
    }

    /**
     * 拷贝文件用字节流
     *
     * @param srcPath
     * @param destPath
     */
    public static void copyFile(String srcPath, String destPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            // 打开流
            out = new FileOutputStream(destPath);
            in = new FileInputStream(srcPath);

            // 使用流
            byte[] data = new byte[1024];
            for (int len = -1; (len = in.read(data)) != -1; ) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // 删除文件夹 和 文件
    public void removeFileAsyn(final File operator_file) {
        if (!operator_file.exists()) {
            return;
        }
        /**
         * 子线程中删除
         */
        XCApplication.getBase_cache_threadpool().execute(new Runnable() {
            @Override
            public void run() {
                if (operator_file.isDirectory()) {
                    removeDir(operator_file);
                } else {
                    operator_file.delete();
                }
            }
        });
    }

    /**
     * 删除文件夹
     */
    public void removeDir(File dir) {
        boolean isGoOnDeleting = true; // 如果是实际中, 可能存在正在删除, 而应用此时退出的情况
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {// 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组。最好加入判断。
                if (files != null) {
                    // 2,对遍历到的file对象判断是否是目录。
                    if (isGoOnDeleting) {
                        if (file.isDirectory()) {
                            removeDir(file);
                        } else {
                            file.delete();
                        }
                    } else {
                        break;
                    }
                }
            }
            dir.delete();
        }
    }

    /**
     * 获取目录中的所有文件(包括传入遍历的根目录)
     *
     * @param dir  目录
     * @param list
     * @return 返回一个装有所有目录文件的集合
     */
    public static List<File> getAllFilesByDir(File dir, List<File> list) {
        if (dir != null && list != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();// 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组。最好加入判断。
            if (files != null) {
                for (File file : files) {
                    // 2,对遍历到的file对象判断是否是目录。
                    if (file.isDirectory()) {
                        getAllFilesByDir(file, list);
                    } else {
                        list.add(file);
                    }
                }
            }
            list.add(dir);
            return list;
        }
        return null;
    }

    /**
     * 根据指定的过滤器在指定目录下获取所有的符合过滤条件的文件，并存储到list集合中。
     *
     * @param dir    目录
     * @param filter 这里用的是FileFilter,和FileNameFilter差不多,该FileUtils中有FileFilter模板,
     *               FileFilter可以在构造中初始化一个条件字符串如".java"
     * @param list
     * @return
     */
    public static List<File> getFilterFilesByDir(File dir, FileFilter filter, List<File> list) {
        if (dir != null && list != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();// 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组。最好加入判断。
            // File[] files = dir.listFiles(filter);//
            // 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组。最好加入判断。
            if (files != null) {
                for (File file : files) {
                    // 2,对遍历到的file对象判断是否是目录。
                    if (file.isDirectory()) {
                        getFilterFilesByDir(file, filter, list);
                    } else {
                        if (filter != null && filter.accept(file)) {
                            list.add(file);
                        }
                    }
                }
            }
            return list;
        }
        return null;
    }

    /**
     * deserialization from file
     *
     * @param filePath
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static Object deserialization(String filePath) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            Object o = in.readObject();
            in.close();
            return o;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * serialize to file
     *
     * @param filePath
     * @param obj
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(String filePath, Object obj) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(obj);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 获得指定后缀名的文件文件
     *
     * @param list 用来存放本地视频的集合
     * @param file 在哪个文件路径下查找
     */
    public <T> void getVideoFile(final LinkedList<T> list, File file) {
        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                // sdCard找到视频名称
                String name = pathname.getName();

                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4") || name.equalsIgnoreCase(".3gp") || name.equalsIgnoreCase(".wmv")) {

                        // list.add(mi);
                        return true;
                    }
                } else if (pathname.isDirectory()) {
                    getVideoFile(list, pathname);
                }
                return false;
            }
        });

    }

    /**
     * 一个FileFilter模板
     *
     * @author xiaocoder
     */
    static class MyFilter implements FileFilter {

        private String name;

        public MyFilter(String name) {
            this.name = name;
        }

        @Override
        public boolean accept(File dir) {
            return dir.getAbsolutePath().endsWith(name);
        }
    }
}
