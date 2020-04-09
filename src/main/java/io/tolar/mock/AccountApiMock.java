package io.tolar.mock;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.api.AccountApi;
import io.tolar.proto.Account;
import org.springframework.stereotype.Service;

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
    public byte[] listAddresses() {
        return new byte[0];
    }

    @Override
    public boolean verifyAddress(ByteString address) {
        return false;
    }

    @Override
    public ByteString createNewAddress(String name, String lockPassword, String hint) {
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
}
