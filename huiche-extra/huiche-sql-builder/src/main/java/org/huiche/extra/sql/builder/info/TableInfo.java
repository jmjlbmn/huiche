package org.huiche.extra.sql.builder.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 表信息对象
 *
 * @author Maning
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
public class TableInfo implements Serializable {
    private String name;
    private String comment;
    private String charset;
    private String engine;
    private String collation;
    private List<ColumnInfo> columnInfoList;

}
