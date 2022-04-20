package io.tolar.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tolar.proto.Blockchain;

import java.io.IOException;

public class GetPastEventsResponseSerializer extends JsonSerializer<Blockchain.GetPastEventsResponse> {
    @Override
    public void serialize(Blockchain.GetPastEventsResponse getPastEventsResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("past_events", getPastEventsResponse.getPastEventsList());
        jsonGenerator.writeEndObject();
    }
}