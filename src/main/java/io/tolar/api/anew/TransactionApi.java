package io.tolar.api.anew;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import tolar.proto.tx.TransactionOuterClass;

@JsonRpcService(value = "/tolar")
public interface TransactionApi {
    @JsonRpcMethod("transaction_sendSignedTransaction")
    ByteString sendSignedTransaction(@JsonRpcParam(value = "transaction") TransactionOuterClass.SignedTransaction signedTransaction);
}
