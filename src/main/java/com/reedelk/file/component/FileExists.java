package com.reedelk.file.component;

import com.reedelk.file.internal.attribute.FileAttribute;
import com.reedelk.file.internal.exception.NotValidFileException;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.DynamicValueUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.reedelk.file.internal.commons.Messages.FileRead.FILE_NAME_ERROR;
import static com.reedelk.runtime.api.commons.StringUtils.isBlank;


@ModuleComponent("File Exists")
@Description("The File Exists component Tests whether a file with the given path exists. " +
        "The file path can be a text only or dynamic expression.")
@Component(service = FileExists.class, scope = ServiceScope.PROTOTYPE)
public class FileExists implements ProcessorSync {

    @Property("File name")
    @Hint("/var/logs/log1.txt")
    @Example("/var/logs/log1.txt")
    @Description("The path and name of the file to be checked for existence.")
    private DynamicString fileName;

    @Property("Base path")
    @Hint("/var/logs")
    @Example("/var/logs")
    @Description("Optional base path from which files with the given <i>File name</i> will be checked for existence. " +
            "The final file will be checked from <i>Base Path</i> + <i>File Name</i>.")
    private String basePath;

    @Property("Target Variable")
    @Hint("myFileExists")
    @Example("myFileExists")
    @Group("Advanced")
    @Description("If the property is not empty, the result of the file exists check is assigned to the given context" +
            " variable instead of the message payload.")
    private DynamicString target;

    @Reference
    private ScriptEngineService service;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        Optional<String> evaluated = service.evaluate(fileName, flowContext, message);

        return evaluated.map(filePath -> {

            Path path = isBlank(basePath) ? Paths.get(filePath) : Paths.get(basePath, filePath);

            boolean exists = Files.exists(path);

            // If the target variable has been set, we assign to a context variable
            // the result of the file exists check and we return the original message.

            if (DynamicValueUtils.isNotNullOrBlank(target)) {
                service.evaluate(target, flowContext, message)
                        .ifPresent(contextVariableName ->
                                flowContext.put(contextVariableName, exists));
                return message;

            } else {
                MessageAttributes attributes = new FileAttribute(path.toString());
                return MessageBuilder.get(FileExists.class)
                        .attributes(attributes)
                        .withJavaObject(exists)
                        .build();
            }

        }).orElseThrow(() -> new NotValidFileException(FILE_NAME_ERROR.format(fileName.toString())));
    }

    public void setFileName(DynamicString fileName) {
        this.fileName = fileName;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setTarget(DynamicString target) {
        this.target = target;
    }
}
