package io.tolar.model;

import com.google.gson.annotations.SerializedName;
import com.google.protobuf.ByteString;
import lombok.Data;
import tolar.proto.Blockchain.GetTransactionResponse;

@Data
public class Transaction {
    @SerializedName("block_hash")
    ByteString blockHash;

    @SerializedName("transaction_index")
    long transactionIndex;

    @SerializedName("sender_address")
    ByteString senderAddress;

    @SerializedName("receiver_address")
    ByteString receiverAddress;

    ByteString value;

    ByteString gas;

    @SerializedName("gas_price")
    ByteString gasPrice;

    String data;

    ByteString nonce;

    @SerializedName("gas_used")
    ByteString gasUsed;

    @SerializedName("gas_refunded")
    ByteString gasRefunded;

    @SerializedName("new_address")
    ByteString newAddress;

    String output;

    boolean excepted;

    @SerializedName("confirmation_timestamp")
    long confirmationTimestamp;

    public Transaction(GetTransactionResponse getTransactionResponse) {
        this.blockHash = getTransactionResponse.getBlockHash();
        this.transactionIndex = getTransactionResponse.getTransactionIndex();
        this.senderAddress = getTransactionResponse.getSenderAddress();
        this.receiverAddress = getTransactionResponse.getReceiverAddress();
        this.value = getTransactionResponse.getValue();
        this.gas = getTransactionResponse.getGas();
        this.gasPrice = getTransactionResponse.getGasPrice();
        this.data = getTransactionResponse.getData();
        this.nonce = getTransactionResponse.getNonce();
        this.gasUsed = getTransactionResponse.getGasUsed();
        this.gasRefunded = getTransactionResponse.getGasRefunded();
        this.newAddress = getTransactionResponse.getNewAddress();
        this.output = getTransactionResponse.getOutput();
        this.excepted = getTransactionResponse.getExcepted();
        this.confirmationTimestamp = getTransactionResponse.getConfirmationTimestamp();
    }
}