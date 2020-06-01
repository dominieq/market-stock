package org.example.marketstock.models.asset;

import java.io.Serializable;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import org.example.marketstock.models.exchange.CommodityExchange;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class Commodity extends Asset implements Serializable {
    
    private String unitOfTrading;
    private String currency;
    private CommodityExchange commodityExchange;

    @Deprecated
    public Commodity() {
        this.unitOfTrading = "";
        this.currency = "";
    }

    @Deprecated
    public void initialize(String kombo, CommodityExchange commodityExchange) {
        String[] czesci = kombo.split(";");
        String nazwa = czesci[0];
        String jh = czesci[1];
        this.setName(nazwa);
        this.setUnitOfTrading(jh);
        this.setCurrency(this.drawCurrency());
        
        Random rand = new Random ();
        this.setCurrentRate((double) rand.nextInt(5000) + rand.nextDouble());
        this.setMaxRate(0.0);
        this.setMinRate(0.0);
        
        this.commodityExchange = commodityExchange;
        this.setExchangeMargin(commodityExchange.getMargin());
    }

    @Deprecated
    public String drawCurrency() {
        String currency;
        String [] strings = new String[] {"Grosz Krakowski","Grosz Praski",
            "Marka","Liwr","Funt Szterling","Floren","Gulden","Talar","Frank",
            "Korona Novigradzkie","Oren","Rupia","Jen","Szekel","Dukat"};
        ArrayList<String> stringArrayList = new ArrayList<>(Arrays.asList(strings));

        Random rand = new Random();
        int index = rand.nextInt(stringArrayList.size());
        currency = stringArrayList.get(index);
        return currency;
    }

    public String getUnitOfTrading() {
        return this.unitOfTrading;
    }

    public void setUnitOfTrading(String unitOfTrading) {
        this.unitOfTrading = unitOfTrading ;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CommodityExchange getCommodityExchange() {
        return commodityExchange;
    }

    public void setCommodityExchange(CommodityExchange commodityExchange) {
        this.commodityExchange = commodityExchange;
    }
}
