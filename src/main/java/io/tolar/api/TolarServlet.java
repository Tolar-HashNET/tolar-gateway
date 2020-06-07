package io.tolar.api;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.ProxyUtil;
import io.tolar.utils.TolarErrorResolver;
import io.tolar.utils.TolarHttpStatusCodeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class TolarServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(TolarServlet.class);
    private JsonRpcServer jsonRpcServer;

    @Autowired
    private AccountApiImpl adminApi;

    @Autowired
    private TolarApiImpl blockApi;

    @Autowired
    private NetworkApiImpl networkApi;

    @Autowired
    private TransactionApiImpl transactionApi;

    @Autowired
    private EthereumApiImpl ethereumApi;

    @Qualifier("entityDeserializerAndSerializer")
    @Autowired
    private Module module;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            jsonRpcServer.handle(request, response);
        } catch (IOException e) {
            LOGGER.error("Failed to handle request.", e);
        }
    }

    public void init(ServletConfig servletConfig) {
        Object compositeService = ProxyUtil.createCompositeServiceProxy(
                this.getClass().getClassLoader(),
                new Object[]{adminApi, blockApi, networkApi, transactionApi, ethereumApi},
                new Class<?>[]{AccountApi.class, TolarApi.class, NetworkApi.class, TransactionApi.class, EthereumApi.class},
                false);

        jsonRpcServer = new JsonRpcServer(new ObjectMapper().registerModule(module), compositeService);
        jsonRpcServer.setErrorResolver(new TolarErrorResolver());
        jsonRpcServer.setHttpStatusCodeProvider(new TolarHttpStatusCodeProvider());
        jsonRpcServer.setAllowLessParams(true);
    }
}
