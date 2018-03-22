package org.huiche.core.controller;

import org.huiche.core.api.Page;
import org.huiche.core.entity.BaseEntity;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写BaseController)
 *
 * @author Maning
 */
public class RestCrudPageController<T extends BaseEntity> extends RestCrudController<T> implements Page<T> {
}
