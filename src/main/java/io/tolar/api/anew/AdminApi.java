package io.tolar.api.anew;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import tolar.proto.Account.AddressBalance;

import java.math.BigInteger;
import java.util.List;

public interface AdminApi {
    @JsonRpcMethod("admin_create")
    boolean create(@JsonRpcParam(value = "master_password") String masterPassword);

    @JsonRpcMethod("admin_open")
    boolean open(@JsonRpcParam(value = "master_password") String masterPassword);

    @JsonRpcMethod("admin_listAddresses")
    List<ByteString> listAddresses();

    @JsonRpcMethod("admin_verifyAddress")
    boolean verifyAddress(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("admin_createNewAddress")
    ByteString createNewAddress(@JsonRpcParam(value = "name") String name,
                                @JsonRpcParam(value = "lock_password") String lockPassword,
                                @JsonRpcParam(value = "lock_hint") String lockHint);

    @JsonRpcMethod("admin_exportKeyFile")
    String exportKeyFile(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("admin_importKeyFile")
    boolean importKeyFile(@JsonRpcParam(value = "json_key_file") String jsonKeyFile,
                          @JsonRpcParam(value = "name") String name,
                          @JsonRpcParam(value = "lock_password") String lockPassword,
                          @JsonRpcParam(value = "lock_hint") String lockHint);

    @JsonRpcMethod("admin_listBalancePerAddress")
    List<AddressBalance> listBalancePerAddress();

    @JsonRpcMethod("admin_sendRawTransaction")
    ByteString sendRawTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                  @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                  @JsonRpcParam(value = "amount") BigInteger amount,
                                  @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                  @JsonRpcParam(value = "gas") BigInteger gas,
                                  @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                  @JsonRpcParam(value = "data") String data,
                                  @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("admin_changePassword")
    boolean changePassword(@JsonRpcParam(value = "old_master_password") String oldMasterPassword,
                           @JsonRpcParam(value = "new_master_password") String newMasterPassword);

    @JsonRpcMethod("admin_changeAddressPassword")
    boolean changeAddressPassword(@JsonRpcParam(value = "address") ByteString address,
                                  @JsonRpcParam(value = "old_password") String oldPassword,
                                  @JsonRpcParam(value = "new_password") String newPassword);

    @JsonRpcMethod("admin_sendFundTransferTransaction")
    ByteString sendFundTransferTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                           @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                           @JsonRpcParam(value = "amount") BigInteger amount,
                                           @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                           @JsonRpcParam(value = "gas") BigInteger gas,
                                           @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                           @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("admin_sendDeployContractTransaction")
    ByteString sendDeployContractTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                             @JsonRpcParam(value = "amount") BigInteger amount,
                                             @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                             @JsonRpcParam(value = "gas") BigInteger gas,
                                             @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                             @JsonRpcParam(value = "data") String data,
                                             @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("admin_sendExecuteFunctionTransaction")
    ByteString sendExecuteFunctionTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                              @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                              @JsonRpcParam(value = "amount") BigInteger amount,
                                              @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                              @JsonRpcParam(value = "gas") BigInteger gas,
                                              @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                              @JsonRpcParam(value = "data") String data,
                                              @JsonRpcParam(value = "nonce") BigInteger nonce);
}
