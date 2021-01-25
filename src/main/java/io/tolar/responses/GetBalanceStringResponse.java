package io.tolar.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.tolar.utils.BalanceConverter;
import lombok.Data;
import tolar.proto.Blockchain;

@Data
public class GetBalanceStringResponse {
    private String balance;
    @JsonProperty("block_index")
    private String blockIndex;

    public static GetBalanceStringResponse createFromGrpc(Blockchain.GetBalanceResponse response) {
        GetBalanceStringResponse result = new GetBalanceStringResponse();
        result.setBalance(BalanceConverter.toBigInteger(response.getBalance()).toString());
        result.setBlockIndex(String.valueOf(response.getBlockIndex()));
        return result;
    }

}
