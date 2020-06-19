package io.tolar.api;

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.tolar.caching.NewTxCache;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.ChannelUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tolar.proto.Blockchain.*;
import tolar.proto.BlockchainServiceGrpc;
import tolar.proto.tx.TransactionOuterClass;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TolarApiImpl implements TolarApi {
    private final ChannelUtils channelUtils;
    private final NewTxCache txCache;

    private static final Logger LOGGER = LoggerFactory.getLogger(TolarApiImpl.class);

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

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getBlockCount(getBlockCountRequest)
                    .getBlockCount();
        } finally {
            channelUtils.release(channel);
        }
    }

    private void initCache(){
        long currentBlock = getBlockCount();
        this.blockCount = currentBlock - 50;

        LOGGER.info("Started block cache init!");

        for (long i = blockCount + 1; i <= currentBlock; i++) {
            long blockNumber = i;
            getBlockByIndex(blockNumber);
        }

        for (long i = 0; i < 10; i++) {
            long blockNumber = i;
           getBlockByIndex(blockNumber);
        }

        LOGGER.info("Done with block cache init!");
    }

    @Scheduled(fixedDelay = 1_000)
    private void refreshCache() {
        if (blockCount == 0) {
            initCache();
            return;
        }
        GetBlockchainInfoResponse latestBlocks = getBlockchainInfo();
        long confirmedBlocksCount = latestBlocks.getConfirmedBlocksCount();

        for (long i = blockCount; i < confirmedBlocksCount; i++) {
            GetBlockResponse blockByIndex = getBlockByIndex(i);

            List<String> list = blockByIndex.getTransactionHashesList()
                    .stream()
                    .map(ByteString::toStringUtf8)
                    .collect(Collectors.toList());
            txCache.remove(list);
            LOGGER.info("Removed {} from cache", list.size());
        }

        blockCount = confirmedBlocksCount;
    }

    @Override
    public GetBlockResponse getBlockByHash(ByteString blockHash) {
        Channel channel = null;
        
        try {
            GetBlockByHashRequest getBlockByHashRequest = GetBlockByHashRequest
                    .newBuilder()
                    .setBlockHash(blockHash)
                    .build();

            channel = channelUtils.getChannel();
            
            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getBlockByHash(getBlockByHashRequest);

        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public GetBlockResponse getBlockByIndex(Long blockIndex) {
        return retryBlock(blockIndex, 0);
    }

    private GetBlockResponse retryBlock(Long blockIndex, int tries) {
        GetBlockResponse block = txCache.getBlock(blockIndex);

        if (block != null) {
            return block;
        }

        LOGGER.info("finding block: {}, tries: {}", blockIndex, tries);

        Channel channel = null;

        try {
            Instant now = Instant.now();
            GetBlockByIndexRequest getBlockByIndexRequest = GetBlockByIndexRequest
                    .newBuilder()
                    .setBlockIndex(blockIndex)
                    .build();

            channel = channelUtils.getChannel();

            GetBlockResponse blockByIndex = BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getBlockByIndex(getBlockByIndexRequest);

            LOGGER.info("Got block: " + blockIndex + " in " + ChronoUnit.MILLIS.between(now, Instant.now()) + " milis.");
            txCache.put(blockIndex, blockByIndex);

            return blockByIndex;

        } catch (StatusRuntimeException ex) {
            LOGGER.warn("Could not get block: {}, tries: {}", blockIndex, tries);

            if (Status.NOT_FOUND.getCode().value() == ex.getStatus().getCode().value()
                    && tries <= 10) {
                return retryBlock(blockIndex, tries + 1);
            } else {
                throw ex;
            }
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public GetTransactionResponse getTransaction(ByteString transactionHash) {
        GetTransactionRequest getTransactionRequest = GetTransactionRequest
                .newBuilder()
                .setTransactionHash(transactionHash)
                .build();

        while (!txCache.canProceed(transactionHash.toStringUtf8())) {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }

        Channel channel = null;

        try {
            Instant now = Instant.now();

            channel = channelUtils.getChannel();

            GetTransactionResponse transaction = BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getTransaction(getTransactionRequest);

            LOGGER.info("Tx get in: " + ChronoUnit.MILLIS.between(now, Instant.now()) + " milis.");

            return transaction;
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public GetBlockchainInfoResponse getBlockchainInfo() {
        GetBlockchainInfoRequest getBlockchainInfoRequest = GetBlockchainInfoRequest
                .newBuilder()
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getBlockchainInfo(getBlockchainInfoRequest);
        } finally {
            channelUtils.release(channel);
        }
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

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getTransactionList(getTransactionListRequest);
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public BigInteger getNonce(ByteString address) {
        Channel channel = null;

        try {
            Instant now = Instant.now();
            GetNonceRequest getNonceRequest = GetNonceRequest
                    .newBuilder()
                    .setAddress(address)
                    .build();

            channel = channelUtils.getChannel();

            ByteString nonce = BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getNonce(getNonceRequest)
                    .getNonce();

            LOGGER.info("Nonce get in: " + ChronoUnit.MILLIS.between(now, Instant.now()) + " milis.");

            return BalanceConverter.toBigInteger(nonce);
        }  finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public GetBalanceResponse getBalance(ByteString address, long blockIndex) {
        GetBalanceRequest getBalanceRequest = GetBalanceRequest
                .newBuilder()
                .setAddress(address)
                .setBlockIndex(blockIndex)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getBalance(getBalanceRequest);
        } finally {
            channelUtils.release(channel);
        }


    }

    @Override
    public GetBalanceResponse getLatestBalance(ByteString address) {
        GetBalanceRequest getBalanceRequest = GetBalanceRequest
                .newBuilder()
                .setAddress(address)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getLatestBalance(getBalanceRequest);
        } finally {
            channelUtils.release(channel);
        }
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

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            TryCallTransactionResponse tryCallTransactionResponse = BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .tryCallTransaction(transaction);

            return tryCallTransactionResponse;
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public GetTransactionReceiptResponse getTransactionReceipt(ByteString transactionHash) {
        GetTransactionReceiptRequest getTransactionReceiptRequest = GetTransactionReceiptRequest
                .newBuilder()
                .setTransactionHash(transactionHash)
                .build();

        while (!txCache.canProceed(transactionHash.toStringUtf8())) {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getTransactionReceipt(getTransactionReceiptRequest);
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public long getGasEstimate(TransactionOuterClass.Transaction transaction) {
        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return BlockchainServiceGrpc
                    .newBlockingStub(channel)
                    .getGasEstimate(transaction)
                    .getGasEstimate();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public String getTransactionProtobuf(TransactionOuterClass.Transaction transaction) {
        return Base64.toBase64String(transaction.toByteString().toByteArray());
    }

}
