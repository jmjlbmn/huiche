package org.huiche.exception;

/**
 * @author Maning
 */
public class HuicheException extends RuntimeException {
    public HuicheException(String message) {
        super(message, null, true, true);
    }

    public HuicheException(String message, Throwable cause) {
        super(message, cause, true, true);
    }
}
