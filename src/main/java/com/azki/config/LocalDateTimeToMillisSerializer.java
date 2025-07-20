package com.azki.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeToMillisSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        long millis = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        gen.writeNumber(millis);
    }
}