package org.example.marketstock.models.index;

import org.example.marketstock.models.index.builder.NumericMaxIndexBuilder;
import org.example.marketstock.models.index.builder.NumericMinIndexBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexIntegrationTest {

    @Test
    public void should_store_unique_indices_in_set() {

        // given
        final Index maxIndex1 = NumericMaxIndexBuilder.builder()
                .withName("NumericMaxIndex1")
                .withSize(2)
                .build();

        final Index minIndex1 = NumericMinIndexBuilder.builder()
                .withName("NumericMinIndex1")
                .withSize(2)
                .build();

        final Set<Index> set = new HashSet<>();
        set.add(maxIndex1);
        set.add(minIndex1);

        assertThat(set).containsOnly(maxIndex1, minIndex1);

        // when
        final Index maxIndex2 = NumericMaxIndexBuilder.builder()
                .withName("NumericMaxIndex2")
                .withSize(3)
                .build();

        final Index minIndex2 = NumericMinIndexBuilder.builder()
                .withName("NumericMinIndex2")
                .withSize(3)
                .build();

        // then
        assertThat(set.contains(maxIndex2)).isTrue();
        assertThat(set.contains(minIndex2)).isTrue();
    }
}
