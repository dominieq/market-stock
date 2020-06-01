package org.example.marketstock.models.asset;

import org.example.marketstock.models.company.Company;

import java.io.Serializable;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class Share extends Asset implements Serializable {
    
    private Company company;

    @Deprecated
    public Share() {}

    @Deprecated
    public void initialize(String companyName, Double currentRate, Company company) {
        this.setName(companyName + "'s share");
        this.setCurrentRate(currentRate);
        this.setMaxRate(0.0);
        this.setMinRate(0.0);
        this.addRate(currentRate);
        this.setCompany(company);
        this.setExchangeMargin(company.getStock().getMargin());
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}