package io.tolar.responses;

import com.google.protobuf.ByteString;
import lombok.Data;
import org.web3j.utils.Numeric;
import tolar.proto.Blockchain;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EthBlock {
    String number;
    String parentHash;
    List<String> transactions;
    String timestamp;

    public EthBlock(Blockchain.GetBlockResponse tolarBlock) {
        this.number = Numeric.encodeQuantity(BigInteger.valueOf(tolarBlock.getBlockIndex()));
        this.parentHash = "0x" + tolarBlock.getPreviousBlockHash().toStringUtf8();
        this.transactions = tolarBlock.getTransactionHashesList()
                .stream()
                .map(ByteString::toStringUtf8)
                .collect(Collectors.toList());
        this.timestamp = Numeric.encodeQuantity(BigInteger.valueOf(tolarBlock.getConfirmationTimestamp()));
    }

}
