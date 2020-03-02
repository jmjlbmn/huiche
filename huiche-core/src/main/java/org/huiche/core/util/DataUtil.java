package org.huiche.core.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.huiche.core.consts.If;
import org.huiche.core.enums.ValEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * 数据工具类,提供数据转换,复制,去重,随机等操作
 *
 * @author Maning
 */
@Slf4j
@UtilityClass
public class DataUtil {
    private static final Map<String, Map<String, PropertyInfo>> PROPERTY_CACHE = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Class<?>, Method[]> METHOD_CACHE = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Class<?>, PropertyInfo[]> FIELD_CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * 复制列表
     *
     * @param source 源集合
     * @param mapper 复制实现
     * @param <S>    源类型
     * @param <T>    目标类型
     * @return 目标数据集合
     */
    @Nonnull
    public static <S, T> List<T> copyList(@Nullable Collection<S> source, @Nonnull Function<S, T> mapper) {
        return copyList(source, mapper, true);
    }

    /**
     * 复制列表
     *
     * @param source     源集合
     * @param mapper     复制实现
     * @param ignoreNull 是否跳过空条目
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标数据集合
     */
    @Nonnull
    public static <S, T> List<T> copyList(@Nullable Collection<S> source, @Nonnull Function<S, T> mapper, boolean ignoreNull) {
        List<T> target = new ArrayList<>();
        if (HuiCheUtil.isEmpty(source)) {
            return target;
        }
        for (S obj : source) {
            if (null == obj) {
                if (!ignoreNull) {
                    target.add(null);
                }
            } else {
                target.add(mapper.apply(obj));
            }
        }
        return target;
    }

    /**
     * 复制对象属性,包含源对象中值为null的属性
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <S>    源类型
     * @param <T>    目标类型
     * @return 复制后的对象, 如果src为null, 直接返回target对象
     */
    @Nonnull
    public static <S, T> T copy(@Nullable S source, @Nonnull T target) {
        return copyProperties(source, target, true);
    }

    /**
     * 复制对象属性,,跳过源对象中值为null的属性,如果src为null,直接返回target对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <S>    源类型
     * @param <T>    目标类型
     * @return 复制后的对象, 如果src为null, 直接返回target对象
     */
    @Nonnull
    public static <S, T> T copyIgnoreNull(@Nullable S source, @Nonnull T target) {
        return copyProperties(source, target, false);
    }

    /**
     * 数组转集合
     *
     * @param t   数组
     * @param <T> 类
     * @return 集合
     */
    @Nonnull
    public static <T> List<T> arr2List(@Nonnull T[] t) {
        List<T> list = new ArrayList<>();
        if (t.length > 0) {
            list = Arrays.asList(t);
        }
        return list;
    }

    /**
     * 在一个集合中随机取出N个对象
     *
     * @param list   集合
     * @param length 随机取出数量
     * @param <T>    对象类
     * @return 随机对象
     */
    @Nonnull
    public static <T> List<T> randomList(@Nonnull List<T> list, int length) {
        if (list.size() > 0) {
            int size = list.size();
            if (length >= size) {
                return list;
            }
            Set<T> result = new HashSet<>();
            int dtSize = result.size();
            while (dtSize < length) {
                result.add(list.get(ThreadLocalRandom.current().nextInt(size)));
                dtSize = result.size();
            }
            return new ArrayList<>(result);
        }
        return list;
    }

    /**
     * 集合去重
     *
     * @param list 集合
     * @param <T>  对象类
     * @return 去重后的集合
     */
    @Nonnull
    public static <T extends Comparable<?>> List<T> distinctList(@Nonnull List<T> list) {
        if (HuiCheUtil.isNotEmpty(list)) {
            List<T> newList = new ArrayList<>();
            Set<T> set = new TreeSet<>();
            for (T t : list) {
                if (set.add(t)) {
                    newList.add(t);
                }
            }
            return newList;

        } else {
            return list;
        }
    }


