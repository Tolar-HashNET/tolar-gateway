package io.tolar.utils;

import com.google.protobuf.ByteString;

import java.math.BigInteger;

public class BalanceConverter {

    public static BigInteger toBigInteger(ByteString balance) {
        return new BigInteger(1, balance.toByteArray());
    }

    public static ByteString toByteString(BigInteger balance) {
        return ByteString.copyFrom(balance.toByteArray());
    }
}
