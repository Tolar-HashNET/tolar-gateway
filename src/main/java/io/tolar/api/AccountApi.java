package io.tolar.api;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/account")
public interface AccountApi {
    boolean create(@JsonRpcParam(value = "master_password") String masterPassword);

    boolean open(@JsonRpcParam(value = "master_password") String masterPassword);

    byte[] listAddresses();
}
