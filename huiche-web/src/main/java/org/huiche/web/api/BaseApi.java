package org.huiche.web.api;

import org.huiche.web.ErrorVO;
import org.huiche.web.util.ResponseUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Maning
 */
public class BaseApi {
    @Resource
    protected HttpServletRequest httpServletRequest;
    @Resource
    protected HttpServletResponse httpServletResponse;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorVO handleException(Exception e, HttpServletResponse response) {
        return ResponseUtil.error(e, response, false);
    }
}
