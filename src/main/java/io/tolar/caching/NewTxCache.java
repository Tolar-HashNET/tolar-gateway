package io.tolar.caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;
import tolar.proto.Blockchain;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NewTxCache {
    private final Cache<String, String> reverseTxCache;
    private final Cache<Long, Blockchain.GetBlockResponse> blockCache;

    public NewTxCache(){
        reverseTxCache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();

        blockCache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    public boolean canProceed(String hash){
        return ! reverseTxCache.asMap().containsKey(hash);
    }

    public void remove(List<String> hashes){
        for (String hash : hashes) {
            reverseTxCache.invalidate(hash);
        }
    }

    public void put(Long blockNumber, Blockchain.GetBlockResponse block){
        blockCache.put(blockNumber, block);
    }

    public Blockchain.GetBlockResponse getBlock(Long blockNumber){
        return blockCache.getIfPresent(blockNumber);
    }

    public void put(String txHash) {
        reverseTxCache.put(txHash, txHash);
    }
}
