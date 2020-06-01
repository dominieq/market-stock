package org.example.marketstock.fxml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.app.MarketApp;
import org.example.marketstock.exceptions.*;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.marketstock.models.asset.*;
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
public class SimulationLayoutController {

    private static final Logger LOGGER = LogManager.getLogger(SimulationLayoutController.class);
    private MarketApp marketApp;

    /* Belongings tab */
    @FXML
    private TableView<Asset> briefcaseAssetsTableView;
    @FXML
    private TableColumn<Asset, String> briefcaseAssetNamesTableColumn;
    @FXML
    private Label briefcaseAssetNameLabel;
    @FXML
    private Label briefcaseAssetCurrentRateLabel;
    @FXML
    private Label briefcaseAssetMinRateLabel;
    @FXML
    private Label briefcaseAssetMaxRateLabel;
    @FXML
    private Label briefcaseAssetsNumberLabel;
    @FXML
    private Label playersFirstNameLabel;
    @FXML
    private Label playersLastNameLabel;
    @FXML
    private Label playersBudgetLabel;
    @FXML
    private Label playersAssetsNumberLabel;
    @FXML
    private Label playersAssetsValueLabel;
    @FXML
    private TextField numberOfAssetsToBuyTextField;
    @FXML
    private TextField numberOfAssetsToSellTextField;
    @FXML
    private LineChart<Integer, Double> assetLineChart;
    @FXML
    private NumberAxis xAxisTime;
    @FXML
    private NumberAxis yAxisValue;

    /* Stock exchange tab */
    @FXML
    private TableView<StockExchange> stockExchangeTableView;
    @FXML
    private TableColumn<StockExchange, String> stockExchangeNameTableColumn;
    @FXML
    private Label stockExchangeNameLabel;
    @FXML
    private Label stockExchangeCountryLabel;
    @FXML
    private Label stockExchangeCityLabel;
    @FXML
    private Label stockExchangeAddressLabel;
    @FXML
    private Label stockExchangeCurrencyLabel;
    @FXML
    private Label stockExchangeMarginLabel;
    @FXML
    private TableView<Company> companyTableView;
    @FXML
    private TableColumn<Company, String> companyNameTableColumn;
    @FXML
    private Label companyNameLabel;
    @FXML
    private Label companyDateOfFirstValuationLabel;
    @FXML
    private Label companyOpeningQuotationLabel;
    @FXML
    private Label companyCurrentRateLabel;
    @FXML
    private Label companyMinRateLabel;
    @FXML
    private Label companyMaxRateLabel;
    @FXML
    private Label companyNumberOfAssetsLabel;
    @FXML
    private Label companyProfitLabel;
    @FXML
    private Label companyRevenueLabel;
    @FXML
    private Label companyEquityCapitalLabel;
    @FXML
    private Label companyOpeningCapitalLabel;
    @FXML
    private Label companyTurnoverLabel;
    @FXML
    private Label companyVolumeLabel;
    @FXML
    private TextField numberOfSharesTextField;
    @FXML
    private TableView<Index> indexTableView;
    @FXML
    private TableColumn<Index, String> indexNameTableColumn;
    @FXML
    private TableView<Company> indexCompaniesTableView;
    @FXML
    private TableColumn<Company, String> indexCompanyNameTableColumn;
    @FXML
    private Label indexValueLabel;
    @FXML
    private Label indexNameLabel;

    /* Currency exchange tab */
    @FXML
    private TableView<CurrencyExchange> currencyExchangeTableView;
    @FXML
    private TableColumn<CurrencyExchange, String> currencyExchangeNameTableColumn;
    @FXML
    private Label currencyExchangeNameLabel;
    @FXML
    private Label currencyExchangeCountryLabel;
    @FXML
    private Label currencyExchangeCityLabel;
    @FXML
    private Label currencyExchangeAddressLabel;
    @FXML
    private Label currencyExchangeCurrencyLabel;
    @FXML
    private Label currencyExchangeMarginLabel;
    @FXML
    private TableView<Currency> currencyTableView;
    @FXML
    private TableColumn<Currency, String> currencyNameTableColumn;
    @FXML
    private Label currencyNameLabel;
    @FXML
    private Label currencyCurrentRateLabel;
    @FXML
    private Label currencyMinRateLabel;
    @FXML
    private Label currencyMaxRateLabel;
    @FXML
    private Label currencyUberCurrencyLabel;
    @FXML
    private TableView<String> currencyCountriesTableView;
    @FXML
    private TableColumn<String, String> currencyCountryNameTableView;
    @FXML
    private TextField numberOfCurrenciesTextField;

    /* Commodity exchange tab */
    @FXML
    private TableView<CommodityExchange> commodityExchangeTableView;
    @FXML
    private TableColumn<CommodityExchange, String> commodityExchangeNameTableColumn;
    @FXML
    private Label commodityExchangeNameLabel;
    @FXML
    private Label commodityExchangeCountryLabel;
    @FXML
    private Label commodityExchangeCityLabel;
    @FXML
    private Label commodityExchangeAddressLabel;
    @FXML
    private Label commodityExchangeCurrencyLabel;
    @FXML
    private Label commodityExchangeMarginLabel;
    @FXML
    private TableView<Commodity> commodityTableView;
    @FXML
    private TableColumn<Commodity, String> commodityNameTableColumn;
    @FXML
    private Label commodityNameLabel;
    @FXML
    private Label commodityCurrentRateLabel;
    @FXML
    private Label commodityMaxRateLabel;
    @FXML
    private Label commodityMinRateLabel;
    @FXML
    private Label commodityUnitOfTradingLabel;
    @FXML
    private Label commodityCurrencyLabel;
    @FXML
    private TextField numberOfCommoditiesTextField;

    /* Investors and investment funds tab */
    @FXML
    private TableView<Investor> investorTableView;
    @FXML
    private TableColumn<Investor, String> investorFirstNameTableColumn;
    @FXML
    private TableColumn<Investor, String> investorLastNameTableColumn;
    @FXML
    private Label investorFirstNameLabel;
    @FXML
    private Label investorLastNameLabel;
    @FXML
    private Label investorPESELLabel;
    @FXML
    private Label investorBudgetLabel;
    @FXML
    private Label investorAssetsValueLabel;
    @FXML
    private TableView<InvestmentFund> investmentFundTableView;
    @FXML
    private TableColumn<InvestmentFund, String> investmentFundNameTableColumn;
    @FXML
    private Label investmentFundNameLabel;
    @FXML
    private Label investmentFundFirstNameLabel;
    @FXML
    private Label investmentFundLastNameLabel;
    @FXML
    private Label investmentFundBudgetLabel;
    @FXML
    private Label investmentUnitNumberLabel;
    @FXML
    private Label investmentUnitCurrentRateLabel;
    @FXML
    private Label investmentUnitMinRateLabel;
    @FXML
    private Label investmentUnitMaxRateLabel;
    @FXML
    private Label investmentUnitNameLabel;
    @FXML
    private TextField numberOfInvestmentUnitsTextField;

