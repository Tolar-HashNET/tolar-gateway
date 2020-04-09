package io.tolar.mock;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.api.AccountApi;
import org.springframework.stereotype.Service;

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
    public byte[] listAddresses() {
        return new byte[0];
    }
}
