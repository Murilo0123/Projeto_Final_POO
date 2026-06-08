package com.faculdade.taskmanager.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
