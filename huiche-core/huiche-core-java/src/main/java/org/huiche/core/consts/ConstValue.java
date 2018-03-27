package org.huiche.core.consts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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

    public static ConstValue put(String value, String text) {
        ConstValue constValue = new ConstValue();
        constValue.value = value;
        constValue.text = text;
        return constValue;
    }

    public static ConstValue put(String value, String text, String extra) {
        ConstValue constValue = new ConstValue();
        constValue.value = value;
        constValue.text = text;
        constValue.extra = extra;
        return constValue;
    }
}