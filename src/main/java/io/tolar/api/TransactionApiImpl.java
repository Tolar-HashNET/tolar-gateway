package io.tolar.api;

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.tolar.caching.NewTxCache;
import io.tolar.utils.ChannelUtils;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import tolar.proto.Client;
import tolar.proto.TransactionServiceGrpc;
import tolar.proto.tx.TransactionOuterClass;

@Component
public class TransactionApiImpl implements TransactionApi {
    private final ChannelUtils channelUtils;
    private final NewTxCache txCache;

    public TransactionApiImpl(ChannelUtils channelUtils, NewTxCache txCache) {
        this.channelUtils = channelUtils;
        this.txCache = txCache;
    }

    @Override
    public ByteString sendSignedTransaction(TransactionOuterClass.SignedTransaction signedTransaction) {
        Client.SendSignedTransactionRequest sendSignedTransactionRequest = Client.SendSignedTransactionRequest
                .newBuilder()
                .setSignedTransaction(signedTransaction)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel(signedTransaction.getBody().getSenderAddress().toStringUtf8());

            ByteString transactionHash = TransactionServiceGrpc
                    .newBlockingStub(channel)
                    .sendSignedTransaction(sendSignedTransactionRequest)
                    .getTransactionHash();

            txCache.put(transactionHash.toStringUtf8());
            return transactionHash;
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public String getHashHex(TransactionOuterClass.Transaction transaction) {
        byte[] bytes = Hash.sha3(transaction.toByteString().toByteArray());
        return Numeric.toHexStringNoPrefix(bytes);
    }

}
