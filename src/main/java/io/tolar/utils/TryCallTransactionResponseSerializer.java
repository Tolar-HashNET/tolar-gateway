package io.tolar.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tolar.proto.Blockchain;

import java.io.IOException;

public class TryCallTransactionResponseSerializer extends JsonSerializer<Blockchain.TryCallTransactionResponse> {
    @Override
    public void serialize(Blockchain.TryCallTransactionResponse tryCallTransactionResponse,
                          JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("output", tryCallTransactionResponse.getOutput());
        jsonGenerator.writeObjectField("excepted", tryCallTransactionResponse.getExcepted());
        jsonGenerator.writeEndObject();
    }
}
