package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.tolar.model.Block;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;

@JsonRpcService("/Client")
public interface ClientApi {
    @JsonRpcMethod("SendSignedTransaction")
    ByteString sendSignedTransaction(@JsonRpcParam(value = "transaction") SignedTransaction signedTransaction);

    @JsonRpcMethod("PeerCount")
    long peerCount();

    @JsonRpcMethod("MasterNodeCount")
    long masterNodeCount();

    @JsonRpcMethod("IsMasterNode")
    boolean isMasterNode();

    @JsonRpcMethod("MaxPeerCount")
    long maxPeerCount();

    @JsonRpcMethod("GetBlockCount")
    long getBlockCount();

    @JsonRpcMethod("GetBlockByHash")
    Block getBlockByHash(@JsonRpcParam(value = "block_hash") ByteString blockHash);

    @JsonRpcMethod("GetBlockByIndex")
    Block getBlockByIndex(@JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("GetTransaction")
    Block getTransaction(@JsonRpcParam(value = "transaction_hash") ByteString transactionHash);
}
