package org.huiche.test.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huiche.annotation.sql.Column;
import org.huiche.annotation.sql.Table;
import org.huiche.data.entity.BaseEntity;

/**
 * @author Maning
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString(callSuper = true)
@Table(comment = "班级信息表")
public class Classes extends BaseEntity<Classes> {
    @Column(comment = "名称", length = 3)
    private String name;
    @Column(comment = "年级")
    private Integer grade;
}
