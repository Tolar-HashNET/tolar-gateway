package io.tolar.utils;

import com.google.protobuf.ByteString;

import java.math.BigInteger;
import java.util.Arrays;

public class BalanceConverter {

    public static BigInteger toBigInteger(ByteString balance) {
        return new BigInteger(1, balance.toByteArray());
    }

    public static ByteString toByteString(BigInteger balance) {
        return ByteString.copyFrom(convertToUnsignedBytes(balance.toByteArray()));
    }

    private static byte[] convertToUnsignedBytes(byte[] signedBytes) {
        byte[] unsignedBytes = new byte[signedBytes.length];

        Arrays.fill(unsignedBytes, (byte) (signedBytes[0] & 0xFF));
        return unsignedBytes;
    }
}
