package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Maning
 */
@UtilityClass
@Slf4j
public class LogUtil {
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
