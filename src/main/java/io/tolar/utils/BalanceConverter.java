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

        int zeroes = countLeadingZeroes(balanceBytes);

        if (zeroes > 0 && balanceBytes.length > 1) {
            balanceBytes = Arrays.copyOfRange(balanceBytes, zeroes, balanceBytes.length);
        }

        return ByteString.copyFrom(balanceBytes);
    }

    private static int countLeadingZeroes(byte[] bytes) {
        int result = 0;

        while (bytes[result] == 0) {
            result++;
        }

        return result;
    }

}
