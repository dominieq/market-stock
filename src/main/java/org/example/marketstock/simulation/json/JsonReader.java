package org.example.marketstock.simulation.json;

/**
 * Provides a method to retrieve a string array from a specified location.
 *
 * @since 1.0.0
 * @author Domink Szmyt
 */
public interface JsonReader {

    /**
     * An implementation can either search for the destination in local resources or make an API call.
     * @param location - Preferably an URL to a specified resource.
     * @return String[] - An array of string values.
     */
    String[] getResource(String location);
}
