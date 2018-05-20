package org.huiche.core.consts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * 常量值的Bean
 *
 * @author Maning
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ConstValue implements Serializable {
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

    public ConstValue(@Nonnull String value, @Nullable String text) {
        this.value = value;
        this.text = text;
    }

    public ConstValue(@Nonnull String value, @Nullable String text, @Nullable String extra) {
        this.value = value;
        this.text = text;
        this.extra = extra;
    }
}