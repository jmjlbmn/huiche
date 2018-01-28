package org.huiche.extra.sql.builder.sql;

import org.huiche.core.annotation.Column;
import org.huiche.core.annotation.Table;
import org.huiche.extra.sql.builder.BeanUtil;
import org.huiche.extra.sql.builder.info.ColumnCompareInfo;
import org.huiche.extra.sql.builder.info.ColumnInfo;
import org.huiche.extra.sql.builder.info.TableInfo;
import org.huiche.extra.sql.builder.naming.NamingRule;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Maning
 * @date 2018/1/22
 */
public interface Sql {
    String SPACE = " ";
    String TAB = "\t";
    String BR = System.lineSeparator();
    String BRACKETS_START = "(";
    String BRACKETS_END = ")";
    String COMMA = ",";

    /**
     * 获取创建表语句
     *
     * @param tableInfo 表信息
     * @return 语句
     */
    String getCreate(TableInfo tableInfo);

    /**
     * 获取创建表的语句(最开头)
     *
     * @param tableInfo 表信息
     * @return 语句
     */
    default String getCreateTableStart(TableInfo tableInfo) {
        return "CREATE TABLE " + tableInfo.getName() + SPACE + BRACKETS_START + BR;
    }

    /**
     * 获取创建表的额外语句(结尾)
     *
     * @param tableInfo 表信息
     * @return 语句
     */
    default String getCreateTableEnd(TableInfo tableInfo) {
        return BR + BRACKETS_END;
    }

    /**
     * 获取创建字段语句
     *
     * @param columnInfo 字段信息
     * @return 语句
     */
    String getCreateColumn(ColumnInfo columnInfo);


    /**
     * 获取删除列语句
     *
     * @param tableName  表
     * @param columnName 列
     * @return 语句
     */
    default String getDropColumn(String tableName, String columnName) {
        return "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
    }

    /**
     * 获取修改表增加字段语句
     *
     * @param tableName  表
     * @param columnInfo 列
     * @return 语句
     */
    default String getAlterAddColumn(String tableName, ColumnInfo columnInfo) {
        return "ALTER TABLE " + tableName + " ADD COLUMN " + getCreateColumn(columnInfo);
    }

    /**
     * 获取删除索引语句
     *
     * @param tableName 表名
     * @param indexName 索引名
     * @return 语句
     */
    default String getDropIndex(String tableName, String indexName) {
        return "ALTER TABLE " + tableName + " DROP INDEX " + indexName;
    }

    /**
     * 获取修改表修改字段语句
     *
     * @param tableName  表
     * @param columnInfo 列
     * @return 语句
     */
    default String getAlterModifyColumn(String tableName, ColumnInfo columnInfo) {
        return "ALTER TABLE " + tableName + " MODIFY COLUMN " + getCreateColumn(columnInfo);
    }

