package org.huiche.extra.sql.builder;


import org.huiche.core.annotation.Column;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class FieldUtil {
    public static List<Field> getField(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>(16);
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : Arrays.asList(clazz.getDeclaredFields())) {
                int modifiers = field.getModifiers();
                if (Modifier.isPrivate(modifiers) && !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                    if (!map.containsKey(field.getName())) {
                        map.put(field.getName(), field);
                    }
                }
            }
        }
        return map.entrySet().stream().map(Map.Entry::getValue).sorted((a, b) -> {
            Column colA = a.getAnnotation(Column.class);
            Column colB = b.getAnnotation(Column.class);
            if (null != colA && null != colB) {
                int pk = 2;
                int notNull = 1;
                int sumA = (colA.isPrimaryKey() ? pk : 0) + (colA.notNull() ? notNull : 0);
                int sumB = (colB.isPrimaryKey() ? pk : 0) + (colB.notNull() ? notNull : 0);
                return sumB - sumA;
            }
            if (null == colA) {
                return 1;
            } else {
                return -1;
            }
        }).collect(Collectors.toList());
    }
    public static List<Class<?>> scan(String rootPath, Predicate<File> filePredicate, Predicate<Class<?>> classPredicate) {
        List<Class<?>> list = new ArrayList<>();
        File dir;
        if (null == rootPath) {
            dir = new File(System.getProperty("user.dir"));
        } else {
            dir = new File(rootPath);
        }
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("rootPath不正确:" + rootPath);
        } else {
            System.out.println("扫描根路径: " + dir.getPath());
        }
        Predicate<File> defaultFilePredicate = file -> {
            if (file.exists() && !file.isDirectory()) {
                String path = file.getPath();
                return path.contains("classes") && !path.contains("$") && path.endsWith(".class");
            }
            return false;
        };
        List<File> fileList = new ArrayList<>();
        fetchFileList(dir, fileList);
        for (File file : fileList) {
            String path = file.getPath();
            if (defaultFilePredicate.test(file)) {
                boolean fitFile = null != filePredicate && filePredicate.test(file);
                if (null == filePredicate || fitFile) {
                    String className = path.substring(8 + path.lastIndexOf("classes"), path.indexOf(".class")).replaceAll("\\\\", ".");
                    try {
                        Class<?> clazz = Class.forName(className);
                        boolean fitClass = null != classPredicate && classPredicate.test(clazz);
                        if (null == classPredicate || fitClass) {
                            list.add(clazz);
                        }
                    } catch (ClassNotFoundException ignored) {
                    }
                }
            }
        }
        return list;
    }
    public static List<Class<?>> scan(String rootPath, Predicate<Class<?>> classPredicate) {
        return scan(rootPath, null, classPredicate);
    }
    public static void fetchFileList(File dir, List<File> fileList) {
        if (dir.isDirectory()) {
            File[] list = dir.listFiles();
            if (null != list) {
                for (File f : list) {
                    fetchFileList(f, fileList);
                }
            }
        } else {
            fileList.add(dir);
        }
    }
}
