package org.huiche.core.util;

import org.huiche.core.consts.ConstClass;
import org.huiche.core.consts.ConstVal;
import org.huiche.core.consts.If;
import org.huiche.core.exception.Assert;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author Maning
 */
public class CheckUtil {
	/**
	 * 验证字符串是否是手机号码
	 *
	 * @param str 要验证的字符串
	 * @return 是否是手机号码
	 */
	public static boolean isPhoneNumber(String str) {
		return checkRegExp(str, "^(1[3-9])\\d{9}$");
	}

	public static boolean isIdNumber(String str) {
		return checkRegExp(str, "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
	}

	public static boolean isChinaName(String str) {
		return checkRegExp(str, "^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]+)*$");
	}

	public static boolean checkRegExp(String str, String regExp) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean checkLength(String str, int min, int max) {
		Assert.notNull("手机密码不能为空", str);
		Assert.ok("传入长度不符合规则", min >= 0 && max >= 0 && max >= min);
		return str.trim().length() >= min && str.trim().length() <= max;
	}

	public static boolean checkLength(String str, int max) {
		return checkLength(str, 0, max);
	}

	public static boolean isBooleanNumber(Integer test) {
		return BaseUtil.equals(If.YES, test) || BaseUtil.equals(If.NO, test);
	}

	public static <T extends ConstClass> boolean inConstant(Class<T> t, Object value) {
		List<ConstVal> list = ConstUtil.getValList(t);
		for (ConstVal val : list) {
			if (BaseUtil.equals(value.toString(), val.value)) {
				return true;
			}
		}
		return false;
	}
}
