package org.huiche.support;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class ReflectUtil {
    private static final Map<String, Field[]> CACHE = new WeakHashMap<>();
    private static final Predicate<Field> NORMAL_FIELD = field -> {
        int mod = field.getModifiers();
        return !Modifier.isStatic(mod) && !Modifier.isTransient(mod);
    };

    public static List<Field> scanFields(@NotNull Class<?> clazz, @Nullable Predicate<Field> filter) {
        List<Field> list = new ArrayList<>();
        if (clazz != Object.class) {
            list.addAll(0, scanFields(clazz.getSuperclass(), filter));
        }
        Field[] fields = CACHE.computeIfAbsent(clazz.getCanonicalName(), k -> clazz.getDeclaredFields());
        if (filter != null) {
            list.addAll(Arrays.stream(fields).filter(filter).collect(Collectors.toList()));
        } else {
            list.addAll(Arrays.asList(fields));
        }
        return list;
    }

    public static List<Field> scanNormalFields(@NotNull Class<?> clazz) {
        return scanFields(clazz, NORMAL_FIELD);
    }
}
