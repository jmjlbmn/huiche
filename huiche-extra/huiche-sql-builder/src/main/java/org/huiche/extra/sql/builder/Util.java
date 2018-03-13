package org.huiche.extra.sql.builder;

import org.huiche.core.annotation.Column;
import org.huiche.extra.sql.builder.info.FieldColumn;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Maning
 */
public class Util {
    public static Map<String, Class<?>> getParameterizedTypeMap(Class<?> clazz) {
        if (clazz != Object.class) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                TypeVariable[] typeVariable = clazz.getSuperclass().getTypeParameters();
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                Map<String, Class<?>> map = new HashMap<>(4);
                for (int i = 0; i < typeVariable.length; i++) {
                    try {
                        map.put(typeVariable[i].getName(), Class.forName(actualTypeArguments[i].getTypeName()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return map;
            } else {
                return getParameterizedTypeMap(clazz.getSuperclass());
            }
        }
        return Collections.emptyMap();
    }

    public static void handleField(Class<?> clazz, List<Field> fieldList) {
        if (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            pull(fields, fieldList);
            handleField(clazz.getSuperclass(), fieldList);
        }
    }

    public static List<FieldColumn> getField(Class<?> clazz) {
        Map<String, Field> map = new LinkedHashMap<>(16);
        List<Field> fieldList = new ArrayList<>();
        handleField(clazz, fieldList);
        Collections.reverse(fieldList);
        for (Field field : fieldList) {
            //去重,让子类Field覆盖父类
            map.put(field.getName(), field);
        }
        List<FieldColumn> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Field field = map.get(key);
            Class type = field.getType();
            if (SUPPORT_TYPE_LIST.contains(type) || type.isEnum()) {
                list.add(new FieldColumn(key, type, field.getAnnotation(Column.class)));
            } else {
                if (field.getType().getName().equals(field.getGenericType().getTypeName())) {
                    System.err.println("类 " + clazz.getName() + "的 " + key + " 属性不支持处理,已跳过");
                } else {
                    Map<String, Class<?>> parameterizedTypeMap = getParameterizedTypeMap(clazz);
                    String parameterizedType = field.getGenericType().getTypeName();
                    if (parameterizedTypeMap.containsKey(parameterizedType)) {
                        list.add(new FieldColumn(key, parameterizedTypeMap.get(parameterizedType), field.getAnnotation(Column.class)));
                    } else {
                        System.err.println("类 " + clazz.getName() + "的 " + key + " 属性不支持处理,已跳过");
                    }
                }
            }
        }
        return list;
    }

    private static final List<Class> SUPPORT_TYPE_LIST = Arrays.asList(
            Boolean.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class);

    private static boolean isInvalid(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    private static void pull(Field[] src, List<Field> target) {
        if (src.length > 0) {
            for (int i = src.length - 1; i >= 0; i--) {
                // 倒序添加进去
                if (!isInvalid(src[i])) {
                    target.add(src[i]);
                }
            }
        }
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
