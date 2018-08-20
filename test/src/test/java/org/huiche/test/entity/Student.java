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
@Table(comment = "学生信息表")
public class Student extends BaseEntity<Student> {
    @Column(comment = "姓名", length = 3)
    private String name;
    @Column(comment = "性别")
    private Integer sex;
    @Column(comment = "生日")
    private String birthday;
    @Column(comment = "班级")
    private Long classesId;
}
