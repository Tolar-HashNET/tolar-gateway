package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.protobuf.ByteString;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.DataConverter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.utils.Numeric;
import tolar.proto.tx.TransactionOuterClass;

import javax.activation.DataContentHandler;
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

        String inputData = objectMapper.readValue(node.get("data").traverse(), String.class);
        ByteString data = DataConverter.tryParseDataAsHex(inputData);

        return TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(objectMapper.convertValue(node.get("sender_address"), ByteString.class))
                .setReceiverAddress(objectMapper.convertValue(node.get("receiver_address"), ByteString.class))
                .setValue(tryParseAsNumber("amount", node, objectMapper))
                .setGas(tryParseAsNumber("gas", node, objectMapper))
                .setGasPrice(tryParseAsNumber("gas_price", node, objectMapper))
                .setData(data)
                .setNonce(tryParseAsNumber("nonce", node, objectMapper))
                .build();
    }

    private Module createDeserializationModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());
        return module;
    }

    private ByteString tryParseAsNumber(String fieldName, TreeNode node, ObjectMapper mapper) {
        Object toConvert;

        TreeNode value = node.get(fieldName);

        if (value instanceof TextNode) {
            toConvert = Numeric.cleanHexPrefix(((TextNode) value).asText());
        } else {
            toConvert = value;
        }

        try {
            return BalanceConverter.toByteString(mapper.convertValue(toConvert, BigInteger.class));
        } catch (IllegalArgumentException ex) {
            log.warn("Cannot parse as BigInteger! field: {}, value: {}", fieldName, value);
            return mapper.convertValue(toConvert, ByteString.class);
        }
    }

}