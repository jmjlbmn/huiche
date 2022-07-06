package org.huiche.support;

import org.jetbrains.annotations.NotNull;

import java.beans.Introspector;

/**
 * @author Maning
 */
public class NamingUtil {
    public static String camel2snake(@NotNull String camelCase) {
        int length = camelCase.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = camelCase.charAt(i);
            if (i > 0 && Character.isUpperCase(ch) && Character.isLowerCase(camelCase.charAt(i - 1))) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(ch));
        }
        return sb.toString();
    }

    public static String pascal2camel(@NotNull String pascalCase) {
        return Introspector.decapitalize(pascalCase);
    }
}
