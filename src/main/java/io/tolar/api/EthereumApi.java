package io.tolar.api;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import io.tolar.responses.EthBlock;

public interface EthereumApi {
    @JsonRpcMethod("eth_getBalance")
    String ethGetBalance(@JsonRpcParam(value = "address") String address, @JsonRpcParam(value = "tag") String tag);

    @JsonRpcMethod("eth_blockNumber")
    String ethBlockNumber();

    @JsonRpcMethod("eth_getBlockByNumber")
    EthBlock ethGetBlockByNumber(@JsonRpcParam(value = "tag") String tag, @JsonRpcParam(value = "full_transaction") boolean isFullTransaction);
}
