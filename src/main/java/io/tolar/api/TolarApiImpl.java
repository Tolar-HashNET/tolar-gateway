package io.tolar.api;

import com.google.protobuf.ByteString;
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

        return BlockchainServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .getBlockCount(getBlockCountRequest)
                .getBlockCount();
    }

    @Scheduled(fixedRate = 2_000)
    private void refreshCache() {
        if (blockCount == 0) {
            blockCount = getBlockCount();
            return;
        }
        long latestBlocks = getBlockCount();

        for (long i = blockCount + 1; i <= latestBlocks; i++) {
            GetBlockResponse blockByIndex = getBlockByIndex(i);

            List<String> list = blockByIndex.getTransactionHashesList()
                    .stream()
                    .map(t -> t.toStringUtf8())
                    .collect(Collectors.toList());
            txCache.remove(list);
            LOGGER.info("Removed {} from cache", list.size());
        }

        LOGGER.info("Cache cleanup done.");
    }

    @Override
    public GetBlockResponse getBlockByHash(ByteString blockHash) {
        try {
            channelUtils.acquire();

            GetBlockByHashRequest getBlockByHashRequest = GetBlockByHashRequest
                    .newBuilder()
                    .setBlockHash(blockHash)
                    .build();

            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getBlockByHash(getBlockByHashRequest);

        } finally {
            channelUtils.release();
        }
    }

    @Override
    public GetBlockResponse getBlockByIndex(long blockIndex) {
        channelUtils.acquire();
        try {
            GetBlockByIndexRequest getBlockByIndexRequest = GetBlockByIndexRequest
                    .newBuilder()
                    .setBlockIndex(blockIndex)
                    .build();

            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getBlockByIndex(getBlockByIndexRequest);

        } finally {
            channelUtils.release();
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

        try {
            channelUtils.acquire();
            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getTransaction(getTransactionRequest);

        } finally {
            channelUtils.release();
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

        try {
            channelUtils.acquire();
            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getTransactionList(getTransactionListRequest);
        } finally {
            channelUtils.release();
        }
    }

    @Override
    public BigInteger getNonce(ByteString address) {
        try {
            channelUtils.acquire();
            GetNonceRequest getNonceRequest = GetNonceRequest
                    .newBuilder()
                    .setAddress(address)
                    .build();

            ByteString nonce = BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getNonce(getNonceRequest)
                    .getNonce();
            return BalanceConverter.toBigInteger(nonce);
        }  finally {
            channelUtils.release();
        }
    }

    @Override
    public GetBalanceResponse getBalance(ByteString address, long blockIndex) {
        GetBalanceRequest getBalanceRequest = GetBalanceRequest
                .newBuilder()
                .setAddress(address)
                .setBlockIndex(blockIndex)
                .build();
        try {
            channelUtils.acquire();
            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getBalance(getBalanceRequest);
        } finally {
            channelUtils.release();
        }


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

        try {
            channelUtils.acquire();

            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getTransactionReceipt(getTransactionReceiptRequest);
        } finally {
            channelUtils.release();
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
