package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.ByteString;
import io.tolar.utils.BalanceConverter;
import org.bouncycastle.util.encoders.Hex;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetTransactionResponseSerializer extends JsonSerializer<Blockchain.GetTransactionResponse> {

    @Override
    public void serialize(Blockchain.GetTransactionResponse transactionResponse, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        // return empty result if no transaction is found
        if (transactionResponse == null) {
            return;
        }

        String data = Hex.toHexString(transactionResponse.getData().toByteArray());
        String output = Hex.toHexString(transactionResponse.getOutput().toByteArray());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("transaction_hash", transactionResponse.getTransactionHash());
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
        jsonGenerator.writeObjectField("data", data);
        jsonGenerator.writeObjectField("nonce",
                BalanceConverter.toBigInteger(transactionResponse.getNonce()));
        jsonGenerator.writeObjectField("gas_used",
                BalanceConverter.toBigInteger(transactionResponse.getGasUsed()));
        jsonGenerator.writeObjectField("gas_refunded",
                BalanceConverter.toBigInteger(transactionResponse.getGasRefunded()));
        jsonGenerator.writeObjectField("new_address", transactionResponse.getNewAddress());
        jsonGenerator.writeObjectField("output", output);
        jsonGenerator.writeObjectField("excepted", transactionResponse.getExcepted());
        jsonGenerator.writeObjectField("confirmation_timestamp", transactionResponse.getConfirmationTimestamp());
        jsonGenerator.writeEndObject();
    }

}