    public void setMarketApp(MarketApp marketApp) {
        try {
            this.marketApp = marketApp;

            this.briefcaseAssetsTableView.setItems(marketApp.getPlayer().getBriefcase().getAssetsObservableArrayList());
            this.stockExchangeTableView.setItems(marketApp.getStockExchanges());
            this.commodityExchangeTableView.setItems(marketApp.getCommodityExchanges());
            this.currencyExchangeTableView.setItems(marketApp.getCurrencyExchanges());
            this.investorTableView.setItems(marketApp.getInvestors());
            this.investmentFundTableView.setItems(marketApp.getInvestmentFunds());

            this.showPlayerDetails(this.marketApp.getPlayer());
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void showActionWithoutSelection() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("No selection");
        alert.setHeaderText("No object selected");
        alert.setContentText("Please select an object to perform an action.");
        alert.showAndWait();
    }

    public void showAddingObjectWarning () {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("No more assets for a selected market.");
        alert.setContentText("There are no random assets left for this market.");
        alert.showAndWait();
    }

    public void showNotEnoughSharesWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("No shares available on market");
        alert.setContentText("There are currently no shares available. You should wait for a company to issue more.");
        alert.showAndWait();
    }

    public void showSellingTooMuch () {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("Not enough asset in your inventory.");
        alert.setContentText("You don't have that much of a selected asset.");
        alert.showAndWait();
    }

    public void showAddingWithoutMarkingWarning () {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("No stock exchange selected.");
        alert.setContentText("Please select a stock exchange to an object.");
        alert.showAndWait();
    }

    @FXML
    private void initialize () {
        //##########################################//
        //                                          //
        //      Initialize player's briefcase       //
        //                                          //
        //##########################################//

        this.briefcaseAssetsTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showBriefcaseDetails(newValue, this.marketApp.getPlayer()));

