package org.huiche.exception;

/**
 * @author Maning
 */
public class HuicheException extends RuntimeException {
    public HuicheException(String message) {
        super(message, null, false, false);
    }

    public HuicheException(String message, Throwable cause) {
        super(message, cause, false, false);
    }
}
