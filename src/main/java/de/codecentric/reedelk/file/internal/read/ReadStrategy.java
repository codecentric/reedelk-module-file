package de.codecentric.reedelk.file.internal.read;

import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;

import java.nio.file.Path;

public interface ReadStrategy {

    void read(Path path, ReadConfigurationDecorator decorator, MessageBuilder messageBuilder, MimeType actualMimeType);

}
