package com.reedelk.file.component;

import com.reedelk.file.internal.attribute.FileAttribute;
import com.reedelk.file.internal.exception.FileWriteException;
import com.reedelk.file.internal.write.WriteConfiguration;
import com.reedelk.file.internal.write.WriteMode;
import com.reedelk.file.internal.write.Writer;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.component.ProcessorAsync;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.reedelk.file.internal.commons.Messages.FileWrite.ERROR_FILE_WRITE;
import static com.reedelk.file.internal.commons.Messages.FileWrite.ERROR_FILE_WRITE_WITH_PATH;
import static com.reedelk.runtime.api.commons.StackTraceUtils.rootCauseMessageOf;
import static com.reedelk.runtime.api.commons.StringUtils.isBlank;

@ModuleComponent("File Write")
@ComponentOutput(
        attributes = FileAttribute.class,
        payload = Void.class)
@ComponentInput(
        payload = { byte[].class, String.class },
        description = "The data to be written on the file. The expected input is byte array or string.")
@Description("Writes a file to the file system to the given File name and optionally provided Base path. " +
                "The write mode can be used to override an existing file, create new file if it does not exists " +
                "or append to the existing file if exists already.")
@Component(service = FileWrite.class, scope = ServiceScope.PROTOTYPE)
public class FileWrite implements ProcessorAsync {

    @Property("File name")
    @Hint("/var/logs/log1.txt")
    @Example("/var/logs/log1.txt")
    @Description("The path and name of the file to be written on the file system.")
    private DynamicString fileName;

    @Property("Base path")
    @Hint("/var/logs")
    @Example("/var/logs")
    @Description("Optional base path from which files with the given <i>File name</i> will be written to. " +
            "The final file will be written into <i>Base Path</i> + <i>File Name</i>.")
    private String basePath;

    @Property("Write mode")
    @Example("APPEND")
    @InitValue("OVERWRITE")
    @DefaultValue("OVERWRITE")
    @Description("Sets the file write mode. Possible values are <b>OVERWRITE</b>, <b>CREATE_NEW</b>, <b>APPEND</b>.")
    private WriteMode mode;

    @Property("Configuration")
    @Group("Configuration")
    private FileWriteConfiguration configuration;

    @Reference
    private ScriptEngineService scriptService;
    @Reference
    private ConverterService converterService;

    private final Writer writer = new Writer();

    @Override
    public void apply(FlowContext flowContext, Message message, OnResult callback) {


        Optional<String> evaluated = scriptService.evaluate(fileName, flowContext, message);

        if (evaluated.isPresent()) {

            Path finalPath = null;

            try {

                WriteConfiguration config = new WriteConfiguration(configuration, mode);

                String filePath = evaluated.get();

                finalPath = isBlank(basePath) ? Paths.get(filePath) : Paths.get(basePath, filePath);

                if (config.isCreateParentDirectory()) {
                    Files.createDirectories(finalPath.getParent());
                }

                TypedPublisher<?> originalStream = message.content().stream();

                TypedPublisher<byte[]> originalStreamAsBytes = converterService.convert(originalStream, byte[].class);

                writer.write(config, flowContext, callback, finalPath, originalStreamAsBytes);

            } catch (Exception exception) {
                String errorMessage = finalPath != null ?
                        ERROR_FILE_WRITE_WITH_PATH.format(finalPath.toString(), rootCauseMessageOf(exception)) :
                        ERROR_FILE_WRITE.format(rootCauseMessageOf(exception));
                callback.onError(flowContext, new FileWriteException(errorMessage, exception));
            }

        } else {
            callback.onError(flowContext, new PlatformException("Could not write file"));
        }
    }

    public void setFileName(DynamicString fileName) {
        this.fileName = fileName;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setMode(WriteMode mode) {
        this.mode = mode;
    }

    public void setConfiguration(FileWriteConfiguration configuration) {
        this.configuration = configuration;
    }
}

