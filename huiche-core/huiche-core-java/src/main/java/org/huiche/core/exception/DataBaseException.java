package org.huiche.core.exception;

import lombok.Getter;
import lombok.Setter;
import org.huiche.core.util.StringUtil;

import java.util.Collection;

/**
 * @author Maning
 * 数据库异常
 */
@Setter
@Getter
public class DataBaseException extends BaseException {
    public DataBaseException(String... msg) {
        super(SystemError.DB_NOT_ALLOW_FIELD_ERROR, StringUtil.join(msg));
    }

    public DataBaseException(Collection<String> msg) {
        super(SystemError.DB_NOT_ALLOW_FIELD_ERROR, StringUtil.join(msg));
    }
}
