package de.codecentric.reedelk.file.internal.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class MaxRetriesExceeded extends PlatformException {

    public MaxRetriesExceeded(String message) {
        super(message);
    }
}
