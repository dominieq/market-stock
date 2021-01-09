package org.example.marketstock.models.asset;

import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class CountableAssetTest {

    private CountableAsset subject;

    @ParameterizedTest
    @CsvSource({
            "100000, 10000",
            "200000, 20000",
            "150000, 50000",
            "1000000, 2000000",
            "45054000, 56065000"
    })
    public void should_increase_number_of_asset(int numberOfAssets, int addend) {

        // given
        subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withNumberOfAssets(numberOfAssets)
                .build();

        // when
        subject.increaseNumberOfAssets(addend);

        // then
        assertThat(subject.getNumberOfAssets()).isEqualTo(numberOfAssets + addend);
    }

    @ParameterizedTest
    @CsvSource({
            "100000, 10000",
            "200000, 20000",
            "150000, 50000",
            "1000000, 1000000",
            "45054000, 45054000"
    })
    public void should_decrease_number_of_assets(int numberOfAssets, int subtrahend) {

        // given
        subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withNumberOfAssets(numberOfAssets)
                .build();

        // when
        subject.decreaseNumberOfAssets(subtrahend);

        // then
        assertThat(subject.getNumberOfAssets()).isEqualTo(numberOfAssets - subtrahend);
    }

    @ParameterizedTest
    @CsvSource({
            "100, 200",
            "45000, 50000",
            "1000000, 2000000",
            "5005, 6006",
            "121, 122"
    })
    public void should_not_decrease_number_of_assets(int numberOfAssets, int subtrahend) {

        // given
        subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withNumberOfAssets(numberOfAssets)
                .build();

        // when
        subject.decreaseNumberOfAssets(subtrahend);

        // then
        assertThat(subject.getNumberOfAssets()).isEqualTo(numberOfAssets);
    }
}
