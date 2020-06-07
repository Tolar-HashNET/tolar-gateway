package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.ByteString;
import io.tolar.utils.BalanceConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tolar.proto.tx.TransactionOuterClass;

import java.io.IOException;
import java.math.BigInteger;

public class TransactionDeserializer extends JsonDeserializer<TransactionOuterClass.Transaction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDeserializer.class);

    @Override
    public TransactionOuterClass.Transaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        TreeNode node = parser.getCodec().readTree(parser);

        LOGGER.info("tx: {}", node);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(createDeserializationModule());
        TreeNode sender_address = node.get("sender_address");
        LOGGER.info("senderTx: {}", sender_address);

        return TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(objectMapper.convertValue(node.get("sender_address"), ByteString.class))
                .setReceiverAddress(objectMapper.convertValue(node.get("receiver_address"), ByteString.class))
                .setValue(BalanceConverter.toByteString(objectMapper.convertValue(node.get("amount"), BigInteger.class)))
                .setGas(BalanceConverter.toByteString(objectMapper.convertValue(node.get("gas"), BigInteger.class)))
                .setGasPrice(BalanceConverter.toByteString(objectMapper.convertValue(node.get("gas_price"), BigInteger.class)))
                .setData(objectMapper.convertValue(node.get("data"), String.class))
                .setNonce(BalanceConverter.toByteString(objectMapper.convertValue(node.get("nonce"), BigInteger.class)))
                .build();
    }

    private Module createDeserializationModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());
        return module;
    }
}