    /**
     * 复制属性
     *
     * @param source   源对象
     * @param target   目标对象
     * @param copyNull 是否复制null
     * @param <S>      源类型
     * @param <T>      目标类型
     * @return 复制后的target对象
     */
    @Nonnull
    private static <S, T> T copyProperties(@Nullable S source, @Nonnull T target, boolean copyNull) {
        if (null == source) {
            log.debug("传入转换对象为空,直接返回传入的target");
            return target;
        }
        Assert.ok("复制集合或数组请使用copyList或copyArray", HuiCheUtil.isNotListAndArray(source) && HuiCheUtil.isNotListAndArray(target));
        Map<String, PropertyInfo> sourceMap = getPropertyMap(source.getClass());
        Map<String, PropertyInfo> targetMap = getPropertyMap(target.getClass());
        for (String propertyName : targetMap.keySet()) {
            if (sourceMap.containsKey(propertyName)) {
                try {
                    PropertyInfo sourceInfo = sourceMap.get(propertyName);
                    PropertyInfo targetInfo = targetMap.get(propertyName);
                    Class<?> sourceType = sourceInfo.field.getType();
                    Class<?> targetType = targetInfo.field.getType();
                    Method readMethod = sourceInfo.readMethod;
                    Method writeMethod = targetInfo.writeMethod;
                    if (null == readMethod) {
                        log.warn("[数据复制] {} 的 {} 属性找不到Get方法,将跳过", source.getClass().getSimpleName(), propertyName);
                        continue;
                    }
                    if (null == writeMethod) {
                        log.warn("[数据复制] {} 的 {} 属性找不到Set方法,将跳过 ", target.getClass().getSimpleName(), propertyName);
                        continue;
                    }
                    Object sourceVal = readMethod.invoke(source);
                    if (null != sourceVal || copyNull) {
                        if (null == sourceVal) {
                            Method targetReadMethod = targetInfo.readMethod;
                            if (null != targetReadMethod) {
                                Object targetVal = targetReadMethod.invoke(target);
                                if (null != targetVal) {
                                    writeMethod.invoke(target, (Object) null);
                                    log.warn("[数据复制][复写] {} 的 {} 被复写为 null ,原值为 {}", target.getClass().getSimpleName(), propertyName, targetVal);
                                }
                            }
                        } else {
                            if (sourceType.equals(targetType) && !Collection.class.isAssignableFrom(targetType)) {
                                // 类型相同且不是集合,直接赋值
                                writeMethod.invoke(target, sourceVal);
                            } else if (Boolean.class.equals(sourceType) && Integer.class.equals(targetType)) {
                                // 原类型Boolean,目标类型Integer
                                Boolean ok = (Boolean) sourceVal;
                                writeMethod.invoke(target, ok ? If.YES : If.NO);
                            } else if (Integer.class.equals(sourceType) && Boolean.class.equals(targetType)) {
                                // 原类型Integer,目标类型Boolean
                                Integer val = (Integer) sourceVal;
                                writeMethod.invoke(target, HuiCheUtil.equals(If.YES, val));
                            } else if (BigDecimal.class.equals(sourceType) && Double.class.equals(targetType)) {
                                // 原类型BigDecimal,目标类型Double
                                BigDecimal val = (BigDecimal) sourceVal;
                                writeMethod.invoke(target, val.doubleValue());
                            } else if (Double.class.equals(sourceType) && BigDecimal.class.equals(targetType)) {
                                // 原类型Double,目标类型BigDecimal
                                Double val = (Double) sourceVal;
                                writeMethod.invoke(target, new BigDecimal(val));
                            } else if (sourceType.isEnum() && Integer.class.equals(targetType)) {
                                //原类型枚举,目标类型Integer
                                if (ValEnum.class.isAssignableFrom(sourceType)) {
                                    ValEnum valEnum = (ValEnum) sourceVal;
                                    writeMethod.invoke(target, valEnum.val());
                                } else {
                                    Enum<?> valEnum = (Enum<?>) sourceVal;
                                    writeMethod.invoke(target, valEnum.ordinal());
                                    log.warn("[数据复制][警告] 源对象 {} 的 {} 是枚举类型 {},而目标对象 {} 的 {} 是Integer 现在根据ordinal赋值,极有可能不准确,请调整让枚举实现ValEnum接口或手动赋值",
                                            source.getClass().getSimpleName(),
                                            propertyName,
                                            sourceType.getName(),
                                            target.getClass().getSimpleName(),
                                            propertyName);
                                }
                            } else if (Integer.class.equals(sourceType) && targetType.isEnum()) {
                                //原类型Integer,目标类型枚举
                                if (ValEnum.class.isAssignableFrom(targetType)) {
                                    for (Object object : targetType.getEnumConstants()) {
                                        ValEnum valEnum = (ValEnum) object;
                                        if (HuiCheUtil.equals(valEnum.val(), sourceVal)) {
                                            writeMethod.invoke(target, valEnum);
                                            break;
                                        }
                                    }
                                } else {
                                    log.warn("[数据复制][警告] 源对象 {} 的 {} 是Integer {},而目标对象 {} 的 {} 是枚举类型 现在根据ordinal赋值,极有可能不准确,请调整让枚举实现ValEnum接口或手动赋值",
                                            source.getClass().getSimpleName(),
                                            propertyName,
                                            target.getClass().getSimpleName(),
                                            propertyName,
                                            targetType.getName());
                                    for (Object object : targetType.getEnumConstants()) {
                                        Enum<?> valEnum = (Enum<?>) object;
                                        if (HuiCheUtil.equals(valEnum.ordinal(), sourceVal)) {
                                            writeMethod.invoke(target, valEnum);
                                            break;
                                        }
                                    }
                                }

                            } else if (sourceType.isEnum() && targetType.isEnum()) {
                                // 原类型与目标类型都是枚举
                                Enum<?> enumVal = (Enum<?>) sourceVal;
                                for (Object oItem : targetType.getEnumConstants()) {
                                    Enum<?> item = (Enum<?>) oItem;
                                    if (item.name().equals(enumVal.name())) {
                                        writeMethod.invoke(target, item);
                                        break;
                                    }
                                }
                            } else if (List.class.isAssignableFrom(targetType)) {
                                try {
                                    Type targetItemType = getActualType(targetInfo.field);
                                    Type sourceItemType = getActualType(sourceInfo.field);
                                    if (null != targetItemType && null != sourceItemType) {
                                        if (targetItemType.equals(sourceItemType)) {
                                            writeMethod.invoke(target, sourceVal);
                                            List<Object> list = new ArrayList<>();
                                            if (sourceType.isArray()) {
                                                list.addAll(Arrays.asList((Object[]) sourceVal));
                                            } else if (Collection.class.isAssignableFrom(sourceType)) {
                                                list.addAll(((Collection<?>) sourceVal));
                                            } else {
                                                log.warn("[数据复制][出错] {} 的 {}({}) 与 {} 的 {}({}) 类型不匹配,未进行处理,跳过复制,如需要赋值请手动赋值",
                                                        source.getClass().getSimpleName(),
                                                        propertyName,
                                                        sourceType.getName(),
                                                        target.getClass().getSimpleName(),
                                                        propertyName,
                                                        targetType.getName());
                                            }
                                            writeMethod.invoke(target, list);
                                        } else {
                                            Class<?> targetItemClass = (Class<?>) targetItemType;
                                            Class<?> sourceItemClass = (Class<?>) sourceItemType;
                                            if (targetItemClass.isPrimitive() || sourceItemClass.isPrimitive()) {
                                                writeMethod.invoke(target, targetItemClass.cast(sourceVal));
                                            } else {
                                                List<Object> list = new ArrayList<>();
                                                if (sourceType.isArray()) {
                                                    for (Object item : (Object[]) sourceVal) {
                                                        list.add(copyProperties(item, targetItemClass.getConstructor().newInstance(), copyNull));
                                                    }
                                                } else if (Collection.class.isAssignableFrom(sourceType)) {
                                                    Collection<?> collection = (Collection<?>) sourceVal;
                                                    for (Object item : collection) {
                                                        list.add(copyProperties(item, targetItemClass.getConstructor().newInstance(), copyNull));
                                                    }

                                                } else {
                                                    log.warn("[数据复制][出错] {} 的 {}({}) 与 {} 的 {}({}) 类型不匹配,未进行处理,跳过复制,如需要赋值请手动赋值",
                                                            source.getClass().getSimpleName(),
                                                            propertyName,
                                                            sourceType.getName(),
                                                            target.getClass().getSimpleName(),
                                                            propertyName,
                                                            targetType.getName());
                                                }
                                                writeMethod.invoke(target, list);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.warn("[数据复制][出错] {} 的 {}({}) 与 {} 的 {}({}) 是集合且泛型类型不一致,处理出错,跳过复制,如需要赋值请手动赋值",
                                            source.getClass().getSimpleName(),
                                            propertyName,
                                            sourceType.getName(),
                                            target.getClass().getSimpleName(),
                                            propertyName,
                                            targetType.getName());
                                }
                            } else {
                                try {
                                    copyProperties(sourceVal, targetType.getConstructor().newInstance(), copyNull);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.warn("[数据复制][跳过] {} 的 {}({}) 与 {} 的 {}({}) 类型不一致,跳过复制,如需要赋值请手动赋值",
                                            source.getClass().getSimpleName(),
                                            propertyName,
                                            sourceType.getName(),
                                            target.getClass().getSimpleName(),
                                            propertyName,
                                            targetType.getName());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("[数据复制][出错] 复制 {} 的 {} 到 {} 出错,如需要赋值请手动赋值,出现错误: {}", source.getClass().getSimpleName(), propertyName, target.getClass().getSimpleName(), e.getLocalizedMessage());
                }
            } else {
                log.info("[数据复制][跳过] {} 没有 {} 的 {} 属性,如需要赋值请手动赋值", source.getClass().getSimpleName(), target.getClass().getSimpleName(), propertyName);
            }

        }
        return target;
    }

    /**
     * 获取类的属性map
     *
     * @param clazz 类
     * @return 熟悉map
     */
    @Nonnull
    private static Map<String, PropertyInfo> getPropertyMap(@Nonnull Class<?> clazz) {
        String className = clazz.getName();
        if (PROPERTY_CACHE.containsKey(className)) {
            return PROPERTY_CACHE.get(className);
        }
        Map<String, PropertyInfo> map = new WeakHashMap<>();
        for (PropertyInfo propertyInfo : getAllPropertyDescriptor(clazz)) {
            map.put(propertyInfo.name, propertyInfo);
        }
        PROPERTY_CACHE.put(className, map);
        return map;
    }

    /**
     * 获取类的属性信息列表
     *
     * @param clazz 类
     * @return 属性列表
     */
    @Nonnull
    private static List<PropertyInfo> getAllPropertyDescriptor(@Nonnull Class<?> clazz) {
        return Arrays.asList(new BeanInfo(clazz).getPropertyInfo());
    }

    /**
     * 获取字段的第一个泛型类型
     *
     * @param field 字段
     * @return 泛型类型
     */
    @Nullable
    private static Type getActualType(@Nonnull Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }

    private static class BeanInfo {
        @Nonnull
        private final Class<?> clazz;

        private BeanInfo(@Nonnull Class<?> clazz) {
            this.clazz = clazz;
            METHOD_CACHE.put(clazz, getMethods(clazz));
            FIELD_CACHE.put(clazz, getPropertyInfo(clazz));
        }

        /**
         * 静态方法,获取一个bean封装的属性信息,通过反射
         *
         * @param clazz 类型
         * @param <T>   泛型类型
         * @return 属性信息
         */
        @Nonnull
        private static <T> PropertyInfo[] getPropertyInfo(@Nonnull Class<T> clazz) {
            List<PropertyInfo> ps = new ArrayList<>();
            Field[] fields = getFields(clazz);
            for (Field field : fields) {
                if (field.getName().contains("$") || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                PropertyInfo info = new PropertyInfo(clazz, field);
                ps.add(info);
            }
            return ps.toArray(new PropertyInfo[0]);
        }

        /**
         * 静态方法,获取一个bean封装的Field信息,通过反射
         *
         * @param clazz 类型
         * @param <T>   泛型类型
         * @return 字段信息
         */
        @Nonnull
        private static <T> Field[] getFields(@Nonnull Class<T> clazz) {
            List<Field> fields = new ArrayList<>();
            for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
                fields.addAll(Arrays.asList(c.getDeclaredFields()));
            }
            return fields.toArray(new Field[0]);
        }

        /**
         * 静态方法,获取一个bean封装的Method信息,通过反射
         *
         * @param clazz 类型
         * @param <T>   泛型类型
         * @return 方法信息
         */
        @Nonnull
        private static <T> Method[] getMethods(@Nonnull Class<T> clazz) {
            List<Method> methods = new ArrayList<>();
            for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
                methods.addAll(Arrays.asList(c.getDeclaredMethods()));
            }
            return methods.toArray(new Method[0]);
        }

        /**
         * 获取这个bean的属性信息
         *
         * @return 属性信息
         */
        @Nonnull
        private PropertyInfo[] getPropertyInfo() {
            PropertyInfo[] propertyInfo;
            propertyInfo = FIELD_CACHE.get(clazz);
            if (propertyInfo == null) {
                propertyInfo = getPropertyInfo(clazz);
            }
            return propertyInfo;
        }
    }

    private static class PropertyInfo {
        private static final String GET = "get";
        private static final String SET = "set";
        private static final String IS = "is";
        private final Field field;
        private final String name;
        private Method readMethod;
        private Method writeMethod;

        private PropertyInfo(Class<?> clazz, Field field) {
            this.field = field;
            Method[] methods = METHOD_CACHE.get(clazz);
            Class<?> type = field.getType();
            name = field.getName();
            String fileName = StringUtil.convertFirstToUpperCase(name);
            String isReadMethodName = PropertyInfo.IS + fileName;
            String getReadMethodName = PropertyInfo.GET + fileName;
            String writeMethodName = PropertyInfo.SET + fileName;
            for (Method method : methods) {
                if (type == Boolean.class) {
                    if (getReadMethodName.equals(method.getName()) || isReadMethodName.equals(method.getName())) {
                        readMethod = method;
                    }
                } else {
                    if (getReadMethodName.equals(method.getName())) {
                        readMethod = method;
                    }
                }
                if (writeMethodName.equals(method.getName())) {
                    writeMethod = method;
                }
                if (readMethod != null && writeMethod != null) {
                    break;
                }
            }
        }
    }
}
