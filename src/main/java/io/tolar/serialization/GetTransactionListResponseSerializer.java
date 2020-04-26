package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetTransactionListResponseSerializer extends JsonSerializer<Blockchain.GetTransactionListResponse> {
    @Override
    public void serialize(Blockchain.GetTransactionListResponse getTransactionListResponse,
                          JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("transactions", getTransactionListResponse.getTransactionsList());
        jsonGenerator.writeEndObject();
    }
}
