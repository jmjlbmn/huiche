package org.huiche.core.file;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 上传文件Api
 *
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
    @Nonnull
    String uploadFile(@Nonnull byte[] bytes, @Nonnull Dir dir, @Nullable String suffix);

    /**
     * 上传文件
     *
     * @param bytes    文件
     * @param dir      目录
     * @param fileName 文件名
     * @return 文件访问地址
     */
    @Nonnull
    String uploadFileKeepName(@Nonnull byte[] bytes, @Nonnull Dir dir, @Nonnull String fileName);


    /**
     * 文件上传
     *
     * @param bytes 文件
     * @param dir   目录
     * @return 文件访问地址
     */
    @Nonnull
    default String uploadFile(@Nonnull byte[] bytes, @Nonnull Dir dir) {
        return uploadFile(bytes, dir, null);
    }

}
