package io.tolar.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.primitives.Bytes;
import tolar.proto.Account.AddressBalance;

import java.io.IOException;
import java.nio.ByteBuffer;

public class AddressBalanceSerializer extends JsonSerializer<AddressBalance> {
    private static final int MIN_LENGTH_OF_DOUBLE = 8;

    @Override
    public void serialize(AddressBalance addressBalance, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("address", addressBalance.getAddress());
        jsonGenerator.writeObjectField("name", addressBalance.getAddressName());

        byte[] balanceInBytes = Bytes.ensureCapacity(addressBalance.getBalance().toByteArray(),
                MIN_LENGTH_OF_DOUBLE, 0);

        jsonGenerator.writeObjectField("balance", ByteBuffer.wrap(balanceInBytes).getDouble());
        jsonGenerator.writeEndObject();
    }
}