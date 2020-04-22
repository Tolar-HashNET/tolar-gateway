package io.tolar.utils;

import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;

import java.math.BigInteger;

public class BalanceConverter {
    private static final int MIN_LENGTH_OF_DOUBLE = 8;

    public static BigInteger convertBalance(ByteString balance) {
        byte[] balanceInBytes = Bytes.ensureCapacity(balance.toByteArray(),
                MIN_LENGTH_OF_DOUBLE, 0);

        return new BigInteger(balanceInBytes);
    }
}
