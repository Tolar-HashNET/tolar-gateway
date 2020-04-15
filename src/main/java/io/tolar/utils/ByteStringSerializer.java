package io.tolar.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.ByteString;

import java.io.IOException;

public class ByteStringSerializer extends JsonSerializer<ByteString> {

    @Override
    public void serialize(ByteString bytes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(bytes.toStringUtf8());
    }
}