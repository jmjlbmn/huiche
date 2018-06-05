package org.huiche.core.consts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用于ConstUtil获取带有@ConstVal注解的常量类的说明和额外信息
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

    public ConstValue(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public ConstValue(String value, String text, String extra) {
        this.value = value;
        this.text = text;
        this.extra = extra;
    }
}