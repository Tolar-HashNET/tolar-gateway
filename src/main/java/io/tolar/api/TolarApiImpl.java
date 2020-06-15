package io.tolar.api;

import com.google.protobuf.ByteString;
import io.grpc.StatusRuntimeException;
import io.tolar.caching.NewTxCache;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.ChannelUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tolar.proto.Blockchain.*;
import tolar.proto.BlockchainServiceGrpc;
import tolar.proto.tx.TransactionOuterClass;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TolarApiImpl implements TolarApi {
    private static final int MAX_RETRY = 5;
    private final ChannelUtils channelUtils;
    private final NewTxCache txCache;
    private long blockCount;

    public TolarApiImpl(ChannelUtils channelUtils, NewTxCache txCache) {
        this.channelUtils = channelUtils;
        this.txCache = txCache;
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

    @Scheduled(fixedRate = 2_000)
    private void refreshCache(){
        if(blockCount == 0){
            blockCount = getBlockCount();
            return;
        }
        long latestBlocks = getBlockCount();

        for(long i = blockCount + 1; i <= latestBlocks; i++){
            GetBlockResponse blockByIndex = getBlockByIndex(i);

            List<String> list = blockByIndex.getTransactionHashesList()
                    .stream()
                    .map(t -> t.toStringUtf8())
                    .collect(Collectors.toList());
            txCache.remove(list);
            log.info("Removed {} from cache", list.size());
        }

        log.info("Cache cleanup done.");
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
        return getTransaction(transactionHash, 0);
    }

    public GetTransactionResponse getTransaction(ByteString transactionHash, int tries) {
        GetTransactionRequest getTransactionRequest = GetTransactionRequest
                .newBuilder()
                .setTransactionHash(transactionHash)
                .build();

        while(!txCache.canProceed(transactionHash.toStringUtf8())){
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        try {
            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getTransaction(getTransactionRequest);

        } catch (StatusRuntimeException ex){
            if (tries >= MAX_RETRY) {
                throw ex;
            }
            // retry
            return getTransaction(transactionHash, tries + 1);
        }
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

        //TODO: See how to send empty list of addresses
        if (addresses == null || addresses.isEmpty()) {
            getTransactionListRequest = GetTransactionListRequest
                    .newBuilder()
                    .setLimit(limit)
                    .setSkip(skip)
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

        ByteString nonce = BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getNonce(getNonceRequest)
                .getNonce();

        return BalanceConverter.toBigInteger(nonce);
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
                                                         BigInteger amount, BigInteger gas, BigInteger gasPrice,
                                                         String data, BigInteger nonce) {
        TransactionOuterClass.Transaction transaction = TransactionOuterClass.Transaction
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setReceiverAddress(receiverAddress)
                .setValue(BalanceConverter.toByteString(amount))
                .setGas(BalanceConverter.toByteString(gas))
                .setGasPrice(BalanceConverter.toByteString(gasPrice))
                .setData(data)
                .setNonce(BalanceConverter.toByteString(nonce))
                .build();

        TryCallTransactionResponse tryCallTransactionResponse = BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .tryCallTransaction(transaction);

        return tryCallTransactionResponse;
    }

    @Override
    public GetTransactionReceiptResponse getTransactionReceipt(ByteString transactionHash) {
        return getTransactionReceipt(transactionHash, 0);
    }

    public GetTransactionReceiptResponse getTransactionReceipt(ByteString transactionHash, int tries) {
            GetTransactionReceiptRequest getTransactionReceiptRequest = GetTransactionReceiptRequest
                    .newBuilder()
                    .setTransactionHash(transactionHash)
                    .build();

        while(!txCache.canProceed(transactionHash.toStringUtf8())){
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

            try {
                return BlockchainServiceGrpc
                        .newBlockingStub(channelUtils.getChannel())
                        .getTransactionReceipt(getTransactionReceiptRequest);
            } catch (StatusRuntimeException e){
                if (tries >= MAX_RETRY) {
                    throw e;
                }

                return getTransactionReceipt(transactionHash, tries + 1);
            }
    }

    @Override
    public long getGasEstimate(TransactionOuterClass.Transaction transaction) {
        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getGasEstimate(transaction)
                .getGasEstimate();
    }

    @Override
    public String getTransactionProtobuf(TransactionOuterClass.Transaction transaction) {
        return Base64.toBase64String(transaction.toByteString().toByteArray());
    }

}
