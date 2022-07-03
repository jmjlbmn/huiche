package org.huiche.codegen;

import org.huiche.annotation.Table;
import org.huiche.codegen.dialect.MySqlDialect;
import org.huiche.codegen.dialect.SqlDialect;
import org.huiche.codegen.domain.ColumnInfo;
import org.huiche.codegen.domain.TableInfo;
import org.huiche.support.ReflectUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.asm.ClassReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class HuicheCodegen {
    public static void ddlPrint(@NotNull SqlDialect dialect, @NotNull String... packages) {
        ddl(dialect, packages).forEach(System.out::println);
    }

    public static List<String> ddl(@NotNull SqlDialect dialect, @NotNull String... packages) {
        List<String> list = new ArrayList<>();
        for (Class<?> entity : scan(i -> i.isAnnotationPresent(Table.class), packages)) {
            list.add(dialect.createTable(TableInfo.of(entity),
                    ReflectUtil.scanFields(entity, field -> {
                        int mod = field.getModifiers();
                        return !Modifier.isStatic(mod) && !Modifier.isTransient(mod);
                    }).stream().map(ColumnInfo::of).collect(Collectors.toList())));
        }
        return list;
    }

    public static List<String> ddlMysql(@NotNull String... packages) {
        return ddl(MySqlDialect.DEFAULT, packages);
    }

    public static void ddlMysqlPrint(@NotNull String... packages) {
        ddlMysql(packages).forEach(System.out::println);
    }

    @NotNull
    public static Set<Class<?>> scan(@Nullable Predicate<Class<?>> predicate, @NotNull String... packages) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Set<String> nameSet = new HashSet<>();
        try {
            for (String pkg : packages) {
                for (Resource resource : resolver.getResources("classpath*:" + pkg.replaceAll("\\.", "/") + "/**/*.class")) {
                    ClassReader reader = new ClassReader(resource.getInputStream());
                    nameSet.add(reader.getClassName().replaceAll("/", "\\."));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<Class<?>> set = new HashSet<>(nameSet.size());
        for (String className : nameSet) {
            try {
                Class<?> clazz = Class.forName(className);
                if (predicate != null && predicate.test(clazz)) {
                    set.add(clazz);
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        return set;
    }
}
