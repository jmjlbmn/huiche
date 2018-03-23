package org.huiche.core.exception;

import lombok.Getter;
import lombok.Setter;
import org.huiche.core.util.StringUtil;

import java.util.Collection;

/**
 * 数据库异常
 *
 * @author Maning
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
