package org.huiche.core.encoder;

/**
 * 密码加密接口定义
 *
 * @author Maning
 */
public interface PasswordEncoder {
    /**
     * 加密
     *
     * @param plainPassword 明文密码
     * @return 加密后密码
     */
    String encode(CharSequence plainPassword);

    /**
     * 测试匹配
     *
     * @param plainPassword  明文密码
     * @param encodePassword 加密密码
     * @return 是否匹配
     */
    boolean matches(CharSequence plainPassword, String encodePassword);
}
