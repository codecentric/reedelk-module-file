package com.reedelk.file.internal.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class FileReadException extends PlatformException {

    public FileReadException(String message, Throwable exception) {
        super(message, exception);
    }
}
