package io.tolar.response;

import com.google.protobuf.ByteString;
import lombok.Data;
import tolar.proto.Account;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListAddressResponse {
    private List<String> addresses;

    public ListAddressResponse(Account.ListAddressesResponse response) {
        this.addresses = response.getAddressesList()
                .stream()
                .map(ByteString::toStringUtf8)
                .collect(Collectors.toList());
    }
}
