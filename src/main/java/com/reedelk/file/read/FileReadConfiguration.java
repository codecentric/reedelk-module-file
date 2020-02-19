package com.reedelk.file.read;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = FileReadConfiguration.class, scope = PROTOTYPE)
public class FileReadConfiguration implements Implementor {

    @Example("true")
    @DefaultRenameMe("false")
    @Property("Lock file")
    @PropertyDescription("If true a lock on the file is acquired before reading its content.")
    private Boolean lockFile;

    @Hint("5")
    @Example("7")
    @DefaultRenameMe("3")
    @When(propertyName = "lockFile", propertyValue = "true")
    @Property("Lock retry max attempts")
    @PropertyDescription("Sets the max lock attempts before throwing an error.")
    private Integer lockRetryMaxAttempts;

    @Hint("700")
    @Example("500")
    @DefaultRenameMe("500")
    @When(propertyName = "lockFile", propertyValue = "true")
    @Property("Lock retry wait time (ms)")
    @PropertyDescription("Sets the wait time between two file lock attempts in milliseconds.")
    private Long lockRetryWaitTime;

    @Hint("65536")
    @Example("262144")
    @DefaultRenameMe("65536")
    @Property("Read buffer size")
    @PropertyDescription("The buffer size used to read the files from filesystem. " +
            "This parameter can be used to improve read performances. " +
            "If the files are big the buffer size should be bigger, " +
            "otherwise for very small files it should be kept smaller.")
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

