package org.example.marketstock.models.exchange;

import org.example.marketstock.exceptions.AddingObjectException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.marketstock.models.asset.Currency;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class CurrencyExchange extends Exchange implements Serializable {

    private ArrayList<Currency> currencies;
    private final ArrayList<String> allCurrencies;

    @Deprecated
    public CurrencyExchange() {
        this.currencies = new ArrayList<>();
        this.allCurrencies = new ArrayList<>();
    }

    public CurrencyExchange(String country, String city, String address, String currency, double margin,
                            ArrayList<Currency> currencies, ArrayList<String> allCurrencies) {
        super(country, city, address, currency, margin);

        String name = "Currency exchange in " + city;

        this.setName(name);
        this.currencies = currencies;
        this.allCurrencies = allCurrencies;

        LOGGER.info("Currency exchange with name: {} and currencies created.", name);
    }

    @Deprecated
    public void initialize() {
        this.drawValues();
        this.setName("Currency exchange in " + this.getCity());
        this.initializeAllCurrencies();
    }
    
    /**
     * @return Resource in a text form.
     * @throws AddingObjectException when there are no currencies to choose from.
     */
    @Deprecated
    public String drawCurrency() throws AddingObjectException {
        if(this.allCurrencies.isEmpty()) {
            throw new AddingObjectException("There are no resources to choose from.");
        } else {
            Random rand = new Random();
            int index = rand.nextInt(allCurrencies.size());

            String currencyRaw = allCurrencies.get(index);
            this.allCurrencies.remove(currencyRaw);
            return currencyRaw;
        }
    }

    @Deprecated
    private void initializeAllCurrencies() {
        String [] array = new String[] {"Grosz Krakowski","Grosz Praski",
            "Marka","Liwr","Funt Szterling","Floren","Gulden","Talar","Frank",
            "Korona Novigradzkie","Oren","Rupia","Jen","Szekel","Dukat"};

        allCurrencies.addAll(Arrays.asList(array));
    }

    public ArrayList<Currency> getCurrencies() {
        return this.currencies;
    }

    public ObservableList<Currency> getCurrenciesObservableArrayList() {
        return FXCollections.observableArrayList(this.currencies);
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }

    public void addCurrency(Currency currency) {
        currencies.add(currency);
    }

    public void removeCurrency(Currency currency) {
        currencies.remove(currency);
    }

    public ArrayList<String> getAllCurrencies() {
        return allCurrencies;
    }

    public void addCurrencyRaw(String currencyRaw) {
        this.allCurrencies.add(currencyRaw);
    }

    public void removeCurrencyRaw(String currencyRaw) {
        this.allCurrencies.remove(currencyRaw);
    }
}
