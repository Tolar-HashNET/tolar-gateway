package io.tolar.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetBlockResponseSerializer extends JsonSerializer<Blockchain.GetBlockResponse> {
    @Override
    public void serialize(Blockchain.GetBlockResponse getBlockResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("block_index", getBlockResponse.getBlockIndex());
        jsonGenerator.writeObjectField("previous_block_hash", getBlockResponse.getPreviousBlockHash());
        jsonGenerator.writeObjectField("transaction_hashes", getBlockResponse.getTransactionHashesList());
        jsonGenerator.writeObjectField("confirmation_timestamp", getBlockResponse.getConfirmationTimestamp());
        jsonGenerator.writeEndObject();
    }
}
