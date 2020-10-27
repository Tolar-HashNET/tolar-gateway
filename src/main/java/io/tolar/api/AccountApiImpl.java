package io.tolar.api;

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.ChannelUtils;
import org.springframework.stereotype.Component;
import tolar.proto.Account.*;
import tolar.proto.AccountServiceGrpc;

import java.math.BigInteger;
import java.util.List;

@Component
public class AccountApiImpl implements AccountApi {
    private final ChannelUtils channelUtils;

    public AccountApiImpl(ChannelUtils channelUtils) {
        this.channelUtils = channelUtils;
    }

    @Override
    public boolean create(String masterPassword) {
        //todo: check if password is optional?
        CreateRequest createRequest = CreateRequest
                .newBuilder()
                .setMasterPassword(masterPassword)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .create(createRequest)
                    .getResult();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public boolean open(String masterPassword) {
        OpenRequest openRequest = OpenRequest
                .newBuilder()
                .setMasterPassword(masterPassword)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();
            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .open(openRequest)
                    .getResult();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public List<ByteString> listAddresses() {
        ListAddressesRequest listAddressesRequest = ListAddressesRequest
                .newBuilder()
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();
            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .listAddresses(listAddressesRequest)
                    .getAddressesList();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public boolean verifyAddress(ByteString address) {
        VerifyAddressRequest verifyAddressRequest = VerifyAddressRequest
                .newBuilder()
                .setAddress(address)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .verifyAddress(verifyAddressRequest)
                    .getResult();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public ByteString createNewAddress(String name, String lockPassword, String lockHint) {
        CreateNewAddressRequest.Builder createNewAddressRequestBuilder = CreateNewAddressRequest
                .newBuilder();

        if (name != null) {
            createNewAddressRequestBuilder.setName(name);
        }

        if (lockPassword != null) {
            createNewAddressRequestBuilder.setLockPassword(lockPassword);
        }

        if (lockHint != null) {
            createNewAddressRequestBuilder.setLockHint(lockHint);
        }

        CreateNewAddressRequest createNewAddressRequest = createNewAddressRequestBuilder.build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .createNewAddress(createNewAddressRequest)
                    .getAddress();
        } finally {
            channelUtils.release(channel);
        }
    }


    @Override
    public String exportKeyFile(ByteString address) {
        ExportKeyFileRequest exportKeyFileRequest = ExportKeyFileRequest
                .newBuilder()
                .setAddress(address)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .exportKeyFile(exportKeyFileRequest)
                    .getJsonKeyFile();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public boolean importKeyFile(String jsonKeyFile, String name, String lockPassword, String lockHint) {
        ImportKeyFileRequest.Builder requestBuilder = ImportKeyFileRequest.newBuilder();
        requestBuilder.setJsonKeyFile(jsonKeyFile);

        if (name != null) {
            requestBuilder.setName(name);
        }

        if (lockPassword != null) {
            requestBuilder.setLockPassword(lockPassword);
        }

        if (lockHint != null) {
            requestBuilder.setLockHint(lockHint);
        }

        ImportKeyFileRequest request = requestBuilder.build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();
            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .importKeyFile(request)
                    .getResult();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public List<AddressBalance> listBalancePerAddress() {
        ListBalancePerAddressRequest listBalancePerAddressRequest = ListBalancePerAddressRequest
                .newBuilder()
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .listBalancePerAddress(listBalancePerAddressRequest)
                    .getAddressesList();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public ByteString sendRawTransaction(ByteString senderAddress, ByteString receiverAddress, BigInteger amount,
                                         String senderAddressPassword, BigInteger gas, BigInteger gasPrice,
                                         ByteString data, BigInteger nonce) {
        //todo: check if the data field is optional?
        SendRawTransactionRequest sendRawTransactionRequest = SendRawTransactionRequest
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setReceiverAddress(receiverAddress)
                .setAmount(BalanceConverter.toByteString(amount))
                .setSenderAddressPassword(senderAddressPassword)
                .setGas(BalanceConverter.toByteString(gas))
                .setGasPrice(BalanceConverter.toByteString(gasPrice))
                .setData(data)
                .setNonce(BalanceConverter.toByteString(nonce))
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .sendRawTransaction(sendRawTransactionRequest)
                    .getTransactionHash();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public boolean changePassword(String oldMasterPassword, String newMasterPassword) {
        //todo: check if the address has now password, is it an optional argument then?
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest
                .newBuilder()
                .setOldMasterPassword(oldMasterPassword)
                .setNewMasterPassword(newMasterPassword)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .changePassword(changePasswordRequest)
                    .getResult();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public boolean changeAddressPassword(ByteString address, String oldPassword, String newPassword) {
        //todo: check if the address has now password, is it an optional argument then?
        ChangeAddressPasswordRequest changeAddressPasswordRequest = ChangeAddressPasswordRequest
                .newBuilder()
                .setAddress(address)
                .setOldPassword(oldPassword)
                .setNewPassword(newPassword)
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .changeAddressPassword(changeAddressPasswordRequest)
                    .getResult();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public ByteString sendFundTransferTransaction(ByteString senderAddress, ByteString receiverAddress,
                                                  BigInteger amount, String senderAddressPassword, BigInteger gas,
                                                  BigInteger gasPrice, BigInteger nonce) {

        SendFundTransferTransactionRequest sendFundTransferTransactionRequest = SendFundTransferTransactionRequest
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setReceiverAddress(receiverAddress)
                .setAmount(BalanceConverter.toByteString(amount))
                .setSenderAddressPassword(senderAddressPassword)
                .setGas(BalanceConverter.toByteString(gas))
                .setGasPrice(BalanceConverter.toByteString(gasPrice))
                .setNonce(BalanceConverter.toByteString(nonce))
                .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .sendFundTransferTransaction(sendFundTransferTransactionRequest)
                    .getTransactionHash();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public ByteString sendDeployContractTransaction(ByteString senderAddress, BigInteger amount,
                                                    String senderAddressPassword, BigInteger gas,
                                                    BigInteger gasPrice, ByteString data, BigInteger nonce) {
        SendDeployContractTransactionRequest sendDeployContractTransactionRequest =
                SendDeployContractTransactionRequest
                        .newBuilder()
                        .setSenderAddress(senderAddress)
                        .setAmount(BalanceConverter.toByteString(amount))
                        .setSenderAddressPassword(senderAddressPassword)
                        .setGas(BalanceConverter.toByteString(gas))
                        .setGasPrice(BalanceConverter.toByteString(gasPrice))
                        .setData(data)
                        .setNonce(BalanceConverter.toByteString(nonce))
                        .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .sendDeployContractTransaction(sendDeployContractTransactionRequest)
                    .getTransactionHash();
        } finally {
            channelUtils.release(channel);
        }
    }

    @Override
    public ByteString sendExecuteFunctionTransaction(ByteString senderAddress, ByteString receiverAddress,
                                                     BigInteger amount, String senderAddressPassword,
                                                     BigInteger gas, BigInteger gasPrice, ByteString data,
                                                     BigInteger nonce) {
        SendExecuteFunctionTransactionRequest sendExecuteFunctionTransactionRequest =
                SendExecuteFunctionTransactionRequest
                        .newBuilder()
                        .setSenderAddress(senderAddress)
                        .setReceiverAddress(receiverAddress)
                        .setAmount(BalanceConverter.toByteString(amount))
                        .setSenderAddressPassword(senderAddressPassword)
                        .setGas(BalanceConverter.toByteString(gas))
                        .setGasPrice(BalanceConverter.toByteString(gasPrice))
                        .setData(data)
                        .setNonce(BalanceConverter.toByteString(nonce))
                        .build();

        Channel channel = null;

        try {
            channel = channelUtils.getChannel();

            return AccountServiceGrpc
                    .newBlockingStub(channel)
                    .sendExecuteFunctionTransaction(sendExecuteFunctionTransactionRequest)
                    .getTransactionHash();
        } finally {
            channelUtils.release(channel);
        }
    }

}
