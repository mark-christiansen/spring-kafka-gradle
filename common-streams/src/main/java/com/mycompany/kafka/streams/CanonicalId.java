package com.mycompany.kafka.streams;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CanonicalId {

    public static UUID getId(String source, String sourceType, long id) {
        // identify uniquely identifies source and ID
        try {
            return UUID.nameUUIDFromBytes((source + "-" + sourceType + "-" + id).getBytes(StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
