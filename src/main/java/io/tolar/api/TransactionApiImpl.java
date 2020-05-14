package io.tolar.api;

import com.google.protobuf.ByteString;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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




}