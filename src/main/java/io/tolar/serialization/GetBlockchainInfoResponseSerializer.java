package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetBlockchainInfoResponseSerializer extends JsonSerializer<Blockchain.GetBlockchainInfoResponse> {
    @Override
    public void serialize(Blockchain.GetBlockchainInfoResponse getBlockchainInfoResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("confirmed_blocks_count", getBlockchainInfoResponse.getConfirmedBlocksCount());
        jsonGenerator.writeObjectField("total_block_count", getBlockchainInfoResponse.getTotalBlocksCount());
        jsonGenerator.writeObjectField("last_confirmed_block_hash", getBlockchainInfoResponse.getLastConfimedBlockHash());
        jsonGenerator.writeEndObject();
    }
}
