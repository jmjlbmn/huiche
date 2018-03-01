package org.huiche.core.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 实体工具类
 *
 * @author Maning
 * @version 2017/9/19
 */
public class BeanUtil {
    public static String[] getNullFields(Object bean) {
        List<Field> list = getNullFieldList(bean);
        if (null == list) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (Field field : list) {
            if (!result.contains(field.getName())) {
                result.add(field.getName());
            }
        }
        return result.toArray(new String[result.size()]);
    }


    public static List<Field> getNullFieldList(Object bean) {
        if (null == bean) {
            return null;
        }
        List<Field> fields = getFieldsWithSuper(bean.getClass());
        if (null == fields) {
            return null;
        }
        List<Field> result = new ArrayList<>();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.get(bean) == null) {
                    result.add(field);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<Field> getFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                list.add(field);
            }
        }
        return list;
    }

    public static List<Field> getFieldsWithSuper(Class clazz) {
        List<Field> fields = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            fields.addAll(getFields(clazz));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static List<Class<?>> scan() {
        return scan(null, null, null);
    }

    public static List<Class<?>> scan(Predicate<Class<?>> classPredicate) {
        return scan(null, null, classPredicate);
    }

    public static List<Class<?>> scan(String rootPath, Predicate<Class<?>> classPredicate) {
        return scan(rootPath, null, classPredicate);
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
        FileUtil.fetchFileList(dir, fileList);
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


}
