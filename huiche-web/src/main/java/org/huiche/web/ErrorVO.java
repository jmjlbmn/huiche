package org.huiche.web;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Maning
 */
@Data
@Accessors(chain = true)
public class ErrorVO implements Serializable {
    private Integer errCode;
    private String errMsg;
}
