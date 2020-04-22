package io.tolar.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tolar.proto.Account.AddressBalance;

import java.io.IOException;

public class AddressBalanceSerializer extends JsonSerializer<AddressBalance> {
    @Override
    public void serialize(AddressBalance addressBalance, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("address", addressBalance.getAddress());
        jsonGenerator.writeObjectField("name", addressBalance.getAddressName());
        jsonGenerator.writeObjectField("balance", BalanceConverter.convertBalance(addressBalance.getBalance()));
        jsonGenerator.writeEndObject();
    }
}