package de.codecentric.reedelk.file.internal.read;

import de.codecentric.reedelk.file.component.FileReadConfiguration;
import de.codecentric.reedelk.file.internal.commons.LockType;

import java.util.Optional;

import static de.codecentric.reedelk.file.internal.commons.Defaults.FileRead.*;

public class ReadConfigurationDecorator {

    private final LockType lockType;
    private final int readByfferSizeInKb;
    private final long retryWaitTime;
    private final int retryMaxAttempts;

    public ReadConfigurationDecorator(FileReadConfiguration configuration) {
        this.lockType = getLockType(configuration);
        this.readByfferSizeInKb = getReadBufferSizeInKb(configuration);
        this.retryMaxAttempts = getRetryMaxAttempts(configuration);
        this.retryWaitTime = getRetryWaitTime(configuration);
    }

    LockType getLockType() {
        return lockType;
    }

    int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    long getRetryWaitTime() {
        return retryWaitTime;
    }

    int getReadBufferSizeInKb() {
        return readByfferSizeInKb;
    }

    private int getReadBufferSizeInKb(FileReadConfiguration configuration) {
        Integer kiloBytes = Optional.ofNullable(configuration)
                .flatMap(config -> Optional.ofNullable(config.getReadBufferSize()))
                .orElse(READ_FILE_BUFFER_SIZE_KB);
        return bytesFrom(kiloBytes);
    }

    private LockType getLockType(FileReadConfiguration configuration) {
        return Optional.ofNullable(configuration)
                .flatMap(config -> Optional.ofNullable(config.getLockFile()))
                .map(shouldLock -> shouldLock ? LockType.LOCK : LockType.NONE)
                .orElse(LockType.NONE);
    }

    private int getRetryMaxAttempts(FileReadConfiguration configuration) {
        return Optional.ofNullable(configuration)
                .flatMap(config -> Optional.ofNullable(config.getLockRetryMaxAttempts()))
                .orElse(RETRY_MAX_ATTEMPTS);
    }

    private long getRetryWaitTime(FileReadConfiguration configuration) {
        return Optional.ofNullable(configuration)
                .flatMap(config -> Optional.ofNullable(config.getLockRetryWaitTime()))
                .orElse(RETRY_WAIT_TIME);
    }

    private static int bytesFrom(int kilobytes) {
        // calculates Bytes
        // 1 KB = 1024 bytes
        return kilobytes * 1024;
    }
}
