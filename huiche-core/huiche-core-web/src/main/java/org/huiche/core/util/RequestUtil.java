package org.huiche.core.util;

import org.huiche.core.consts.Const;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Maning
 */
public class RequestUtil {
    private static final String UN_KNOWN = "unknown";
    private static final String LOCALHOST_V4 = "127.0.0.1";
    private static final String LOCALHOST_V6 = "0:0:0:0:0:0:0:1";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;

    public static String getIp(HttpServletRequest request) {
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
                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
        }
        if (ipAddress != null && ipAddress.indexOf(Const.COMMA) > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(Const.COMMA));
        }
        return ipAddress;
    }

    public static String getRequestURL(HttpServletRequest req) {
        StringBuilder url = new StringBuilder();
        String scheme = req.getScheme();
        int port = req.getServerPort();
        url.append(scheme);
        url.append("://");

        url.append(req.getServerName());
        if (port != HTTP_PORT && port != HTTPS_PORT) {
            url.append(':');
            url.append(req.getServerPort());
        }
        url.append(req.getContextPath());
        return url.toString();
    }
}
