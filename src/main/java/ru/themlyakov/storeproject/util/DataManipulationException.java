package ru.themlyakov.storeproject.util;

import lombok.Getter;

public class DataManipulationException extends RuntimeException {

    @Getter
    private String reason;
    public DataManipulationException(String message) {
        this.reason = message;
    }
}
