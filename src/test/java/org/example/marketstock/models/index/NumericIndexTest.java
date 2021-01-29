package org.example.marketstock.models.index;

import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.example.marketstock.models.index.builder.NumericMaxIndexBuilder;
import org.example.marketstock.models.index.builder.NumericMinIndexBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

public class NumericIndexTest {

    @Test
    public void should_update_index_and_calculate_value_for_max_implementation() {

        // given
        final NumericIndex subject = NumericMaxIndexBuilder.builder()
                .withName("NumericMaxIndex")
                .withSize(2)
                .build();

        final List<Asset> content = getAssets();

        // when
        subject.updateIndex(content);

        // then
        assertThat(subject.getContent())
                .hasSize(2)
                .containsOnly(content.get(1), content.get(2))
                .contains(content.get(2), atIndex(0))
                .contains(content.get(1), atIndex(1))
                .doesNotContain(content.get(0));

        assertThat(subject.getValue()).isEqualTo(10D);
    }

    @Test
    public void should_update_index_and_calculate_value_for_min_implementation() {

        // given
        final NumericIndex subject = NumericMinIndexBuilder.builder()
                .withName("NumericMinIndex")
                .withSize(2)
                .build();

        final List<Asset> content = getAssets();

        // when
        subject.updateIndex(content);

        // then
        assertThat(subject.getContent())
                .hasSize(2)
                .containsOnly(content.get(0), content.get(1))
                .contains(content.get(0), atIndex(0))
                .contains(content.get(1), atIndex(1))
                .doesNotContain(content.get(2));

        assertThat(subject.getValue()).isEqualTo(6D);
    }

    private List<Asset> getAssets() {
        final List<Asset> assets = new ArrayList<>();

        for (double i = 2D; i <= 6D; i += 2D) {
            assets.add(CompanyBuilder.builder()
                    .withName("TestCompany" + (i / 2D))
                    .withCurrentRate(i)
                    .build()
            );
        }

        return assets;
    }
}
