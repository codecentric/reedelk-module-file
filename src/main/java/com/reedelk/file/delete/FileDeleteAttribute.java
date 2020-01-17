package com.reedelk.file.delete;

import java.io.Serializable;
import java.util.Map;

public enum FileDeleteAttribute {

    FILE_NAME("deleteFileName");

    private final String attributeName;

    FileDeleteAttribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public void set(Map<String, Serializable> attributesMap, Serializable value) {
        attributesMap.put(attributeName, value);
    }
}
