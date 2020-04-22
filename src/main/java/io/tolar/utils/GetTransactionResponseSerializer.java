package io.tolar.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetTransactionResponseSerializer extends JsonSerializer<Blockchain.GetTransactionResponse> {
    @Override
    public void serialize(Blockchain.GetTransactionResponse transactionResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("block_hash", transactionResponse.getBlockHash());
        jsonGenerator.writeObjectField("transaction_index", transactionResponse.getTransactionIndex());
        jsonGenerator.writeObjectField("sender_address", transactionResponse.getSenderAddress());
        jsonGenerator.writeObjectField("receiver_address", transactionResponse.getReceiverAddress());
        jsonGenerator.writeObjectField("value",
                BalanceConverter.convertBalance(transactionResponse.getValue()));
        jsonGenerator.writeObjectField("gas",
                BalanceConverter.convertBalance(transactionResponse.getGas()));
        jsonGenerator.writeObjectField("gas_price",
                BalanceConverter.convertBalance(transactionResponse.getGasPrice()));
        jsonGenerator.writeObjectField("data", transactionResponse.getData());
        jsonGenerator.writeObjectField("nonce",
                BalanceConverter.convertBalance(transactionResponse.getNonce()));
        jsonGenerator.writeObjectField("gas_used",
                BalanceConverter.convertBalance(transactionResponse.getGasUsed()));
        jsonGenerator.writeObjectField("gas_refunded",
                BalanceConverter.convertBalance(transactionResponse.getGasRefunded()));
        jsonGenerator.writeObjectField("new_address", transactionResponse.getNewAddress());
        jsonGenerator.writeObjectField("output", transactionResponse.getOutput());
        jsonGenerator.writeObjectField("excepted", transactionResponse.getExcepted());
        jsonGenerator.writeObjectField("confirmation_timestamp", transactionResponse.getConfirmationTimestamp());
        jsonGenerator.writeEndObject();
    }
}
