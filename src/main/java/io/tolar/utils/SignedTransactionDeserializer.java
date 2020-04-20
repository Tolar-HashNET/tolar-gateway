package io.tolar.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import tolar.proto.Common;
import tolar.proto.tx.TransactionOuterClass;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;
import tolar.proto.tx.TransactionOuterClass.Transaction;

import java.io.IOException;

public class SignedTransactionDeserializer extends JsonDeserializer<SignedTransaction> {

    @Override
    public SignedTransaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(TransactionOuterClass.Transaction.class, new TransactionDeserializer());
        module.addDeserializer(Common.SignatureData.class, new SignatureDataDeserializer());
        objectMapper.registerModule(module);

        TreeNode node = parser.readValueAsTree();
        Transaction transaction = objectMapper.convertValue(node.get("transaction"), Transaction.class);
        Common.SignatureData signatureData = objectMapper.convertValue(node.get("signatureData"), Common.SignatureData.class);

        return SignedTransaction
                .newBuilder()
                .setBody(transaction)
                .setSigData(signatureData)
                .build();
    }
}