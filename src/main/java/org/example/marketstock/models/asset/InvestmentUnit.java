package org.example.marketstock.models.asset;

import org.example.marketstock.models.entity.InvestmentFund;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class InvestmentUnit extends Asset implements Serializable {
    
    private InvestmentFund investmentFund;

    @Deprecated
    public InvestmentUnit() {}
    
    @Deprecated
    public void initialize(String companyName, InvestmentFund investmentFund) {
        Random rand = new Random();
        double upperBoundary = 7.0;
        double lowerBoundary = 1.0;
        double currentRate = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        this.setName(companyName + "'s investment fund");
        this.setCurrentRate(currentRate);
        this.setMaxRate(0.0);
        this.setMinRate(0.0);
        this.addRate(currentRate);
        this.setInvestmentFund(investmentFund);
    }   

    public InvestmentFund getInvestmentFund() {
        return investmentFund;
    }

    public void setInvestmentFund(InvestmentFund investmentFund) {
        this.investmentFund = investmentFund;
    }
}
