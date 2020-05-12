package io.tolar.api.anew;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import tolar.proto.Account.AddressBalance;

import java.math.BigInteger;
import java.util.List;

public interface AccountApi {
    @JsonRpcMethod("account_create")
    boolean create(@JsonRpcParam(value = "master_password") String masterPassword);

    @JsonRpcMethod("account_open")
    boolean open(@JsonRpcParam(value = "master_password") String masterPassword);

    @JsonRpcMethod("account_listAddresses")
    List<ByteString> listAddresses();

    @JsonRpcMethod("account_verifyAddress")
    boolean verifyAddress(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("account_createNewAddress")
    ByteString createNewAddress(@JsonRpcParam(value = "name") String name,
                                @JsonRpcParam(value = "lock_password") String lockPassword,
                                @JsonRpcParam(value = "lock_hint") String lockHint);

    @JsonRpcMethod("account_exportKeyFile")
    String exportKeyFile(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("account_importKeyFile")
    boolean importKeyFile(@JsonRpcParam(value = "json_key_file") String jsonKeyFile,
                          @JsonRpcParam(value = "name") String name,
                          @JsonRpcParam(value = "lock_password") String lockPassword,
                          @JsonRpcParam(value = "lock_hint") String lockHint);

    @JsonRpcMethod("account_listBalancePerAddress")
    List<AddressBalance> listBalancePerAddress();

    @JsonRpcMethod("account_sendRawTransaction")
    ByteString sendRawTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                  @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                  @JsonRpcParam(value = "amount") BigInteger amount,
                                  @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                  @JsonRpcParam(value = "gas") BigInteger gas,
                                  @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                  @JsonRpcParam(value = "data") String data,
                                  @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("account_changePassword")
    boolean changePassword(@JsonRpcParam(value = "old_master_password") String oldMasterPassword,
                           @JsonRpcParam(value = "new_master_password") String newMasterPassword);

    @JsonRpcMethod("account_changeAddressPassword")
    boolean changeAddressPassword(@JsonRpcParam(value = "address") ByteString address,
                                  @JsonRpcParam(value = "old_password") String oldPassword,
                                  @JsonRpcParam(value = "new_password") String newPassword);

    @JsonRpcMethod("account_sendFundTransferTransaction")
    ByteString sendFundTransferTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                           @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                           @JsonRpcParam(value = "amount") BigInteger amount,
                                           @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                           @JsonRpcParam(value = "gas") BigInteger gas,
                                           @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                           @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("account_sendDeployContractTransaction")
    ByteString sendDeployContractTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                             @JsonRpcParam(value = "amount") BigInteger amount,
                                             @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                             @JsonRpcParam(value = "gas") BigInteger gas,
                                             @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                             @JsonRpcParam(value = "data") String data,
                                             @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("account_sendExecuteFunctionTransaction")
    ByteString sendExecuteFunctionTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                              @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                              @JsonRpcParam(value = "amount") BigInteger amount,
                                              @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                              @JsonRpcParam(value = "gas") BigInteger gas,
                                              @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                              @JsonRpcParam(value = "data") String data,
                                              @JsonRpcParam(value = "nonce") BigInteger nonce);
}
