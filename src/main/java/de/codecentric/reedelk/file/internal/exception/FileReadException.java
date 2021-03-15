package de.codecentric.reedelk.file.internal.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class FileReadException extends PlatformException {

    public FileReadException(String message, Throwable exception) {
        super(message, exception);
    }
}
