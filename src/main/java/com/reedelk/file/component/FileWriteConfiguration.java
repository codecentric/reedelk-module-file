package com.reedelk.file.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = FileWriteConfiguration.class, scope = PROTOTYPE)
public class FileWriteConfiguration implements Implementor {

    @Property("Create directories")
    @Example("true")
    @DefaultValue("false")
    @Description("If true, missing directories will be created on the filesystem before writing the file.")
    private Boolean createParentDirectory;

    @Property("Lock file")
    @Example("true")
    @DefaultValue("false")
    @Description("If true a lock on the file is acquired before writing the content.")
    private Boolean lockFile;

    @Property("Lock retry max attempts")
    @Hint("3")
    @Example("5")
    @DefaultValue("3")
    @When(propertyName = "lockFile", propertyValue = "true")
    @Description("Sets the max lock attempts before throwing an error.")
    private Integer lockRetryMaxAttempts;

    @Property("Lock retry wait time (ms)")
    @Hint("500")
    @Example("600")
    @DefaultValue("500")
    @When(propertyName = "lockFile", propertyValue = "true")
    @Description("Sets the wait time (in milliseconds) between two file lock attempts.")
    private Long lockRetryWaitTime;

    @Property("Write buffer size")
    @Hint("65536")
    @Example("524288")
    @DefaultValue("65536")
    @Description("The buffer size used to write the files to filesystem. " +
            "This parameter can be used to improve write performances. " +
            "If the files are big the buffer size should be bigger, otherwise for very small " +
            "files it should be kept smaller.")
    private Integer writeBufferSize;

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
