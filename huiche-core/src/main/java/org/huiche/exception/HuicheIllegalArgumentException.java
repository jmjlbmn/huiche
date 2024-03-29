package org.huiche.exception;

/**
 * @author Maning
 */
public class HuicheIllegalArgumentException extends HuicheException {
    public HuicheIllegalArgumentException(String message) {
        super(message);
    }

    public HuicheIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
