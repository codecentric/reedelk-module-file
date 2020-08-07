package com.reedelk.file.internal.read;

import com.reedelk.file.internal.exception.FileReadException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.reedelk.file.internal.commons.Messages.FileRead.FILE_READ_ERROR;

public class ReadStrategyDefault implements ReadStrategy {

    @Override
    public Publisher<byte[]> read(Path path, ReadConfigurationDecorator config) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            return Mono.just(bytes);
        } catch (IOException e) {
            String message = FILE_READ_ERROR.format(path, e.getMessage());
            throw new FileReadException(message, e);
        }
    }
}
