package org.example.marketstock.simulation.json;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public final class SimpleJsonReader implements JsonReader {

    private static final Logger LOGGER = LogManager.getLogger(SimpleJsonReader.class);

    @Override
    public String[] getResource(String location) {
        final JSONParser parser = new JSONParser();
        String[] resources = new String[]{};

        try (FileReader reader = new FileReader(location)) {

            final Object object = parser.parse(reader);
            final JSONArray jsonArray = (JSONArray) object;
            final Object[] objects = jsonArray.toArray();
            resources = Arrays.copyOf(objects, objects.length, String[].class);

        } catch (IOException | ParseException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }

        return resources;
    }
}