package io.tolar.api.anew;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tolar.proto.Network;
import tolar.proto.NetworkServiceGrpc;

@Service
@AutoJsonRpcServiceImpl
public class NetworkApiImpl implements NetworkApi {
    @Autowired
    private ChannelUtils channelUtils;

    @Override
    public long peerCount() {
        Network.PeerCountRequest peerCountRequest = Network.PeerCountRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .peerCount(peerCountRequest)
                .getCount();
    }

    @Override
    public long masterNodeCount() {
        Network.MasterNodeCountRequest masterNodeCountRequest = Network.MasterNodeCountRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .masterNodeCount(masterNodeCountRequest)
                .getCount();
    }

    @Override
    public boolean isMasterNode() {
        Network.IsMasterNodeRequest isMasterNodeRequest = Network.IsMasterNodeRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .isMasterNode(isMasterNodeRequest)
                .getIsMaster();
    }

    @Override
    public long maxPeerCount() {
        Network.MaxPeerCountRequest maxPeerCountRequest = Network.MaxPeerCountRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .maxPeerCount(maxPeerCountRequest)
                .getCount();
    }
}
