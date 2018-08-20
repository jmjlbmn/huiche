package org.huiche.test.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huiche.annotation.sql.Column;

/**
 * @author Maning
 * 测试确保该类不会被创建
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString(callSuper = true)
public class ClassesExtend extends Classes{
    @Column(comment = "名称", length = 3)
    private String name1;
    @Column(comment = "年级")
    private Integer grade2;
}
