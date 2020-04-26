package io.tolar.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import io.tolar.serialization.*;
import io.tolar.utils.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tolar.proto.Account.AddressBalance;
import tolar.proto.Blockchain;
import tolar.proto.Common;
import tolar.proto.tx.TransactionOuterClass;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Bean
    public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
        AutoJsonRpcServiceImplExporter exporter = new AutoJsonRpcServiceImplExporter();
        exporter.setErrorResolver(new TolarErrorResolver());
        exporter.setHttpStatusCodeProvider(new TolarHttpStatusCodeProvider());
        return exporter;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("", "/swagger");
    }

    @Bean
    public Module entityDeserializerAndSerializer() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());

        module.addDeserializer(Common.SignatureData.class, new SignatureDataDeserializer());
        module.addDeserializer(TransactionOuterClass.Transaction.class, new TransactionDeserializer());
        module.addDeserializer(TransactionOuterClass.SignedTransaction.class, new SignedTransactionDeserializer());
        module.addSerializer(AddressBalance.class, new AddressBalanceSerializer());
        module.addSerializer(Blockchain.GetTransactionReceiptResponse.class,
                new GetTransactionReceiptResponseSerializer());
        module.addSerializer(Blockchain.TryCallTransactionResponse.class, new TryCallTransactionResponseSerializer());
        module.addSerializer(Blockchain.GetBalanceResponse.class, new GetBalanceResponseSerializer());
        module.addSerializer(Blockchain.GetTransactionListResponse.class, new GetTransactionListResponseSerializer());
        module.addSerializer(Blockchain.GetTransactionResponse.class, new GetTransactionResponseSerializer());
        module.addSerializer(Blockchain.GetBlockchainInfoResponse.class, new GetBlockchainInfoResponseSerializer());
        module.addSerializer(Blockchain.GetBlockResponse.class, new GetBlockResponseSerializer());
        return module;
    }
}