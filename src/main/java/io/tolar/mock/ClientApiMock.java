package io.tolar.mock;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.api.ClientApi;
import io.tolar.model.Block;
import io.tolar.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tolar.proto.Blockchain;
import tolar.proto.tx.TransactionOuterClass;

import java.util.List;

@Service
@AutoJsonRpcServiceImpl
public class ClientApiMock implements ClientApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApiMock.class);

    @Override
    public ByteString sendSignedTransaction(TransactionOuterClass.SignedTransaction signedTransaction) {
        LOGGER.info(signedTransaction.getBody().getSenderAddress().toStringUtf8());
        LOGGER.info(signedTransaction.getBody().getReceiverAddress().toStringUtf8());
        LOGGER.info(signedTransaction.getBody().getValue().toStringUtf8());
        LOGGER.info(signedTransaction.getBody().getGas().toStringUtf8());
        LOGGER.info(signedTransaction.getBody().getGasPrice().toStringUtf8());
        LOGGER.info(signedTransaction.getBody().getData());
        LOGGER.info(signedTransaction.getBody().getNonce().toStringUtf8());

        LOGGER.info(signedTransaction.getSigData().getHash().toStringUtf8());
        LOGGER.info(signedTransaction.getSigData().getSignature().toStringUtf8());
        LOGGER.info(signedTransaction.getSigData().getSignerId().toStringUtf8());
        return null;
    }

    @Override
    public long peerCount() {
        return 0;
    }

    @Override
    public long masterNodeCount() {
        return 0;
    }

    @Override
    public boolean isMasterNode() {
        return false;
    }

    @Override
    public long maxPeerCount() {
        return 0;
    }

    @Override
    public long getBlockCount() {
        return 0;
    }

    @Override
    public Block getBlockByHash(ByteString blockHash) {
        return null;
    }

    @Override
    public Block getBlockByIndex(long blockIndex) {
        return null;
    }

    @Override
    public Transaction getTransaction(ByteString transactionHash) {
        return null;
    }

    @Override
    public Blockchain.GetBlockchainInfoResponse getBlockchainInfo() {
        return null;
    }

    @Override
    public Blockchain.GetTransactionListResponse getTransactionList(List<ByteString> addresses, long limit, long skip) {
        return null;
    }

    @Override
    public ByteString getNonce(ByteString address) {
        return null;
    }

    @Override
    public Blockchain.GetBalanceResponse getBalance(ByteString address, long blockIndex) {
        return null;
    }

    @Override
    public Blockchain.GetBalanceResponse getLatestBalance(ByteString address) {
        return null;
    }

    @Override
    public Blockchain.TryCallTransactionResponse tryCallTransaction(ByteString senderAddress, ByteString receiverAddress, ByteString amount, ByteString gas, ByteString gasPrice, String data, ByteString nonce) {
        return null;
    }

    @Override
    public Blockchain.GetTransactionReceiptResponse getTransactionReceipt(ByteString transactionHash) {
        return null;
    }

    @Override
    public long getGasEstimate(TransactionOuterClass.Transaction transaction) {
        return 0;
    }
}
