package com.reedelk.file.component;

import com.reedelk.file.delete.FileDeleteAttribute;
import com.reedelk.file.exception.FileDeleteException;
import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.PropertyInfo;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.*;
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

@ESBComponent("File Delete")
@Component(service = FileDelete.class, scope = ServiceScope.PROTOTYPE)
public class FileDelete implements ProcessorSync {

    @Property("File name")
    @PropertyInfo("The path and name of the file to be deleted from the file system.")
    private DynamicString fileName;

    @Reference
    private ScriptEngineService service;

    @Override
    public void initialize() {
        requireNotNull(FileDelete.class, fileName, "The file name must not be null");
    }

    @Override
    public Message apply(Message message, FlowContext flowContext) {
        return service.evaluate(fileName, flowContext, message).flatMap(evaluatedFileNameToRemove -> {
            try {
                Files.delete(Paths.get(evaluatedFileNameToRemove));
            } catch (Exception error) {
                String errorMessage = format("The file could not be deleted: %s", error.getMessage());
                throw new FileDeleteException(errorMessage, error);
            }

            Map<String, Serializable> attributesMap = new HashMap<>();
            MessageAttributes attributes = new DefaultMessageAttributes(FileDelete.class, attributesMap);
            FileDeleteAttribute.FILE_NAME.set(attributesMap, evaluatedFileNameToRemove);
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