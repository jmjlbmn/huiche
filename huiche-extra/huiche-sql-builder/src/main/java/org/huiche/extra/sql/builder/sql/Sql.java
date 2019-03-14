package org.huiche.extra.sql.builder.sql;

import org.huiche.annotation.sql.Column;
import org.huiche.annotation.sql.Table;
import org.huiche.extra.sql.builder.Util;
import org.huiche.extra.sql.builder.info.ColumnCompareInfo;
import org.huiche.extra.sql.builder.info.ColumnInfo;
import org.huiche.extra.sql.builder.info.FieldColumn;
import org.huiche.extra.sql.builder.info.TableInfo;
import org.huiche.extra.sql.builder.naming.NamingRule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 默认的sql建表
 *
 * @author Maning
 */
public interface Sql {
    String SPACE = " ";
    String TAB = "\t";
    String BR = System.lineSeparator();
    String BRACKETS_START = "(";
    String BRACKETS_END = ")";
    String COMMA = ",";
    List<String> KEYWORD = Arrays.asList(
            "select", "insert", "update", "delete",
            "create", "alter", "drop", "truncate",
            "grant", "deny", "remove", "revoke", "transaction", "declare",
            "commit", "rollback", "savepoint", "begin", "call", "default",
            "to", "key", "primary", "foreign", "references", "not", "is", "null", "table", "values", "value", "length",
            "count", "distinct", "top", "as", "from", "where", "set", "into", "add", "or", "all", "like", "in",
            "group", "by", "order", "asc", "desc", "between", "having",
            "cross", "join", "left", "right", "outer", "inner", "natural", "using", "union", "except", "intersect",
            "avg", "min", "max", "sum",
            "int", "bigint", "smallint", "mediumint", "tinyint", "integer",
            "float", "double", "decimal", "numeric", "real", "precision",
            "char", "varchar", "text", "longtext", "mediumtext", "tinytext",
            "blob", "binary", "longblob", "mediumblob", "tinyblob", "varbinary",
            "bit", "bool", "boolean", "enum",
            "date", "datetime", "time", "timestamp", "year"
    );

    /**
     * 检查表/字段名称
     *
     * @param name 名称
     * @return 名称
     */
    @Nonnull
    static String checkName(@Nullable String name) {
        String regex = "^[a-zA-Z0-9_]*$";
        if (null == name || "".equals(name.trim())) {
            throw new RuntimeException("名称不能为空");
        }
        name = name.trim().toLowerCase();
        if (!name.matches(regex)) {
            throw new RuntimeException(name + "不符合规则,只能是字母数字下划线");
        }
        if (KEYWORD.contains(name)) {
            throw new RuntimeException(name + "是关键字/保留字,不可以使用");
        }
        return name;
    }

    /**
     * 获取表信息
     *
     * @param clazz      实体类
     * @param namingRule 命名规则
     * @return 表信息
     */
    @Nonnull
    static TableInfo getInfo(@Nonnull Class<?> clazz, @Nonnull NamingRule namingRule) {
        Table table = clazz.getAnnotation(Table.class);
        if (null == table) {
            throw new RuntimeException("实体类必须添加注解:" + Table.class.getName());
        }
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(checkName("".equals(table.value().trim()) ? namingRule.javaName2SqlName(clazz.getSimpleName()) : table.value().trim()));
        tableInfo.setComment(table.comment().trim());
        tableInfo.setCharset(table.charset().trim());
        tableInfo.setEngine(table.engine().trim());
        tableInfo.setCollation(table.collation().trim());
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        ColumnInfo columnInfo;
        for (FieldColumn field : Util.getField(clazz)) {
            columnInfo = new ColumnInfo();
            Column column = field.getColumn();
            if (null == column) {
                columnInfo.setName(checkName(namingRule.javaName2SqlName(field.getName())));
                columnInfo.setNotNull(false);
                columnInfo.setUnique(false);
                columnInfo.setIsPrimaryKey(false);
                columnInfo.setIsAutoIncrement(false);
            } else {
                if (!column.isDbField()) {
                    continue;
                }
                columnInfo.setName(checkName("".equals(column.value().trim()) ? namingRule.javaName2SqlName(field.getName()) : column.value().trim()));
                columnInfo.setNotNull(column.notNull());
                columnInfo.setUnique(column.unique());
                columnInfo.setIsPrimaryKey(column.isPrimaryKey());
                if (column.isPrimaryKey()) {
                    columnInfo.setIsAutoIncrement(column.isAutoIncrement());
                } else {
                    columnInfo.setIsAutoIncrement(false);
                }
                columnInfo.setComment("".equals(column.comment().trim()) ? "" : column.comment().trim());
            }
            Class<?> javaType = field.getType();
            boolean isBoolean = false;
            switch (javaType.getName()) {
                case "java.lang.Boolean":
                    columnInfo.setType(JDBCType.TINYINT);
                    isBoolean = true;
                    break;
                case "java.lang.Integer":
                    columnInfo.setType(JDBCType.INTEGER);
                    break;
                case "java.lang.Long":
                    columnInfo.setType(JDBCType.BIGINT);
                    break;
                case "java.lang.Float":
                case "java.lang.Double":
                    columnInfo.setType(JDBCType.DOUBLE);
                    break;
                case "java.math.BigDecimal":
                    columnInfo.setType(JDBCType.DECIMAL);
                    break;
                case "java.lang.String":
                    columnInfo.setType(JDBCType.VARCHAR);
                    break;
                default:
                    break;
            }
            if (null == columnInfo.getType()) {
                if (javaType.isEnum()) {
                    columnInfo.setType(JDBCType.VARCHAR);
                    if (null == columnInfo.getComment()) {
                        columnInfo.setComment(Arrays.toString(javaType.getEnumConstants()));
                    }
                } else {
                    throw new RuntimeException("目前不支持此数据类型:" + field.getName() + "(" + javaType.getName() + ")");
                }
            }
            if (columnInfo.getType().equals(JDBCType.VARCHAR)) {
                int length = 255;
                if (null != column) {
                    length = 0 == column.length() ? length : column.length();
                }
                columnInfo.setLength(length);
            } else if (isBoolean) {
                columnInfo.setLength(1);
            } else if (columnInfo.getType().equals(JDBCType.DECIMAL) || columnInfo.getType().equals(JDBCType.DOUBLE)) {
                if (null != column) {
                    int length = column.length();
                    int precision = column.precision();
                    if (precision > length) {
                        throw new RuntimeException("精度不能超过长度:" + field.getName());
                    }
                    if (length > 0) {
                        columnInfo.setLength(length);
                        columnInfo.setPrecision(precision);
                    }
                }
            }
            columnInfoList.add(columnInfo);
        }
        tableInfo.setColumnInfoList(columnInfoList);
        return tableInfo;
    }

