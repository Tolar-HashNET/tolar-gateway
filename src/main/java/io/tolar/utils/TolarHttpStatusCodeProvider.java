package io.tolar.utils;

import com.googlecode.jsonrpc4j.HttpStatusCodeProvider;

public class TolarHttpStatusCodeProvider implements HttpStatusCodeProvider {
    @Override
    public int getHttpStatusCode(int i) {
        if (i == 11) {
            return 409;
        }
        return 0;
    }

    @Override
    public Integer getJsonRpcCode(int i) {
        if (i == 409) {
            return 11;
        }
        return null;
    }
}
