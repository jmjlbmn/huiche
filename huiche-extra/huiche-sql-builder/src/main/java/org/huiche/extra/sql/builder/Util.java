package org.huiche.extra.sql.builder;

import org.huiche.extra.sql.builder.info.FieldColumn;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Maning
 */
public class Util {
    public static void main(String[] args) {
        getField(Child.class);
    }

    public static List<FieldColumn> getField(Class<?> clazz) {
        Map<String, Field> map = new LinkedHashMap<>(16);
        List<Field> list = new ArrayList<>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            pull(fields, list);
        }
        Collections.reverse(list);
        for (Field field : list) {
            //去重,让子类Field覆盖父类
            map.put(field.getName(), field);
        }
        System.out.println(map.entrySet());
        return null;
    }

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


    public static class Parent<PK> {
        private PK id;

        public PK getId() {
            return id;
        }

        public void setId(PK id) {
            this.id = id;
        }
    }

    public static class Child extends Parent<Long> {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
