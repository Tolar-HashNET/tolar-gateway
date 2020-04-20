package io.tolar.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.protobuf.ByteString;

import java.io.IOException;

public class ByteStringDeserializer extends JsonDeserializer<ByteString> {
    @Override
    public ByteString deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return ByteString.copyFromUtf8(parser.getValueAsString());
    }
}