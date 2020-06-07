package io.tolar.api;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;

public interface EthereumApi {
    @JsonRpcMethod("eth_getBalance")
    String ethGetBalance(@JsonRpcParam(value = "address") String address, @JsonRpcParam(value = "tag") String tag);

    @JsonRpcMethod("eth_blockNumber")
    String ethBlockNumber();
}
