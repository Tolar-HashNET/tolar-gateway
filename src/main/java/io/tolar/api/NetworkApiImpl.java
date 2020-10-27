package io.tolar.api;

import io.grpc.Channel;
import io.tolar.utils.ChannelUtils;
import org.springframework.stereotype.Component;
import tolar.proto.Network;
import tolar.proto.NetworkServiceGrpc;

@Component
public class NetworkApiImpl implements NetworkApi {
    private final ChannelUtils channelUtils;

    public NetworkApiImpl(ChannelUtils channelUtils) {
        this.channelUtils = channelUtils;
    }

    @Override
    public long peerCount() {
        Network.PeerCountRequest peerCountRequest = Network.PeerCountRequest
                .newBuilder()
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return NetworkServiceGrpc
                    .newBlockingStub(channel)
                    .peerCount(peerCountRequest)
                    .getCount();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public long masterNodeCount() {
        Network.MasterNodeCountRequest masterNodeCountRequest = Network.MasterNodeCountRequest
                .newBuilder()
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return NetworkServiceGrpc
                    .newBlockingStub(channel)
                    .masterNodeCount(masterNodeCountRequest)
                    .getCount();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public boolean isMasterNode() {
        Network.IsMasterNodeRequest isMasterNodeRequest = Network.IsMasterNodeRequest
                .newBuilder()
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return NetworkServiceGrpc
                    .newBlockingStub(channel)
                    .isMasterNode(isMasterNodeRequest)
                    .getIsMaster();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public long maxPeerCount() {
        Network.MaxPeerCountRequest maxPeerCountRequest = Network.MaxPeerCountRequest
                .newBuilder()
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return NetworkServiceGrpc
                    .newBlockingStub(channel)
                    .maxPeerCount(maxPeerCountRequest)
                    .getCount();
        } finally {
            channelUtils.release(channel);
        }
    }
}
