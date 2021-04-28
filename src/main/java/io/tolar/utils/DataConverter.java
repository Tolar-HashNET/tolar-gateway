package io.tolar.utils;

import com.google.protobuf.ByteString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.utils.Numeric;

@UtilityClass
@Slf4j
public class DataConverter {

    public ByteString tryParseDataAsHex(String inputData) {
        try {
            String noPrefixData = Numeric.cleanHexPrefix(inputData);
            byte[] decodedHex = Hex.decode(noPrefixData);
            return ByteString.copyFrom(decodedHex);
        } catch (DecoderException ex) {
            log.warn("Cannot convert to hex! fallback to regular string... message: {}", ex.getMessage());
            return ByteString.copyFromUtf8(inputData);
        }
    }

}