    /**
     * 通过连接获取信息
     *
     * @param conn      连接
     * @param tableName 表名
     * @return 信息
     */
    @Nonnull
    static List<ColumnInfo> getInfo(@Nonnull Connection conn, @Nonnull String tableName) {
        List<ColumnInfo> list = new ArrayList<>();
        try {
            ResultSet rs = conn.getMetaData().getColumns(conn.getCatalog(), null, tableName, null);
            ColumnInfo columnInfo;
            while (rs.next()) {
                columnInfo = new ColumnInfo();
                columnInfo.setName(rs.getString("COLUMN_NAME"));
                columnInfo.setType(JDBCType.valueOf(rs.getInt("DATA_TYPE")));
                columnInfo.setNotNull("0".endsWith(rs.getString("NULLABLE")));
                columnInfo.setComment(rs.getString("REMARKS"));
                switch (columnInfo.getType()) {
                    case VARCHAR:
                        String typeName = rs.getString("TYPE_NAME");
                        if ("VARCHAR".equalsIgnoreCase(typeName)) {
                            columnInfo.setLength(rs.getInt("COLUMN_SIZE"));
                        }
                        break;
                    case DOUBLE:
                    case DECIMAL:
                        columnInfo.setLength(rs.getInt("COLUMN_SIZE"));
                        columnInfo.setPrecision(rs.getInt("DECIMAL_DIGITS"));
                        break;
                    default:
                        break;
                }
                list.add(columnInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 列比较
     *
     * @param javaList java列
     * @param dbList   数据库列
     * @return 差异
     */
    @Nonnull
    static ColumnCompareInfo compare(@Nonnull List<ColumnInfo> javaList, @Nonnull List<ColumnInfo> dbList) {
        ColumnCompareInfo columnCompareInfo = new ColumnCompareInfo();

        List<ColumnInfo> addList = new ArrayList<>();
        Iterator<ColumnInfo> javaIterator = javaList.iterator();
        while (javaIterator.hasNext()) {
            ColumnInfo java = javaIterator.next();
            if (!dbList.contains(java)) {
                addList.add(java);
                javaIterator.remove();
            }
        }
        columnCompareInfo.setAddList(addList);

        List<ColumnInfo> delList = new ArrayList<>();
        Iterator<ColumnInfo> dbIterator = dbList.iterator();
        while (dbIterator.hasNext()) {
            ColumnInfo db = dbIterator.next();
            if (!javaList.contains(db)) {
                dbIterator.remove();
                delList.add(db);
            }
        }
        columnCompareInfo.setDelList(delList);

        List<ColumnInfo> modifyList = new ArrayList<>();

        if (!javaList.isEmpty() && !dbList.isEmpty()) {
            for (ColumnInfo java : javaList) {
                for (ColumnInfo db : dbList) {
                    // 比较名称是否相同,已复写equals
                    if (java.equals(db) && !java.getIsPrimaryKey()) {
                        String comment = java.getComment();
                        if (!java.getType().equals(db.getType())) {
                            // 类型不同
                            // 判断长字符串
                            if (!(java.getType().equals(JDBCType.VARCHAR) && db.getType().equals(JDBCType.LONGVARCHAR))) {
                                modifyList.add(java);
                            }
                        } else if (null != comment && !"".contentEquals(comment.trim()) && !comment.equals(db.getComment())) {
                            // 注释不同
                            modifyList.add(java);
                        } else {
                            if (!java.getNotNull().equals(db.getNotNull())) {
                                // 是否非空不同
                                modifyList.add(java);
                            } else {
                                Integer length = java.getLength();
                                boolean lengthNoEq = null != length && !length.equals(db.getLength());
                                boolean noLength = null == length && null != db.getLength();
                                switch (java.getType()) {
                                    case VARCHAR:
                                        if (lengthNoEq || noLength) {
                                            //长度不同
                                            modifyList.add(java);
                                        }
                                        break;
                                    case DOUBLE:
                                    case DECIMAL:
                                        if (lengthNoEq) {
                                            //长度不同
                                            modifyList.add(java);
                                        } else {
                                            Integer precision = java.getPrecision();
                                            if (null != precision && !precision.equals(db.getPrecision())) {
                                                //精度不同
                                                modifyList.add(java);
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
        columnCompareInfo.setModifyList(modifyList);
        return columnCompareInfo;
    }

    /**
     * 获取创建表语句
     *
     * @param tableInfo 表信息
     * @return 语句
     */
    @Nonnull
    String getCreate(@Nonnull TableInfo tableInfo);

    /**
     * 获取创建表的语句(最开头)
     *
     * @param tableInfo 表信息
     * @return 语句
     */
    @Nonnull
    default String getCreateTableStart(@Nonnull TableInfo tableInfo) {
        return "CREATE TABLE " + tableInfo.getName() + SPACE + BRACKETS_START + BR;
    }

    /**
     * 获取创建表的额外语句(结尾)
     *
     * @param tableInfo 表信息
     * @return 语句
     */
    @Nonnull
    default String getCreateTableEnd(@Nonnull TableInfo tableInfo) {
        return BR + BRACKETS_END;
    }

    /**
     * 获取创建字段语句
     *
     * @param columnInfo 字段信息
     * @return 语句
     */
    @Nonnull
    String getCreateColumn(@Nonnull ColumnInfo columnInfo);

    /**
     * 获取删除列语句
     *
     * @param tableName  表
     * @param columnName 列
     * @return 语句
     */
    @Nonnull
    default String getDropColumn(@Nonnull String tableName, @Nonnull String columnName) {
        return "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
    }

    /**
     * 获取修改表增加字段语句
     *
     * @param tableName  表
     * @param columnInfo 列
     * @return 语句
     */
    @Nonnull
    default String getAlterAddColumn(@Nonnull String tableName, @Nonnull ColumnInfo columnInfo) {
        return "ALTER TABLE " + tableName + " ADD COLUMN " + getCreateColumn(columnInfo);
    }

    /**
     * 获取修改表修改字段语句
     *
     * @param tableName  表
     * @param columnInfo 列
     * @return 语句
     */
    @Nonnull
    default String getAlterModifyColumn(@Nonnull String tableName, @Nonnull ColumnInfo columnInfo) {
        return "ALTER TABLE " + tableName + " MODIFY COLUMN " + getCreateColumn(columnInfo);
    }

    /**
     * 获取修改表注释语句
     *
     * @param tableInfo 表
     * @return 语句
     */
    @Nonnull
    default String getAlterTableComment(@Nonnull TableInfo tableInfo) {
        String comment = null == tableInfo.getComment() ? "" : tableInfo.getComment();
        return "ALTER TABLE " + tableInfo.getName() + " COMMENT " + wrap(comment);
    }

    /**
     * 检查表是否存在
     *
     * @param conn      链接
     * @param tableName 表名
     * @return 是否存在
     */
    default boolean checkTableExists(@Nonnull Connection conn, @Nonnull String tableName) {
        try {
            conn.prepareStatement("SELECT 1 FROM " + tableName).execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取表的注释
     *
     * @param conn      连接
     * @param tableName 表名
     * @return 注释
     */
    @Nonnull
    default String getTableComment(@Nonnull Connection conn, @Nonnull String tableName) {
        String comment = "";
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(conn.getCatalog(), null, tableName, new String[]{"TABLE"});
            if (resultSet.next()) {
                return resultSet.getString("REMARKS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }

    /**
     * 中文格式化
     *
     * @param name 名称
     * @return 名称
     */
    @Nonnull
    default String wrap(String name) {
        return "'" + name + "'";
    }
}
