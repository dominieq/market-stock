package org.example.marketstock.simulation.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class SimpleJsonReaderTest {

    private SimpleJsonReader subject;

    @BeforeEach
    public void setUp() {
        subject = new SimpleJsonReader();
    }

    @Test
    public void should_read_json_array() {

        // given
        final List<String> expected = Arrays.asList(
                "Fancy text",
                "Boring text",
                "Clever response",
                "Angry reaction",
                "Fight"
        );

        final URL url = getClass().getClassLoader().getResource("simple-json-reader-test/simple-array.json");
        assertThat(url).isNotNull();

        // when
        final String[] actual = subject.getResource(url.getPath());

        // then
        assertThat(actual)
                .hasSize(expected.size())
                .hasSameElementsAs(expected);
    }

    @Test
    public void should_not_read_json_array_because_path_is_invalid() {

        // given
        final String incorrectPath = "incorrectPath";

        // when
        assertThatCode(() -> subject.getResource(incorrectPath)).doesNotThrowAnyException();
        final String[] actual = subject.getResource(incorrectPath);

        // when
        assertThat(actual).isEmpty();
    }
}
