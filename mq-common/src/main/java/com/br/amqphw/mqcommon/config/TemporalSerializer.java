package com.br.amqphw.mqcommon.config;

import java.io.IOException;
import java.time.temporal.Temporal;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class TemporalSerializer extends JsonSerializer<Temporal> {
    
    @Override
    public void serialize(final Temporal value, 
            final JsonGenerator generator, 
            final SerializerProvider serializers) throws IOException {
        if (value != null && generator != null) {
            generator.writeString(value.toString());
        }
    }
}
