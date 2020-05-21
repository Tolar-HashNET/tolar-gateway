package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.tolar.utils.BalanceConverter;
import tolar.proto.Account.AddressBalance;

import java.io.IOException;

public class AddressBalanceSerializer extends JsonSerializer<AddressBalance> {
    @Override
    public void serialize(AddressBalance addressBalance, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("address", addressBalance.getAddress());
        jsonGenerator.writeObjectField("address_name", addressBalance.getAddressName());
        jsonGenerator.writeObjectField("balance", BalanceConverter.toBigInteger(addressBalance.getBalance()));
        jsonGenerator.writeEndObject();
    }
}