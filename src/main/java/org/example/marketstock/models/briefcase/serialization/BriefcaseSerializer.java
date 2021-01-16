package org.example.marketstock.models.briefcase.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.briefcase.Briefcase;

import java.io.IOException;
import java.util.Map;

public class BriefcaseSerializer extends StdSerializer<Briefcase> {

    public BriefcaseSerializer() {
        this(null);
    }

    public BriefcaseSerializer(Class<Briefcase> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(final Briefcase briefcase,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartArray();

        for (Map.Entry<Asset, Integer> entry : briefcase.getMap().entrySet()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("asset", entry.getKey());
            jsonGenerator.writeNumberField("numberOfAsset", entry.getValue());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }
}
