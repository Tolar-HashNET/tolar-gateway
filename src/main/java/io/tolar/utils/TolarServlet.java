package io.tolar.utils;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.ProxyUtil;
import io.tolar.api.anew.*;
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
    private JsonRpcServer jsonRpcServer;

    @Autowired
    private AdminApiImpl adminApi;

    @Autowired
    private BlockApiImpl blockApi;

    @Autowired
    private NetworkApiImpl networkApi;

    @Autowired
    private TransactionApiImpl transactionApi;

    @Qualifier("entityDeserializerAndSerializer")
    @Autowired
    private Module module;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            jsonRpcServer.handle(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(ServletConfig servletConfig) {
        Object compositeService = ProxyUtil.createCompositeServiceProxy(
                ClassLoader.getSystemClassLoader(),
                new Object[]{adminApi, blockApi, networkApi, transactionApi},
                new Class<?>[]{AdminApi.class, BlockApi.class, NetworkApi.class, TransactionApi.class},
                true);


        jsonRpcServer = new JsonRpcServer(new ObjectMapper().registerModule(module), compositeService);
        jsonRpcServer.setErrorResolver(new TolarErrorResolver());
        jsonRpcServer.setHttpStatusCodeProvider(new TolarHttpStatusCodeProvider());
        jsonRpcServer.setAllowLessParams(true);
    }
}
