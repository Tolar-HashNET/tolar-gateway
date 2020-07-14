package io.tolar.caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.grpc.Channel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NewTxCache {
    private final Cache<String, String> reverseTxCache;
    private final Cache<Long, BlockWithChannel> blockCache;
    private final Cache<String, Channel> txToChannelCache;

    public NewTxCache() {
        reverseTxCache = CacheBuilder.newBuilder()
                .maximumSize(100_000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();

        txToChannelCache = CacheBuilder.newBuilder()
                .maximumSize(100_000)
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .build();


        blockCache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    public boolean canProceed(String hash) {
        return ! reverseTxCache.asMap().containsKey(hash);
    }

    public void remove(List<String> hashes, Channel channel) {
        for (String hash : hashes) {
            reverseTxCache.invalidate(hash);
        }

        if(channel != null){
            hashes.forEach(t -> txToChannelCache.put(t, channel));
        }
    }

    public Channel getChannelForTx(String tx) {
        return txToChannelCache.getIfPresent(tx);
    }

    public long notFlushedTx(){
        return reverseTxCache.size();
    }

    public void put(Long blockNumber, BlockWithChannel block){
        blockCache.put(blockNumber, block);
    }

    public BlockWithChannel getBlock(Long blockNumber) {
        return blockCache.getIfPresent(blockNumber);
    }

    public void put(String txHash) {
        reverseTxCache.put(txHash, txHash);
    }
}
