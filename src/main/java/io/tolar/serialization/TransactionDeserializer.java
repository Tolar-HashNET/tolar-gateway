package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.ByteString;
import io.tolar.serialization.ByteStringDeserializer;
import io.tolar.serialization.ByteStringSerializer;
import tolar.proto.tx.TransactionOuterClass;

import java.io.IOException;

public class TransactionDeserializer extends JsonDeserializer<TransactionOuterClass.Transaction> {
    @Override
    public TransactionOuterClass.Transaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        TreeNode node = parser.getCodec().readTree(parser);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(createDeserializationModule());

        return TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(objectMapper.convertValue(node.get("sender_address"), ByteString.class))
                .setReceiverAddress(objectMapper.convertValue(node.get("receiver_address"), ByteString.class))
                .setValue(objectMapper.convertValue(node.get("amount"), ByteString.class))
                .setGas(objectMapper.convertValue(node.get("gas"), ByteString.class))
                .setGasPrice(objectMapper.convertValue(node.get("gas_price"), ByteString.class))
                .setData(objectMapper.readValue(node.get("data").traverse(), String.class))
                .setNonce(objectMapper.convertValue(node.get("nonce"), ByteString.class))
                .build();
    }

    private Module createDeserializationModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());
        return module;
    }
}