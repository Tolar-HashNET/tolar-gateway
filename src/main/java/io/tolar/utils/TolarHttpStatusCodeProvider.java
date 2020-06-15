package io.tolar.utils;

import com.googlecode.jsonrpc4j.HttpStatusCodeProvider;

public class TolarHttpStatusCodeProvider implements HttpStatusCodeProvider {
    @Override
    public int getHttpStatusCode(int i) {
        switch (i) {
            case 3:
            case 0:
                return 200;
            case 1:
                return 499;
            case 2:
            case 13:
            case 15:
            case -32001:
                return 500;
            case 9:
            case 11:
                return 400;
            case 4:
                return 504;
            case 5:
                return 404;
            case 6:
            case 10:
                return 409;
            case 7:
                return 403;
            case 8:
                return 429;
            case 12:
                return 501;
            case 14:
                return 503;
            case 16:
                return 401;
        }
        return 0;
    }

    @Override
    public Integer getJsonRpcCode(int i) {
        return null;
    }
}
