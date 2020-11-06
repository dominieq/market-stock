package org.example.marketstock.simulation.croupier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class StringArrayProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                (Object) new String[]{"Object 1", "Object 2", "Object 3"},
                (Object) new String[]{"Fancy text", "Boring text", "Something else"}).map(Arguments::of);
    }
}
