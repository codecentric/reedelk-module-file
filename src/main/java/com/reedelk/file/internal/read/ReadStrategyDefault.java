package com.reedelk.file.internal.read;

import com.reedelk.file.internal.commons.LockType;
import com.reedelk.file.internal.exception.FileReadException;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;

import static com.reedelk.file.internal.commons.Messages.FileRead.FILE_READ_ERROR;

public class ReadStrategyDefault implements ReadStrategy {

    private static final String LOCK_MODE = "rw";
    private static final String READ_MODE = "r";

    @Override
    public void read(Path path, ReadConfigurationDecorator decorator, MessageBuilder messageBuilder, MimeType actualMimeType) {

        FileChannel channel = null;
        FileLock lock = null;
        try {
            LockType lockType = decorator.getLockType();

            // If we want to acquire a lock on the file we must open it using read/write.
            String openMode = LockType.LOCK.equals(lockType) ? LOCK_MODE : READ_MODE;

            // Get a file channel for the file
            File file = new File(path.toString());
            channel = new RandomAccessFile(file, openMode).getChannel();

            // Use the file channel to create a lock on the file.
            // This method blocks until it can retrieve the lock.
            if (LockType.LOCK.equals(lockType)) {
                lock = channel.lock();
            }

            int fileSize = (int) channel.size();

            // We directly allocate a buffer with the entire file size.
            // We won't use ByteBuffer do avoid creating new byte arrays
            // in order to grow capacity when new buffer data is read.
            ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);

            channel.read(byteBuffer);

            byteBuffer.flip();

            byte[] bytes = byteBuffer.array();

            byteBuffer.clear();

            messageBuilder.withBinary(bytes, actualMimeType);

        } catch (IOException exception) {

            String message = FILE_READ_ERROR.format(path, exception.getMessage());

            throw new FileReadException(message, exception);

        } finally {

            // Release the lock - if it is not null!
            if (lock != null) {
                try {
                    lock.release();
                } catch (IOException e) {
                    // nothing we can do here
                }
            }

            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    // nothing we can do here
                }
            }
        }
    }
}
