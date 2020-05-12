package io.tolar.api.anew;

import com.googlecode.jsonrpc4j.JsonRpcMethod;

public interface NetworkApi {
    @JsonRpcMethod("net_peerCount")
    long peerCount();

    @JsonRpcMethod("net_masterNodeCount")
    long masterNodeCount();

    @JsonRpcMethod("net_isMasterNode")
    boolean isMasterNode();

    @JsonRpcMethod("net_maxPeerCount")
    long maxPeerCount();
}
