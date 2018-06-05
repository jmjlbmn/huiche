package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.consts.ConstValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类,提供验证手机号,正则,中文姓名,长度,是否是常量属性等方法
 *
 * @author Maning
 */
@UtilityClass
public class CheckUtil {
    /**
     * 验证字符串是否是手机号码
     *
     * @param str 要验证的字符串
     * @return 是否是手机号码
     */
    public static boolean isPhoneNumber(@Nonnull String str) {
        return checkRegExp(str, "^(1[3-9])\\d{9}$");
    }

    /**
     * 是否是身份证号码
     *
     * @param str 字符串
     * @return 是否是身份证
     */
    public static boolean isIdNumber(@Nonnull String str) {
        return checkRegExp(str, "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    }

    /**
     * 是否是中文
     *
     * @param str 字符串
     * @return 是否中文
     */
    public static boolean isChinaName(@Nonnull String str) {
        return checkRegExp(str, "^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]+)*$");
    }

    /**
     * 是否匹配正则
     *
     * @param str    字符串
     * @param regExp 正则
     * @return 是否匹配
     */
    public static boolean checkRegExp(@Nonnull String str, @Nonnull String regExp) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检查字符串字符长度是否在范围内
     *
     * @param str 字符串
     * @param min 最小
     * @param max 最大
     * @return 是否在范围内
     */
    public static boolean checkLength(@Nonnull String str, int min, int max) {
        Assert.notNull("字符串不能为空", str);
        Assert.ok("传入长度不符合规则", min >= 0 && max >= 0 && max >= min);
        return str.trim().length() >= min && str.trim().length() <= max;
    }

    /**
     * 检查字符串是否不超过多少个字符
     *
     * @param str 字符串
     * @param max 最大字符
     * @return 是否不超过
     */
    public static boolean checkLength(@Nonnull String str, int max) {
        return checkLength(str, 0, max);
    }

    /**
     * 值是否是常量的值之一
     *
     * @param t     常量
     * @param value 值
     * @param <T>   常量类
     * @return 是否是常量值之一
     */
    public static <T> boolean inConstant(@Nonnull Class<T> t, @Nullable Object value) {
        if (HuiCheUtil.isNotEmpty(value)) {
            List<ConstValue> list = ConstUtil.list(t);
            for (ConstValue val : list) {
                if (HuiCheUtil.equals(value.toString(), val.value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
