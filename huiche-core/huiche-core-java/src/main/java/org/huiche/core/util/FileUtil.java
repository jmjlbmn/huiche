package org.huiche.core.util;

import org.huiche.core.consts.Const;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * 文件工具类
 *
 * @author Maning
 */
public class FileUtil {
    private static final long NIO_LIMIT = 10;

    public static String getRandomName() {
        return DateUtil.now("yyyyMMddHHmmssSSS") + new Random().nextInt(1000);
    }

    public static String getFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (size < Const.FILE_SCALE) {
            return size + " B";
        } else if (size < Const.FILE_SCALE * Const.FILE_SCALE) {
            return df.format(size / (double) (Const.FILE_SCALE)) + " KB";
        } else if (size < Const.FILE_SCALE * Const.FILE_SCALE * Const.FILE_SCALE) {
            return df.format(size / (double) (Const.FILE_SCALE * Const.FILE_SCALE)) + " MB";
        } else {
            return df.format(size / (double) (Const.FILE_SCALE * Const.FILE_SCALE * Const.FILE_SCALE)) + " GB";
        }
    }

    public static boolean saveFile(String path, byte[] bytes) {
        if (bytes.length > Const.FILE_SCALE * Const.FILE_SCALE * NIO_LIMIT) {
            return saveFileNIO(path, bytes);
        } else {
            return saveFileIO(path, bytes);
        }
    }

    public static boolean saveFileIO(String path, byte[] bytes) {
        File file = new File(path);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
            bufferedOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveFileNIO(String path, byte[] bytes) {
        File file = new File(path);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             FileChannel fileChannel = fileOutputStream.getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 创建文件夹
     *
     * @param path 路径
     */
    public static void createFile(String path) {
        File file = new File(path.substring(0, path.lastIndexOf("/")));
        if (!file.exists()) {
            try {
                if (!file.mkdirs()) {
                    System.err.println("创建文件夹失败: " + file.getPath());
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static byte[] file2Bytes(File file) {
        if (null == file || !file.exists()) {
            return new byte[]{};
        }
        byte[] buffer;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
        return buffer;
    }

    public static byte[] file2Bytes(String path) {
        return file2Bytes(new File(path));
    }

    public static String getContentType(String filePath) {
        String type = null;
        try {
            type = Files.probeContentType(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return type;
    }

    public static void fetchFileList(File dir, List<File> fileList) {
        if (dir.isDirectory()) {
            File[] list = dir.listFiles();
            if (null != list) {
                for (File f : list) {
                    fetchFileList(f, fileList);
                }
            }
        } else {
            fileList.add(dir);
        }
    }


}
