package org.huiche.web.util;

import org.huiche.core.consts.Const;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 请求工具类,提供获取IP等方法
 *
 * @author Maning
 */
public class RequestUtil {
    private static final String UN_KNOWN = "unknown";
    private static final String LOCALHOST_V4 = "127.0.0.1";
    private static final String LOCALHOST_V6 = "0:0:0:0:0:0:0:1";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;

    /**
     * 获取真实的IP地址
     *
     * @param request 请求
     * @return IP地址
     */
    @Nonnull
    public static String getIp(@Nullable HttpServletRequest request) {
        if (null == request) {
            return UN_KNOWN;
        }
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UN_KNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UN_KNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UN_KNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCALHOST_V4.equals(ipAddress) || LOCALHOST_V6.equals(ipAddress)) {
                try {
                    InetAddress address = InetAddress.getLocalHost();
                    ipAddress = address.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
        }
        if (ipAddress != null && ipAddress.indexOf(Const.COMMA) > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(Const.COMMA));
        }
        return null == ipAddress ? UN_KNOWN : ipAddress;
    }

    /**
     * 获取app运行的url
     *
     * @param request 请求
     * @return url
     */
    @Nonnull
    public static String getRequestUrl(@Nullable HttpServletRequest request) {
        if (null == request) {
            return "/";
        }
        StringBuilder url = new StringBuilder();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        url.append(scheme);
        url.append("://");

        url.append(request.getServerName());
        if (port != HTTP_PORT && port != HTTPS_PORT) {
            url.append(':');
            url.append(request.getServerPort());
        }
        url.append(request.getContextPath());
        return url.toString();
    }
}
