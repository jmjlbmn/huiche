package org.huiche.core.consts;

import java.io.Serializable;

/**
 * 用于ConstUtil获取带有@ConstVal注解的常量类的说明和额外信息
 *
 * @author Maning
 */
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

    public String getValue() {
        return value;
    }

    public ConstValue setValue(String value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public ConstValue setText(String text) {
        this.text = text;
        return this;
    }

    public String getExtra() {
        return extra;
    }

    public ConstValue setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    @Override
    public String toString() {
        return "ConstValue{" +
                "value='" + value + '\'' +
                ", text='" + text + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}