        this.briefcaseAssetsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.briefcaseAssetNamesTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        this.briefcaseAssetsTableView.setRowFactory(doubleClick -> {
            TableRow<Asset> assetTableRow = new TableRow<>();

            assetTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !assetTableRow.isEmpty()) {
                    Asset asset = assetTableRow.getItem();

                    showBriefcaseDetails(asset, this.marketApp.getPlayer());
                }
            });

            return assetTableRow ;
        });

        //##########################################//
        //                                          //
        //        Initialize stock exchanges        //
        //                                          //
        //##########################################//

        stockExchangeNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        
        stockExchangeTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showStockExchange(newValue));

        stockExchangeTableView.setRowFactory(doubleClick -> {
            TableRow<StockExchange> stockExchangeTableRow = new TableRow<>();

            stockExchangeTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !stockExchangeTableRow.isEmpty()) {
                    StockExchange stockExchange = stockExchangeTableRow.getItem();

                    stockExchange.updateIndices();
                    showStockExchange(stockExchange);
                }
            });

            return stockExchangeTableRow ;
        });

        //##########################################//
        //                                          //
        //            Initialize companies          //
        //                                          //
        //##########################################//

        companyTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCompany(newValue));

        companyTableView.setRowFactory(doubleClick -> {
            TableRow<Company> companyTableRow = new TableRow<>();

            companyTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! companyTableRow.isEmpty()) ) {
                    Company company = companyTableRow.getItem();
                    showCompany(company);
                }
            });

            return companyTableRow ;
        });

        //##########################################//
        //                                          //
        //             Initialize indices           //
        //                                          //
        //##########################################//

        indexTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showIndexDetails(newValue));

        indexTableView.setRowFactory(doubleClick -> {
            StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();
            TableRow<Index> indexTableRow = new TableRow<>();

            indexTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! indexTableRow.isEmpty()) ) {
                    Index index = indexTableRow.getItem();

                    if (stockExchange != null) {
                        index.updateCompanies(stockExchange.getCompanies());
                        showIndexDetails(index);
                    } else {
                        showActionWithoutSelection();
                    }
                }
            });

            return indexTableRow;
        });

        //##########################################//
        //                                          //
        //    Initialize companies from index       //
        //                                          //
        //##########################################//

        indexCompaniesTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCompany(newValue));

        //##########################################//
        //                                          //
        //      Initialize currency exchanges       //
        //                                          //
        //##########################################//

        currencyExchangeNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        
        currencyExchangeTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCurrencyExchangeDetails(newValue));

        currencyExchangeTableView.setRowFactory(doubleClick -> {
            TableRow<CurrencyExchange> currencyExchangeTableRow = new TableRow<>();

            currencyExchangeTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! currencyExchangeTableRow.isEmpty()) ) {
                    CurrencyExchange currencyExchange = currencyExchangeTableRow.getItem();

                    showCurrencyExchangeDetails(currencyExchange);
                }
            });

            return currencyExchangeTableRow ;
        });

        //##########################################//
        //                                          //
        //            Initialize currencies         //
        //                                          //
        //##########################################//

        currencyTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCurrencyDetails(newValue));

        currencyTableView.setRowFactory(doubleClick -> {
            TableRow<Currency> currencyTableRow = new TableRow<>();

            currencyTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! currencyTableRow.isEmpty()) ) {
                    Currency currency = currencyTableRow.getItem();

                    showCurrencyDetails(currency);
                }
            });

            return currencyTableRow ;
        });

        //##########################################//
        //                                          //
        //      Initialize commodity exchanges      //
        //                                          //
        //##########################################//

        commodityExchangeNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        
        commodityExchangeTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCommodityExchangeDetails(newValue));

        commodityExchangeTableView.setRowFactory(doubleClick -> {
            TableRow<CommodityExchange> commodityExchangeTableRow = new TableRow<>();

            commodityExchangeTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! commodityExchangeTableRow.isEmpty()) ) {
                    CommodityExchange commodityExchange = commodityExchangeTableRow.getItem();

                    showCommodityExchangeDetails(commodityExchange);
                }
            });

            return commodityExchangeTableRow ;
        });

        //##########################################//
        //                                          //
        //           Initialize commodities         //
        //                                          //
        //##########################################//

        commodityTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCommodityDetails(newValue));

        commodityTableView.setRowFactory(doubleClick -> {
            TableRow<Commodity> commodityTableRow = new TableRow<>();

            commodityTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! commodityTableRow.isEmpty()) ) {
                    Commodity commodity = commodityTableRow.getItem();

                    showCommodityDetails(commodity);
                }
            });

            return commodityTableRow ;
        });

        //##########################################//
        //                                          //
        //           Initialize investors           //
        //                                          //
        //##########################################//

        investorFirstNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());

        investorLastNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());

        investorTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showInvestorDetails(newValue));

        investorTableView.setRowFactory(doubleClick -> {
            TableRow<Investor> investorTableRow = new TableRow<>();

            investorTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! investorTableRow.isEmpty()) ) {
                    Investor investor = investorTableRow.getItem();

                    showInvestorDetails(investor);
                }
            });

            return investorTableRow ;
        });

        //##########################################//
        //                                          //
        //       Initialize investment funds        //
        //                                          //
        //##########################################//

        investmentFundNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        investmentFundTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showInvestmentFundDetails(newValue));

        investmentFundTableView.setRowFactory(doubleClick -> {
            TableRow<InvestmentFund> investmentFundTableRow = new TableRow<>();

            investmentFundTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! investmentFundTableRow.isEmpty()) ) {
                    InvestmentFund investmentFund = investmentFundTableRow.getItem();

                    showInvestmentFundDetails(investmentFund);
                }
            });

            return investmentFundTableRow ;
        });
    }

    //########################################################//
    //                                                        //
    //                    Player's briefcase                  //
    //                                                        //
    //########################################################//

    /**
     * Displays player's inventory (briefcase content).
     * @param asset Asset from player's briefcase.
     * @param player Player from simulation.
     */
    public void showBriefcaseDetails(Asset asset, Player player) {
        if(asset != null && player != null){
            briefcaseAssetNameLabel.setText(asset.getName());
            briefcaseAssetCurrentRateLabel.setText(Double.toString(asset.getCurrentRate()));
            briefcaseAssetMinRateLabel.setText(Double.toString(asset.getMinRate()));
            briefcaseAssetMaxRateLabel.setText(Double.toString(asset.getMaxRate()));

            int assetIndex = player.getBriefcase().getAssets().indexOf(asset);
            int countIndex = player.getBriefcase().getNumbersOfAssets().get(assetIndex);
            briefcaseAssetsNumberLabel.setText(Integer.toString(countIndex));

            showAssetLineChart();
        } else {
            briefcaseAssetNameLabel.setText("");
            briefcaseAssetCurrentRateLabel.setText("");
            briefcaseAssetMinRateLabel.setText("");
            briefcaseAssetMaxRateLabel.setText("");
            briefcaseAssetsNumberLabel.setText("");
        }
    }

    /**
     * Displays player's characteristics.
     * @param player Player from simulation.
     */
    public void showPlayerDetails(Player player) {
        if (player != null) {
            this.playersFirstNameLabel.setText(player.getFirstName());
            this.playersLastNameLabel.setText(player.getLastName());
            this.playersBudgetLabel.setText(Double.toString(player.getBudget()));

            double summarizedValueOfAssets = 0.0;
            for(Asset asset : player.getBriefcase().getAssets()) {
                summarizedValueOfAssets += asset.getCurrentRate();
            }
            this.playersAssetsValueLabel.setText(Double.toString(summarizedValueOfAssets));

            int summarizedCountOfAssets = 0;
            for(Integer count : player.getBriefcase().getNumbersOfAssets()) {
                summarizedCountOfAssets += count;
            }
            this.playersAssetsNumberLabel.setText(Integer.toString(summarizedCountOfAssets));
        } else {
            this.playersFirstNameLabel.setText("");
            this.playersLastNameLabel.setText("");
            this.playersBudgetLabel.setText("");
            this.playersAssetsValueLabel.setText("");
            this.playersAssetsNumberLabel.setText("");
        }
    }
    
    /**
     * Allows player to buy a number of asset from their belongings.
     */
    @FXML
    private synchronized void handleBuyAsset() {
        Asset asset = briefcaseAssetsTableView.getSelectionModel().getSelectedItem();
        int number = Integer.parseInt(numberOfAssetsToBuyTextField.getText());

        if(asset != null) {
            if (asset instanceof Share) {
                Company company = ((Share) asset).getCompany();

                if(company.getNumberOfAssets() - number >= 0) {
                    this.marketApp.getPlayer().buyAsset(asset, number);
                } else {
                    showNotEnoughSharesWarning();
                }
            } else if (asset instanceof InvestmentUnit) {
                InvestmentFund investmentFund =  ((InvestmentUnit) asset).getInvestmentFund();

                if(investmentFund.getNumberOfInvestmentUnits() - number >= 0) {
                    this.marketApp.getPlayer().buyAsset(asset, number);
                } else {
                    showNotEnoughSharesWarning();
                }
            } else {
                this.marketApp.getPlayer().buyAsset(asset, number);
            }

            this.briefcaseAssetsTableView.setItems(this.marketApp.getPlayer().getBriefcase().getAssetsObservableArrayList());
            this.showPlayerDetails(this.marketApp.getPlayer());
            numberOfAssetsToBuyTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }
    
    /**
     * Allows player to sell a number of asset from their briefcase.
     */
    @FXML
    private synchronized void handleSellAsset() {
        try {
            Asset asset = briefcaseAssetsTableView.getSelectionModel().getSelectedItem();
            int number = Integer.parseInt(numberOfAssetsToSellTextField.getText());

            if(asset != null) {
                if (asset instanceof Share) {
                    Company company = ((Share) asset).getCompany();
                    double margin = company.getStock().getMargin();

                    this.marketApp.getPlayer().sellAsset(asset, number, margin);
                } else if (asset instanceof InvestmentUnit) {
                    InvestmentFund investmentFund = ((InvestmentUnit) asset).getInvestmentFund();
                    double margin = investmentFund.getMargin();

                    this.marketApp.getPlayer().sellAsset(asset, number, margin);
                } else if (asset instanceof Currency) {
                    CurrencyExchange currencyExchange = ((Currency) asset).getCurrencyExchange();
                    double margin = currencyExchange.getMargin();

                    this.marketApp.getPlayer().sellAsset(asset, number, margin);
                } else {
                    CommodityExchange commodityExchange = ((Commodity) asset).getCommodityExchange();
                    double margin = commodityExchange.getMargin();

                    this.marketApp.getPlayer().sellAsset(asset, number, margin);
                }

                this.briefcaseAssetsTableView.setItems(this.marketApp.getPlayer().getBriefcase().getAssetsObservableArrayList());
                this.showPlayerDetails(this.marketApp.getPlayer());
                numberOfAssetsToSellTextField.clear();
            } else {
                showActionWithoutSelection();
            }
        } catch (BuyingException error) {
            showSellingTooMuch();
        }
    }

    //########################################################//
    //                                                        //
    //                     Stock exchange                     //
    //                                                        //
    //########################################################//

    /**
     * Shows characteristics of a selected stock exchange.
     * @param stockExchange Stock exchange from simulation.
     */
    public void showStockExchange(StockExchange stockExchange) {
        if(stockExchange != null) {
            /* Set relevant labels to display the characteristics of a selected stock exchange. */
            stockExchangeNameLabel.setText(stockExchange.getName());
            stockExchangeCountryLabel.setText(stockExchange.getCountry());
            stockExchangeCityLabel.setText(stockExchange.getCity());
            stockExchangeAddressLabel.setText(stockExchange.getAddress());
            stockExchangeCurrencyLabel.setText(stockExchange.getCurrency());
            stockExchangeMarginLabel.setText(Double.toString(stockExchange.getMargin()));

            /* Set companyTableView with companies from selected stock exchange. */
            companyTableView.setItems(stockExchange.getCompaniesObservableArrayList());
            companyNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

            /* Set indexTableView with indices from selected stock exchange. */
            indexTableView.setItems(stockExchange.getIndicesObservableArrayList());
            indexNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        } else {
            /* Set relevant stock exchange labels to none. */
            stockExchangeNameLabel.setText("");
            stockExchangeCountryLabel.setText("");
            stockExchangeCityLabel.setText("");
            stockExchangeAddressLabel.setText("");
            stockExchangeCurrencyLabel.setText("");
            stockExchangeMarginLabel.setText("");

            /* Set companyTableView to none. */
            companyTableView.setItems(null);
            companyNameTableColumn.setCellValueFactory(null);

            /* Set indexTableView to none. */
            indexTableView.setItems(null);
            indexNameTableColumn.setCellValueFactory(null);
        }
    }
    
    /**
     * Add a new stock exchange to simulation.
     */
    @FXML
    private void handleAddStockExchange() {
        StockExchange stockExchange = new StockExchange();
        stockExchange.initialize();

        this.marketApp.getStockExchanges().add(stockExchange);
        LOGGER.trace("{} added to simulation: \t{}.",
                new Object[]{stockExchange.getName(), this.marketApp.getStockExchanges().toArray()});
    }
    
    /**
     * Removes a selected stock exchange from simulation.
     */
    @FXML
    private void handleRemoveStockExchange() {
        StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();

        if(stockExchange != null) {
            /* Remove stock exchange from simulation. */
            this.marketApp.getStockExchanges().remove(stockExchange);
            LOGGER.trace("{} removed from simulation: \t{}.",
                    new Object[]{stockExchange.getName(), this.marketApp.getStockExchanges().toArray()});

            /* Remove all companies that belonged to a selected stock exchange from simulation. */
            for (Company company : stockExchange.getCompanies()) {
                company.terminate();
                this.marketApp.getCompanies().remove(company);
                // TODO remove company's share from simulation
            }
            LOGGER.trace("Removed companies that belonged to {} from simulation: \t{}",
                    new Object[]{stockExchange.getName(), this.marketApp.getCompanies().toArray()});

            /* Set companyTableView to none. */
            this.companyTableView.setItems(null);
            this.companyNameTableColumn.setCellValueFactory(null);
            this.showCompany(null);

            /* Remove all indices that belonged to a selected stock exchange from simulation. */
            for (Index index : stockExchange.getIndices()) {
                this.marketApp.getIndices().remove(index);
            }
            LOGGER.trace("Removed indices that belonged to {} from simulation: \t{}",
                    new Object[]{stockExchange.getName(), this.marketApp.getCompanies().toArray()});

            /* Set indexTableView to none. */
            this.indexTableView.setItems(null);
            this.indexNameTableColumn.setCellValueFactory(null);
            this.showIndexDetails(null);
        } else {
            showActionWithoutSelection();
        }
    }
    
    /**
     * Shows characteristics of a selected company.
     * @param company Company that belonged to certain stock exchange.
     */
    public void showCompany(Company company) {
        if(company != null) {
            companyNameLabel.setText(company.getName());
            companyDateOfFirstValuationLabel.setText(company.getDateOfFirstValuation());
            companyOpeningQuotationLabel.setText(Double.toString(company.getOpeningQuotation()));
            companyCurrentRateLabel.setText(Double.toString(company.getCurrentRate()));
            companyMinRateLabel.setText(Double.toString(company.getMinRate()));
            companyMaxRateLabel.setText(Double.toString(company.getMaxRate()));
            companyNumberOfAssetsLabel.setText(Integer.toString(company.getNumberOfAssets()));
            companyRevenueLabel.setText(Double.toString(company.getRevenue()));
            companyProfitLabel.setText(Double.toString(company.getProfit()));
            companyEquityCapitalLabel.setText(Double.toString(company.getEquityCapital()));
            companyOpeningCapitalLabel.setText(Double.toString(company.getOpeningCapital()));
            companyTurnoverLabel.setText(Double.toString(company.getTurnover()));
            companyVolumeLabel.setText(Double.toString(company.getVolume()));
        } else {
            companyNameLabel.setText("");
            companyDateOfFirstValuationLabel.setText("");
            companyOpeningQuotationLabel.setText("");
            companyCurrentRateLabel.setText("");
            companyMinRateLabel.setText("");
            companyMaxRateLabel.setText("");
            companyNumberOfAssetsLabel.setText("");
            companyProfitLabel.setText("");
            companyRevenueLabel.setText("");
            companyOpeningCapitalLabel.setText("");
            companyEquityCapitalLabel.setText("");
            companyVolumeLabel.setText("");
            companyTurnoverLabel.setText("");
        }
    }
    
    /**
     * Add new company to a selected stock exchange and to the list of all companies.
     */
    @FXML
    private void handleAddCompany() {
        StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();

        if(stockExchange != null) {
            Company company = new Company();
            company.initialize(stockExchange);

            /* Add new company to a selected stock exchange. */
            stockExchange.addCompany(company);
            LOGGER.trace("Company {} added to {}: \t{}",
                    new Object[]{company.getName(), stockExchange.getName(), stockExchange.getCompanies().toArray()});

            /* Add new company to simulation. */
            this.marketApp.getCompanies().add(company);
            LOGGER.trace("Company {} added to simulation: \t{}.",
                    new Object[]{company.getName(), this.marketApp.getCompanies().toArray()});

            /* Add new company's share to simulation. */
            this.marketApp.getShares().add(company.getShare());
            LOGGER.trace("{}'s share added to simulation: \t{}.",
                    new Object[]{company.getName(), this.marketApp.getShares().toArray()});

            /* Set companyTableView with companies from selected stock exchange. */
            companyTableView.setItems(stockExchange.getCompaniesObservableArrayList());
            companyNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

            /* Start company's thread. */
            Thread thread = new Thread(company);
            thread.start();
            company.setThreadId(thread.getId());

            /* Update selected stock exchange indices. */
            stockExchange.updateIndices();
            this.showIndexDetails(this.indexTableView.getSelectionModel().getSelectedItem());

            /* Refresh simulation to add possible new investors and investment funds. */
            this.marketApp.addEntities();
        } else {
            showAddingWithoutMarkingWarning();
        }
    }
       
    /**
     *  Removes a selected company from a selected stock exchange and the list of all companies.
     */
    @FXML
    private void handleRemoveCompany() {
        StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();
        Company company = companyTableView.getSelectionModel().getSelectedItem();

        if(stockExchange != null && company != null) {
            try {
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

                for (Thread thread : threadSet) {
                    if (thread.getId() == company.getThreadId()) {
                        company.terminate();
                        thread.join();
                    }
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            } finally {
                stockExchange.removeCompany(company);
                LOGGER.trace("Company {} removed from {}: \t{}.",
                        new Object[]{company.getName(), stockExchange.getName(), stockExchange.getCompanies().toArray()});

                this.marketApp.getCompanies().remove(company);
                LOGGER.trace("Company {} removed from simulation: \t{}.",
                        new Object[]{company.getName(), this.marketApp.getCompanies().toArray()});

                this.marketApp.getShares().remove(company.getShare());
                LOGGER.trace("{}'s share removed from simulation: \t{}.",
                        new Object[]{company.getName(), this.marketApp.getShares().toArray()});

                companyTableView.getItems().remove(company);
            }
        } else {
            showActionWithoutSelection();
        }
    }


    /**
     * Allows a player to buy a number of shares from a selected company. Number is taken from a TextField.
     * After transaction is completed the TextField is cleared.
     */
    @FXML
    private synchronized void handleBuyShares() {
        Company company = companyTableView.getSelectionModel().getSelectedItem();

        if (company != null) {
            Share share = company.getShare();
            int number = Integer.parseInt(numberOfSharesTextField.getText());

            this.marketApp.getPlayer().buyAsset(share, number);

            this.briefcaseAssetsTableView.setItems(this.marketApp.getPlayer().getBriefcase().getAssetsObservableArrayList());
            this.showPlayerDetails(this.marketApp.getPlayer());

            numberOfSharesTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Displays a selected Index characteristics.
     * @param index Index that belonged to certain stock exchange.
     */
    public void showIndexDetails(Index index) {
        if(index != null) {
            indexValueLabel.setText(Double.toString(index.getValue()));
            indexNameLabel.setText(index.getName());
            
            indexCompaniesTableView.setItems(index.getCompanyObservableArrayList());
            indexCompanyNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        } else {
            indexValueLabel.setText("");
            indexNameLabel.setText("");
            indexCompaniesTableView.setItems(null);
            indexCompanyNameTableColumn.setCellValueFactory(null);
        }
    }

    /**
     * Adds new Index to a selected stock exchange and the list of all indices.
     */
    @FXML
    private void handleAddIndex() {
        StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();

        try {
            if (stockExchange != null) {
                String name = stockExchange.drawIndex();
                String [] parts = name.split(";");

                Index index = new Index(parts[0], Integer.parseInt(parts[1]), parts[2], stockExchange.getCompanies());

                /* Add new index to a selected stock exchange. */
                stockExchange.addIndex(index);
                LOGGER.trace("Index {} added to {}:\t{}.",
                        new Object[]{index.getName(), stockExchange.toString(), stockExchange.getIndices().toArray()});

                /* Update new index to check if it should display companies from it's stock exchange. */
                stockExchange.updateIndices();

                /* Add new index to simulation. */
                this.marketApp.getIndices().add(index);
                LOGGER.trace("Index {} added to simulation:\t{}.",
                        new Object[]{index.getName(), this.marketApp.getIndices().toArray()});

                /* Set indexTableView with indices from a selected stock exchange. */
                this.indexTableView.setItems(stockExchange.getIndicesObservableArrayList());
                this.indexNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            } else {
                showAddingWithoutMarkingWarning();
            }
        } catch (AddingObjectException addingObjectException) {
            addingObjectException.printStackTrace();
        }
    }

    /**
     * Removes a selected Index from a selected stock exchange and the list of all indices.
     */
    @FXML
    private void handleRemoveIndex() {
        StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();
        Index index = indexTableView.getSelectionModel().getSelectedItem();

        if(stockExchange != null && index != null) {
            String name = index.getName();
            int maxSize = index.getMaxSize();
            String accessCondition = index.getAccessCondition();

            stockExchange.addIndexRaw(name + ";" + maxSize + ";" + accessCondition);
            stockExchange.removeIndex(index);

            this.marketApp.getIndices().remove(index);
            indexTableView.getItems().remove(index);
        } else {
            showActionWithoutSelection();
        }
    }

    //########################################################//
    //                                                        //
    //                   Currency exchange                    //
    //                                                        //
    //########################################################//

    /**
     * Displays a selected currency exchange characteristics.
     * @param currencyExchange CurrencyExchange from simulation.
     */
    public void showCurrencyExchangeDetails(CurrencyExchange currencyExchange) {
        if(currencyExchange != null) {
            currencyExchangeNameLabel.setText(currencyExchange.getName());
            currencyExchangeCountryLabel.setText(currencyExchange.getCountry());
            currencyExchangeCityLabel.setText(currencyExchange.getCity());
            currencyExchangeAddressLabel.setText(currencyExchange.getAddress());
            currencyExchangeCurrencyLabel.setText(currencyExchange.getCurrency());
            currencyExchangeMarginLabel.setText(Double.toString(currencyExchange.getMargin()));
            
            currencyTableView.setItems(currencyExchange.getCurrenciesObservableArrayList());
            currencyNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        } else {
            currencyExchangeNameLabel.setText("");
            currencyExchangeCountryLabel.setText("");
            currencyExchangeCityLabel.setText("");
            currencyExchangeAddressLabel.setText("");
            currencyExchangeCurrencyLabel.setText("");
            currencyExchangeMarginLabel.setText("");
            
            currencyTableView.setItems(null);
            currencyNameTableColumn.setCellValueFactory(null);
        }
    }

    /**
     * Add a new currency exchange to simulation.
     */
    @FXML
    private void handleAddCurrencyExchange() {
        CurrencyExchange currencyExchange = new CurrencyExchange();
        currencyExchange.initialize();

        this.marketApp.getCurrencyExchanges().add(currencyExchange);
        LOGGER.trace("{} added to simulation:\t{}.",
                new Object[]{currencyExchange.getName(), this.marketApp.getCurrencyExchanges().toArray()});
    }

    /**
     * Removes a selected currency exchange from simulation.
     */
    @FXML
    private void handleRemoveCurrencyExchange() {
        CurrencyExchange currencyExchange = this.currencyExchangeTableView.getSelectionModel().getSelectedItem();

        if(currencyExchange != null) {
            /* Remove a selected currency exchange from simulation. */
            this.marketApp.getCurrencyExchanges().remove(currencyExchange);
            LOGGER.trace("{} removed from simulation:\t{}.",
                    new Object[]{currencyExchange.getName(), this.marketApp.getCurrencyExchanges().toArray()});

            /* Remove currencies that belonged to a selected currency exchange from simulation. */
            for (Currency currency : currencyExchange.getCurrencies()) {
                this.marketApp.getCurrencies().remove(currency);
            }
            LOGGER.trace("{} removed from simulation:\t{}.",
                    new Object[]{currencyExchange.getCurrencies().toArray(), this.marketApp.getCurrencies().toArray()});

            // TODO remove currencies that belonged to a selected currency exchange from all briefcases.
        } else {
            this.showAddingWithoutMarkingWarning();
        }
    }

    /**
     * Displays a selected currency characteristics.
     * @param currency Currency from a selected currency exchange.
     */
    public void showCurrencyDetails(Currency currency) {
        if (currency != null) {
            /* Set relevant labels with a selected currency characteristics. */
            this.currencyNameLabel.setText(currency.getName());
            this.currencyCurrentRateLabel.setText(Double.toString(currency.getCurrentRate()));
            this.currencyMinRateLabel.setText(Double.toString(currency.getMinRate()));
            this.currencyMaxRateLabel.setText(Double.toString(currency.getMaxRate()));
            this.currencyUberCurrencyLabel.setText(currency.getComparisonCurrency().getName());

            /* Set currencyCountriesTableView with countries from currency. */
            this.currencyCountriesTableView.setItems(currency.getCountriesObservableArrayList());
            this.currencyCountryNameTableView.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        } else {
            /* Set relevant labels to none. */
            this.currencyNameLabel.setText("");
            this.currencyCurrentRateLabel.setText("");
            this.currencyMinRateLabel.setText("");
            this.currencyMaxRateLabel.setText("");
            this.currencyUberCurrencyLabel.setText("");

            /* Set currencyCountriesTableView to none. */
            this.currencyCountriesTableView.setItems(null);
            this.currencyCountryNameTableView.setCellValueFactory(null);
        }
    }

    /**
     * Add new currency to a selected currency exchange and the list a all currencies.
     */
    @FXML
    private void handleAddCurrency() {
        CurrencyExchange currencyExchange = currencyExchangeTableView.getSelectionModel().getSelectedItem();

        if(currencyExchange != null) {
            try {
                Currency currency = new Currency();
                currency.initializeValues(currencyExchange.drawCurrency(), currencyExchange);

                /* Add new currency to a selected currency exchange. */
                currencyExchange.addCurrency(currency);
                LOGGER.trace("{} added to {}:\t{}.",
                        new Object[]{currency.getName(), currencyExchange.getName(), currencyExchange.getCurrencies().toArray()});

                /* Add new currency to simulation. */
                this.marketApp.getCurrencies().add(currency);
                LOGGER.trace("{} added to simulation:\t{}.",
                        new Object[]{currency.getName(), this.marketApp.getCurrencies().toArray()});

                /* Add currency to currencyTableView and display it's characteristics. */
                this.currencyTableView.getItems().add(currency);
                this.currencyTableView.getSelectionModel().select(currency);
                this.showCurrencyDetails(currency);

                /* Refresh simulation to add possible new investors and investment funds. */
                this.marketApp.addEntities();
            } catch (AddingObjectException addingObjectException) {
                this.showAddingObjectWarning();
            }
        } else {
            showAddingWithoutMarkingWarning();
        }
    }

    /**
     * Removes a selected currency from a selected currency exchange and the list of all currencies.
     */
    @FXML
    private void handleRemoveCurrency() {
        CurrencyExchange currencyExchange = currencyExchangeTableView.getSelectionModel().getSelectedItem();
        Currency currency = currencyTableView.getSelectionModel().getSelectedItem();

        if(currencyExchange != null && currency != null) {
            String name = currency.getName();

            /* Remove a selected currency from a selected currency exchange. */
            currencyExchange.addCurrencyRaw(name);
            currencyExchange.removeCurrency(currency);
            LOGGER.trace("{} removed from {}:\t{}.",
                    new Object[]{currency.getName(), currencyExchange.getName(), currencyExchange.getCurrencies().toArray()});

            /* Remove a selected currency from simulation. */
            this.marketApp.getCurrencies().remove(currency);
            LOGGER.trace("{} removed from simulation:\t{}.",
                    new Object[]{currency.getName(), this.marketApp.getCurrencies().toArray()});

            // TODO remove currency from all briefcases.

            /* Stop displaying a selected currency. */
            this.currencyTableView.getItems().remove(currency);
            this.currencyTableView.getSelectionModel().clearSelection();
            this.showCurrencyDetails(null);
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Allows player to buy a selected number of currency.
     */
    @FXML
    private void handleBuyCurrency() {
        Currency currency = currencyTableView.getSelectionModel().getSelectedItem();

        if(currency != null) {
            int number = Integer.parseInt(numberOfCurrenciesTextField.getText());
            this.marketApp.getPlayer().buyAsset(currency, number);

            this.briefcaseAssetsTableView.setItems(this.marketApp.getPlayer().getBriefcase().getAssetsObservableArrayList());
            this.showPlayerDetails(this.marketApp.getPlayer());

            this.numberOfCurrenciesTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }

    //########################################################//
    //                                                        //
    //                  Commodity exchange                    //
    //                                                        //
    //########################################################//

    /**
     * Displays a selected commodity exchange characteristics.
     * @param commodityExchange CommodityExchange from simulation.
     */
    public void showCommodityExchangeDetails(CommodityExchange commodityExchange) {
        if(commodityExchange != null) {
            commodityExchangeNameLabel.setText(commodityExchange.getName());
            commodityExchangeCountryLabel.setText(commodityExchange.getCountry());
            commodityExchangeCityLabel.setText(commodityExchange.getCity());
            commodityExchangeAddressLabel.setText(commodityExchange.getAddress());
            commodityExchangeCurrencyLabel.setText(commodityExchange.getCurrency());
            commodityExchangeMarginLabel.setText(Double.toString(commodityExchange.getMargin()));
            
            commodityTableView.setItems(commodityExchange.getCommoditiesObservableArrayList());
            commodityNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        } else {
            commodityExchangeNameLabel.setText("");
            commodityExchangeCountryLabel.setText("");
            commodityExchangeCityLabel.setText("");
            commodityExchangeAddressLabel.setText("");
            commodityExchangeCurrencyLabel.setText("");
            commodityExchangeMarginLabel.setText("");
            
            commodityTableView.setItems(null);
            commodityNameTableColumn.setCellValueFactory(null);
        }
    }

    /**
     * Adds a new commodity exchange to simulation.
     */
    @FXML
    private void handleAddCommodityExchange() {
        CommodityExchange commodityExchange = new CommodityExchange();
        commodityExchange.initialize();

        /* Add new commodity exchange to simulation. */
        this.marketApp.getCommodityExchanges().add(commodityExchange);
        LOGGER.trace("{} added to simulation:\t{}.",
                new Object[]{commodityExchange.getName(), this.marketApp.getCommodityExchanges().toArray()});
    }

    /**
     * Removes a selected commodity exchange from simulation.
     */
    @FXML
    private void handleRemoveCommodityExchange() {
        CommodityExchange commodityExchange = this.commodityExchangeTableView.getSelectionModel().getSelectedItem();

        if(commodityExchange != null) {
            /* Remove a selected commodity exchange from simulation. */
            this.marketApp.getCommodityExchanges().remove(commodityExchange);
            LOGGER.trace("{} removed from simulation:\t{}.",
                    new Object[]{commodityExchange.getName(), this.marketApp.getCommodityExchanges().toArray()});

            /* Remove commodities that belonged to a selected commodity exchange. */
            for (Commodity commodity : commodityExchange.getCommodities()) {
                this.marketApp.getCommodities().remove(commodity);
            }
            LOGGER.trace("{} removed from simulation:\t{}.",
                    new Object[]{commodityExchange.getCommodities().toArray(), this.marketApp.getCommodities().toArray()});

            // TODO remove commodities that belonged to a selected commodity exchange from all briefcases.

            /* Set commodityTableView to none. */
            this.commodityTableView.setItems(null);
            this.commodityNameTableColumn.setCellValueFactory(null);
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Displays a selected commodity characteristics.
     * @param commodity Commodity from a selected commodity exchange.
     */
    public void showCommodityDetails(Commodity commodity) {
        if(commodity != null) {
            commodityNameLabel.setText(commodity.getName());
            commodityCurrentRateLabel.setText(Double.toString(commodity.getCurrentRate()));
            commodityMinRateLabel.setText(Double.toString(commodity.getMinRate()));
            commodityMaxRateLabel.setText(Double.toString(commodity.getMaxRate()));
            commodityUnitOfTradingLabel.setText(commodity.getUnitOfTrading());
            commodityCurrencyLabel.setText(commodity.getCurrency());
        } else {
            commodityNameLabel.setText("");
            commodityCurrentRateLabel.setText("");
            commodityMinRateLabel.setText("");
            commodityMaxRateLabel.setText("");
            commodityUnitOfTradingLabel.setText("");
            commodityCurrencyLabel.setText("");
        }
    }

    /**
     * Adds new commodity to a selected commodity exchange and to the list of all commodities.
     */
    @FXML
    private void handleAddCommodity() {
        CommodityExchange commodityExchange = commodityExchangeTableView.getSelectionModel().getSelectedItem();

        if(commodityExchange != null) {
            try {
                Commodity commodity = new Commodity();
                commodity.initialize(commodityExchange.drawResource(), commodityExchange);

                /* Add new commodity to a selected commodity exchange. */
                commodityExchange.addResource(commodity);
                LOGGER.trace("{} added to {}:\t{}.",
                        new Object[]{commodity.getName(), commodityExchange.getName(), commodityExchange.getCommodities().toArray()});

                /* Add new commodity to simulation. */
                this.marketApp.getCommodities().add(commodity);
                LOGGER.trace("{} added to simulation:\t{}",
                        new Object[]{commodity.getName(), this.marketApp.getCommodities().toArray()});

                /* Add commodity to commodityTableView and display its characteristics. */
                this.commodityTableView.getItems().add(commodity);
                this.commodityTableView.getSelectionModel().select(commodity);
                this.showCommodityDetails(commodity);

                /* Refresh simulation to add possible new investors and investment funds. */
                this.marketApp.addEntities();
            } catch (AddingObjectException addingObjectException) {
                this.showAddingObjectWarning();
            }
        } else {
            showAddingWithoutMarkingWarning();
        }
    }

    /**
     * Removes a selected commodity from a selected commodity exchange and from the list of all commodities.
     */
    @FXML
    private void handleRemoveCommodity() {
        CommodityExchange commodityExchange = commodityExchangeTableView.getSelectionModel().getSelectedItem();
        Commodity commodity = commodityTableView.getSelectionModel().getSelectedItem();

        if(commodityExchange != null && commodity != null) {
            String name = commodity.getName();
            String unitOfTrading = commodity.getUnitOfTrading();

            /* Remove commodity from a selected commodity exchange. */
            commodityExchange.addResourceRaw(name+ ";" + unitOfTrading);
            commodityExchange.removeResource(commodity);
            LOGGER.trace("{} removed from {}:\t{}.",
                    new Object[]{commodity.getName(), commodityExchange.getName(), commodityExchange.getCommodities().toArray()});

            /* Remove commodity from simulation. */
            this.marketApp.getCommodities().remove(commodity);
            LOGGER.trace("{} removed from simulation:\t{}.",
                    new Object[]{commodity.getName(), this.marketApp.getCommodities().toArray()});

            /* Stop displaying selected commodity. */
            this.commodityTableView.getItems().remove(commodity);
            this.commodityTableView.getSelectionModel().clearSelection();
            this.showCommodityDetails(null);
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Allows player to buy a selected number of commodity.
     */
    @FXML
    private void handleBuyCommodity() {
        Commodity commodity = commodityTableView.getSelectionModel().getSelectedItem();

        if (commodity != null) {
            int number = Integer.parseInt(numberOfCommoditiesTextField.getText());
            this.marketApp.getPlayer().buyAsset(commodity, number);

            this.briefcaseAssetsTableView.setItems(this.marketApp.getPlayer().getBriefcase().getAssetsObservableArrayList());
            this.showPlayerDetails(this.marketApp.getPlayer());

            this.numberOfCommoditiesTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }

    //########################################################//
    //                                                        //
    //             Investors and investment funds             //
    //                                                        //
    //########################################################//

    /**
     * Removes a selected investor from simulation
     */
    @FXML
    private void handleRemoveInvestor() {
        Investor investor = investorTableView.getSelectionModel().getSelectedItem();
        if(investor != null) {
            try {
                Set<Thread> tempThreadSet = Thread.getAllStackTraces().keySet();

                for(Thread thread : tempThreadSet) {
                    if(thread.getId() == investor.getThreadId()) {
                        investor.terminate();
                        thread.join();
                    }
                }

                LOGGER.warn("Investor's thread {} was successfully stopped.", investor.getThreadId());
            } catch (InterruptedException exception) {
                LOGGER.warn("Investor's thread {} couldn't be stopped.", investor.getThreadId());
                LOGGER.error(exception.getMessage(), exception);
            } finally {
                this.marketApp.getInvestors().remove(investor);
                investorTableView.getItems().remove(investor);

                LOGGER.info("Investor {} {} was removed from simulation.",
                        new Object[]{investor.getFirstName(), investor.getFirstName()});
            }
        } else {
            showActionWithoutSelection();

            LOGGER.warn("No investor was selected.");
        }
    }
    
    /**
     * Displays a selected investor characteristics.
     * @param investor Investor from a simulation.
     */
    private void showInvestorDetails(Investor investor) {
        if(investor != null) {
            investorFirstNameLabel.setText(investor.getFirstName());
            investorLastNameLabel.setText(investor.getLastName());
            investorPESELLabel.setText(investor.getPESEL());
            investorBudgetLabel.setText(Double.toString(investor.getBudget()));

            double summarizedValueOfAssets = 0.0;
            for(Asset asset : investor.getBriefcase().getAssets()) {
                summarizedValueOfAssets += asset.getCurrentRate();
            }
            investorAssetsValueLabel.setText(Double.toString(summarizedValueOfAssets));
        } else {
            investorFirstNameLabel.setText("");
            investorLastNameLabel.setText("");
            investorPESELLabel.setText("");
            investorBudgetLabel.setText("");
            investorAssetsValueLabel.setText("");
        }
    } 
    
    /**
     * Removes a selected investment fund from simulation.
     */
    @FXML
    private void handleRemoveInvestmentFund() {
        InvestmentFund investmentFund = investmentFundTableView.getSelectionModel().getSelectedItem();

        if(investmentFund != null) {
            try {
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

                for (Thread thread : threadSet) {
                    if(thread.getId() == investmentFund.getThreadId()) {
                        investmentFund.terminate();
                        thread.join();
                    }
                }

                LOGGER.warn("Investment fund's thread {} was successfully stopped.", investmentFund.getThreadId());
            } catch (InterruptedException exception) {
                LOGGER.warn("Investment fund's thread {} couldn't be stopped.", investmentFund.getThreadId());
                LOGGER.error(exception.getMessage(), exception);
            } finally {
                InvestmentUnit investmentUnit = investmentFund.getInvestmentUnit();

                this.marketApp.getInvestmentUnits().remove(investmentUnit);
                this.marketApp.getInvestmentFunds().remove(investmentFund);
                investmentFundTableView.getItems().remove(investmentFund);

                LOGGER.info("Investment fund {} was removed from simulation.", investmentFund.getName());
            }
        } else {
            showActionWithoutSelection();

            LOGGER.warn("No investment fund was selected.");
        }
    } 
    
    /**
     * Shows a selected investment fund characteristics.
     * @param investmentFund InvestmentFund from a simulation
     */
    private void showInvestmentFundDetails(InvestmentFund investmentFund) {
        if(investmentFund != null) {
            investmentFundFirstNameLabel.setText(investmentFund.getFirstName());
            investmentFundLastNameLabel.setText(investmentFund.getLastName());
            investmentFundNameLabel.setText(investmentFund.getName());
            investmentFundBudgetLabel.setText(Double.toString(investmentFund.getBudget()));
            investmentUnitNumberLabel.setText(Integer.toString(investmentFund.getNumberOfInvestmentUnits()));

            InvestmentUnit investmentUnit = investmentFund.getInvestmentUnit();

            investmentUnitCurrentRateLabel.setText(Double.toString(investmentUnit.getCurrentRate()));
            investmentUnitMinRateLabel.setText(Double.toString(investmentUnit.getMinRate()));
            investmentUnitMaxRateLabel.setText(Double.toString(investmentUnit.getMaxRate()));
            investmentUnitNameLabel.setText(investmentUnit.getName());
        } else {
            investmentFundFirstNameLabel.setText("");
            investmentFundLastNameLabel.setText("");
            investmentFundNameLabel.setText("");
            investmentFundBudgetLabel.setText("");
            investmentUnitNumberLabel.setText("");

            investmentUnitCurrentRateLabel.setText("");
            investmentUnitMinRateLabel.setText("");
            investmentUnitMaxRateLabel.setText("");
            investmentUnitNameLabel.setText("");
        }
    }
    
    /**
     * Allows player to buy a number of investment units from a investment fund.
     */
    @FXML
    private void handleBuyInvestmentUnits() {
        InvestmentFund investmentFund = investmentFundTableView.getSelectionModel().getSelectedItem();

        if(investmentFund != null) {
            InvestmentUnit investmentUnit = investmentFund.getInvestmentUnit();
            int number =  Integer.parseInt(numberOfInvestmentUnitsTextField.getText());

            this.marketApp.getPlayer().buyAsset(investmentUnit, number);

            this.briefcaseAssetsTableView.setItems(this.marketApp.getPlayer().getBriefcase().getAssetsObservableArrayList());
            this.showPlayerDetails(this.marketApp.getPlayer());

            investmentFund.setNumberOfInvestmentUnits(investmentFund.getNumberOfInvestmentUnits() - number);
            numberOfInvestmentUnitsTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }

    private void showAssetLineChart() {
        try {
           assetLineChart.getData().clear();
           assetLineChart.setCreateSymbols(false);
           assetLineChart.getXAxis().setAutoRanging(true);
           assetLineChart.getYAxis().setAutoRanging(true);
           
           if(briefcaseAssetsTableView.getSelectionModel().getSelectedItems().size() < 2) {
                Asset asset = briefcaseAssetsTableView.getSelectionModel().getSelectedItem();

                xAxisTime = new NumberAxis(0,1,1);
                xAxisTime.setLabel("Time");

                yAxisValue = new NumberAxis(asset.getMinRate(), asset.getMaxRate(),10);
                yAxisValue.setLabel("Value");

                XYChart.Series<Integer, Double> series = new XYChart.Series<>();
                int iterator = 0;
                for (double rate : asset.getRateChangeArrayList()) {
                    series.getData().add(new XYChart.Data<>(iterator, rate));
                    iterator++;
                }

                series.setName(asset.getName());
                assetLineChart.getData().add(series);
           } else {
               for (Asset asset : briefcaseAssetsTableView.getSelectionModel().getSelectedItems()) {
                   xAxisTime =  new NumberAxis(0, 1, 1);
                   yAxisValue = new NumberAxis(-100, 100, 0);

                   XYChart.Series<Integer, Double> series = new XYChart.Series<>();
                   int iterator = 0;
                   for (double rate : asset.getRateChangeArrayList()) {
                       double ratio = rate / asset.getRateChangeArrayList().get(0);

                       series.getData().add(new XYChart.Data<>(iterator, ratio));
                       iterator++;
                   }

                   series.setName(asset.getName());
                   assetLineChart.getData().add(series);
               }
           }
        } catch (IndexOutOfBoundsException exception){
            exception.printStackTrace();
        }
    }
}
