package org.example.marketstock.models.exchange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.index.Index;

import static java.util.Objects.nonNull;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class StockExchange extends Exchange implements Serializable {

    private final List<Index> indices;
    private final List<Company> companies;

    @JsonIgnore
    private final transient ExecutorService companiesService = Executors.newFixedThreadPool(100);

    public StockExchange(final String name,
                         final String country,
                         final String city,
                         final String address,
                         final String currency,
                         final double margin,
                         final List<Index> indices,
                         final List<Company> companies) {

        super(name, country, city, address, currency, margin);

        this.indices = indices;
        this.companies = companies;
    }

    public void updateIndices() {
        if (nonNull(indices) && !indices.isEmpty()) {
            indices.forEach(index -> index.updateIndex(new ArrayList<>(companies)));
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("country", country)
                .add("city", city)
                .add("address", address)
                .add("currency", currency)
                .add("margin", margin)
                .add("indices", indices)
                .add("companies", companies)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockExchange)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void addCompany(Company company) {
        companies.add(company);
    }

    public void removeCompany(Company company) {
        companies.remove(company);
    }

    public List<Index> getIndices() {
        return indices;
    }

    public void addIndex(Index index) {
        indices.add(index);
    }

    public void removeIndex(Index index) {
        indices.remove(index);
    }

    public ExecutorService getCompaniesService() {
        return companiesService;
    }
}
