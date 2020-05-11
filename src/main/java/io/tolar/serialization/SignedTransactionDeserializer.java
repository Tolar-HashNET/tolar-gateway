package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import tolar.proto.Common.SignatureData;
import tolar.proto.tx.TransactionOuterClass;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;
import tolar.proto.tx.TransactionOuterClass.Transaction;

import java.io.IOException;

public class SignedTransactionDeserializer extends JsonDeserializer<SignedTransaction> {

    @Override
    public SignedTransaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(createDeserializationModule());
        String valueAsString = parser.getValueAsString();
        JsonNode otherNode = objectMapper.readTree(valueAsString);
        Transaction transaction = objectMapper.convertValue(otherNode.get("body"), Transaction.class);
        SignatureData signatureData = objectMapper.convertValue(otherNode.get("sig_data"), SignatureData.class);

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