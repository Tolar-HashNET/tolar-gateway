package io.tolar.utils;

import com.google.protobuf.ByteString;

import java.math.BigInteger;

public class BalanceConverter {

    public static BigInteger convertBalance(ByteString balance) {
        return new BigInteger(balance.toByteArray());
    }
}
