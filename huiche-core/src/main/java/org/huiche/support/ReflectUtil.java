package org.huiche.support;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class ReflectUtil {
    @NotNull
    public static List<Field> scanFields(@NotNull Class<?> clazz, @Nullable Predicate<Field> filter) {
        List<Field> list = new ArrayList<>();
        if (clazz != Object.class) {
            list.addAll(0, scanFields(clazz.getSuperclass(), filter));
        }
        if (filter != null) {
            list.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(filter).collect(Collectors.toList()));
        } else {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return list;
    }
}