    /**
     * 获取修改表注释语句
     *
     * @param tableInfo 表
     * @return 语句
     */
    default String getAlterTableComment(TableInfo tableInfo) {
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
    default boolean checkTableExists(Connection conn, String tableName) {
        try {
            conn.prepareStatement("SELECT 1 FROM " + tableName).execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * 中文格式化
     *
     * @param name 名称
     * @return 名称
     */
    default String wrap(String name) {
        return "'" + name + "'";
    }

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
    static String checkName(String name) {
        if (null == name || "".equals(name.trim())) {
            throw new RuntimeException("名称不能为空");
        }
        name = name.trim().toLowerCase();
        if (!name.matches("^[a-zA-Z0-9_]*$")) {
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
    static TableInfo getInfo(Class<?> clazz, NamingRule namingRule) {
        Table table = clazz.getAnnotation(Table.class);
        if (null == table) {
            throw new RuntimeException("实体类必须添加注解:" + Table.class.getName());
        }
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(checkName("".equals(table.value().trim()) ? namingRule.javaName2SqlName(clazz.getSimpleName()) : table.value().trim()));
        tableInfo.setComment(table.comment().trim());
        tableInfo.setCharset(table.charset().trim());
        tableInfo.setEngine(table.engine().trim());
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        ColumnInfo columnInfo;
        for (Field field : BeanUtil.getField(clazz)) {
            columnInfo = new ColumnInfo();
            Column column = field.getAnnotation(Column.class);
            if (null == column) {
                columnInfo.setName(checkName(namingRule.javaName2SqlName(field.getName())));
                columnInfo.setNotNull(false);
                columnInfo.setUnique(false);
                columnInfo.setPrimaryKey(false);
            } else {
                if (!column.isDbField()) {
                    continue;
                }
                columnInfo.setName(checkName("".equals(column.value().trim()) ? namingRule.javaName2SqlName(field.getName()) : column.value().trim()));
                columnInfo.setNotNull(column.notNull());
                columnInfo.setUnique(column.unique());
                columnInfo.setPrimaryKey(column.isPrimaryKey());
                columnInfo.setComment("".equals(column.comment().trim()) ? null : column.comment().trim());
            }
            Class<?> javaType = field.getType();
            boolean isBoolean = false;
            boolean isEnum = false;
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
                    isEnum = true;
                    if (null == columnInfo.getComment()) {
                        columnInfo.setComment(Arrays.toString(javaType.getEnumConstants()));
                    }
                } else {
                    throw new RuntimeException("目前不支持此数据类型:" + field.getName() + "(" + javaType.getName() + ")");
                }
            }
            if (columnInfo.getType().equals(JDBCType.VARCHAR)) {
                int length = isEnum ? 20 : 100;
                if (null != column) {
                    length = 0 == column.length() ? length : column.length();
                }
                columnInfo.setLength(length);
            } else if (isBoolean) {
                columnInfo.setLength(1);
            } else if (columnInfo.getType().equals(JDBCType.DECIMAL)) {
                int length = 10;
                int precision = 2;
                if (null != column) {
                    length = 0 == column.length() ? length : column.length();
                    precision = 0 == column.precision() ? precision : column.precision();
                    if (precision > length) {
                        throw new RuntimeException("精度不能超过长度:" + field.getName());
                    }
                }
                columnInfo.setLength(length);
                columnInfo.setPrecision(precision);
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
    static List<ColumnInfo> getInfo(Connection conn, String tableName) {
        List<ColumnInfo> list = new ArrayList<>();
        try {
            ResultSet rs = conn.getMetaData().getColumns(null, null, tableName, null);
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
    static ColumnCompareInfo compare(List<ColumnInfo> javaList, List<ColumnInfo> dbList) {
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
                delList.add(db);
                dbIterator.remove();
            }
        }
        columnCompareInfo.setDelList(delList);

        List<ColumnInfo> modifyList = new ArrayList<>();

        if (!javaList.isEmpty() && !dbList.isEmpty()) {
            for (ColumnInfo java : javaList) {
                for (ColumnInfo db : dbList) {
                    if (java.equals(db)) {
                        if (!java.getType().equals(db.getType())) {
                            // 类型不同
                            modifyList.add(java);
                        } else {
                            // 类型相同
                            Integer length = java.getLength();
                            if (JDBCType.VARCHAR.equals(java.getType())) {
                                if (null != length && !length.equals(db.getLength())) {
                                    //长度不同
                                    modifyList.add(java);
                                }
                            }
                            if (JDBCType.DECIMAL.equals(java.getType())) {
                                Integer precision = java.getPrecision();
                                if ((null != length && !length.equals(db.getLength())) || (null != precision && !precision.equals(db.getPrecision()))) {
                                    //长度/精度不同
                                    modifyList.add(java);
                                }
                            }
                            if(!java.getNotNull().equals(db.getNotNull())){
                                // 是否非空不同
                                modifyList.add(java);
                            }
                            String comment = java.getComment();
                            if (null != comment && !"".contentEquals(comment.trim()) && !comment.equals(db.getComment())) {
                                // 注释不同
                                modifyList.add(java);
                            }
                        }
                    }
                }
            }
        }
        columnCompareInfo.setModifyList(modifyList);
        return columnCompareInfo;
    }
}
