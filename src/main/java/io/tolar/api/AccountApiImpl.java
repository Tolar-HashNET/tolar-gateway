package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.utils.BalanceConverter;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tolar.proto.Account.*;
import tolar.proto.AccountServiceGrpc;

import java.math.BigInteger;
import java.util.List;

@Service
@AutoJsonRpcServiceImpl
public class AccountApiImpl implements AccountApi {
    @Autowired
    private ChannelUtils channelUtils;

    @Override
    public boolean create(String masterPassword) {
        CreateRequest createRequest = CreateRequest
                .newBuilder()
                .setMasterPassword(masterPassword)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .create(createRequest)
                .getResult();
    }

    @Override
    public boolean open(String masterPassword) {
        OpenRequest openRequest = OpenRequest
                .newBuilder()
                .setMasterPassword(masterPassword)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .open(openRequest)
                .getResult();
    }

    @Override
    public List<ByteString> listAddresses() {
        ListAddressesRequest listAddressesRequest = ListAddressesRequest
                .newBuilder()
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .listAddresses(listAddressesRequest)
                .getAddressesList();
    }

    @Override
    public boolean verifyAddress(ByteString address) {
        VerifyAddressRequest verifyAddressRequest = VerifyAddressRequest
                .newBuilder()
                .setAddress(address)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .verifyAddress(verifyAddressRequest)
                .getResult();
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

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .createNewAddress(createNewAddressRequest)
                .getAddress();
    }

    @Override
    public String exportKeyFile(ByteString address) {
        ExportKeyFileRequest exportKeyFileRequest = ExportKeyFileRequest
                .newBuilder()
                .setAddress(address)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .exportKeyFile(exportKeyFileRequest)
                .getJsonKeyFile();
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
        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .importKeyFile(request)
                .getResult();
    }

    @Override
    public List<AddressBalance> listBalancePerAddress() {
        ListBalancePerAddressRequest listBalancePerAddressRequest = ListBalancePerAddressRequest
                .newBuilder()
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .listBalancePerAddress(listBalancePerAddressRequest)
                .getAddressesList();
    }

    @Override
    public ByteString sendRawTransaction(ByteString senderAddress, ByteString receiverAddress, BigInteger amount,
                                         String senderAddressPassword, BigInteger gas, BigInteger gasPrice,
                                         String data, BigInteger nonce) {
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

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendRawTransaction(sendRawTransactionRequest)
                .getTransactionHash();

    }

    @Override
    public boolean changePassword(String oldMasterPassword, String newMasterPassword) {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest
                .newBuilder()
                .setOldMasterPassword(oldMasterPassword)
                .setNewMasterPassword(newMasterPassword)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .changePassword(changePasswordRequest)
                .getResult();
    }

    @Override
    public boolean changeAddressPassword(ByteString address, String oldPassword, String newPassword) {
        ChangeAddressPasswordRequest changeAddressPasswordRequest = ChangeAddressPasswordRequest
                .newBuilder()
                .setAddress(address)
                .setOldPassword(oldPassword)
                .setNewPassword(newPassword)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .changeAddressPassword(changeAddressPasswordRequest)
                .getResult();
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

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendFundTransferTransaction(sendFundTransferTransactionRequest)
                .getTransactionHash();
    }

    @Override
    public ByteString sendDeployContractTransaction(ByteString senderAddress, BigInteger amount,
                                                    String senderAddressPassword, BigInteger gas,
                                                    BigInteger gasPrice, String data, BigInteger nonce) {
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

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendDeployContractTransaction(sendDeployContractTransactionRequest)
                .getTransactionHash();
    }

    @Override
    public ByteString sendExecuteFunctionTransaction(ByteString senderAddress, ByteString receiverAddress,
                                                     BigInteger amount, String senderAddressPassword,
                                                     BigInteger gas, BigInteger gasPrice, String data,
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

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendExecuteFunctionTransaction(sendExecuteFunctionTransactionRequest)
                .getTransactionHash();
    }
}
