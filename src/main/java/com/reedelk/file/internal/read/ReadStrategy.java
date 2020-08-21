package com.reedelk.file.internal.read;

import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;

import java.nio.file.Path;

public interface ReadStrategy {

    void read(Path path, ReadConfigurationDecorator decorator, MessageBuilder messageBuilder, MimeType actualMimeType);

}
