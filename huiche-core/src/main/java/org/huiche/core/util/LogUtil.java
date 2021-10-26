package org.huiche.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Maning
 */
public class LogUtil {
    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);
    public static void error(@Nonnull String msg, @Nonnull Throwable ex) {
        log.error(msg + System.lineSeparator() + printStackTrace(ex));
    }

    public static void error(@Nonnull Throwable ex) {
        log.error(printStackTrace(ex));
    }

    public static String printStackTrace(@Nonnull Throwable ex) {
        StringWriter sbw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sbw));
        return sbw.toString();
    }
}
