package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.ByteString;
import tolar.proto.Common;

import java.io.IOException;

public class SignatureDataDeserializer extends JsonDeserializer<Common.SignatureData> {

    @Override
    public Common.SignatureData deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());
        objectMapper.registerModule(module);

        TreeNode node = parser.getCodec().readTree(parser);

        return Common.SignatureData.newBuilder()
                .setHash(objectMapper.convertValue(node.get("hash"), ByteString.class))
                .setSignature(objectMapper.convertValue(node.get("signature"), ByteString.class))
                .setSignerId(objectMapper.convertValue(node.get("signer_id"), ByteString.class))
                .build();
    }
}