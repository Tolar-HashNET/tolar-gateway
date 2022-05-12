package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.ByteString;
import org.bouncycastle.util.encoders.Hex;
import tolar.proto.Blockchain;

import java.io.IOException;

public class PastEventSerializer extends JsonSerializer<Blockchain.PastEvent> {
	@Override
    public void serialize(Blockchain.PastEvent pastEvent, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

    	String data = Hex.toHexString(pastEvent.getData().toByteArray());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("address", pastEvent.getAddress());
        jsonGenerator.writeObjectField("topic", pastEvent.getTopic());
        jsonGenerator.writeObjectField("topic_arg_0", pastEvent.getTopicArg0());
        jsonGenerator.writeObjectField("topic_arg_1", pastEvent.getTopicArg1());
        jsonGenerator.writeObjectField("topic_arg_2", pastEvent.getTopicArg2());
        jsonGenerator.writeObjectField("data", data);        
        jsonGenerator.writeObjectField("transaction_hash", pastEvent.getTransactionHash());
        jsonGenerator.writeObjectField("block_hash", pastEvent.getBlockHash());
        jsonGenerator.writeObjectField("block_index", pastEvent.getBlockIndex());
        jsonGenerator.writeEndObject();
    }
}