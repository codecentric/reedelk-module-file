package com.reedelk.file.component;

import com.reedelk.file.commons.MimeTypeParser;
import com.reedelk.file.exception.NotValidFileException;
import com.reedelk.file.read.*;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.reactivestreams.Publisher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.reedelk.file.commons.Messages.FileReadComponent.FILE_NAME_ERROR;
import static com.reedelk.file.read.FileReadAttribute.FILE_NAME;
import static com.reedelk.file.read.FileReadAttribute.TIMESTAMP;
import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static com.reedelk.runtime.api.commons.StringUtils.isBlank;

@ModuleComponent(
        name = "File Read",
        description = "Reads a file from the file system from the given File name and optionally provided Base path. " +
                "The file read strategy determines if the file should be streamed from the file system or " +
                "loaded into memory before continuing with the execution of the flow. " +
                "The component can also be configured to acquire a lock before reading the file.")
@Component(service = FileRead.class, scope = ServiceScope.PROTOTYPE)
public class FileRead implements ProcessorSync {

    @Example("/var/logs/log1.txt")
    @Property("File name")
    @PropertyDescription("The path and name of the file to be read from the file system.")
    private DynamicString fileName;

    @Example("/var/logs")
    @Property("Base path")
    @PropertyDescription("Optional base path from which files with the given <i>File name</i> will be read from. " +
            "The final file will be read from <i>Base Path</i> + <i>File Name</i>.")
    private String basePath;

    @Example("STREAM")
    @InitValue("DEFAULT")
    @DefaultRenameMe("DEFAULT")
    @Property("Read mode")
    @PropertyDescription("Determines the read strategy. When <i>Default</i> the file is completely read into memory. " +
            "When <i>Stream</i> the file is read only on demand only when the message payload is being consumed. " +
            "This is the preferred method to read large files from the filesystem.")
    private ReadMode mode;

    @Example("false")
    @InitValue("true")
    @DefaultRenameMe("false")
    @Property("Auto mime type")
    @PropertyDescription("If true, the mime type of the payload is determined from the extension of the file read.")
    private boolean autoMimeType;

    @MimeTypeCombo
    @Example(MimeType.MIME_TYPE_TEXT_XML)
    @InitValue(MimeType.MIME_TYPE_APPLICATION_BINARY)
    @DefaultRenameMe(MimeType.MIME_TYPE_APPLICATION_BINARY)
    @When(propertyName = "autoMimeType", propertyValue = "false")
    @When(propertyName = "autoMimeType", propertyValue = When.BLANK)
    @Property("Mime type")
    @PropertyDescription("The mime type of the file read from the filesystem.")
    private String mimeType;

    @Property("Configuration")
    private FileReadConfiguration configuration;

    @Reference
    private ScriptEngineService service;

    private ReadStrategy strategy;

    @Override
    public void initialize() {
        strategy = ReadMode.STREAM.equals(mode) ?
                new ReadStrategyStream() :
                new ReadStrategyDefault();
    }

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        Optional<String> evaluated = service.evaluate(fileName, flowContext, message);

        return evaluated.map(filePath -> {

            MimeType actualMimeType = MimeTypeParser.from(autoMimeType, mimeType, filePath, MimeType.APPLICATION_BINARY);

            Path path = isBlank(basePath) ? Paths.get(filePath) : Paths.get(basePath, filePath);

            ReadConfigurationDecorator config = new ReadConfigurationDecorator(configuration);

            MessageAttributes attributes = new DefaultMessageAttributes(FileRead.class,
                    of(FILE_NAME, path.toString(), TIMESTAMP, System.currentTimeMillis()));

            Publisher<byte[]> data = strategy.read(path, config);

            return MessageBuilder.get()
                    .attributes(attributes)
                    .withBinary(data, actualMimeType)
                    .build();

        }).orElseThrow(() -> new NotValidFileException(FILE_NAME_ERROR.format(fileName.toString())));
    }

    public void setConfiguration(FileReadConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setFileName(DynamicString fileName) {
        this.fileName = fileName;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setMode(ReadMode mode) {
        this.mode = mode;
    }

    public void setAutoMimeType(boolean autoMimeType) {
        this.autoMimeType = autoMimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
