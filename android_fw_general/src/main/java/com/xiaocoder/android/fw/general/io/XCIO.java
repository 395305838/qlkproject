package com.xiaocoder.android.fw.general.io;

import com.xiaocoder.android.fw.general.function.runnable.XCDownloadRunnable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class XCIO {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");// 换行符
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");// 如环境变量的路径分隔符
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");// 如c:/

    /**
     * createFile("e:","good.txt")-->在e盘下建一个good.txt文件
     * createFile("e:/learn/chinese","englis.txt")-->在e盘下建立一个learn/chinese的二级文件夹,并建立englis.txt的文件
     */
    public static File createFile(String dirPath, String fileName) {

        try {
            File destDir = new File(dirPath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File file = new File(destDir, fileName);
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
     * createDir("e:/haha/enen.o/hexx.&...we/android.txt"),没错,这创建出来的是文件夹
     */
    public static File createDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static InputStream getInputStreamFromPath(String path) {
        File file = new File(path);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static String getStringFromFile(File file) {
        if (!file.exists()) {
            return null;
        }
        BufferedReader br = null;
        try {
            FileReader reader = new FileReader(file);
            br = new BufferedReader(reader);
            StringBuilder buffer = new StringBuilder();
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

    public static String toStringByInputStream(InputStream inputStream) {
        BufferedReader br = null;
        try {
            StringBuilder s = new StringBuilder("");
            InputStreamReader in = new InputStreamReader(inputStream);
            br = new BufferedReader(in);
            String line = null;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (Exception e) {
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
     * 把输入流转为字节数组
     *
     * @param in ：InputStream
     * @return byte[] :字节数组
     */
    public static byte[] toBytesByInputStream(InputStream in) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();// 字节数组输出流
            byte[] buffer = new byte[10240];

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
     * <p/>
     * 这里可以设置一个下载进度的监听
     */
    public static void toFileByInputStream(InputStream in, File file) {
        toFileByInputStream(in, file, 0, null, false);
    }

    public static void toFileByInputStream(InputStream in, File file, long totalSize, XCDownloadRunnable.DownloadListener listener, boolean append) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file, append);// 只会创建文件,不会创建文件夹,否则异常
            byte[] buffer = new byte[10240];
            int len = -1;
            long totalProgress = 0;
            if (listener != null) {
                listener.downloadStart(totalSize, file);
            }
            while ((len = in.read(buffer)) != -1) {
                os.write(buffer, 0, len);
                if (listener != null) {
                    totalProgress = totalProgress + len;
                    listener.downloadProgress(len, totalProgress, totalSize, file);
                }
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

    public static boolean toFileByBytes(File desFile, byte[] bytes) {
        return toFileByBytes(desFile, bytes, false);
    }

    public static boolean toFileByBytes(File desFile, byte[] bytes, boolean append) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(desFile, append);
            fos.write(bytes);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
     * 复制文件夹
     */
    public static void copyDirAndFile(String fromPath, String toPath) throws Exception {
        // 将原始路径和目标路径转为File
        File fromFile = new File(fromPath);
        if (!fromFile.exists()) {
            throw new RuntimeException("源文件不存在，无法复制");
        }

        File toFile = new File(toPath);
        if (!toFile.exists()) {
            toFile.mkdir();
        }

        if (fromFile.isFile()) {
            copyFile(fromFile.getAbsolutePath(), toPath + fromFile.getName());
        } else if (fromFile.isDirectory()) {
            File[] listFiles = fromFile.listFiles();
            if (listFiles == null) {
                return;
            }
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    // 递归遍历,因为是文件夹，所以最后都要加上"/"
                    copyFile(file.getAbsolutePath() + "/", toPath + file.getName() + "/");
                } else {
                    copyFile(file.getAbsolutePath(), toPath + file.getName());
                }
            }
        }
    }

    /**
     * 这里拷贝的是文件，不是文件夹
     * 拷贝文件用字节流即可（如果是对文本的操作，如修改，则得用字符流）
     */
    public static void copyFile(String srcPath, String destPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            out = new FileOutputStream(destPath);
            in = new FileInputStream(srcPath);

            byte[] data = new byte[10240];
            for (int len = -1; (len = in.read(data)) != -1; ) {
                out.write(data, 0, len);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("复制" + srcPath + "到" + destPath + "失败", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以队列的方式获取目录里的文件
     */
    public static List<File> getAllFilesByDirQueue(File dir, List<File> result) {

        LinkedList<File> queue = new LinkedList<File>();

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                queue.addFirst(file);
            } else {
                result.add(file);
            }
        }
        //遍历队列 子目录都在队列中
        while (!queue.isEmpty()) {
            //从队列中取出子目录
            File subDir = queue.removeLast();
            result.add(subDir);
            //遍历子目录。
            File[] subFiles = subDir.listFiles();
            for (File subFile : subFiles) {
                //子目录中还有子目录，继续存到队列
                if (subFile.isDirectory()) {
                    queue.addFirst(subFile);
                } else {
                    result.add(subFile);
                }
            }
        }
        return result;
    }

    /**
     * 获取目录中的所有文件（递归方式）(包括传入遍历的根目录)
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
     * 反序列化
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
     * 序列化
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
     * 根据指定的过滤器在指定目录下获取所有的符合过滤条件的文件，并存储到list集合中。
     *
     * @param dir    目录
     * @param filter 这里用的是FileFilter,和FileNameFilter差不多
     * @param list   返回list
     * @return list
     */
    public static List<File> getFilterFilesByDir(File dir, FileFilter filter, List<File> list) {
        if (dir != null && list != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            // File[] files = dir.listFiles(filter);
            // 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组
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
     * 获得指定后缀名的文件文件
     *
     * @param list 用来存放本地视频的集合
     * @param file 在哪个文件路径下查找
     */
    public void getVideoFile(final LinkedList<File> list, File file) {

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                // sdCard找到视频名称
                String name = file.getName();

                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4") || name.equalsIgnoreCase(".3gp") || name.equalsIgnoreCase(".wmv")) {
                        list.add(file);
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });
    }

    /**
     * 一个FileFilter模板
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


    /**
     * @param inputStream  System.in ,FileInputStream
     * @param outputStream System.out ,FileOutPutStream
     */
    public static void print(InputStream inputStream, OutputStream outputStream) {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            //字符流里面有一个字节缓冲区
            //true自动刷新，只对print println有效
            writer = new PrintWriter(outputStream, true);
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
                // writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Properties readProperty(File configFile) {
        FileReader fr = null;
        try {
            fr = new FileReader(configFile);
            Properties prop = new Properties();
            prop.load(fr);
            return prop;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Properties storeProperty(File file, Properties properties, String desc) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            properties.store(fw, desc);//desc为注释,不要写中文
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并文件
     *
     * @param mergedDir 需要合并的文件所在的文件夹
     * @param destFile  合并到哪个文件
     * @param fileType  mp3，mp4,等文件类型
     * @throws IOException
     */
    public static void mergeFile(File mergedDir, File destFile, String fileType) {
        SequenceInputStream sequenceInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {

            Vector<FileInputStream> vector = new Vector<FileInputStream>();
            File[] files = mergedDir.listFiles();
            if (files == null) {
                return;
            }
            //把需要合并的文件添加到集合
            for (File item : files) {
                if (item.getName().endsWith(fileType)) {
                    vector.add(new FileInputStream(item));
                }
            }

            sequenceInputStream = new SequenceInputStream(vector.elements());
            fileOutputStream = new FileOutputStream(destFile);

            byte[] buf = new byte[10240];
            int length = -1;
            while ((length = sequenceInputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, length);
                fileOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sequenceInputStream != null) {
                    sequenceInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
