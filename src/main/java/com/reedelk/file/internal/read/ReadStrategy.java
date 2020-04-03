package com.reedelk.file.internal.read;

import org.reactivestreams.Publisher;

import java.nio.file.Path;

public interface ReadStrategy {

    Publisher<byte[]> read(Path path, ReadConfigurationDecorator config);

}
