package com.reedelk.file.write;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = FileWriteConfiguration.class, scope = PROTOTYPE)
public class FileWriteConfiguration implements Implementor {

    @Property("Create directories")
    @PropertyInfo("If true, missing directories will be created on the filesystem before writing the file.")
    private Boolean createParentDirectory;

    @Property("Lock file")
    @PropertyInfo("If true a lock on the file is acquired before writing the content.")
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

    @Property("Write buffer size")
    @Hint("65536")
    private Integer writeBufferSize;

    public boolean isCreateParentDirectory() {
        return createParentDirectory;
    }

    public void setCreateParentDirectory(boolean createParentDirectory) {
        this.createParentDirectory = createParentDirectory;
    }

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

    public Integer getWriteBufferSize() {
        return writeBufferSize;
    }

    public void setWriteBufferSize(Integer writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }

    public Boolean getCreateParentDirectory() {
        return createParentDirectory;
    }
}
