package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import tolar.proto.Common.SignatureData;
import tolar.proto.tx.TransactionOuterClass;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;

import java.io.IOException;

public class SignedTransactionDeserializer extends JsonDeserializer<SignedTransaction> {

    @Override
    public SignedTransaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {

        TreeNode node = parser.getCodec().readTree(parser);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(createDeserializationModule());

        TransactionOuterClass.Transaction transaction = objectMapper
                .convertValue(node.get("body"), TransactionOuterClass.Transaction.class);
        SignatureData signatureData = objectMapper
                .convertValue(node.get("sig_data"), SignatureData.class);

        return SignedTransaction
                .newBuilder()
                .setBody(transaction)
                .setSigData(signatureData)
                .build();
    }

    private Module createDeserializationModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TransactionOuterClass.Transaction.class, new TransactionDeserializer());
        module.addDeserializer(SignatureData.class, new SignatureDataDeserializer());
        return module;
    }
}