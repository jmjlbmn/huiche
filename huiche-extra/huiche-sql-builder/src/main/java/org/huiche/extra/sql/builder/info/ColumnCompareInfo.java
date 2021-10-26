package org.huiche.extra.sql.builder.info;

import java.io.Serializable;
import java.util.List;

/**
 * 列比较信息对象
 *
 * @author Maning
 */
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

    public List<ColumnInfo> getAddList() {
        return addList;
    }

    public void setAddList(List<ColumnInfo> addList) {
        this.addList = addList;
    }

    public List<ColumnInfo> getModifyList() {
        return modifyList;
    }

    public void setModifyList(List<ColumnInfo> modifyList) {
        this.modifyList = modifyList;
    }

    public List<ColumnInfo> getDelList() {
        return delList;
    }

    public void setDelList(List<ColumnInfo> delList) {
        this.delList = delList;
    }
}
