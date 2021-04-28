package io.tolar.utils;

import com.google.protobuf.ByteString;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.util.Arrays;

@UtilityClass
public class BalanceConverter {

    public BigInteger toBigInteger(ByteString balance) {
        return new BigInteger(1, balance.toByteArray());
    }

    public ByteString toByteString(BigInteger balance) {
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

    private int countLeadingZeroes(byte[] bytes) {
        int result = 0;

        while (bytes[result] == 0 && result + 1 < bytes.length) {
            result++;
        }

        return result;
    }

}
