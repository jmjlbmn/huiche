package org.huiche.web.util;

import org.huiche.core.exception.HuiCheException;
import org.huiche.core.util.LogUtil;
import org.huiche.web.ErrorVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 响应工具类,提供响应JSON,JS,CSS等方法
 *
 * @author Maning
 */
public class ResponseUtil {
    public static void respJSON(HttpServletResponse response, String json) {
        respStr(response, json, "application/json");
    }

    public static void respJS(HttpServletResponse response, String js) {
        respStr(response, js, "application/javascript");
    }

    public static void respCSS(HttpServletResponse response, String css) {
        respStr(response, css, "text/css");
    }

    public static void respStr(HttpServletResponse response, String str, String contentType) {
        response.setCharacterEncoding("utf-8");
        response.setContentType(contentType);
        try {
            response.getWriter().write(str);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new HuiCheException("返回数据出错");
        }
    }

    public static ErrorVO error(Exception e, HttpServletResponse response, boolean debug) {
        LogUtil.error(e);
        if (e instanceof HuiCheException) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ErrorVO().setErrCode(((HuiCheException) e).getCode()).setErrMsg(((HuiCheException) e).getMsg());
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ErrorVO().setErrCode(-1).setErrMsg(debug ? e.getLocalizedMessage() : "服务器错误");
        }
    }
}
