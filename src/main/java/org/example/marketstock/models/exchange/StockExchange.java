package org.example.marketstock.models.exchange;

import org.example.marketstock.exceptions.AddingObjectException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.index.Index;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class StockExchange extends Exchange implements Serializable {

    private ArrayList<Index> indices;
    private ArrayList<Company> companies;
    private final ArrayList<String> allIndices;

    @Deprecated
    public StockExchange() {
        this.indices = new ArrayList<>();
        this.allIndices = new ArrayList<>();
        this.companies = new ArrayList<>();
    }

    public StockExchange(String country, String city, String address, String currency, double margin,
                         ArrayList<Index> indices, ArrayList<String> allIndices, ArrayList<Company> companies) {
        super(country, city, address, currency, margin);

        String name = "Stock exchange in " + city;

        this.setName(name);
        this.indices = indices;
        this.companies = companies;
        this.allIndices = allIndices;

        LOGGER.info("Stock exchange with name: {}, companies and indices created.", name);
    }

    @Deprecated
    public void initialize() {
        this.drawValues();
        this.setName("Stock exchange in " + this.getCity());
        this.initializeAllIndices();
    }

    public void updateIndices() {
        if (!this.indices.isEmpty()) {
            for (Index index : this.indices) {
                index.updateCompanies(this.companies);
            }
        }
    }
    
    /**
     * @return Index in a text form.
     * @throws AddingObjectException when there are no indices to choose from.
     */
    @Deprecated
    public String drawIndex() throws AddingObjectException {
        if(allIndices.isEmpty()) {
            throw new AddingObjectException("Nie ma nazw");
        } else {
            Random rand = new Random();
            int index = rand.nextInt(this.allIndices.size());

            String indexRaw = this.allIndices.get(index);
            this.removeIndexRaw(indexRaw);
            return indexRaw;
        }
    } 

    @Deprecated
    private void initializeAllIndices() {
        String [] array = new String [] {"5 best companies;5;max",
            "5 worst companies;5;min"};

        this.getAllIndices().addAll(Arrays.asList(array));
    }

    public ArrayList<Company> getCompanies() {
        return this.companies;
    }

    public ObservableList<Company> getCompaniesObservableArrayList() {
        return FXCollections.observableArrayList(this.companies);
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public void addCompany(Company company) {
        this.companies.add(company);
    }

    public void removeCompany(Company company) {
        this.companies.remove(company);
    }

    public ArrayList<Index> getIndices() {
        return this.indices;
    }

    public ObservableList<Index> getIndicesObservableArrayList() {
        return FXCollections.observableArrayList(this.indices);
    }

    public void setIndices(ArrayList<Index> indices) {
        this.indices = indices;
    }

    public void addIndex(Index index) {
        this.indices.add(index);
    }

    public void removeIndex(Index index) {
        this.indices.remove(index);
    }

    public ArrayList<String> getAllIndices() {
        return this.allIndices;
    }

    public void addIndexRaw(String indexRaw) {
        this.allIndices.add(indexRaw);
    }

    public void removeIndexRaw(String indexRaw) {
        this.allIndices.remove(indexRaw);
    }
}
