package com.reedelk.file.internal.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class FileDeleteException extends PlatformException {

    public FileDeleteException(String message, Throwable exception) {
        super(message, exception);
    }
}
