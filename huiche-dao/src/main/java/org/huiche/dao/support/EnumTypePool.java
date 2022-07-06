package org.huiche.dao.support;

import com.querydsl.sql.types.EnumByNameType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maning
 */
public class EnumTypePool {
    private static final Map<String, EnumByNameType<? extends Enum<?>>> POOL = new HashMap<>();

    public static <T extends Enum<T>> void put(Class<T> clazz, EnumByNameType<T> type) {
        POOL.put(clazz.getCanonicalName(), type);
    }

    public static Collection<EnumByNameType<? extends Enum<?>>> types() {
        return POOL.values();
    }
}
