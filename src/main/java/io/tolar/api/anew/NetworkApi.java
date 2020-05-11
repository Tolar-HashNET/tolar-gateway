package io.tolar.api.anew;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/tolar")
public interface NetworkApi {
    @JsonRpcMethod("network_peerCount")
    long peerCount();

    @JsonRpcMethod("network_masterNodeCount")
    long masterNodeCount();

    @JsonRpcMethod("network_isMasterNode")
    boolean isMasterNode();

    @JsonRpcMethod("network_maxPeerCount")
    long maxPeerCount();
}
