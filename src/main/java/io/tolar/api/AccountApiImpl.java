package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tolar.proto.Account.*;
import tolar.proto.AccountServiceGrpc;

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
        CreateNewAddressRequest createNewAddressRequest = CreateNewAddressRequest
                .newBuilder()
                .setName(name)
                .setLockPassword(lockPassword)
                .setLockHint(lockHint)
                .build();

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
        ImportKeyFileRequest importKeyFileRequest = ImportKeyFileRequest
                .newBuilder()
                .setJsonKeyFile(jsonKeyFile)
                .setLockPassword(lockPassword)
                .setLockHint(lockHint)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .importKeyFile(importKeyFileRequest)
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
    public ByteString sendRawTransaction(ByteString senderAddress, ByteString receiverAddress, ByteString amount,
                                         String senderAddressPassword, ByteString gas, ByteString gasPrice,
                                         String data, ByteString nonce) {
        SendRawTransactionRequest sendRawTransactionRequest = SendRawTransactionRequest
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setReceiverAddress(receiverAddress)
                .setAmount(amount)
                .setSenderAddressPassword(senderAddressPassword)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setData(data)
                .setNonce(nonce)
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
                                                  ByteString amount, String senderAddressPassword, ByteString gas,
                                                  ByteString gasPrice, ByteString nonce) {
        SendFundTransferTransactionRequest sendFundTransferTransactionRequest = SendFundTransferTransactionRequest
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setReceiverAddress(receiverAddress)
                .setAmount(amount)
                .setSenderAddressPassword(senderAddressPassword)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setNonce(nonce)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendFundTransferTransaction(sendFundTransferTransactionRequest)
                .getTransactionHash();
    }

    @Override
    public ByteString sendDeployContractTransaction(ByteString senderAddress, ByteString amount,
                                                    String senderAddressPassword, ByteString gas,
                                                    ByteString gasPrice, String data, ByteString nonce) {
        SendDeployContractTransactionRequest sendDeployContractTransactionRequest =
                SendDeployContractTransactionRequest
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setAmount(amount)
                .setSenderAddressPassword(senderAddressPassword)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setData(data)
                .setNonce(nonce)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendDeployContractTransaction(sendDeployContractTransactionRequest)
                .getTransactionHash();
    }

    @Override
    public ByteString sendExecuteFunctionTransaction(ByteString senderAddress, ByteString receiverAddress,
                                                     ByteString amount, String senderAddressPassword,
                                                     ByteString gas, ByteString gasPrice, String data,
                                                     ByteString nonce) {
        SendExecuteFunctionTransactionRequest sendExecuteFunctionTransactionRequest =
                SendExecuteFunctionTransactionRequest
                .newBuilder()
                .setSenderAddress(senderAddress)
                .setReceiverAddress(receiverAddress)
                .setAmount(amount)
                .setSenderAddressPassword(senderAddressPassword)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setData(data)
                .setNonce(nonce)
                .build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .sendExecuteFunctionTransaction(sendExecuteFunctionTransactionRequest)
                .getTransactionHash();
    }
}
