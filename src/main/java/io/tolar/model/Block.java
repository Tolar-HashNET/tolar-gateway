package io.tolar.model;

import com.google.gson.annotations.SerializedName;
import com.google.protobuf.ByteString;
import lombok.Data;
import tolar.proto.Blockchain.GetBlockResponse;

import java.util.List;

@Data
public class Block {
    @SerializedName("block_index")
    long blockIndex;

    @SerializedName("previous_block_hash")
    ByteString previousBlockHash;

    @SerializedName("transaction_hashes")
    List<ByteString> transactionHashes;

    @SerializedName("confirmation_timestamp")
    long confirmationTimestamp;

    public Block(GetBlockResponse getBlockResponse) {
        this.blockIndex = getBlockResponse.getBlockIndex();
        this.previousBlockHash = getBlockResponse.getPreviousBlockHash();
        this.transactionHashes = getBlockResponse.getTransactionHashesList();
        this.confirmationTimestamp = getBlockResponse.getConfirmationTimestamp();
    }
}
