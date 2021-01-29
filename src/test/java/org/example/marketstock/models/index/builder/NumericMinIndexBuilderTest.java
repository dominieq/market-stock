package org.example.marketstock.models.index.builder;

import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.example.marketstock.models.index.NumericMinIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NumericMinIndexBuilderTest {

    private NumericMinIndexBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = NumericMinIndexBuilder.builder();
    }

    @Test
    public void should_build_valid_numeric_min_index_with_content_smaller_than_size() {

        // given
        final Company company1 = CompanyBuilder.builder()
                .withCurrentRate(1D)
                .build();

        final Company company2 = CompanyBuilder.builder()
                .withCurrentRate(1D)
                .build();

        final List<Asset> content = new ArrayList<>(Arrays.asList(company1, company2));

        // when
        final NumericMinIndex actual = subject
                .withName("NumericMinIndex")
                .withValue(1D)
                .withSize(5L)
                .withContent(content)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "NumericMinIndex")
                .hasFieldOrPropertyWithValue("size", 5L)
                .hasFieldOrPropertyWithValue("value", 2D);

        assertThat(actual.getContent()).containsExactlyInAnyOrderElementsOf(content);
    }

    @Test
    public void should_build_valid_numeric_min_index_with_content_bigger_than_size() {

        // given
        final Company company1 = CompanyBuilder.builder()
                .withCurrentRate(1D)
                .build();

        final Company company2 = CompanyBuilder.builder()
                .withCurrentRate(2D)
                .build();

        final List<Asset> content = new ArrayList<>(Arrays.asList(company1, company2));

        // when
        final NumericMinIndex actual = subject
                .withName("NumericMinIndex")
                .withValue(1D)
                .withSize(1L)
                .withContent(content)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "NumericMinIndex")
                .hasFieldOrPropertyWithValue("size", 1L)
                .hasFieldOrPropertyWithValue("value", 1D);

        assertThat(actual.getContent()).containsOnly(company1);
    }

    @Test
    public void should_build_valid_numeric_min_index_with_null_content() {

        // given
        final NumericMinIndex actual = subject
                .withName("NumericMinIndex")
                .withSize(5L)
                .withValue(1D)
                .withContent(null)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "NumericMinIndex")
                .hasFieldOrPropertyWithValue("size", 5L)
                .hasFieldOrPropertyWithValue("value", 0D)
                .hasFieldOrPropertyWithValue("content", new ArrayList<>());
    }
}
