package de.codecentric.reedelk.file.internal.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class FileDeleteException extends PlatformException {

    public FileDeleteException(String message, Throwable exception) {
        super(message, exception);
    }
}
