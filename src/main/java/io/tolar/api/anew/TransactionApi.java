package io.tolar.api.anew;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import tolar.proto.tx.TransactionOuterClass;

public interface TransactionApi {
    @JsonRpcMethod("tx_sendSignedTransaction")
    ByteString sendSignedTransaction(@JsonRpcParam(value = "transaction") TransactionOuterClass.SignedTransaction signedTransaction);
}
