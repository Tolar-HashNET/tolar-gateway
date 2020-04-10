package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import tolar.proto.Account.AddressBalance;

import java.util.List;

@JsonRpcService("/account")
public interface AccountApi {
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
}
