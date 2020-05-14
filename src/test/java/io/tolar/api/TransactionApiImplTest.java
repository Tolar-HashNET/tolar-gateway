package io.tolar.api;

import com.google.common.base.MoreObjects;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.grpc.CallOptions;
import io.grpc.ClientCall;
import io.grpc.Metadata;
import io.grpc.Status;
import io.tolar.utils.ChannelUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tolar.proto.Client;
import tolar.proto.tx.TransactionOuterClass;

import javax.annotation.Nullable;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static org.junit.Assert.*;
import static tolar.proto.TransactionServiceGrpc.SERVICE_NAME;

@Ignore
public class TransactionApiImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionApiImplTest.class);

    @Test
    public void forDebuggingPurposes() {
        Client.SendSignedTransactionRequest sendSignedTransactionRequest = Client.SendSignedTransactionRequest
                .newBuilder()
                .setSignedTransaction((TransactionOuterClass.SignedTransaction) null) //create the tx here!
                .build();

        ChannelUtils channelUtils = new ChannelUtils();//set url config here

        io.grpc.MethodDescriptor<tolar.proto.Client.SendSignedTransactionRequest, tolar.proto.Client.SendSignedTransactionResponse> getSendSignedTransactionMethod =
                io.grpc.MethodDescriptor.<tolar.proto.Client.SendSignedTransactionRequest, tolar.proto.Client.SendSignedTransactionResponse>newBuilder()
                        .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                        .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendSignedTransaction"))
                        .setSampledToLocalTracing(true)
                        .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                tolar.proto.Client.SendSignedTransactionRequest.getDefaultInstance()))
                        .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                tolar.proto.Client.SendSignedTransactionResponse.getDefaultInstance()))
                        .setSchemaDescriptor(new TransactionServiceMethodDescriptorSupplier("SendSignedTransaction"))
                        .build();
        ClientCall<Client.SendSignedTransactionRequest, Client.SendSignedTransactionResponse> clientCall =
                channelUtils.getChannel().newCall(getSendSignedTransactionMethod, CallOptions.DEFAULT);
        GrpcFuture<Client.SendSignedTransactionResponse> responseFuture = new GrpcFuture<>(clientCall);
        UnaryStreamToFuture<Client.SendSignedTransactionResponse> listener = new UnaryStreamToFuture<>(responseFuture);
        clientCall.start(listener, new Metadata());
        clientCall.request(2);
        clientCall.sendMessage(sendSignedTransactionRequest);
        clientCall.halfClose();
        //todo try to get future to se how to debug errors...

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        String toJson = gson.toJson(sendSignedTransactionRequest);
        System.out.println(toJson);
        LOGGER.info(toJson);
    }

    private static abstract class TransactionServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        TransactionServiceBaseDescriptorSupplier() {}

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return tolar.proto.Client.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("TransactionService");
        }
    }

    private static final class TransactionServiceMethodDescriptorSupplier
            extends TransactionServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        TransactionServiceMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static final class GrpcFuture<RespT> extends AbstractFuture<RespT> {
        private final ClientCall<?, RespT> call;

        // Non private to avoid synthetic class
        GrpcFuture(ClientCall<?, RespT> call) {
            this.call = call;
        }

        @Override
        protected void interruptTask() {
            call.cancel("GrpcFuture was cancelled", null);
        }

        @Override
        protected boolean set(@Nullable RespT resp) {
            return super.set(resp);
        }

        @Override
        protected boolean setException(Throwable throwable) {
            return super.setException(throwable);
        }

        @SuppressWarnings("MissingOverride") // Add @Override once Java 6 support is dropped
        protected String pendingToString() {
            return MoreObjects.toStringHelper(this).add("clientCall", call).toString();
        }
    }

    private static final class UnaryStreamToFuture<RespT> extends ClientCall.Listener<RespT> {
        private final GrpcFuture<RespT> responseFuture;
        private RespT value;

        // Non private to avoid synthetic class
        UnaryStreamToFuture(GrpcFuture<RespT> responseFuture) {
            this.responseFuture = responseFuture;
        }

        @Override
        public void onHeaders(Metadata headers) {
        }

        @Override
        public void onMessage(RespT value) {
            if (this.value != null) {
                throw Status.INTERNAL.withDescription("More than one value received for unary call")
                        .asRuntimeException();
            }
            this.value = value;
        }

        @Override
        public void onClose(Status status, Metadata trailers) {
            if (status.isOk()) {
                if (value == null) {
                    // No value received so mark the future as an error
                    responseFuture.setException(
                            Status.INTERNAL.withDescription("No value received for unary call")
                                    .asRuntimeException(trailers));
                }
                responseFuture.set(value);
            } else {
                responseFuture.setException(status.asRuntimeException(trailers));
            }
        }
    }

}