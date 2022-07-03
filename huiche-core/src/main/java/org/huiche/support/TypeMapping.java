package org.huiche.support;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maning
 */
public class TypeMapping {
    private static final Map<String, JDBCType> TYPE_MAP;

    static {
        TYPE_MAP = new HashMap<>();
        TYPE_MAP.put(Boolean.class.getCanonicalName(), JDBCType.BOOLEAN);

        TYPE_MAP.put(Byte.class.getCanonicalName(), JDBCType.TINYINT);
        TYPE_MAP.put(Short.class.getCanonicalName(), JDBCType.SMALLINT);
        TYPE_MAP.put(Integer.class.getCanonicalName(), JDBCType.INTEGER);
        TYPE_MAP.put(Long.class.getCanonicalName(), JDBCType.BIGINT);
        TYPE_MAP.put(Float.class.getCanonicalName(), JDBCType.DECIMAL);
        TYPE_MAP.put(Double.class.getCanonicalName(), JDBCType.DECIMAL);
        TYPE_MAP.put(BigInteger.class.getCanonicalName(), JDBCType.BIGINT);
        TYPE_MAP.put(BigDecimal.class.getCanonicalName(), JDBCType.DECIMAL);

        TYPE_MAP.put(Character.class.getCanonicalName(), JDBCType.CHAR);
        TYPE_MAP.put(String.class.getCanonicalName(), JDBCType.VARCHAR);
        TYPE_MAP.put(Enum.class.getCanonicalName(), JDBCType.VARCHAR);

        TYPE_MAP.put(byte[].class.getCanonicalName(), JDBCType.BLOB);

        TYPE_MAP.put(Date.class.getCanonicalName(), JDBCType.TIMESTAMP);
        TYPE_MAP.put(LocalDate.class.getCanonicalName(), JDBCType.DATE);
        TYPE_MAP.put(LocalTime.class.getCanonicalName(), JDBCType.TIME);
        TYPE_MAP.put(LocalDateTime.class.getCanonicalName(), JDBCType.TIMESTAMP);
        TYPE_MAP.put(OffsetTime.class.getCanonicalName(), JDBCType.TIME);
        TYPE_MAP.put(OffsetDateTime.class.getCanonicalName(), JDBCType.TIMESTAMP);
        TYPE_MAP.put(ZonedDateTime.class.getCanonicalName(), JDBCType.TIMESTAMP);
    }

    @NotNull
    public static JDBCType parseJdbcType(@NotNull Class<?> clazz) {
        JDBCType type = parseJdbcType(clazz.getCanonicalName());
        if (type == null && clazz.isEnum()) {
            type = parseJdbcType(Enum.class.getCanonicalName());
        }
        if (type == null) {
            throw new RuntimeException("can not support this type:" + clazz.getCanonicalName());
        }
        return type;
    }

    @Nullable
    public static JDBCType parseJdbcType(String className) {
        return TYPE_MAP.get(className);
    }
}
