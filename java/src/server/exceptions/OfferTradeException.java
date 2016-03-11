package server.exceptions;

/**
 * Exception for an invalid player
 */
public class OfferTradeException extends Exception {
    public OfferTradeException(String message) {
        super(message);
    }
}
