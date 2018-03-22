package org.huiche.core.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Maning
 */
public interface GetSome<T extends BaseEntity> extends Api<T> {
    /**
     * 获取单条数据
     *
     * @param ids 逗号分隔的
     * @return 数据
     */
    @GetMapping("some")
    default BaseResult<List<T>> get(String ids) {
        return ResultUtil.ok(service().get(ids));
    }
}