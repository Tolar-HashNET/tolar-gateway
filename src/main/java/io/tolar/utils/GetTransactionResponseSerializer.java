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
                BalanceConverter.toBigInteger(transactionResponse.getValue()));
        jsonGenerator.writeObjectField("gas",
                BalanceConverter.toBigInteger(transactionResponse.getGas()));
        jsonGenerator.writeObjectField("gas_price",
                BalanceConverter.toBigInteger(transactionResponse.getGasPrice()));
        jsonGenerator.writeObjectField("data", transactionResponse.getData());
        jsonGenerator.writeObjectField("nonce",
                BalanceConverter.toBigInteger(transactionResponse.getNonce()));
        jsonGenerator.writeObjectField("gas_used",
                BalanceConverter.toBigInteger(transactionResponse.getGasUsed()));
        jsonGenerator.writeObjectField("gas_refunded",
                BalanceConverter.toBigInteger(transactionResponse.getGasRefunded()));
        jsonGenerator.writeObjectField("new_address", transactionResponse.getNewAddress());
        jsonGenerator.writeObjectField("output", transactionResponse.getOutput());
        jsonGenerator.writeObjectField("excepted", transactionResponse.getExcepted());
        jsonGenerator.writeObjectField("confirmation_timestamp", transactionResponse.getConfirmationTimestamp());
        jsonGenerator.writeEndObject();
    }
}
