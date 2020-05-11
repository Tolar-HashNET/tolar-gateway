package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import tolar.proto.Blockchain.*;
import tolar.proto.tx.TransactionOuterClass;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;

import java.math.BigInteger;
import java.util.List;

@JsonRpcService("/")
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

    @JsonRpcMethod("block_getBlockCount")
    long getBlockCount();

    @JsonRpcMethod("block_getBlockByHash")
    GetBlockResponse getBlockByHash(@JsonRpcParam(value = "block_hash") ByteString blockHash);

    @JsonRpcMethod("block_getBlockByIndex")
    GetBlockResponse getBlockByIndex(@JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("block_getTransaction")
    GetTransactionResponse getTransaction(@JsonRpcParam(value = "transaction_hash") ByteString transactionHash);

    @JsonRpcMethod("block_getBlockchainInfo")
    GetBlockchainInfoResponse getBlockchainInfo();

    @JsonRpcMethod("block_getTransactionList")
    GetTransactionListResponse getTransactionList(@JsonRpcParam(value = "addresses") List<ByteString> addresses,
                                                  @JsonRpcParam(value = "limit") long limit,
                                                  @JsonRpcParam(value = "skip") long skip);

    @JsonRpcMethod("block_getNonce")
    BigInteger getNonce(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("block_getBalance")
    GetBalanceResponse getBalance(@JsonRpcParam(value = "address") ByteString address,
                                  @JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("block_getLatestBalance")
    GetBalanceResponse getLatestBalance(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("block_tryCallTransaction")
    TryCallTransactionResponse tryCallTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                                  @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                                  @JsonRpcParam(value = "amount") BigInteger amount,
                                                  @JsonRpcParam(value = "gas") BigInteger gas,
                                                  @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                                  @JsonRpcParam(value = "data") String data,
                                                  @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("block_getTransactionReceipt")
    GetTransactionReceiptResponse getTransactionReceipt(@JsonRpcParam(value = "transaction_hash")
                                                                ByteString transactionHash);

    @JsonRpcMethod("block_getGasEstimate")
    long getGasEstimate(@JsonRpcParam(value = "object") TransactionOuterClass.Transaction transaction);
}
