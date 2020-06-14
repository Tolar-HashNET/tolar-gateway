package io.tolar.utils;

import com.google.protobuf.ByteString;

import java.math.BigInteger;
import java.util.Arrays;

public class BalanceConverter {

    public static BigInteger toBigInteger(ByteString balance) {
        return new BigInteger(1, balance.toByteArray());
    }

    public static ByteString toByteString(BigInteger balance) {
        if (balance == null) {
            return null;
        }
        byte[] balanceBytes = balance.toByteArray();

        if (balanceBytes[0] == 0) {
            balanceBytes = Arrays.copyOfRange(balanceBytes, 1, balanceBytes.length);
        }
        return ByteString.copyFrom(balanceBytes);
    }
}
