package io.tolar.api;

import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.tolar.utils.ChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tolar.proto.Account;
import tolar.proto.Account.CreateRequest;
import tolar.proto.AccountServiceGrpc;

import java.util.List;

@Service
@AutoJsonRpcServiceImpl
public class AccountApiImpl implements AccountApi {
    @Autowired
    private ChannelUtils channelUtils;

    @Override
    public boolean create(String masterPassword) {
        CreateRequest createRequest = CreateRequest.newBuilder().setMasterPassword(masterPassword).build();

        return AccountServiceGrpc
                .newBlockingStub(channelUtils.getChannel())
                .create(createRequest)
                .getResult();
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
