package br.com.java_brasil.boleto.exception;

/**
 * Classe de Exceptions da biblioteca
 */
public class BoletoException extends RuntimeException{

    public BoletoException() {
    }

    public BoletoException(String message) {
        super(message);
    }

    public BoletoException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoletoException(Throwable cause) {
        super(cause);
    }

    public BoletoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
