package com.reedelk.file.internal.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class MaxRetriesExceeded extends PlatformException {

    public MaxRetriesExceeded(String message) {
        super(message);
    }
}
