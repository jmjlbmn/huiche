package org.huiche.core.consts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 常量值的Bean
 *
 * @author Maning
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ConstValue {
    /**
     * 值
     */
    public String value;
    /**
     * 描述
     */
    public String text;
    /**
     * 扩展信息
     */
    public String extra;

    @Nonnull
    public static ConstValue put(@Nonnull String value, @Nullable String text) {
        ConstValue constValue = new ConstValue();
        constValue.value = value;
        constValue.text = text;
        return constValue;
    }

    @Nonnull
    public static ConstValue put(@Nonnull String value, @Nullable String text, @Nullable String extra) {
        ConstValue constValue = new ConstValue();
        constValue.value = value;
        constValue.text = text;
        constValue.extra = extra;
        return constValue;
    }
}