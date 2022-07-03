package org.huiche.support;

import org.jetbrains.annotations.NotNull;

import java.beans.Introspector;

/**
 * @author Maning
 */
public class NamingUtil {
    @NotNull
    public static String camel2underLine(@NotNull String camelStr) {
        int length = camelStr.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = camelStr.charAt(i);
            if (i > 0 && Character.isUpperCase(ch) && Character.isLowerCase(camelStr.charAt(i - 1))) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(ch));
        }
        return sb.toString();
    }

    @NotNull
    public static String deCapitalize(@NotNull String str) {
        return Introspector.decapitalize(str);
    }
}
