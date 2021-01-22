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
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.utils.Numeric;
import tolar.proto.tx.TransactionOuterClass;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TransactionDeserializer extends JsonDeserializer<TransactionOuterClass.Transaction> {

    @Override
    public TransactionOuterClass.Transaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        TreeNode node = parser.getCodec().readTree(parser);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(createDeserializationModule());

        ByteString data;
        String inputData = objectMapper.readValue(node.get("data").traverse(), String.class);

        try {
            String noPrefixData = Numeric.cleanHexPrefix(inputData);
            byte[] decodedHex = Hex.decode(noPrefixData);
            data = ByteString.copyFrom(decodedHex);
        } catch (DecoderException ex) {
            log.warn("Cannot convert to hex! fallback to regular string... message: {}", ex.getMessage());
            data = ByteString.copyFromUtf8(inputData);
        }


        return TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(objectMapper.convertValue(node.get("sender_address"), ByteString.class))
                .setReceiverAddress(objectMapper.convertValue(node.get("receiver_address"), ByteString.class))
                .setValue(BalanceConverter.toByteString(objectMapper.convertValue(node.get("amount"), BigInteger.class)))
                .setGas(BalanceConverter.toByteString(objectMapper.convertValue(node.get("gas"), BigInteger.class)))
                .setGasPrice(BalanceConverter.toByteString(objectMapper.convertValue(node.get("gas_price"), BigInteger.class)))
                .setData(data)
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