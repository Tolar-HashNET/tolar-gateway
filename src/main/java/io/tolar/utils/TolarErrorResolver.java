package io.tolar.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

public class TolarErrorResolver implements ErrorResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(TolarErrorResolver.class);

    @Override
    public JsonError resolveError(Throwable throwable, Method method, List<JsonNode> list) {
        if (throwable.getMessage().startsWith("ALREADY_EXISTS")) {
            return new JsonError(11, throwable.getMessage(), null);
        }
        return JsonError.ERROR_NOT_HANDLED;
    }
}
