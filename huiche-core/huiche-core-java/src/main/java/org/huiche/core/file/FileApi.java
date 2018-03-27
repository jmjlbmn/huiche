package org.huiche.core.file;


/**
 * 上传文件Api
 * @author Maning
 */
public interface FileApi {
    /**
     * 上传文件
     *
     * @param bytes  文件
     * @param dir    目录
     * @param suffix 后缀
     * @return 文件访问地址
     */
    String uploadFile(byte[] bytes, Dir dir, String suffix);

    /**
     * 上传文件
     *
     * @param bytes    文件
     * @param dir      目录
     * @param fileName 文件名
     * @return 文件访问地址
     */
    String uploadFileKeepName(byte[] bytes, Dir dir, String fileName);


    /**
     * 文件上传
     *
     * @param bytes 文件
     * @param dir   目录
     * @return 文件访问地址
     */
    default String uploadFile(byte[] bytes, Dir dir) {
        return uploadFile(bytes, dir, null);
    }

}
