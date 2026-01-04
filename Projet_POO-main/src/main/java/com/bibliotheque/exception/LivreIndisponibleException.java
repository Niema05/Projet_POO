package com.bibliotheque.exception;


public class LivreIndisponibleException extends Exception {
    
    public LivreIndisponibleException(String message) {
        super(message);
    }

    public LivreIndisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
