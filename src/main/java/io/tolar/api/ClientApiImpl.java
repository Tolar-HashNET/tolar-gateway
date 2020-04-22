package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tolar.proto.Blockchain.*;
import tolar.proto.BlockchainServiceGrpc;
import tolar.proto.Client.SendSignedTransactionRequest;
import tolar.proto.Network.IsMasterNodeRequest;
import tolar.proto.Network.MasterNodeCountRequest;
import tolar.proto.Network.MaxPeerCountRequest;
import tolar.proto.Network.PeerCountRequest;
import tolar.proto.NetworkServiceGrpc;
import tolar.proto.TransactionServiceGrpc;
import tolar.proto.tx.TransactionOuterClass;
import tolar.proto.tx.TransactionOuterClass.SignedTransaction;

import java.math.BigInteger;
import java.util.List;

@Service
@AutoJsonRpcServiceImpl
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
    public GetBlockResponse getBlockByHash(ByteString blockHash) {
        GetBlockByHashRequest getBlockByHashRequest = GetBlockByHashRequest
                .newBuilder()
                .setBlockHash(blockHash)
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getBlockByHash(getBlockByHashRequest);

    }

    @Override
    public GetBlockResponse getBlockByIndex(long blockIndex) {
        GetBlockByIndexRequest getBlockByIndexRequest = GetBlockByIndexRequest
                .newBuilder()
                .setBlockIndex(blockIndex)
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getBlockByIndex(getBlockByIndexRequest);

    }

    @Override
    public GetTransactionResponse getTransaction(ByteString transactionHash) {
        GetTransactionRequest getTransactionRequest = GetTransactionRequest
                .newBuilder()
                .setTransactionHash(transactionHash)
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getTransaction(getTransactionRequest);
    }

    @Override
    public GetBlockchainInfoResponse getBlockchainInfo() {
        GetBlockchainInfoRequest getBlockchainInfoRequest = GetBlockchainInfoRequest
                .newBuilder()
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getBlockchainInfo(getBlockchainInfoRequest);
    }

    @Override
    public GetTransactionListResponse getTransactionList(List<ByteString> addresses, long limit, long skip) {
        GetTransactionListRequest getTransactionListRequest;
        //todo: see how to send empty addressess :O
        if (addresses == null || addresses.isEmpty()) {
            getTransactionListRequest = GetTransactionListRequest
                    .newBuilder()
                    .setLimit(limit)
                    .setSkip(skip)
                    .clearAddresses()
                    .build();
        } else {
            getTransactionListRequest = GetTransactionListRequest
                    .newBuilder()
                    .addAllAddresses(addresses)
                    .setLimit(limit)
                    .setSkip(skip)
                    .build();
        }


        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getTransactionList(getTransactionListRequest);
    }

    @Override
    public BigInteger getNonce(ByteString address) {
        GetNonceRequest getNonceRequest = GetNonceRequest
                .newBuilder()
                .setAddress(address)
                .build();

        return BalanceConverter.convertBalance(BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getNonce(getNonceRequest)
                .getNonce());
    }

    @Override
    public GetBalanceResponse getBalance(ByteString address, long blockIndex) {
        GetBalanceRequest getBalanceRequest = GetBalanceRequest
                .newBuilder()
                .setAddress(address)
                .setBlockIndex(blockIndex)
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getBalance(getBalanceRequest);
    }

    @Override
    public GetBalanceResponse getLatestBalance(ByteString address) {
        GetBalanceRequest getBalanceRequest = GetBalanceRequest
                .newBuilder()
                .setAddress(address)
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getLatestBalance(getBalanceRequest);
    }

    @Override
    public TryCallTransactionResponse tryCallTransaction(ByteString senderAddress, ByteString receiverAddress,
                                                         ByteString amount, ByteString gas, ByteString gasPrice,
                                                         String data, ByteString nonce) {
        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setReceiverAddress(receiverAddress)
                .setValue(amount)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setData(data)
                .setNonce(nonce)
                .build();

        return BlockchainServiceGrpc.newBlockingStub(channelUtils.getChannel())
                .tryCallTransaction(transaction);
    }

    @Override
    public GetTransactionReceiptResponse getTransactionReceipt(ByteString transactionHash) {
        GetTransactionReceiptRequest getTransactionReceiptRequest = GetTransactionReceiptRequest
                .newBuilder()
                .setTransactionHash(transactionHash)
                .build();

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getTransactionReceipt(getTransactionReceiptRequest);
    }

    @Override
    public long getGasEstimate(TransactionOuterClass.Transaction transaction) {
        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getGasEstimate(transaction)
                .getGasEstimate();
    }
}
