package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import tolar.proto.Blockchain.*;
import tolar.proto.tx.TransactionOuterClass;

import java.math.BigInteger;
import java.util.List;

public interface TolarApi {
    @JsonRpcMethod("tol_getBlockCount")
    long getBlockCount();

    @JsonRpcMethod("tol_getBlockByHash")
    GetBlockResponse getBlockByHash(@JsonRpcParam(value = "block_hash") ByteString blockHash);

    @JsonRpcMethod("tol_getBlockByIndex")
    GetBlockResponse getBlockByIndex(@JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("tol_getTransaction")
    GetTransactionResponse getTransaction(@JsonRpcParam(value = "transaction_hash") ByteString transactionHash);

    @JsonRpcMethod("tol_getBlockchainInfo")
    GetBlockchainInfoResponse getBlockchainInfo();

    @JsonRpcMethod("tol_getTransactionList")
    GetTransactionListResponse getTransactionList(@JsonRpcParam(value = "addresses") List<ByteString> addresses,
                                                  @JsonRpcParam(value = "limit") long limit,
                                                  @JsonRpcParam(value = "skip") long skip);

    @JsonRpcMethod("tol_getNonce")
    BigInteger getNonce(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("tol_getBalance")
    GetBalanceResponse getBalance(@JsonRpcParam(value = "address") ByteString address,
                                  @JsonRpcParam(value = "block_index") long blockIndex);

    @JsonRpcMethod("tol_getLatestBalance")
    GetBalanceResponse getLatestBalance(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("tol_tryCallTransaction")
    TryCallTransactionResponse tryCallTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                                  @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                                  @JsonRpcParam(value = "amount") BigInteger amount,
                                                  @JsonRpcParam(value = "gas") BigInteger gas,
                                                  @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                                  @JsonRpcParam(value = "data") String data,
                                                  @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("tol_getTransactionReceipt")
    GetTransactionReceiptResponse getTransactionReceipt(@JsonRpcParam(value = "transaction_hash")
                                                                ByteString transactionHash);

    @JsonRpcMethod("tol_getGasEstimate")
    long getGasEstimate(@JsonRpcParam(value = "object") TransactionOuterClass.Transaction transaction);

    @JsonRpcMethod("tol_getTransactionProtobuf")
    String getTransactionProtobuf(@JsonRpcParam(value="transaction") TransactionOuterClass.Transaction transaction);
}
