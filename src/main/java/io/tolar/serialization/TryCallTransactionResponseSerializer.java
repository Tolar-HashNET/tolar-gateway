package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bouncycastle.util.encoders.Hex;
import tolar.proto.Blockchain;

import java.io.IOException;

public class TryCallTransactionResponseSerializer extends JsonSerializer<Blockchain.TryCallTransactionResponse> {
    @Override
    public void serialize(Blockchain.TryCallTransactionResponse tryCallTransactionResponse,
                          JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String data = Hex.toHexString(tryCallTransactionResponse.getOutput().toByteArray());
        
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("output", data);
        jsonGenerator.writeObjectField("excepted", tryCallTransactionResponse.getExcepted());
        jsonGenerator.writeEndObject();
    }
}
