package org.huiche.core.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * @author Maning
 */
public interface DelSome<T extends BaseEntity> extends Api<T> {
    /**
     * 删除一条数据
     *
     * @param ids 以逗号分隔的id
     * @return 成功
     */
    @DeleteMapping("some")
    default BaseResult<Long> del(String ids) {
        return ResultUtil.ok(service().delete(ids));
    }
}