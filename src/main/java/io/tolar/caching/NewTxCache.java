package io.tolar.caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NewTxCache {
    private final Cache<String, String> txCache;

    public NewTxCache(){
        txCache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    public boolean canProceed(String hash){
        return ! txCache.asMap().containsKey(hash);
    }

    public void remove(List<String> hashes){
        for (String hash : hashes) {
            txCache.invalidate(hash);
        }
    }

    public void put(String txHash) {
        txCache.put(txHash, txHash);
    }
}
