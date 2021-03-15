package de.codecentric.reedelk.file.internal.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class FileWriteException extends PlatformException {

    public FileWriteException(String message, Throwable exception) {
        super(message, exception);
    }
}
