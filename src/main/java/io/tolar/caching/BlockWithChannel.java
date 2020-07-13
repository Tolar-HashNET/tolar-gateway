package io.tolar.caching;

import io.grpc.Channel;
import lombok.Builder;
import lombok.Data;
import tolar.proto.Blockchain;

@Data
@Builder
public class BlockWithChannel {
    Channel channel;
    Blockchain.GetBlockResponse response;
}
