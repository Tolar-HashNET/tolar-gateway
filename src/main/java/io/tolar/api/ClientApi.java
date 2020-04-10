package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.tolar.model.Block;
import io.tolar.model.Transaction;
import tolar.proto.Blockchain.*;
import tolar.proto.tx.TransactionOuterClass;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;

import java.util.List;

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
    Transaction getTransaction(@JsonRpcParam(value = "transaction_hash") ByteString transactionHash);

    @JsonRpcMethod("GetBlockchainInfo")
    GetBlockchainInfoResponse getBlockchainInfo();

    @JsonRpcMethod("GetTransactionList")
    GetTransactionListResponse getTransactionList(@JsonRpcParam(value = "addresses") List<ByteString> addresses,
                                                  @JsonRpcParam(value = "limit") long limit,
                                                  @JsonRpcParam(value = "skip") long skip);

    @JsonRpcMethod("GetNonce")
    ByteString getNonce(@JsonRpcParam(value = "skip") ByteString address);

    @JsonRpcMethod("GetBalance")
    GetBalanceResponse getBalance(@JsonRpcParam(value = "address") ByteString address,
                                  @JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("GetLatestBalance")
    GetBalanceResponse getLatestBalance(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("TryCallTransaction")
    TryCallTransactionResponse tryCallTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                                  @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                                  @JsonRpcParam(value = "amount") ByteString amount,
                                                  @JsonRpcParam(value = "gas") ByteString gas,
                                                  @JsonRpcParam(value = "gas_price") ByteString gasPrice,
                                                  @JsonRpcParam(value = "data") String data,
                                                  @JsonRpcParam(value = "nonce") ByteString nonce);

    @JsonRpcMethod("GetTransactionReceipt")
    GetTransactionReceiptResponse getTransactionReceipt(@JsonRpcParam(value = "transaction_hash")
                                                                ByteString transactionHash);

    @JsonRpcMethod("GetGasEstimate")
    long getGasEstimate(@JsonRpcParam(value = "object") TransactionOuterClass.Transaction transaction);
}
