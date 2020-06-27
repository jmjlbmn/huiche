package org.huiche.web.util;

import org.huiche.core.exception.HuiCheException;
import org.huiche.core.util.LogUtil;
import org.huiche.web.ErrorVO;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 响应工具类,提供响应JSON,JS,CSS等方法
 *
 * @author Maning
 */
public class ResponseUtil {
    public static void respJson(HttpServletResponse response, String json) {
        respStr(response, json, "application/json");
    }

    public static void respJs(HttpServletResponse response, String js) {
        respStr(response, js, "application/javascript");
    }

    public static void respCss(HttpServletResponse response, String css) {
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

    public static void download(HttpServletResponse response, File file, String fileName) throws Exception {
        try (InputStream in = new BufferedInputStream(new FileInputStream(file)); OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            byte[] buffer = new byte[in.available()];
            if (in.read(buffer) > 0) {
                response.reset();
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
                response.addHeader("Content-Length", "" + file.length());
                response.setContentType("application/octet-stream");
                out.write(buffer);
            }
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
