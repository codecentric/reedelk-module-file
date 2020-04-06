package com.reedelk.file.component;

import com.reedelk.file.internal.delete.FileDeleteAttribute;
import com.reedelk.file.internal.exception.FileDeleteException;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.reedelk.runtime.api.commons.ConfigurationPreconditions.requireNotNull;
import static java.lang.String.format;

@ModuleComponent("File Delete")
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
            try {
                Files.delete(Paths.get(evaluatedFileNameToRemove));
            } catch (Exception error) {
                String errorMessage = format("The file could not be deleted: %s", error.getMessage());
                throw new FileDeleteException(errorMessage, error);
            }

            Map<String, Serializable> attributesMap = new HashMap<>();
            FileDeleteAttribute.FILE_NAME.set(attributesMap, evaluatedFileNameToRemove);
            MessageAttributes attributes = new DefaultMessageAttributes(FileDelete.class, attributesMap);

            Message outMessage = MessageBuilder.get()
                    .empty()
                    .attributes(attributes)
                    .build();

            return Optional.of(outMessage);

        }).orElse(MessageBuilder.get().empty().build());
    }

    public void setFileName(DynamicString fileName) {
        this.fileName = fileName;
    }
}
