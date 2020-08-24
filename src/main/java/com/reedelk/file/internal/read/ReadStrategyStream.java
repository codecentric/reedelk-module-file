package com.reedelk.file.internal.read;

import com.reedelk.file.internal.commons.FileChannelProvider;
import com.reedelk.file.internal.commons.FileOpenOptions;
import com.reedelk.file.internal.commons.FileOperation;
import com.reedelk.file.internal.exception.FileReadException;
import com.reedelk.file.internal.exception.MaxRetriesExceeded;
import com.reedelk.file.internal.exception.NotValidFileException;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static com.reedelk.file.internal.commons.Messages.FileRead.FILE_IS_DIRECTORY;
import static com.reedelk.file.internal.commons.Messages.FileRead.FILE_READ_ERROR;
import static com.reedelk.file.internal.commons.Messages.Misc.FILE_LOCK_MAX_RETRY_ERROR;
import static com.reedelk.file.internal.commons.Messages.Misc.FILE_NOT_FOUND;
import static com.reedelk.runtime.api.commons.StackTraceUtils.rootCauseMessageOf;

public class ReadStrategyStream implements ReadStrategy {

    public void read(Path path, ReadConfigurationDecorator config, MessageBuilder messageBuilder, MimeType actualMimeType) {

        if (Files.isDirectory(path)) {
            String message = FILE_IS_DIRECTORY.format(path.toString());
            throw new NotValidFileException(message);
        }

        if (!Files.isRegularFile(path)) {
            String message = FILE_NOT_FOUND.format(path.toString());
            throw new NotValidFileException(message);
        }

        Flux<byte[]> stream = Flux.create(sink -> {

            // This consumer is created only when a consumer
            // subscribes to the payload stream.

            OpenOption[] openOptions = FileOpenOptions.from(FileOperation.READ, config.getLockType());

            try (FileChannel channel = FileChannelProvider.from(
                    path,
                    config.getLockType(),
                    config.getRetryMaxAttempts(),
                    config.getRetryWaitTime(),
                    openOptions)) {

                int readBufferSize = config.getReadBufferSizeInKb();

                ByteBuffer byteBuffer = ByteBuffer.allocate(readBufferSize);

                while (channel.read(byteBuffer) > 0) {

                    byteBuffer.flip();

                    byte[] chunk = new byte[byteBuffer.remaining()];

                    byteBuffer.get(chunk);

                    sink.next(chunk);

                    byteBuffer.clear();
                }

                byteBuffer.clear();

                sink.complete();

            } catch (NoSuchFileException exception) {
                String message = FILE_NOT_FOUND.format(path.toString());
                sink.error(new NotValidFileException(message));

            } catch (Throwable exception) {

                if (exception instanceof FileReadException) {
                    sink.error(exception);
                } else if (exception instanceof MaxRetriesExceeded) {
                    String message = FILE_LOCK_MAX_RETRY_ERROR.format(path.toString(), rootCauseMessageOf(exception));
                    sink.error(new FileReadException(message, exception));
                } else {
                    String message = FILE_READ_ERROR.format(path.toString(), rootCauseMessageOf(exception));
                    sink.error(new FileReadException(message, exception));
                }
            }
        });

        messageBuilder.withBinary(stream, actualMimeType);
    }
}
