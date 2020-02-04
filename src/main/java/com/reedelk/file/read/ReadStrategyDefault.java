package com.reedelk.file.read;

import com.reedelk.runtime.api.commons.StreamUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

public class ReadStrategyDefault extends ReadStrategyStream {

    @Override
    public Publisher<byte[]> read(Path path, ReadConfigurationDecorator config) {

        Publisher<byte[]> read = super.read(path, config);

        // We immediately consume the content.
        byte[] consume = StreamUtils.FromByteArray.consume(read);

        return Mono.just(consume);
    }
}