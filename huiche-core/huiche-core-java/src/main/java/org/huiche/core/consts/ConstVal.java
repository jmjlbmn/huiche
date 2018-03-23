package org.huiche.core.consts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 常量值bean
 *
 * @author Maning
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ConstVal {
    public String value;
    public String text;
    public String extra;

    public static ConstVal put(String value, String text) {
        ConstVal constVal = new ConstVal();
        constVal.value = value;
        constVal.text = text;
        return constVal;
    }

    public static ConstVal put(String value, String text, String extra) {
        ConstVal constVal = new ConstVal();
        constVal.value = value;
        constVal.text = text;
        constVal.extra = extra;
        return constVal;
    }
}