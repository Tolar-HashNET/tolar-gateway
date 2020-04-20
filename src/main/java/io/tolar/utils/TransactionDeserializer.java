package io.tolar.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.ByteString;
import tolar.proto.tx.TransactionOuterClass;

import java.io.IOException;

public class TransactionDeserializer extends JsonDeserializer<TransactionOuterClass.Transaction> {
    @Override
    public TransactionOuterClass.Transaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        TreeNode node = parser.getCodec().readTree(parser);

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());
        objectMapper.registerModule(module);

        return TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(objectMapper.convertValue(node.get("sender_address"), ByteString.class))
                .setReceiverAddress(objectMapper.convertValue(node.get("receiver_address"), ByteString.class))
                .setValue(objectMapper.convertValue(node.get("amount"), ByteString.class))
                .setGas(objectMapper.convertValue(node.get("gas"), ByteString.class))
                .setGasPrice(objectMapper.convertValue(node.get("gas_price"), ByteString.class))
                .setData(node.get("data").toString())
                .setNonce(objectMapper.convertValue(node.get("nonce"), ByteString.class))
                .build();
    }
}