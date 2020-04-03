package com.reedelk.file.internal.exception;

import com.reedelk.runtime.api.exception.ESBException;

public class NotValidFileException extends ESBException {

    public NotValidFileException(String message) {
        super(message);
    }

    public NotValidFileException(String message, Throwable exception) {
        super(message, exception);
    }
}
