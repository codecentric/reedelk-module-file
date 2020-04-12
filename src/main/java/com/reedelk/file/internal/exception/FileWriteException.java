package com.reedelk.file.internal.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class FileWriteException extends PlatformException {

    public FileWriteException(String message, Throwable exception) {
        super(message, exception);
    }
}
