package com.reedelk.file.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = FileReadConfiguration.class, scope = PROTOTYPE)
public class FileReadConfiguration implements Implementor {

    @Property("Lock file")
    @Example("true")
    @DefaultValue("false")
    @Description("If true a lock on the file is acquired before reading its content.")
    private Boolean lockFile;

    @Property("Lock retry max attempts")
    @Hint("5")
    @Example("7")
    @DefaultValue("3")
    @When(propertyName = "lockFile", propertyValue = "true")
    @Description("Sets the max lock attempts before throwing an error.")
    private Integer lockRetryMaxAttempts;

    @Property("Lock retry wait time (ms)")
    @Hint("700")
    @Example("500")
    @DefaultValue("500")
    @When(propertyName = "lockFile", propertyValue = "true")
    @Description("Sets the wait time between two file lock attempts in milliseconds.")
    private Long lockRetryWaitTime;

    @Property("Read buffer size")
    @Hint("1024")
    @Example("1024")
    @DefaultValue("1024")
    @Description("The buffer size used to read the files from filesystem. " +
            "This parameter can be used to improve read performances. " +
            "If the files are big the buffer size should be bigger, " +
            "otherwise for very small files it should be kept smaller. " +
            "The read buffer size is expressed in bytes and it can only be applied when the read mode strategy is 'Stream'.")
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

