package io.tolar.api;

import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.tolar.responses.EthBlock;
import io.tolar.utils.AddressConverter;
import io.tolar.utils.BalanceConverter;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;
import tolar.proto.Blockchain;

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
            long blockIndex = Numeric.decodeQuantity(tag).longValue();
            BigInteger balance = BalanceConverter.toBigInteger(tolarApi.getBalance(tolarAddress, blockIndex).getBalance());
            return Numeric.encodeQuantity(balance);
        } catch (NumberFormatException e) {
            if (tag.equals("latest") || tag.equals("pending")) {
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

    @Override
    public String ethBlockNumber() {
        BigInteger blockNumber = BigInteger.valueOf(tolarApi.getBlockCount()).subtract(BigInteger.ONE);
        return Numeric.encodeQuantity(blockNumber);
    }

    @Override
    public EthBlock ethGetBlockByNumber(String tag, boolean isFullTransaction) {
        long blockIndex = -1;
        try {
            blockIndex = Numeric.decodeQuantity(tag).longValue();
        } catch (NumberFormatException e) {
            if (tag.equals("latest") || tag.equals("pending")) {
                blockIndex = Numeric.decodeQuantity(ethBlockNumber()).longValue();
            }

            if (tag.equals("earliest")) {
                blockIndex = 0;
            }
        }

        if (blockIndex == -1) {
            throw new StatusRuntimeException(Status.INVALID_ARGUMENT);
        }

        Blockchain.GetBlockResponse block = tolarApi.getBlockByIndex(blockIndex);
        return new EthBlock(block);
    }

    @Override
    public int netVersion() {
        return 69;
    }

    @Override
    public BigInteger ethGetTransactionCount(String address, String tag) {
        return BigInteger.valueOf(10);
    }
}
