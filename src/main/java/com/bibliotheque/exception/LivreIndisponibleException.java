package com.bibliotheque.exception;

public class LivreIndisponibleException extends Exception {
 
    private static final long serialVersionUID = 1L;

    public LivreIndisponibleException() {
        super();
    }

    public LivreIndisponibleException(String message) {
        super(message);
    }

    public LivreIndisponibleException(Throwable cause) {
        super(cause);
    }

    public LivreIndisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
