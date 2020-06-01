package org.example.marketstock.fxml;

import org.example.marketstock.app.MarketApp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import org.example.marketstock.models.asset.Commodity;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.asset.Share;
import org.example.marketstock.models.asset.InvestmentUnit;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.models.index.Index;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class RootLayoutController {

    private static final Logger LOGGER = Logger.getLogger(RootLayoutController.class.getName());
    private MarketApp marketApp;

    @FXML
    private void initialize () {}

    @FXML
    private void handleSaveFile () {
        try {
            this.serializeSimulation();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleSaveAndCloseFile() {
        try {
            this.stopEveryThread();
            this.serializeSimulation();
            this.marketApp.getPrimaryStage().close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void serializeSimulation() throws IOException {
        FileOutputStream saveFile = new FileOutputStream("C:\\Users\\Public\\Documents\\save_1.txt");
        ObjectOutputStream saveStream = new ObjectOutputStream(saveFile);

        saveStream.writeObject(new ArrayList<>(this.marketApp.getCompanies()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getIndices()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getShares()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getInvestmentUnits()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getCurrencies()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getCommodities()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getInvestmentFunds()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getInvestors()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getStockExchanges()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getCommodityExchanges()));
        saveStream.writeObject(new ArrayList<>(this.marketApp.getCurrencyExchanges()));
        saveStream.writeObject(this.marketApp.getPlayer());

        saveStream.close();
        saveFile.close();
    }

    @FXML
    private void handleLoadFile () {
        try {
            FileInputStream loadFile = new FileInputStream("C:\\Users\\Public\\Documents\\save_1.txt");
            ObjectInputStream loadStream = new ObjectInputStream(loadFile);

            this.marketApp.setCompanies((ArrayList<Company>) loadStream.readObject());
            this.marketApp.setIndices((ArrayList<Index>) loadStream.readObject());
            this.marketApp.setShares((ArrayList<Share>) loadStream.readObject());
            this.marketApp.setInvestmentUnits((ArrayList<InvestmentUnit>)loadStream.readObject());
            this.marketApp.setCurrencies((ArrayList<Currency>) loadStream.readObject());
            this.marketApp.setCommodities((ArrayList<Commodity>) loadStream.readObject());
            this.marketApp.setInvestmentFunds((ArrayList<InvestmentFund>) loadStream.readObject());
            this.marketApp.setInvestors((ArrayList<Investor>) loadStream.readObject());
            this.marketApp.setStockExchanges((ArrayList<StockExchange>) loadStream.readObject());
            this.marketApp.setCommodityExchanges((ArrayList<CommodityExchange>) loadStream.readObject());
            this.marketApp.setCurrencyExchanges((ArrayList<CurrencyExchange>) loadStream.readObject());
            this.marketApp.setPlayer((Player) loadStream.readObject());
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleClose () {
        this.marketApp.getPrimaryStage().close();
        this.stopEveryThread();
    }

    private void stopEveryThread () {
        try {
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

            for (Investor investor : this.marketApp.getInvestors()) {
                for (Thread thread : threadSet) {
                    if (thread.getId() == investor.getThreadId()) {
                        investor.terminate();
                        thread.join();
                    }
                }
            }

            for (InvestmentFund investmentFund : this.marketApp.getInvestmentFunds()) {
                for (Thread thread : threadSet) {
                    if (thread.getId() == investmentFund.getThreadId()) {
                        investmentFund.terminate();
                        thread.join();
                    }
                }
            }

            for(Company company : this.marketApp.getCompanies()) {
                for (Thread thread : threadSet) {
                    if (thread.getId() == company.getThreadId()) {
                        company.terminate();
                        thread.join();
                    }
                }
            }

            LOGGER.log(Level.FINEST, "Managed to stop all threads.");
        } catch (InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Some threads couldn't be stopped.");
            LOGGER.log(Level.SEVERE, exception.getMessage());
        }
    }

    public MarketApp getMarketApp() {
        return marketApp;
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
