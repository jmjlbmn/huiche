package org.huiche.codegen;

import org.springframework.asm.ClassReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Maning
 */
public class CodegenUtil {
    public static Set<Class<?>> scan(Predicate<Class<?>> predicate, String... packages) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Set<String> nameSet = new HashSet<>();
        try {
            for (String pkg : packages) {
                for (Resource resource : resolver.getResources("classpath*:" + pkg.replaceAll("\\.", "/" ) + "/**/*.class" )) {
                    ClassReader reader = new ClassReader(resource.getInputStream());
                    nameSet.add(reader.getClassName().replaceAll("/", "\\." ));
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
