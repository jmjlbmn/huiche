package org.huiche.core.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Maning
 */
public interface Del<T extends BaseEntity> extends Api<T> {
    /**
     * 删除一条数据
     *
     * @param id ID
     * @return 成功
     */
    @DeleteMapping("{id}")
    default BaseResult<Long> del(@PathVariable Long id) {
        return ResultUtil.ok(service().delete(id));
    }
}