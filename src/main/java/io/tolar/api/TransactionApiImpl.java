package io.tolar.api;

import com.google.protobuf.ByteString;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import tolar.proto.Client;
import tolar.proto.TransactionServiceGrpc;
import tolar.proto.tx.TransactionOuterClass;

@Component
public class TransactionApiImpl implements TransactionApi {
    @Autowired
    private ChannelUtils channelUtils;

    @Override
    public ByteString sendSignedTransaction(TransactionOuterClass.SignedTransaction signedTransaction) {
        Client.SendSignedTransactionRequest sendSignedTransactionRequest = Client.SendSignedTransactionRequest
                .newBuilder()
                .setSignedTransaction(signedTransaction)
                .build();

        return TransactionServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendSignedTransaction(sendSignedTransactionRequest)
                .getTransactionHash();
    }

    @Override
    public String getHashHex(TransactionOuterClass.Transaction transaction) {
        byte[] bytes = Hash.sha3(transaction.toByteString().toByteArray());
        return Numeric.toHexStringNoPrefix(bytes);
    }

}
