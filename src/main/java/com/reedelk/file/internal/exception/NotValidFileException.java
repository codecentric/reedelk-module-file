package com.reedelk.file.internal.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class NotValidFileException extends PlatformException {

    public NotValidFileException(String message) {
        super(message);
    }

    public NotValidFileException(String message, Throwable exception) {
        super(message, exception);
    }
}
