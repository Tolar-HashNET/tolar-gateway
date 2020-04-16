package io.tolar.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ByteStringDeserializer extends JsonDeserializer<ByteString> {
    private static Logger LOGGER = LoggerFactory.getLogger(ByteStringDeserializer.class);

    @Override
    public ByteString deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return ByteString.copyFromUtf8(parser.getValueAsString());

    }
}