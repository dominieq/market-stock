package org.example.marketstock.models.entity.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.entity.Player;

import java.io.IOException;

public class PlayerDeserializer extends StdDeserializer<Player> {

    public PlayerDeserializer() {
        this(null);
    }

    public PlayerDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Player deserialize(final JsonParser jsonParser,
                              final DeserializationContext deserializationContext)
            throws IOException {

        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        final String firstName = node.get("firstName").textValue();
        final String lastName = node.get("lastName").textValue();
        final double budget = node.get("budget").doubleValue();
        final Briefcase briefcase = new ObjectMapper().readValue(node.get("briefcase").toString(), Briefcase.class);

        return Player.updateInstance(firstName, lastName, budget, briefcase);
    }
}
