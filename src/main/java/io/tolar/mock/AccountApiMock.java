package io.tolar.mock;

import com.google.protobuf.ByteString;
import io.tolar.api.AccountApi;
import tolar.proto.Account;

import java.math.BigInteger;
import java.util.List;

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
    public List<ByteString> listAddresses() {
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
    public ByteString sendRawTransaction(ByteString senderAddress, ByteString receiverAddress, BigInteger amount,
                                         String senderAddressPassword, BigInteger gas, BigInteger gasPrice,
                                         String data, BigInteger nonce) {
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
    public ByteString sendFundTransferTransaction(ByteString senderAddress, ByteString receiverAddress,
                                                  BigInteger amount, String senderAddressPassword, BigInteger gas,
                                                  BigInteger gasPrice, BigInteger nonce) {
        return null;
    }

    @Override
    public ByteString sendDeployContractTransaction(ByteString senderAddress, BigInteger amount,
                                                    String senderAddressPassword, BigInteger gas, BigInteger gasPrice,
                                                    String data, BigInteger nonce) {
        return null;
    }

    @Override
    public ByteString sendExecuteFunctionTransaction(ByteString senderAddress, ByteString receiverAddress,
                                                     BigInteger amount, String senderAddressPassword, BigInteger gas,
                                                     BigInteger gasPrice, String data, BigInteger nonce) {
        return null;
    }
}
