package org.huiche.dao;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.sql.RelationalPath;
import org.huiche.annotation.Column;
import org.huiche.support.IdGenerator;
import org.huiche.support.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.TypeMismatchDataAccessException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class IdInfo {
    private final String name;
    private final StringPath wherePath;
    private final Path<?> path;
    private final Class<?> type;
    private final PrimaryKey idType;
    private final Field field;
    private final IdGenerator idGenerator;

    public IdInfo(RelationalPath<?> table, Path<?> column) {
        PathMetadata metadata = column.getMetadata();
        this.name = metadata.getName();
        this.path = column;
        this.wherePath = Expressions.stringPath(metadata.getParent(), this.name);
        this.type = column.getType();
        Field field = ReflectionUtils.getFieldOrNull(table.getType(), name);
        if (field == null) {
            throw new TypeMismatchDataAccessException("can not find field: " + name + " in " + type.getCanonicalName());
        } else {
            this.field = field;
        }
        this.field.setAccessible(true);
        Column colAnno = column.getAnnotatedElement().getAnnotation(Column.class);
        if (colAnno == null || PrimaryKey.NOT_PK.equals(colAnno.primaryKey())) {
            throw new TypeMismatchDataAccessException(type.getCanonicalName() + "#" + name + "is not a pk,you need add a @Column and set isPrimaryKey=true");
        }
        this.idType = colAnno.primaryKey();
        if (PrimaryKey.GENERATE.equals(this.idType)) {
            if (colAnno.idGenerator() == IdGenerator.class) {
                this.idGenerator = IdGenerator.DEFAULT;
            } else {
                try {
                    this.idGenerator = colAnno.idGenerator().getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("can not load idGenerator: " + colAnno.idGenerator().getCanonicalName(), e);
                }
            }
        } else {
            this.idGenerator = null;
        }
    }

    @Override
    public String toString() {
        return "IdInfo{" +
                "name='" + name + '\'' +
                ", wherePath=" + wherePath +
                ", path=" + path +
                ", type=" + type +
                ", idType=" + idType +
                ", field=" + field +
                ", idGenerator=" + idGenerator +
                '}';
    }

    public <ID> Predicate idWhere(ID id) {
        return this.wherePath.eq(String.valueOf(id));
    }

    public <ID> Predicate idsWhere(@NotNull Collection<ID> ids) {
        return this.wherePath.in(ids.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    public Path<?> getPath() {
        return path;
    }

    public boolean isAutoIncrement() {
        return PrimaryKey.AUTO.equals(idType);
    }

    public void setIdVal(Object entity, Object val) {
        try {
            field.set(entity, val);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    Object generateId() {
        if (Number.class.isAssignableFrom(type)) {
            return idGenerator.generateNumberId();
        } else if (CharSequence.class.isAssignableFrom(type)) {
            return idGenerator.generateStrId();
        } else {
            throw new RuntimeException("not support type:" + type.getCanonicalName() + " generate id");
        }
    }

    public Object getIdVal(Object entity) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理ID, 如果是null进行赋值
     *
     * @param entity 要存储的实体
     * @return 是否需要自增
     */
    public boolean handleId(Object entity) {
        try {
            Object val = field.get(entity);
            if (val == null) {
                switch (idType) {
                    case AUTO:
                        return true;
                    case GENERATE:
                        field.set(entity, generateId());
                        break;
                    default:
                        throw new RuntimeException("this operation need primary key:" + name + " has a value");
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
