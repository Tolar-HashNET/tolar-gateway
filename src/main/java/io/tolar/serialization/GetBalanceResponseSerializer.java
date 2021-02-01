package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.tolar.utils.BalanceConverter;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetBalanceResponseSerializer extends JsonSerializer<Blockchain.GetBalanceResponse> {
    @Override
    public void serialize(Blockchain.GetBalanceResponse getBalanceResponse, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("balance",
                BalanceConverter.toBigInteger(getBalanceResponse.getBalance()).toString());
        jsonGenerator.writeObjectField("block_index", getBalanceResponse.getBlockIndex());
        jsonGenerator.writeEndObject();
    }
}
