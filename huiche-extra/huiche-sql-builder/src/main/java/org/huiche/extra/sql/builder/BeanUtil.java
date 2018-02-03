package org.huiche.extra.sql.builder;


import org.huiche.core.annotation.Column;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class BeanUtil {
    public static List<Field> getField(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
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

    public static <T extends Annotation> List<Class<?>> scan(Class<T> annotation, String packageName) {
        List<Class<?>> list = new ArrayList<>();
        String path = packageName.replace(".", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            if (url != null && url.toString().startsWith("file")) {
                String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                File dir = new File(filePath);
                List<File> fileList = new ArrayList<>();
                fetchFileList(dir, fileList);
                for (File f : fileList) {
                    String fileName = f.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        String nosuffixFileName = fileName.substring(8 + fileName.lastIndexOf("classes"), fileName.indexOf(".class"));
                        String filePackage = nosuffixFileName.replaceAll("\\\\", ".");
                        Class<?> clazz = Class.forName(filePackage);
                        T t = clazz.getAnnotation(annotation);
                        if (null != t) {
                            list.add(clazz);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void fetchFileList(File dir, List<File> fileList) {
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
