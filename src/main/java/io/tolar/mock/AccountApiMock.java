package io.tolar.mock;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.api.AccountApi;
import org.springframework.stereotype.Service;
import tolar.proto.Account;

import java.util.List;

@Service
@AutoJsonRpcServiceImpl
public class AccountApiMock implements AccountApi {
    @Override
    public boolean create(String masterPassword) {
        return false;
    }

    @Override
    public boolean open(String masterPassword) {
        return false;
    }

    @Override
    public List<String> listAddresses() {
        return null;
    }

    @Override
    public boolean verifyAddress(ByteString address) {
        return false;
    }

    @Override
    public ByteString createNewAddress(String name, String lockPassword, String lockHint) {
        return null;
    }

    @Override
    public String exportKeyFile(ByteString address) {
        return null;
    }

    @Override
    public boolean importKeyFile(String jsonKeyFile, String name, String lockPassword, String lockHint) {
        return false;
    }

    @Override
    public List<Account.AddressBalance> listBalancePerAddress() {
        return null;
    }

    @Override
    public ByteString sendRawTransaction(ByteString senderAddress, ByteString receiverAddress, ByteString amount, String senderAddressPassword, ByteString gas, ByteString gasPrice, String data, ByteString nonce) {
        return null;
    }

    @Override
    public boolean changePassword(String oldMasterPassword, String newMasterPassword) {
        return false;
    }

    @Override
    public boolean changeAddressPassword(ByteString address, String oldPassword, String newPassword) {
        return false;
    }

    @Override
    public ByteString sendFundTransferTransaction(ByteString senderAddress, ByteString receiverAddress, ByteString amount, String senderAddressPassword, ByteString gas, ByteString gasPrice, ByteString nonce) {
        return null;
    }

    @Override
    public ByteString sendDeployContractTransaction(ByteString senderAddress, ByteString amount, String senderAddressPassword, ByteString gas, ByteString gasPrice, String data, ByteString nonce) {
        return null;
    }

    @Override
    public ByteString sendExecuteFunctionTransaction(ByteString senderAddress, ByteString receiverAddress, ByteString amount, String senderAddressPassword, ByteString gas, ByteString gasPrice, String data, ByteString nonce) {
        return null;
    }
}
