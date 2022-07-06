package org.huiche.codegen;

import org.huiche.annotation.Table;
import org.huiche.codegen.dialect.SqlDialect;
import org.huiche.codegen.domain.ColumnInfo;
import org.huiche.codegen.domain.TableInfo;
import org.huiche.support.ReflectUtil;
import org.springframework.asm.ClassReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class Codegen {
    public static void ddlPrint(SqlDialect dialect, String... packages) {
        ddl(dialect, packages).forEach(System.out::println);
    }

    public static List<String> ddl(SqlDialect dialect, String... packages) {
        List<String> list = new ArrayList<>();
        for (Class<?> entity : scan(i -> i.isAnnotationPresent(Table.class), packages)) {
            list.add(dialect.createTable(TableInfo.of(entity),
                    ReflectUtil.scanNormalFields(entity).stream().map(ColumnInfo::of).collect(Collectors.toList())));
        }
        return list;
    }

    public static Set<Class<?>> scan(Predicate<Class<?>> predicate, String... packages) {
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
