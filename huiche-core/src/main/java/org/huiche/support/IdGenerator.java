package org.huiche.support;

import java.util.UUID;

/**
 * @author Maning
 */
public interface IdGenerator {
    IdGenerator DEFAULT = new IdGenerator() {
    };

    /**
     * 生成数字ID
     *
     * @return 数字ID
     */
    default Number generateNumberId() {
        throw new RuntimeException("must override generateNumberId");
    }

    /**
     * 生成字符串ID
     *
     * @return 字符串ID
     */
    default String generateStrId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
