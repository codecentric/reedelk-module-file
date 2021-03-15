package de.codecentric.reedelk.file.internal.attribute;

import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.annotation.TypeProperty;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;

import static de.codecentric.reedelk.file.internal.attribute.FileAttribute.FILE_NAME;
import static de.codecentric.reedelk.file.internal.attribute.FileAttribute.TIMESTAMP;

@Type
@TypeProperty(name = FILE_NAME, type = String.class)
@TypeProperty(name = TIMESTAMP, type = long.class)
public class FileAttribute extends MessageAttributes {

    static final String FILE_NAME =  "fileName";
    static final String TIMESTAMP = "timestamp";

    public FileAttribute(String fileName) {
        put(FILE_NAME, fileName);
        put(TIMESTAMP, System.currentTimeMillis());
    }
}
