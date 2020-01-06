package com.reedelk.file.read;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = FileReadConfiguration.class, scope = PROTOTYPE)
public class FileReadConfiguration implements Implementor {

    @Property("Lock file")
    @PropertyInfo("If true a lock on the file is acquired before reading its content.")
    private Boolean lockFile;

    @Property("Lock retry max attempts")
    @Hint("3")
    @When(propertyName = "lockFile", propertyValue = "true")
    @PropertyInfo("Sets the max lock attempts before throwing an error.")
    private Integer lockRetryMaxAttempts;

    @Property("Lock retry wait time (ms)")
    @Hint("500")
    @When(propertyName = "lockFile", propertyValue = "true")
    @PropertyInfo("Sets the wait time between two file lock attempts.")
    private Long lockRetryWaitTime;

    @Property("Read buffer size")
    @Hint("65536")
    private Integer readBufferSize;

    public Boolean getLockFile() {
        return lockFile;
    }

    public void setLockFile(Boolean lockFile) {
        this.lockFile = lockFile;
    }

    public Integer getLockRetryMaxAttempts() {
        return lockRetryMaxAttempts;
    }

    public void setLockRetryMaxAttempts(Integer lockRetryMaxAttempts) {
        this.lockRetryMaxAttempts = lockRetryMaxAttempts;
    }

    public Long getLockRetryWaitTime() {
        return lockRetryWaitTime;
    }

    public void setLockRetryWaitTime(Long lockRetryWaitTime) {
        this.lockRetryWaitTime = lockRetryWaitTime;
    }

    public Integer getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(Integer readBufferSize) {
        this.readBufferSize = readBufferSize;
    }
}

