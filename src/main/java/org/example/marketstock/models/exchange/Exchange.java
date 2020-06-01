package org.example.marketstock.models.exchange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public abstract class Exchange implements Serializable {

    protected static Logger LOGGER = LogManager.getLogger(Exchange.class);
    private String name;
    private String country;
    private String city;
    private String address;
    private String currency;
    private double margin;

    @Override
    public String toString() {
        return this.name;
    }

    @Deprecated
    public Exchange() {
        this.name = "";
        this.country = "";
        this.city = "";
        this.address = "";
        this.currency = "";
        this.margin = 0.0;

        LOGGER.warn("Exchange used a deprecated constructor.");
    }

    public Exchange(String country, String city, String address, String currency, double margin) {
        this.country = country;
        this.city = city;
        this.address = address;
        this.currency = currency;
        this.margin = margin;

        LOGGER.info("Exchange with values country: {}, city: {}, address: {}, currency: {}, margin: {} created.",
                new Object[] {country, city, address, currency, margin});
    }

    @Deprecated
    public void drawValues() {
        this.setAddress(draw(new String [] {"Flat Earth Society St.",
            "Beneath The Tree Alley","Bad Moon Rising Alley","Up Around the Bend St.",
            "Lover's Cross","Southern Cross","Good Morning St.","Highway to Hell",
            "Tunnel of Love","Love Street","Positively 4th Street","22 Acacia Avenue",
            "Dead End Street","65 Thunder Kiss Street","Fighting Man Street",
            "59 Bridge Street","Itchycoo Park"}));
        this.setCountry(draw(new String [] {"Zasiedmiogórogród","Redania","Rivia",
            "Kaedwen","Temeria","Lyria","Dekuuna","Noveria","Tuchanka","Irune",
            "Palaven","Rannoch","Thessia","Irune","Elysium","Feros","Tatooine",
            "Coruscant","Naboo","Endor"}));
        this.setCity(draw(new String [] {"Anvil","Bravil","Bruma","Cheydinhal",
            "Chorrol","Imperial City","Kvatch","Layawiin","Skingrad","Dawnstar",
            "Falkreath","Markarth","Morthal","Riften","Windhelm","Wyzima","Novigrad",
            "Flotsam","Oxenfurt","Tretogor","Mahakam","Brugge","Gors Velen",
            "Ellander","Toussaint","Metinna","Geso","Attre","Sodden","Vengerberg",
            "Valyria","Asshai","Astapor","Lys","Pentos","Volantis","Port Lotosów",
            "Meereen","Yunkai","Tyria"}));
        this.setCurrency(draw(new String [] {"Grosz Krakowski","Grosz Praski","Marka",
            "Liwr","Funt Szterling","Floren","Gulden","Talar","Frank","Korona Novigradzkie",
            "Oren","Rupia","Jen","Szekel","Dukat"}));

        Random rand = new Random();
        double upperBoundary = 0.15;
        double lowerBoundary = 0.01;
        this.margin = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();
    }

    @Deprecated
    private String draw(String [] array) {
        Random rand =  new Random();
        ArrayList<String> allTexts = new ArrayList<>(Arrays.asList(array));

        return allTexts.get(rand.nextInt(allTexts.size()));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public String getCountry(){
        return this.country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getCurrency(){
        return this.currency;
    }

    public void setCurrency(String currency){
        this.currency = currency;
    }

    public double getMargin() {
        return this.margin;
    }

    public void setMargin(double margin){
        this.margin = margin;
    }
}
