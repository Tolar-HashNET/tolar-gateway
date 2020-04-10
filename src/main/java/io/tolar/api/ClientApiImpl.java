package io.tolar.api;

import com.google.protobuf.ByteString;
import io.tolar.model.Block;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tolar.proto.Blockchain.GetBlockByHashRequest;
import tolar.proto.Blockchain.GetBlockCountRequest;
import tolar.proto.Blockchain.GetBlockResponse;
import tolar.proto.BlockchainServiceGrpc;
import tolar.proto.Client.SendSignedTransactionRequest;
import tolar.proto.Network.IsMasterNodeRequest;
import tolar.proto.Network.MasterNodeCountRequest;
import tolar.proto.Network.MaxPeerCountRequest;
import tolar.proto.Network.PeerCountRequest;
import tolar.proto.NetworkServiceGrpc;
import tolar.proto.TransactionServiceGrpc;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;

public class ClientApiImpl implements ClientApi {
    @Autowired
    private ChannelUtils channelUtils;

    @Override
    public ByteString sendSignedTransaction(SignedTransaction signedTransaction) {
        SendSignedTransactionRequest sendSignedTransactionRequest = SendSignedTransactionRequest
                .newBuilder()
                .setSignedTransaction(signedTransaction)
                .build();

        return TransactionServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendSignedTransaction(sendSignedTransactionRequest)
                .getTransactionHash();
    }

    @Override
    public long peerCount() {
        PeerCountRequest peerCountRequest = PeerCountRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .peerCount(peerCountRequest)
                .getCount();
    }

    @Override
    public long masterNodeCount() {
        MasterNodeCountRequest masterNodeCountRequest = MasterNodeCountRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .masterNodeCount(masterNodeCountRequest)
                .getCount();
    }

    @Override
    public boolean isMasterNode() {
        IsMasterNodeRequest isMasterNodeRequest = IsMasterNodeRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .isMasterNode(isMasterNodeRequest)
                .getIsMaster();
    }

    @Override
    public long maxPeerCount() {
        MaxPeerCountRequest maxPeerCountRequest = MaxPeerCountRequest
                .newBuilder()
                .build();

        return NetworkServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .maxPeerCount(maxPeerCountRequest)
                .getCount();
    }

    @Override
    public long getBlockCount() {
        GetBlockCountRequest getBlockCountRequest = GetBlockCountRequest
                .newBuilder()
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getBlockCount(getBlockCountRequest)
                .getBlockCount();
    }

    @Override
    public Block getBlockByHash(ByteString blockHash) {
        GetBlockByHashRequest getBlockByHashRequest = GetBlockByHashRequest
                .newBuilder()
                .setBlockHash(blockHash)
                .build();

        GetBlockResponse blockByHash = BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getBlockByHash(getBlockByHashRequest);

        return new Block(blockByHash);
    }

    @Override
    public Block getBlockByIndex(long blockIndex) {
        return null;
    }

    @Override
    public Block getTransaction(ByteString transactionHash) {
        return null;
    }
}
