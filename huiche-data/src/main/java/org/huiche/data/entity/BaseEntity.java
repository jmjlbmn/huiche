package org.huiche.data.entity;


import lombok.Getter;
import lombok.ToString;
import org.huiche.annotation.sql.Column;

import java.io.Serializable;

/**
 * 基础实体类,提供Long类型主键,String类型创建和修改时间,主要是用作快速开发,所以写死Long类型,需要其他类型主键时,建议增加一个字段,用作取数据时的主键使用
 *
 * @author Maning
 */
@SuppressWarnings("unchecked")
@Getter
@ToString
public class BaseEntity<T extends BaseEntity> implements Serializable {
    /**
     * 主键
     */
    @Column(isPrimaryKey = true, comment = "主键ID")
    private Long id;
    /**
     * 创建时间
     */
    @Column(length = 19, comment = "创建时间,yyyy-MM-dd HH:mm:ss")
    private String createTime;
    /**
     * 修改时间
     */
    @Column(length = 19, comment = "修改时间,yyyy-MM-dd HH:mm:ss")
    private String modifyTime;

    public T setCreateTime(String createTime) {
        this.createTime = createTime;
        return (T) this;
    }


    public T setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return (T) this;
    }


    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }
}