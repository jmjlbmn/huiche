package org.huiche.core.file;

import javax.annotation.Nonnull;

/**
 * 目录路径接口
 * @author Maning
 */
public interface Dir {
    /**
     * 路径
     *
     * @return 路径
     */
    @Nonnull
    String path();
}
