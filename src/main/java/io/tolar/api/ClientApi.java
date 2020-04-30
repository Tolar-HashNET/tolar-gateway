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
    GetBlockResponse getBlockByHash(@JsonRpcParam(value = "block_hash") ByteString blockHash);

    @JsonRpcMethod("GetBlockByIndex")
    GetBlockResponse getBlockByIndex(@JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("GetTransaction")
    GetTransactionResponse getTransaction(@JsonRpcParam(value = "transaction_hash") ByteString transactionHash);

    @JsonRpcMethod("GetBlockchainInfo")
    GetBlockchainInfoResponse getBlockchainInfo();

    @JsonRpcMethod("GetTransactionList")
    GetTransactionListResponse getTransactionList(@JsonRpcParam(value = "addresses") List<ByteString> addresses,
                                                  @JsonRpcParam(value = "limit") long limit,
                                                  @JsonRpcParam(value = "skip") long skip);

    @JsonRpcMethod("GetNonce")
    BigInteger getNonce(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("GetBalance")
    GetBalanceResponse getBalance(@JsonRpcParam(value = "address") ByteString address,
                                  @JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("GetLatestBalance")
    GetBalanceResponse getLatestBalance(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("TryCallTransaction")
    TryCallTransactionResponse tryCallTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                                  @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                                  @JsonRpcParam(value = "amount") BigInteger amount,
                                                  @JsonRpcParam(value = "gas") BigInteger gas,
                                                  @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                                  @JsonRpcParam(value = "data") String data,
                                                  @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("GetTransactionReceipt")
    GetTransactionReceiptResponse getTransactionReceipt(@JsonRpcParam(value = "transaction_hash")
                                                                ByteString transactionHash);

    @JsonRpcMethod("GetGasEstimate")
    long getGasEstimate(@JsonRpcParam(value = "object") TransactionOuterClass.Transaction transaction);
}
