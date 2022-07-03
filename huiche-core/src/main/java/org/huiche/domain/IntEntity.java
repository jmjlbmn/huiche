package org.huiche.domain;

import org.huiche.annotation.Column;
import org.huiche.support.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Maning
 */
public class IntEntity implements Serializable {
    @Column(comment = "主键ID", primaryKey = PrimaryKey.AUTO)
    private Integer id;
    @Column(comment = "创建时间", length = 3, defaultValue = "CURRENT_TIMESTAMP(3)")
    private LocalDateTime createTime;
    @Column(comment = "修改时间", length = 3, defaultValue = "CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)")
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
