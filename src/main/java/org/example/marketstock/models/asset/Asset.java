package org.example.marketstock.models.asset;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.entity.InvestmentFund;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Commodity.class, name = "commodity"),
        @JsonSubTypes.Type(value = Company.class, name = "company"),
        @JsonSubTypes.Type(value = Currency.class, name = "currency"),
        @JsonSubTypes.Type(value = InvestmentFund.class, name = "investmentFund")
})
public interface Asset {

    String getName();
    double getCurrentRate();
    double getMinRate();
    double getMaxRate();
    List<Double> getRateChanges();
    double getMargin();
    double updateRate(double rate);
}
