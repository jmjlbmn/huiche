package org.huiche.core.annotation.search;

import java.lang.annotation.*;

/**
 * 用于QueryDsl筛选的类
 *
 * @author Maning
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchTable {
    /**
     * 列名
     *
     * @return 列名
     */
    String value() default "";
}
