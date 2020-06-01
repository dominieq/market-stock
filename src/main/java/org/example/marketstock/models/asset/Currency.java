package org.example.marketstock.models.asset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.marketstock.models.exchange.CurrencyExchange;

/**
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class Currency extends Asset implements Serializable {
    
    private Currency comparisonCurrency;
    private ArrayList<String> countriesArrayList;
    private CurrencyExchange currencyExchange;

    @Deprecated
    public Currency() {
       this.countriesArrayList = new ArrayList<>();
       this.comparisonCurrency = null;
    }

    @Deprecated
    public void initializeValues(String name, CurrencyExchange currencyExchange) {
        Random rand = new Random ();
        double value = (double) rand.nextInt(10) + rand.nextDouble();
        
        this.setName(name);
        this.setCurrentRate(value);
        this.setMaxRate(0.0);
        this.setMinRate(0.0);
        this.addRate(value);
        this.comparisonCurrency = new Currency();
        this.initializeUberCurrency();
        this.initializeCountriesArrayList();
        this.setCurrencyExchange(currencyExchange);
        this.setExchangeMargin(currencyExchange.getMargin());
    }

    @Deprecated
    public void initializeUberCurrency() {
        this.getComparisonCurrency().setName("Denar");
        this.getComparisonCurrency().setCurrentRate(1.0);
        this.getComparisonCurrency().setMaxRate(1.0);
        this.getComparisonCurrency().setMinRate(1.0);
        this.getComparisonCurrency().setComparisonCurrency(null);

        String [] countries = new String[] {"Zasiedmiogórogród","Redania",
            "Rivia","Kaedwen","Temeria","Lyria","Dekuuna","Noveria","Tuchanka",
            "Irune","Palaven","Rannoch","Thessia","Irune","Elysium","Feros","Tatooine",
            "Coruscant","Naboo","Endor"};

        this.getComparisonCurrency().getCountriesArrayList().addAll(Arrays.asList(countries));
    }

    @Deprecated
    public void initializeCountriesArrayList() {
        Random rand = new Random();
        String [] countries = new String[] {"Zasiedmiogórogród","Redania",
            "Rivia","Kaedwen","Temeria","Lyria","Dekuuna","Noveria","Tuchanka",
            "Irune","Palaven","Rannoch","Thessia","Irune","Elysium","Feros","Tatooine",
            "Coruscant","Naboo","Endor"};

        ArrayList<String> allCountries = new ArrayList<>(Arrays.asList(countries));
        int number = rand.nextInt(allCountries.size()) + 1;
        int indeks;
             
        for (int i = 0; i < number; i++) {
            indeks = rand.nextInt(allCountries.size());

            this.addCountry(allCountries.get(indeks));
            allCountries.remove(indeks);
        }
    }

    public ArrayList<String> getCountriesArrayList() {
        return this.countriesArrayList;
    }

    public ObservableList<String> getCountriesObservableArrayList() {
        return FXCollections.observableArrayList(this.countriesArrayList);
    }

    public void setCountriesArrayList(ArrayList<String> countriesArrayList) {
        this.countriesArrayList = countriesArrayList;
    }

    public void addCountry(String country) {
        this.countriesArrayList.add(country);
    }

    public void removeCountry(String country) {
        this.countriesArrayList.remove(country);
    }

    public Currency getComparisonCurrency() {
        return this.comparisonCurrency;
    }

    public void setComparisonCurrency(Currency currency) {
        this.comparisonCurrency = currency;
    }

    public CurrencyExchange getCurrencyExchange() {
        return currencyExchange;
    }

    public void setCurrencyExchange(CurrencyExchange currencyExchange) {
        this.currencyExchange = currencyExchange;
    }
}
