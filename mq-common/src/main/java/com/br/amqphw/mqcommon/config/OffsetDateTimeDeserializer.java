package com.br.amqphw.mqcommon.config;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

@JsonComponent
public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    
    @Override
    public OffsetDateTime deserialize(final JsonParser jsonParser, final DeserializationContext context)
            throws IOException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode jsonNode = objectCodec.readTree(jsonParser);

        return jsonNode != null ? deserialize(jsonNode.asText()) : null;
    }

    public OffsetDateTime deserialize(final String date) throws IOException {
        return StringUtils.isEmpty(date) ? null : OffsetDateTime
                .parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
