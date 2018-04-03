package org.huiche.extra.sql.builder.info;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 列比较信息对象
 *
 * @author Maning
 */
@Setter
@Getter
@Accessors(chain = true)
public class ColumnCompareInfo implements Serializable {
    private List<ColumnInfo> addList;
    private List<ColumnInfo> modifyList;
    private List<ColumnInfo> delList;

    @Override
    public String toString() {
        return "增加列=" + addList +
                ", 修改列=" + modifyList +
                ", 删除列=" + delList;
    }

    public boolean isEmpty() {
        return addList.isEmpty() && modifyList.isEmpty() && delList.isEmpty();
    }
}
