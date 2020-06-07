package io.tolar.api;

import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.tolar.utils.AddressConverter;
import io.tolar.utils.BalanceConverter;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Component
public class EthereumApiImpl implements EthereumApi {

    private TolarApi tolarApi;

    public EthereumApiImpl(TolarApi tolarApi) {
        this.tolarApi = tolarApi;
    }

    @Override
    public String ethGetBalance(String address, String tag) {
        ByteString tolarAddress = ByteString.copyFromUtf8(AddressConverter.toTolarAddress(address));

        try {
            long blockIndex = Long.parseLong(tag);
            BigInteger balance = BalanceConverter.toBigInteger(tolarApi.getBalance(tolarAddress, blockIndex).getBalance());
            return Numeric.encodeQuantity(balance);
        } catch (NumberFormatException e) {
            if (tag.equals("latest")) {
                BigInteger balance = BalanceConverter.toBigInteger(tolarApi.getLatestBalance(tolarAddress).getBalance());
                return Numeric.encodeQuantity(balance);
            }

            if (tag.equals("earliest")) {
                BigInteger balance = BalanceConverter.toBigInteger(tolarApi.getBalance(tolarAddress, 0).getBalance());
                return Numeric.encodeQuantity(balance);
            }
        }

        throw new StatusRuntimeException(Status.INVALID_ARGUMENT);
    }
}
