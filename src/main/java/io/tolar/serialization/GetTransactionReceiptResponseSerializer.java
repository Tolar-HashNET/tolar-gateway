package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.tolar.utils.BalanceConverter;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetTransactionReceiptResponseSerializer extends JsonSerializer<Blockchain.GetTransactionReceiptResponse> {
    @Override
    public void serialize(Blockchain.GetTransactionReceiptResponse getTransactionReceiptResponse,
                          JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("transaction_hash", getTransactionReceiptResponse.getTransactionHash());
        jsonGenerator.writeObjectField("block_hash", getTransactionReceiptResponse.getBlockHash());
        jsonGenerator.writeObjectField("transaction_index", getTransactionReceiptResponse.getTransactionIndex());
        jsonGenerator.writeObjectField("sender_address", getTransactionReceiptResponse.getSenderAddress());
        jsonGenerator.writeObjectField("receiver_address", getTransactionReceiptResponse.getReceiverAddress());
        jsonGenerator.writeObjectField("gas_used", BalanceConverter.toBigInteger(getTransactionReceiptResponse.getGasUsed()));
        jsonGenerator.writeObjectField("new_address", getTransactionReceiptResponse.getNewAddress());
        jsonGenerator.writeObjectField("excepted", getTransactionReceiptResponse.getExcepted());
        jsonGenerator.writeObjectField("block_number", getTransactionReceiptResponse.getBlockIndex());
        jsonGenerator.writeObjectField("hash", getTransactionReceiptResponse.getTransactionHash());
        jsonGenerator.writeObjectField("logs", getTransactionReceiptResponse.getLogsList());
        jsonGenerator.writeEndObject();
    }
}
