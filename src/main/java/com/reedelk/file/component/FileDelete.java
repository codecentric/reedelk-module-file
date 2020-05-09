package com.reedelk.file.component;

import com.reedelk.file.internal.attribute.FileAttribute;
import com.reedelk.file.internal.exception.FileDeleteException;
import com.reedelk.file.internal.exception.NotValidFileException;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.reedelk.file.internal.commons.Messages.FileDelete.ERROR_FILE_DELETE;
import static com.reedelk.file.internal.commons.Messages.FileDelete.FILE_NAME_ERROR;
import static com.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotNull;

@ModuleComponent("File Delete")
@ComponentOutput(
        attributes = FileAttribute.class,
        payload = String.class,
        description = "The path and name of the deleted file.")
@Description("Deletes a file from the file system with the given File name. " +
                "An error is raised if the given file could not be found. " +
                "The file name can be a dynamic expression.")
@Component(service = FileDelete.class, scope = ServiceScope.PROTOTYPE)
public class FileDelete implements ProcessorSync {

    @Property("File name")
    @Hint("/var/logs/sample.txt")
    @Example("/var/logs/log1.txt")
    @Description("The path and name of the file to be deleted from the file system.\t")
    private DynamicString fileName;

    @Reference
    private ScriptEngineService service;

    @Override
    public void initialize() {
        requireNotNull(FileDelete.class, fileName, "The file name must not be null");
    }

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        return service.evaluate(fileName, flowContext, message).flatMap(evaluatedFileNameToRemove -> {
            Path filePathToDelete;
            try {
                filePathToDelete = Paths.get(evaluatedFileNameToRemove);
                Files.delete(filePathToDelete);
            } catch (Exception exception) {
                String errorMessage = ERROR_FILE_DELETE.format(exception.getMessage());
                throw new FileDeleteException(errorMessage, exception);
            }

            FileAttribute attributes = new FileAttribute(evaluatedFileNameToRemove);

            Message outMessage = MessageBuilder.get(FileDelete.class)
                    .attributes(attributes)
                    .withString(filePathToDelete.toString(), MimeType.TEXT_PLAIN)
                    .build();

            return Optional.of(outMessage);

        }).orElseThrow(() -> new NotValidFileException(FILE_NAME_ERROR.format(fileName.toString())));
    }

    public void setFileName(DynamicString fileName) {
        this.fileName = fileName;
    }
}
