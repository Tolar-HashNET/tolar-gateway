package io.tolar.api;

import com.google.protobuf.ByteString;
import io.grpc.StatusRuntimeException;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.ChannelUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.context.Theme;
import tolar.proto.Blockchain.*;
import tolar.proto.BlockchainServiceGrpc;
import tolar.proto.tx.TransactionOuterClass;

import java.math.BigInteger;
import java.util.List;

@Component
@Slf4j
public class TolarApiImpl implements TolarApi {
    private ChannelUtils channelUtils;

    public TolarApiImpl(ChannelUtils channelUtils) {
        this.channelUtils = channelUtils;
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
        return getTransaction(transactionHash, false);
    }

    public GetTransactionResponse getTransaction(ByteString transactionHash, boolean tried) {
        GetTransactionRequest getTransactionRequest = GetTransactionRequest
                .newBuilder()
                .setTransactionHash(transactionHash)
                .build();

        try {
            return BlockchainServiceGrpc
                    .newBlockingStub(channelUtils.getChannel())
                    .getTransaction(getTransactionRequest);

        } catch (StatusRuntimeException ex){
            if(tried){
                throw ex;
            }
            // retry
            return getTransaction(transactionHash, true);
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

        return BlockchainServiceGrpc.newBlockingStub(channelUtils.getChannel())
                .tryCallTransaction(transaction);
    }

    @Override
    public GetTransactionReceiptResponse getTransactionReceipt(ByteString transactionHash) {
        return getTransactionReceipt(transactionHash, false);
    }


    public GetTransactionReceiptResponse getTransactionReceipt(ByteString transactionHash, boolean tried) {
            GetTransactionReceiptRequest getTransactionReceiptRequest = GetTransactionReceiptRequest
                    .newBuilder()
                    .setTransactionHash(transactionHash)
                    .build();

            try {
                // let's wait a bit for the tolar crew to get their stuff togather.
                Thread.sleep(5_000);
                return BlockchainServiceGrpc
                        .newBlockingStub(channelUtils.getChannel())
                        .getTransactionReceipt(getTransactionReceiptRequest);
            } catch (StatusRuntimeException e){
                if(tried){
                    throw e;
                }

                return getTransactionReceipt(transactionHash, true);
            } catch (InterruptedException e) {
                // meh
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
