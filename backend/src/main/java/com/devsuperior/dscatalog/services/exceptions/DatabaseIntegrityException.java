package com.devsuperior.dscatalog.services.exceptions;

public class DatabaseIntegrityException extends RuntimeException {
    public DatabaseIntegrityException(String msg) {
        super(msg);
    }
}
