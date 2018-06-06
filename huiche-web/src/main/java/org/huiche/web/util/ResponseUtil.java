package org.huiche.web.util;

import org.huiche.core.exception.HuiCheException;

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
}
