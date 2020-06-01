package org.example.marketstock.models.index;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.models.company.Company;

/**
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class Index implements Serializable {
    
    private static final Logger LOGGER = LogManager.getLogger(Index.class);
    private String name;
    private int maxSize;
    private String accessCondition;
    private ArrayList<Company> companies;
    private double value;

    @Override
    public String toString() {
        return this.name;
    }

    @Deprecated
    public Index(String names) {
        String [] parts = names.split(";");

        this.name = parts[0];
        this.maxSize = Integer.parseInt(parts[1]);
        this.accessCondition = parts[2];
        this.companies = new ArrayList<>();
        this.value = 0.0;

        LOGGER.warn("Index used a deprecated constructor.");
    }

    public Index(String name, int maxSize, String accessCondition) {
        this.name = name;
        this.maxSize = maxSize;
        this.accessCondition = accessCondition;
        this.companies = new ArrayList<>();
        this.value = 0.0;

        LOGGER.info("{} was created.", this.toString());
    }

    public Index(String name, int maxSize, String accessCondition, ArrayList<Company> companies) {
        this.name = name;
        this.maxSize = maxSize;
        this.accessCondition = accessCondition;

        this.updateCompanies(companies);

        LOGGER.info("{} was created with companies:\t{}.", new Object[]{this.toString(), this.companies});
    }

    public Index(String name, int maxSize, String accessCondition, ArrayList<Company> companies, double value) {
        this.name = name;
        this.maxSize = maxSize;
        this.accessCondition = accessCondition;
        this.companies = companies;
        this.value = value;

        LOGGER.info("{} was created with value {} and companies:\t{}.",
                new Object[]{this.toString(), this.value, this.companies});
    }

    /**
     * Index updates a list of it's companies based on it's max size and access condition.
     * @param companies ArrayList of Companies from stock exchange that owns this index.
     */
    public void updateCompanies(ArrayList<Company> companies) {
        this.companies = new ArrayList<>();

        if (!companies.isEmpty()) {
            if(this.accessCondition.equals("max")) {
                double max = Double.MIN_VALUE;
                Company maxCompany = companies.get(0);

                for (int i = 0; i < this.maxSize; i++) {
                    for (Company company : companies) {
                        if(company.getCurrentRate() > max && !this.companies.contains(company)) {
                            max = company.getCurrentRate();
                            maxCompany = company;
                        }
                    }

                    if (maxCompany != null) {
                        this.companies.add(maxCompany);
                    }

                    max = Double.MIN_VALUE;
                    maxCompany = null;
                }
            } else if (this.accessCondition.equals("min")) {
                double min = Double.MAX_VALUE;
                Company minCompany = companies.get(0);

                for (int i = 0; i < this.maxSize; i++) {
                    for (Company company : companies) {
                        if(company.getCurrentRate() < min && !this.companies.contains(company)) {
                            min = company.getCurrentRate();
                            minCompany = company;
                        }
                    }

                    if (minCompany != null) {
                        this.companies.add(minCompany);
                    }

                    min = Double.MAX_VALUE;
                    minCompany = null;
                }
            }
            LOGGER.info("New companies in {}:\t{}.", new Object[]{this.toString(), this.companies.toArray()});

            this.updateValue();
        }
    }

    /**
     * Index updates it's value that is based on companies from this index.
     */
    public void updateValue() {
        this.value = 0.0;

        if (this.companies != null && !this.companies.isEmpty()) {
            for (Company company : this.companies) {
                this.value += company.getCurrentRate();
            }
        }

        LOGGER.info("{} updated value to {}.", new Object[]{this.toString(), this.value});
    }

    public String getName() {
        return this.name;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getAccessCondition() {
        return accessCondition;
    }

    public void setAccessCondition(String accessCondition) {
        this.accessCondition = accessCondition;
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double x) {
        this.value = x;
    }

    public ArrayList<Company> getCompanies() {
        return this.companies;
    }

    public ObservableList<Company> getCompanyObservableArrayList() {
        return FXCollections.observableArrayList(this.companies);
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }
}
