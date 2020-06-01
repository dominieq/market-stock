package org.example.marketstock.models.exchange;

import org.example.marketstock.exceptions.AddingObjectException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.marketstock.models.asset.Commodity;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class CommodityExchange extends Exchange implements Serializable {

    private ArrayList<Commodity> commodities;
    private final ArrayList<String> allResources;

    @Deprecated
    public CommodityExchange() {
        this.commodities = new ArrayList<>();
        this.allResources = new ArrayList<>();
    }

    public CommodityExchange(String country, String city, String address, String currency, double margin,
                             ArrayList<Commodity> commodities, ArrayList<String> allResources) {
        super(country, city, address, currency, margin);

        String name = "Commodity exchange in " + city;

        this.setName(name);
        this.commodities = commodities;
        this.allResources = allResources;

        LOGGER.info("Commodity exchange with name: {} and commodities created.", name);
    }

    @Deprecated
    public void initialize() {
        this.drawValues();
        this.setName("Commodity exchange in " + this.getCity());
        this.initializeAllCommodities();
    }

    /**
     * @return Commodity in a text form.
     * @throws AddingObjectException when there are no resource to choose from.
     */
    @Deprecated
    public String drawResource() throws AddingObjectException {
        if(this.allResources.isEmpty()) {
            throw new AddingObjectException("There are no resources to choose from.");
        } else {
            Random rand = new Random();
            int index = rand.nextInt(allResources.size());

            String commodityRaw = allResources.get(index);
            this.allResources.remove(commodityRaw);
            return commodityRaw;
        }
    }
    
    @Deprecated
    private void initializeAllCommodities() {
        String [] array = new String [] {"Złoto;uncja","Srebro;uncja",
            "Miedź;kilogram","Diament;karat","Żelazo;kilogram","Sól kamienna;kilogram",
            "Saletra;kilogram","Gips;kilogram","Siarka;kilogram","Węgiel kamienny;kilogram",
            "Węgiel brunatny;kilogram","Marmur;kilogram","Proch;gram","Jedwab;kilogram",
            "Bawełna;kilogram","Wełna;kilogram","Skora;kilogram"};

        allResources.addAll(Arrays.asList(array));
    }

    public ArrayList<Commodity> getCommodities() {
        return commodities;
    }

    public ObservableList<Commodity> getCommoditiesObservableArrayList() {
        return FXCollections.observableArrayList(this.commodities);
    }

    public void setCommodities(ArrayList<Commodity> resources) {
        this.commodities = resources;
    }

    public void addResource(Commodity resource) {
        this.commodities.add(resource);
    }

    public void removeResource(Commodity resource) {
        this.commodities.remove(resource);
    }

    public ArrayList<String> getAllResources() {
        return allResources;
    }

    public void addResourceRaw(String resource) {
        this.allResources.add(resource);
    }

    public void removeResourceRaw(String resource) {
        this.allResources.remove(resource);
    }
}
