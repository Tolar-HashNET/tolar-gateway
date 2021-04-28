package io.tolar.utils;

import lombok.experimental.UtilityClass;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

@UtilityClass
public class AddressConverter {

    public String toTolarAddress(String ethereumAddress) {
        String prefix = "T";
        String prefixHex = Numeric.toHexStringNoPrefix(prefix.getBytes());
        String addressHash = Hash.sha3(ethereumAddress);
        String hashOfHash = Hash.sha3(addressHash);

        return prefixHex +
                Numeric.cleanHexPrefix(ethereumAddress) +
                hashOfHash.substring(hashOfHash.length() - 8);
    }

    public String toEthereumAddress(String tolarAddress) {
        return tolarAddress.substring(1, 21);
    }
}
