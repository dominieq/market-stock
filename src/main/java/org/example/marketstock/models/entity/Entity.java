package org.example.marketstock.models.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.app.MarketApp;
import org.example.marketstock.exceptions.BuyingException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.*;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public abstract class Entity implements Serializable {

    protected static Logger LOGGER = LogManager.getLogger(Entity.class);
    private String firstName;
    private String lastName;
    private double budget;
    private final Briefcase briefcase;
    @Deprecated
    private transient volatile Long threadId;
    @Deprecated
    private transient volatile MarketApp marketApp;

    @Override
    public String toString() {
        return this.firstName + this.lastName + " with budget " + this.budget;
    }

    @Deprecated
    public Entity() {
        this.firstName = "";
        this.lastName = "";
        this.budget = 0.0;
        this.briefcase = new Briefcase();

        LOGGER.warn("Entity used deprecated constructor.");
    }

    public Entity(String firstName, String lastName, double budget) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.budget = budget;
        this.briefcase = new Briefcase();

        LOGGER.info("Entity with values first name: {}, last name: {} and budget: {} created.",
                new Object[]{firstName, lastName, budget});
    }

    public Entity(String firstName, String lastName, double budget, Briefcase briefcase) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.budget = budget;
        this.briefcase = briefcase;

        LOGGER.info("Entity with values first name: {}, last name: {}, budget: {} and briefcase created",
                new Object[]{firstName, lastName, budget});
    }
    
    /**
     * An entity finds an asset from their briefcase that has the highest current rate
     * and then sells a random number of the chosen asset.
     */
    public synchronized void chooseAndSellAsset() {
        if (!this.getBriefcase().getAssets().isEmpty()) {
            Asset maxAsset = this.getBriefcase().getAssets().get(0);

            for (Asset asset : this.getBriefcase().getAssets()) {
                if (maxAsset.getCurrentRate() < asset.getCurrentRate()) {
                    maxAsset = asset;
                }
            }

            int index = this.getBriefcase().getAssets().indexOf(maxAsset);
            int numberOfAsset = this.getBriefcase().getNumbersOfAssets().get(index);

            Random rand = new Random();
            int numberOfAssetToSell = rand.nextInt(numberOfAsset);

            try {
                this.sellAsset(maxAsset, numberOfAssetToSell, maxAsset.getExchangeMargin());
            } catch (BuyingException exception) {
                LOGGER.error(exception.getMessage(), exception);
            }
        } else {
            LOGGER.info("{} {} has no assets in their briefcase.", new Object[]{this.firstName, this.lastName});
        }
    }
    
    /**
     * An entity finds an asset from all markets that has the lowest current rate
     * and then buys a random number of the chosen asset.
     */
    public synchronized void chooseAndBuyAsset() {
        Random rand = new Random();
        int indexOfAnAssetToBuy = rand.nextInt(3);

        switch (indexOfAnAssetToBuy) {
            case 0: {
                if(!this.marketApp.getShares().isEmpty()) {
                    Share share = chooseShare(new ArrayList<>(this.marketApp.getShares()));
                    double boundary = this.budget / share.getCurrentRate();

                    if (boundary > 0) {
                        int number = rand.nextInt((int) boundary);
                        this.buyAsset(share, number);
                    } else {
                        LOGGER.info("{} {} doesn't have any money.", new Object[]{this.firstName, this.lastName});
                    }
                }

                break;
            }
            case 1: {
                if(!this.marketApp.getInvestmentUnits().isEmpty()) {
                    InvestmentUnit investmentUnit =
                            chooseInvestmentUnit(new ArrayList<>(this.marketApp.getInvestmentUnits()));
                    double boundary = this.budget / investmentUnit.getCurrentRate();

                    if (boundary > 0) {
                        int number = rand.nextInt((int) boundary);
                        this.buyAsset(investmentUnit, number);
                    } else {
                        LOGGER.info("{} {} doesn't have any money.", new Object[]{this.firstName, this.lastName});
                    }
                }

                break;
            }

            case 2: {
                if (!this.marketApp.getCurrencies().isEmpty()) {
                    Currency currency = chooseCurrency(new ArrayList<>(this.marketApp.getCurrencies()));
                    double boundary = this.budget / currency.getCurrentRate();

                    if (boundary > 0) {
                        int number = rand.nextInt((int) boundary);
                        this.buyAsset(currency, number);
                    } else {
                        LOGGER.info("{} {} doesn't have any money.", new Object[]{this.firstName, this.lastName});
                    }
                }

                break;
            }
            case 3: {
                if(!this.marketApp.getCommodities().isEmpty()) {
                    Commodity commodity = chooseCommodity(new ArrayList<>(this.marketApp.getCommodities()));
                    double boundary = this.budget / commodity.getCurrentRate();

                    if (boundary > 0) {
                        int number = rand.nextInt((int) boundary);
                        this.buyAsset(commodity, number);
                    } else {
                        LOGGER.info("{} {} doesn't have any money.", new Object[]{this.firstName, this.lastName});
                    }
                }

                break;
            }
        }
    }
    
    /**
     * An entity seeks for a company share with the lowest current rate.
     * @param shares A list of all company shares available in simulation.
     * @return A company share with the lowest current rate.
     * @deprecated This method should be a part of the simulation.
     */
    @Deprecated
    public Share chooseShare(ArrayList<Share> shares) {
        Share minShare = shares.get(0);

        for (Share share : shares) {
            if(minShare.getCurrentRate() > share.getCurrentRate()) {
                minShare = share;
            }
        }

        return minShare;
    }
    
    /**
     * An entity seeks for an investment unit with the lowest current rate.
     * @param investmentUnits A list of all investment units available in simulation.
     * @return An investment unit with the lowest current rate.
     * @deprecated This method should be a part of the simulation.
     */
    @Deprecated
    public InvestmentUnit chooseInvestmentUnit(ArrayList<InvestmentUnit> investmentUnits) {
        InvestmentUnit minInvestmentUnit = investmentUnits.get(0);

        for (InvestmentUnit investmentUnit : investmentUnits) {
            if(minInvestmentUnit.getCurrentRate() > investmentUnit.getCurrentRate()) {
                minInvestmentUnit = investmentUnit;
            }
        }

        return minInvestmentUnit;
    }

    /**
     * An entity seeks for a currency with the lowest current rate.
     * @param currencies A list of all currencies available in simulation.
     * @return A currency with the lowest current rate.
     * @deprecated This method should be a part of the simulation.
     */
    @Deprecated
    public Currency chooseCurrency(ArrayList<Currency> currencies) {
        Currency minCurrency = currencies.get(0);

        for(Currency currency : currencies) {
            if(minCurrency.getCurrentRate() > currency.getCurrentRate()) {
                minCurrency = currency;
            }
        }

        return minCurrency;
    }
    
    /**
     * An entity seeks for a commodity with the lowest current rate.
     * @param commodities A list of all commodities available in simulation.
     * @return A commodity with the lowest current rate.
     * @deprecated This method should be a part of the simulation.
     */
    @Deprecated
    public Commodity chooseCommodity(ArrayList<Commodity> commodities) {
        Commodity minCommodity = commodities.get(0);

        for(Commodity commodity : commodities) {
            if(minCommodity.getCurrentRate() > commodity.getCurrentRate()) {
                minCommodity = commodity;
            }
        }

        return minCommodity;
    }
    
    /**
     * An entity buys a number of chosen asset. The price of those assets is subtracted from their budget.
     * The characteristics of a chosen asset are updated accordingly to this transaction.
     * @param asset Asset from the simulation.
     * @param number Number of a chosen asset.
     */
    public synchronized void buyAsset(Asset asset, Integer number) {
        double price = asset.getCurrentRate() * number;
        this.budget -= price;
        this.briefcase.addAsset(asset, number);

        LOGGER.info("{} {} bought {} of {} for {}.",
                new Object[]{this.firstName, this.lastName, number, asset.getName(), price});

        double newRate = asset.getCurrentRate() + asset.getCurrentRate() * 0.05;
        asset.updateRate(newRate);

        if (asset instanceof Share) {
            int prevNumberOfAssets = ((Share) asset).getCompany().getNumberOfAssets();
            ((Share) asset).getCompany().setNumberOfAssets(prevNumberOfAssets - number);
            ((Share) asset).getCompany().updateCurrentRate(price, number);
            ((Share) asset).getCompany().getStock().updateIndices();
        } 
    }
    
    /**
     * An entity sells a number of a chosen asset from their briefcase.
     * The price of those assets is added to their budget.
     * The characteristics of a chosen asset are updated accordingly to this transaction.
     * @param asset Asset from entity's briefcase.
     * @param number Number of a chosen asset.
     * @param margin Value of a margin from market stock.
     * @throws BuyingException when entity sells too much.
     */
    public synchronized void sellAsset(Asset asset, Integer number, Double margin) throws BuyingException {
        try {
            double price = asset.getCurrentRate() * number;
            double priceMinusMargin = price - price * margin;

            this.budget += priceMinusMargin;
            this.briefcase.deleteAsset(asset, number);

            LOGGER.info("{} {} sold {} of {} for {}.",
                    new Object[]{this.firstName, this.lastName, number, asset.getName(), priceMinusMargin});

            double newRate = asset.getCurrentRate() - asset.getCurrentRate() * 0.05;
            asset.updateRate(newRate);

            if (asset instanceof Share) {
                ((Share) asset).getCompany().updateCurrentRate(price, number);
                ((Share) asset).getCompany().getStock().updateIndices();
            }
        } catch (BuyingException exception) {
            LOGGER.info("{} {} wanted to sell too much of {}.",
                    new Object[]{this.firstName, this.lastName, asset.getName()});

            throw new BuyingException("Trying to sell too much.");
        }
    }
    
    /**
     * An entity adds a random amount of money to their budget.
     * The amount is drawn from a range between 20 000 and 50 000.
     */
    public synchronized void increaseBudget() {
        Random rand = new Random();
        double upperBoundary = 50000;
        double lowerBoundary = 20000;
        double value = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();
        this.budget = value;

        LOGGER.info("{} {} added {} to their budget ({}).",
                new Object[]{this.firstName, this.lastName, value, this.budget});
    }    

    @Deprecated
    public void drawAndSetName() {
        Random rand = new Random();

        String [] firstNamesStringArray = new String [] {"Cerys","Crach","Myszowór","Hjalmar",
            "Bran","Lugos","Django","Leo","Abel","Dagobert","Detmold","Dorregray",
            "Fringilla","Adolf","Józef","Benito","Ernesto","Sabrina","Visenna",
            "Aerandir","Aerin","Balan","Ban","Baldir","Baldis","Belladonna",
            "Bellissima","Beril","Brodda","Brytta","Caranthir","Camlost","Dagnir",
            "Damrod","Dinithel","Dushgoi","Hador","Hodor","Halbarad","Hathol",
            "Othrondor","Orlin","Roran","Brom","Murtagh","Arya","Nasuada",
            "Galbatorix","Saphira","Glaedr","Shruiken","Firnen","Geralt","Dijkstra",
            "Vernon","Jarostryj","Lasota","Nawuj","Niegodomaa","Jon"};

        String [] lastNamesStringArray = new String [] {"Wstydliwy","Kędzierzawy","Gnuśny",
            "Sinozęby","Widłobrody","Tłuścioch","Zajęcza Stopa","Bez Kości","Bezradny",
            "Miękki Miecz","Cudaczny","Świniopas","Bezsilny","Rudobrody","Świnka",
            "herbu Ciołek","Zajączek","herbu Jelita","herbu Gryf","herbu Zęby",
            "Targaryen","Arryn","Baratheon","Greyjoy","Lannister","Martell","Stark",
            "Tully","Tyrell","Allyrion","Blackfyre","Blackwood","Bolton","Bracken",
            "Cassel","Celtigar","Cerwyn","Clegane","Connington","Dayne","Dondarrion",
            "Durstin","Estermont","Frey","Glover","Hightower","Yronwood","Whent",
            "Westerling","Umber","Tarly","Swyft","Payne","Mallister","Snow","Sand",
            "Flowers","Stone","Redwyne","Reed"};

        ArrayList<String> firstNamesArrayList = new ArrayList<>(Arrays.asList(firstNamesStringArray));
        this.setFirstName(firstNamesArrayList.get( rand.nextInt(firstNamesArrayList.size()) ));

        ArrayList<String> lastNamesArrayList = new ArrayList<>(Arrays.asList(lastNamesStringArray));
        this.setLastName(lastNamesArrayList.get( rand.nextInt(lastNamesArrayList.size()) ));
    }

    @Deprecated
    public Double drawBudget() {
        Random rand = new Random();

        double upperBoundary = 1000000.0;
        double lowerBoundary = 10000.0;
        return (lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble());
    }

    public String getFirstName() {
        return this.firstName;
    }

    public StringProperty getFirstNameProperty() {
        return new SimpleStringProperty(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName ;
    }

    public String getLastName() {
        return this.lastName;
    }

    public StringProperty getLastNameProperty() {
        return new SimpleStringProperty(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Briefcase getBriefcase() {
        return briefcase;
    }

    @Deprecated
    public Long getThreadId() {
        return threadId;
    }

    @Deprecated
    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    @Deprecated
    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
