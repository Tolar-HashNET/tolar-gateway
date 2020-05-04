package io.tolar.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorResolver;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

public class TolarErrorResolver implements ErrorResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(TolarErrorResolver.class);

    @Override
    public JsonError resolveError(Throwable throwable, Method method, List<JsonNode> list) {

        if (throwable instanceof StatusRuntimeException) {
            return new JsonError(((StatusRuntimeException) throwable).getStatus().getCode().value(),
                    throwable.getMessage(),
                    null);
        }

        LOGGER.error("unhandled error on method: " + method.getName() + ", error: ", throwable);

        return JsonError.ERROR_NOT_HANDLED;
    }

}
