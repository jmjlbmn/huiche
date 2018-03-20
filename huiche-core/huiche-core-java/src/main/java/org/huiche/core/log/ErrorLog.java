package org.huiche.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Maning
 */
public class ErrorLog {
    public static final String BR = System.lineSeparator();
    public static final Logger LOG = LoggerFactory.getLogger("异常追踪");

    public static void error(String msg, Throwable ex) {
        LOG.error(msg + BR + printStackTrace(ex));
    }

    public static void error(Throwable ex) {
        LOG.error(BR + printStackTrace(ex));
    }

    public static String printStackTrace(Throwable ex) {
        StringWriter sbw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sbw));
        return sbw.toString();
    }
}
