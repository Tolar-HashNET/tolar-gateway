package io.tolar.serialization;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.protobuf.ByteString;
import io.tolar.utils.BalanceConverter;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class TransactionDeserializerTest {

    @Test
    public void deserialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(createDeserializationModule());

        TreeNode node = TextNode.valueOf("0x0");

        ByteString value = tryParseAsNumber(node, mapper);
        assertEquals(BigInteger.ZERO, BalanceConverter.toBigInteger(value));
    }

    private ByteString tryParseAsNumber(TreeNode node, ObjectMapper mapper) {
        Object toConvert;

        if (node instanceof TextNode) {
            toConvert = Numeric.cleanHexPrefix(((TextNode) node).asText());
        } else {
            toConvert = node;
        }

        try {
            return BalanceConverter.toByteString(mapper.convertValue(toConvert, BigInteger.class));
        } catch (IllegalArgumentException ex) {
            return mapper.convertValue(toConvert, ByteString.class);
        }
    }

    private Module createDeserializationModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());
        return module;
    }

}