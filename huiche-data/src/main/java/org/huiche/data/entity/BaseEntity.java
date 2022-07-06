package org.huiche.data.entity;


import org.huiche.annotation.sql.Column;

import java.io.Serializable;

/**
 * 基础实体类,提供Long类型主键,主要是用作快速开发,所以写死Long类型,需要其他类型主键时,建议增加一个字段,用作取数据时的主键使用
 *
 * @author Maning
 */
@SuppressWarnings("unchecked")
public class BaseEntity<T extends BaseEntity> implements Serializable {
    /**
     * 主键
     */
    @Column(isPrimaryKey = true, comment = "主键ID")
    private Long id;


    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}