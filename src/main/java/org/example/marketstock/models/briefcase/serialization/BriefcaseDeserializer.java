package org.example.marketstock.models.briefcase.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A custom deserializer for the {@link Briefcase} class.
 *
 * @author Dominik Szmyt
 * @see Briefcase
 * @since 1.0.0
 */
public class BriefcaseDeserializer extends StdDeserializer<Briefcase> {

    public BriefcaseDeserializer() {
        this(null);
    }

    public BriefcaseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Briefcase deserialize(final JsonParser jsonParser,
                                 final DeserializationContext deserializationContext)
            throws IOException {

        final Map<Asset, Integer> map = new LinkedHashMap<>();
        final JsonNode arrayNode = jsonParser.getCodec().readTree(jsonParser);

        if (arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                if (node.isObject()) {
                    final Asset asset = new ObjectMapper().readValue(node.get("asset").toString(), Asset.class);
                    final Integer numberOfAsset = (Integer) node.get("numberOfAsset").numberValue();
                    map.put(asset, numberOfAsset);
                }
            }
        }

        return BriefcaseBuilder.builder().withMap(map).build();
    }
}
