package org.huiche.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 错误日志
 *
 * @author Maning
 */
public class ErrorLog {
    public static final String BR = System.lineSeparator();
    public static final Logger LOG = LoggerFactory.getLogger(ErrorLog.class);

    public static void error(@Nonnull String msg, @Nonnull Throwable ex) {
        LOG.error(msg + BR + printStackTrace(ex));
    }

    public static void error(@Nonnull Throwable ex) {
        LOG.error(BR + printStackTrace(ex));
    }

    public static String printStackTrace(@Nonnull Throwable ex) {
        StringWriter sbw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sbw));
        return sbw.toString();
    }
}
