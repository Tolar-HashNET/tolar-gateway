package io.tolar.api.old;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import tolar.proto.Account.AddressBalance;

import java.math.BigInteger;
import java.util.List;

@JsonRpcService("/Account")
public interface OldAccountApi {
    @JsonRpcMethod("Create")
    boolean create(@JsonRpcParam(value = "master_password") String masterPassword);

    @JsonRpcMethod("Open")
    boolean open(@JsonRpcParam(value = "master_password") String masterPassword);

    @JsonRpcMethod("ListAddresses")
    List<ByteString> listAddresses();

    @JsonRpcMethod("VerifyAddress")
    boolean verifyAddress(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("CreateNewAddress")
    ByteString createNewAddress(@JsonRpcParam(value = "name") String name,
                                @JsonRpcParam(value = "lock_password") String lockPassword,
                                @JsonRpcParam(value = "lock_hint") String lockHint);

    @JsonRpcMethod("ExportKeyFile")
    String exportKeyFile(@JsonRpcParam(value = "address") ByteString address);

    @JsonRpcMethod("ImportKeyFile")
    boolean importKeyFile(@JsonRpcParam(value = "json_key_file") String jsonKeyFile,
                          @JsonRpcParam(value = "name") String name,
                          @JsonRpcParam(value = "lock_password") String lockPassword,
                          @JsonRpcParam(value = "lock_hint") String lockHint);

    @JsonRpcMethod("ListBalancePerAddress")
    List<AddressBalance> listBalancePerAddress();

    @JsonRpcMethod("SendRawTransaction")
    ByteString sendRawTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                  @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                  @JsonRpcParam(value = "amount") BigInteger amount,
                                  @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                  @JsonRpcParam(value = "gas") BigInteger gas,
                                  @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                  @JsonRpcParam(value = "data") String data,
                                  @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("ChangePassword")
    boolean changePassword(@JsonRpcParam(value = "old_master_password") String oldMasterPassword,
                           @JsonRpcParam(value = "new_master_password") String newMasterPassword);

    @JsonRpcMethod("ChangeAddressPassword")
    boolean changeAddressPassword(@JsonRpcParam(value = "address") ByteString address,
                                  @JsonRpcParam(value = "old_password") String oldPassword,
                                  @JsonRpcParam(value = "new_password") String newPassword);

    @JsonRpcMethod("SendFundTransferTransaction")
    ByteString sendFundTransferTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                           @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                           @JsonRpcParam(value = "amount") BigInteger amount,
                                           @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                           @JsonRpcParam(value = "gas") BigInteger gas,
                                           @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                           @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("SendDeployContractTransaction")
    ByteString sendDeployContractTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                             @JsonRpcParam(value = "amount") BigInteger amount,
                                             @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                             @JsonRpcParam(value = "gas") BigInteger gas,
                                             @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                             @JsonRpcParam(value = "data") String data,
                                             @JsonRpcParam(value = "nonce") BigInteger nonce);

    @JsonRpcMethod("SendExecuteFunctionTransaction")
    ByteString sendExecuteFunctionTransaction(@JsonRpcParam(value = "sender_address") ByteString senderAddress,
                                              @JsonRpcParam(value = "receiver_address") ByteString receiverAddress,
                                              @JsonRpcParam(value = "amount") BigInteger amount,
                                              @JsonRpcParam(value = "sender_address_password") String senderAddressPassword,
                                              @JsonRpcParam(value = "gas") BigInteger gas,
                                              @JsonRpcParam(value = "gas_price") BigInteger gasPrice,
                                              @JsonRpcParam(value = "data") String data,
                                              @JsonRpcParam(value = "nonce") BigInteger nonce);
}
