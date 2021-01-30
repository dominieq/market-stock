package org.example.marketstock.fxml;

import javafx.collections.FXCollections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

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
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.simulation.Simulation;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Controls the {@code SimulationLayout} and consequently
 * allows a user to participate in the {@link Simulation}.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class SimulationLayoutController {

    private static final Logger LOGGER = LogManager.getLogger(SimulationLayoutController.class);
    private Simulation simulation;

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
    private TableView<Asset> indexCompaniesTableView;
    @FXML
    private TableColumn<Asset, String> indexCompanyNameTableColumn;
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
    private Label investmentFundBudgetLabel;
    @FXML
    private Label investmentUnitNumberLabel;
    @FXML
    private Label investmentFundCurrentRateLabel;
    @FXML
    private Label investmentFundMinRateLabel;
    @FXML
    private Label investmentFundMaxRateLabel;
    @FXML
    private TextField numberOfInvestmentUnitsTextField;

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(final Simulation simulation) {
        this.simulation = simulation;
    }

    /**
     * Populates main tables with lists from {@link Simulation}.
     */
    public void setSimulationItems() {
        if (isNull(simulation)) {
            return;
        }

        stockExchangeTableView.setItems(simulation.getStockExchanges());
        commodityExchangeTableView.setItems(simulation.getCommodityExchanges());
        currencyExchangeTableView.setItems(simulation.getCurrencyExchanges());
        investorTableView.setItems(simulation.getInvestors());
        investmentFundTableView.setItems(simulation.getInvestmentFunds());
        showPlayerDetails(simulation.getPlayer());
    }

    /**
     * Used when a user wanted to remove, buy or sell an asset or entity without selecting it first.
     */
    public void showActionWithoutSelection() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("No selection");
        alert.setHeaderText("No object selected");
        alert.setContentText("Please select an object to perform an action.");
        alert.showAndWait();
    }

    /**
     * Used when there are no more random assets for a selected exchange.
     */
    public void showAddingObjectWarning () {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("No more assets for a selected market.");
        alert.setContentText("There are no random assets left for this market.");
        alert.showAndWait();
    }

    /**
     * Used when a {@link Countable} object doesn't have enough assets.
     */
    public void showNotEnoughSharesWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("No shares available on market");
        alert.setContentText("There are currently no shares available. You should wait for a company to issue more.");
        alert.showAndWait();
    }

    /**
     * Used when a user wanted to sell a number of assets they don't possess.
     */
    public void showSellingTooMuch () {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("Not enough asset in your inventory.");
        alert.setContentText("You don't have that much of a selected asset.");
        alert.showAndWait();
    }

    /**
     * Used when a user wanted to add an asset without selecting an exchange first.
     */
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
            (observable, oldValue, newValue) -> showBriefcaseDetails(newValue, simulation.getPlayer()));

        this.briefcaseAssetsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.briefcaseAssetNamesTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        this.briefcaseAssetsTableView.setRowFactory(doubleClick -> {
            TableRow<Asset> assetTableRow = new TableRow<>();

            assetTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !assetTableRow.isEmpty()) {
                    Asset abstractAsset = assetTableRow.getItem();

                    showBriefcaseDetails(abstractAsset, simulation.getPlayer());
                }
            });

            return assetTableRow ;
        });

        //##########################################//
        //                                          //
        //        Initialize stock exchanges        //
        //                                          //
        //##########################################//

        stockExchangeNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        
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

        companyNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

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
            (observable, oldValue, newValue) -> showIndex(newValue));

        indexNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        indexTableView.setRowFactory(doubleClick -> {
            StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();
            TableRow<Index> indexTableRow = new TableRow<>();

            indexTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! indexTableRow.isEmpty()) ) {
                    Index index = indexTableRow.getItem();

                    if (nonNull(stockExchange)) {
                        index.updateIndex(new ArrayList<>(stockExchange.getCompanies()));
                        showIndex(index);
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
            (observable, oldValue, newValue) -> showCompany((Company) newValue));

        indexCompanyNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        //##########################################//
        //                                          //
        //      Initialize currency exchanges       //
        //                                          //
        //##########################################//

        currencyExchangeNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        
        currencyExchangeTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCurrencyExchange(newValue));

        currencyExchangeTableView.setRowFactory(doubleClick -> {
            TableRow<CurrencyExchange> currencyExchangeTableRow = new TableRow<>();

            currencyExchangeTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! currencyExchangeTableRow.isEmpty()) ) {
                    CurrencyExchange currencyExchange = currencyExchangeTableRow.getItem();

                    showCurrencyExchange(currencyExchange);
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
            (observable, oldValue, newValue) -> showCurrency(newValue));

        currencyNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        currencyTableView.setRowFactory(doubleClick -> {
            TableRow<Currency> currencyTableRow = new TableRow<>();

            currencyTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! currencyTableRow.isEmpty()) ) {
                    Currency currency = currencyTableRow.getItem();

                    showCurrency(currency);
                }
            });

            return currencyTableRow ;
        });

        currencyCountryNameTableView.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue()));

        //##########################################//
        //                                          //
        //      Initialize commodity exchanges      //
        //                                          //
        //##########################################//

        commodityExchangeNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        
        commodityExchangeTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCommodityExchange(newValue));

        commodityExchangeTableView.setRowFactory(doubleClick -> {
            TableRow<CommodityExchange> commodityExchangeTableRow = new TableRow<>();

            commodityExchangeTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! commodityExchangeTableRow.isEmpty()) ) {
                    CommodityExchange commodityExchange = commodityExchangeTableRow.getItem();

                    showCommodityExchange(commodityExchange);
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
            (observable, oldValue, newValue) -> showCommodity(newValue));

        commodityNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        commodityTableView.setRowFactory(doubleClick -> {
            TableRow<Commodity> commodityTableRow = new TableRow<>();

            commodityTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! commodityTableRow.isEmpty()) ) {
                    Commodity commodity = commodityTableRow.getItem();

                    showCommodity(commodity);
                }
            });

            return commodityTableRow ;
        });

        //##########################################//
        //                                          //
        //           Initialize investors           //
        //                                          //
        //##########################################//

        investorFirstNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));

        investorLastNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));

        investorTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showInvestor(newValue));

        investorTableView.setRowFactory(doubleClick -> {
            TableRow<Investor> investorTableRow = new TableRow<>();

            investorTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! investorTableRow.isEmpty()) ) {
                    Investor investor = investorTableRow.getItem();

                    showInvestor(investor);
                }
            });

            return investorTableRow ;
        });

        //##########################################//
        //                                          //
        //       Initialize investment funds        //
        //                                          //
        //##########################################//

        investmentFundNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        investmentFundTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showInvestmentFund(newValue));

        investmentFundTableView.setRowFactory(doubleClick -> {
            TableRow<InvestmentFund> investmentFundTableRow = new TableRow<>();

            investmentFundTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! investmentFundTableRow.isEmpty()) ) {
                    InvestmentFund investmentFund = investmentFundTableRow.getItem();

                    showInvestmentFund(investmentFund);
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
     * Displays an asset from player's {@code Briefcase}.
     * @param asset An {@code Asset} from player's {@code Briefcase}.
     * @param player The {@code Player} from {@link Simulation}.
     */
    public void showBriefcaseDetails(final Asset asset, final Player player) {
        if(nonNull(asset) && nonNull(player)){
            briefcaseAssetNameLabel.setText(asset.getName());
            briefcaseAssetCurrentRateLabel.setText(Double.toString(asset.getCurrentRate()));
            briefcaseAssetMinRateLabel.setText(Double.toString(asset.getMinRate()));
            briefcaseAssetMaxRateLabel.setText(Double.toString(asset.getMaxRate()));

            final int assetIndex = player.getBriefcase().getAssets().indexOf(asset);
            final int countIndex = player.getBriefcase().getNumbers().get(assetIndex);
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
     * Shows player's characteristics.
     * @param player The {@code Player} from {@link Simulation}.
     */
    public void showPlayerDetails(final Player player) {
        if (nonNull(player)) {
            playersFirstNameLabel.setText(player.getFirstName());
            playersLastNameLabel.setText(player.getLastName());
            playersBudgetLabel.setText(Double.toString(player.getBudget()));

            final double summarizedValueOfAssets = player.getBriefcase().stream()
                    .mapToDouble(tuple -> tuple._1.getCurrentRate() * tuple._2)
                    .sum();

            playersAssetsValueLabel.setText(Double.toString(summarizedValueOfAssets));

            final int summarizedCountOfAssets = player.getBriefcase().getNumbers().stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            playersAssetsNumberLabel.setText(Integer.toString(summarizedCountOfAssets));

            briefcaseAssetsTableView.setItems(FXCollections.observableArrayList(
                    simulation.getPlayer().getBriefcase().getAssets()));
        } else {
            playersFirstNameLabel.setText("");
            playersLastNameLabel.setText("");
            playersBudgetLabel.setText("");
            playersAssetsValueLabel.setText("");
            playersAssetsNumberLabel.setText("");

            briefcaseAssetsTableView.setItems(null);
        }
    }
    
    /**
     * Callback fired when a user wishes to buy a selected number of an {@link Asset} from their {@code Briefcase}.
     * After the transaction is completed the {@link TextField} is cleared.
     */
    @FXML
    private void handleBuyAsset() {
        final Asset asset = briefcaseAssetsTableView.getSelectionModel().getSelectedItem();

        if(nonNull(asset)) {
            final int numberOfAsset = Integer.parseInt(numberOfAssetsToBuyTextField.getText());
            simulation.buySelectedResource(asset, numberOfAsset, asset.getCurrentRate(), simulation.getPlayer());

            showPlayerDetails(simulation.getPlayer());
            showUnknownAsset(asset);
            numberOfAssetsToBuyTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }
    
    /**
     * Callback fired when a user wishes to sell a selected number of an {@link Asset} from their {@code Briefcase}.
     * After the transaction is completed the {@link TextField} is cleared.
     */
    @FXML
    private void handleSellAsset() {
        final Asset asset = briefcaseAssetsTableView.getSelectionModel().getSelectedItem();

        if(nonNull(asset)) {
            int numberOfAsset = Integer.parseInt(numberOfAssetsToSellTextField.getText());
            simulation.sellSelectedResource(asset, numberOfAsset, simulation.getPlayer());

            showPlayerDetails(simulation.getPlayer());
            showUnknownAsset(asset);
            numberOfAssetsToSellTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Displays an instance of an {@link Asset} depending on its implementation.
     * @param asset An asset that is to be displayed in a GUI.
     */
    private void showUnknownAsset(final Asset asset) {
        if (asset instanceof Company) {
            if (Objects.equals(asset, companyTableView.getSelectionModel().getSelectedItem())) {
                showCompany((Company) asset);
            }
        } else if (asset instanceof Commodity) {
            if (Objects.equals(asset, commodityTableView.getSelectionModel().getSelectedItem())) {
                showCommodity((Commodity) asset);
            }
        } else if (asset instanceof Currency) {
            if (Objects.equals(asset, currencyTableView.getSelectionModel().getSelectedItem())) {
                showCurrency((Currency) asset);
            }
        } else if (asset instanceof InvestmentFund) {
            if (Objects.equals(asset, investmentFundTableView.getSelectionModel().getSelectedItem())) {
                showInvestmentFund((InvestmentFund) asset);
            }
        }
    }

    //########################################################//
    //                                                        //
    //                     Stock exchange                     //
    //                                                        //
    //########################################################//

    /**
     * Shows characteristics of a selected {@link StockExchange}.
     * @param stockExchange A {@code StockExchange} from {@link Simulation}.
     */
    public void showStockExchange(final StockExchange stockExchange) {
        if(nonNull(stockExchange)) {
            stockExchangeNameLabel.setText(stockExchange.getName());
            stockExchangeCountryLabel.setText(stockExchange.getCountry());
            stockExchangeCityLabel.setText(stockExchange.getCity());
            stockExchangeAddressLabel.setText(stockExchange.getAddress());
            stockExchangeCurrencyLabel.setText(stockExchange.getCurrency());
            stockExchangeMarginLabel.setText(Double.toString(stockExchange.getMargin()));

            companyTableView.setItems(FXCollections.observableArrayList(stockExchange.getCompanies()));
            indexTableView.setItems(FXCollections.observableArrayList(stockExchange.getIndices()));
        } else {
            stockExchangeNameLabel.setText("");
            stockExchangeCountryLabel.setText("");
            stockExchangeCityLabel.setText("");
            stockExchangeAddressLabel.setText("");
            stockExchangeCurrencyLabel.setText("");
            stockExchangeMarginLabel.setText("");

            companyTableView.setItems(null);
            indexTableView.setItems(null);
        }
    }
    
    /**
     * Callback fired when a user wishes to add a new {@link StockExchange} to {@link Simulation}.
     */
    @FXML
    private void handleAddStockExchange() {
        final StockExchange stockExchange = simulation.addStockExchange();
        stockExchangeTableView.getSelectionModel().select(stockExchange);

        LOGGER.info("[ADDED]: {}", stockExchange);
    }
    
    /**
     * Callback fired when a user wishes to remove a selected {@link StockExchange} from {@link Simulation}.
     */
    @FXML
    private void handleRemoveStockExchange() {
        final StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();

        if(nonNull(stockExchange)) {
            simulation.removeStockExchange(stockExchange);
            stockExchangeTableView.getSelectionModel().select(null);

            LOGGER.info("[REMOVED]: {}", stockExchange);
        } else {
            showActionWithoutSelection();
        }
    }
    
    /**
     * Shows characteristics of a selected {@link Company}.
     * @param company A {@code Company} that belongs to a selected {@link StockExchange}.
     */
    public void showCompany(final Company company) {
        if(nonNull(company)) {
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
     * Callback fired when a user wishes to add a new {@link Company} to a selected {@link StockExchange}.
     */
    @FXML
    private void handleAddCompany() {
        final StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();

        if(nonNull(stockExchange)) {
            final Company company = simulation.addCompany(stockExchange);
            companyTableView.getSelectionModel().select(company);

            showStockExchange(stockExchange);
            showIndex(indexTableView.getSelectionModel().getSelectedItem());

            simulation.issueEntities();

            LOGGER.info("[ADDED]: {}", company);
        } else {
            showAddingWithoutMarkingWarning();
        }
    }
       
    /**
     *  Callback fired when a user wishes to remove a selected {@link Company} from a selected {@link StockExchange}.
     */
    @FXML
    private void handleRemoveCompany() {
        final StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();
        final Company company = companyTableView.getSelectionModel().getSelectedItem();

        if(nonNull(stockExchange) && nonNull(company)) {
            simulation.removeCompany(company, stockExchange);
            companyTableView.getSelectionModel().select(null);

            showStockExchange(stockExchange);
            showIndex(indexTableView.getSelectionModel().getSelectedItem());

            LOGGER.info("[REMOVED]: {}", company);
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Callback fired when a user wishes to buy a selected number of a selected {@link Company}.
     * After the transaction is completed the {@link TextField} is cleared.
     */
    @FXML
    private synchronized void handleBuyShares() {
        final Company company = companyTableView.getSelectionModel().getSelectedItem();

        if (nonNull(company)) {
            final double originalRate = company.getCurrentRate();
            final int numberOfAsset = Integer.parseInt(numberOfSharesTextField.getText());
            simulation.buySelectedResource(company, numberOfAsset, originalRate, simulation.getPlayer());

            showPlayerDetails(simulation.getPlayer());
            showCompany(company);
            showIndex(indexTableView.getSelectionModel().getSelectedItem());
            numberOfSharesTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Shows characteristics of a selected {@link Index}.
     * @param index An {@code Index} that belongs to a selected {@link StockExchange}.
     */
    public void showIndex(final Index index) {
        if(nonNull(index)) {
            indexValueLabel.setText(Double.toString(index.getValue()));
            indexNameLabel.setText(index.getName());
            
            indexCompaniesTableView.setItems(FXCollections.observableArrayList(index.getContent()));
        } else {
            indexValueLabel.setText("");
            indexNameLabel.setText("");

            indexCompaniesTableView.setItems(null);
        }
    }

    /**
     * Callback fired when a user wishes to add a new {@link Index} to a selected {@link StockExchange}.
     */
    @FXML
    private void handleAddIndex() {
        final StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();

        if (nonNull(stockExchange)) {
            final Optional<Index> optionalIndex = simulation.addIndex(stockExchange);

            if (optionalIndex.isPresent()) {
                final Index index = optionalIndex.get();
                indexTableView.getSelectionModel().select(index);

                showStockExchange(stockExchange);

                LOGGER.info("[ADDED]: {}", index);
            } else {
                showAddingObjectWarning();
            }
        } else {
            showAddingWithoutMarkingWarning();
        }
    }

    /**
     * Callback fired when a user wishes to remove a selected {@link Index} from a selected {@link StockExchange}.
     */
    @FXML
    private void handleRemoveIndex() {
        final StockExchange stockExchange = stockExchangeTableView.getSelectionModel().getSelectedItem();
        final Index index = indexTableView.getSelectionModel().getSelectedItem();

        if(nonNull(stockExchange) && nonNull(index)) {
            simulation.removeIndex(index, stockExchange);
            indexTableView.getSelectionModel().select(null);

            showStockExchange(stockExchange);

            LOGGER.info("[REMOVED]: {}", index);
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
     * Shows characteristics of a selected {@link CurrencyExchange}.
     * @param currencyExchange {@code CurrencyExchange} from {@link Simulation}.
     */
    public void showCurrencyExchange(final CurrencyExchange currencyExchange) {
        if (nonNull(currencyExchange)) {
            currencyExchangeNameLabel.setText(currencyExchange.getName());
            currencyExchangeCountryLabel.setText(currencyExchange.getCountry());
            currencyExchangeCityLabel.setText(currencyExchange.getCity());
            currencyExchangeAddressLabel.setText(currencyExchange.getAddress());
            currencyExchangeCurrencyLabel.setText(currencyExchange.getCurrency());
            currencyExchangeMarginLabel.setText(Double.toString(currencyExchange.getMargin()));

            currencyTableView.setItems(FXCollections.observableArrayList(currencyExchange.getCurrencies()));
        } else {
            currencyExchangeNameLabel.setText("");
            currencyExchangeCountryLabel.setText("");
            currencyExchangeCityLabel.setText("");
            currencyExchangeAddressLabel.setText("");
            currencyExchangeCurrencyLabel.setText("");
            currencyExchangeMarginLabel.setText("");

            currencyTableView.setItems(null);
        }
    }

    /**
     * Callback fired when a user wishes to add a new {@link CurrencyExchange} to {@link Simulation}.
     */
    @FXML
    private void handleAddCurrencyExchange() {
        final CurrencyExchange currencyExchange = simulation.addCurrencyExchange();
        currencyExchangeTableView.getSelectionModel().select(currencyExchange);

        LOGGER.info("[ADDED]: {}", currencyExchange);
    }

    /**
     * Callback fired when a user wishes to remove a selected {@link CurrencyExchange} from {@link Simulation}.
     */
    @FXML
    private void handleRemoveCurrencyExchange() {
        final CurrencyExchange currencyExchange = currencyExchangeTableView.getSelectionModel().getSelectedItem();

        if (nonNull(currencyExchange)) {
            simulation.removeCurrencyExchange(currencyExchange);
            currencyExchangeTableView.getSelectionModel().select(null);

            LOGGER.info("[REMOVED]: {}", currencyExchange);
        } else {
            showAddingWithoutMarkingWarning();
        }
    }

    /**
     * Shows characteristics of a selected {@link Currency}.
     * @param currency {@code Currency} from a selected {@link CurrencyExchange}.
     */
    public void showCurrency(final Currency currency) {
        if (nonNull(currency)) {
            currencyNameLabel.setText(currency.getName());
            currencyCurrentRateLabel.setText(Double.toString(currency.getCurrentRate()));
            currencyMinRateLabel.setText(Double.toString(currency.getMinRate()));
            currencyMaxRateLabel.setText(Double.toString(currency.getMaxRate()));
            currencyUberCurrencyLabel.setText(currency.getComparisonCurrency().getName());

            currencyCountriesTableView.setItems(FXCollections.observableArrayList(currency.getCountries()));
        } else {
            currencyNameLabel.setText("");
            currencyCurrentRateLabel.setText("");
            currencyMinRateLabel.setText("");
            currencyMaxRateLabel.setText("");
            currencyUberCurrencyLabel.setText("");

            currencyCountriesTableView.setItems(null);
        }
    }

    /**
     * Callback fired when a user wishes to add a new {@link Currency} to a selected {@link CurrencyExchange}.
     */
    @FXML
    private void handleAddCurrency() {
        final CurrencyExchange currencyExchange = currencyExchangeTableView.getSelectionModel().getSelectedItem();

        if(nonNull(currencyExchange)) {
            final Optional<Currency> optionalCurrency = simulation.addCurrency(currencyExchange);

            if (optionalCurrency.isPresent()) {
                final Currency currency = optionalCurrency.get();
                currencyTableView.getSelectionModel().select(currency);

                showCurrencyExchange(currencyExchange);

                simulation.issueEntities();

                LOGGER.info("[ADDED]: {}", currency);
            } else {
                showAddingObjectWarning();
            }
        } else {
            showAddingWithoutMarkingWarning();
        }
    }

    /**
     * Callback fired when a user wishes to remove a selected {@link Currency} from a selected {@link CurrencyExchange}.
     */
    @FXML
    private void handleRemoveCurrency() {
        final CurrencyExchange currencyExchange = currencyExchangeTableView.getSelectionModel().getSelectedItem();
        final Currency currency = currencyTableView.getSelectionModel().getSelectedItem();

        if(nonNull(currencyExchange) && nonNull(currency)) {
            simulation.removeCurrency(currency, currencyExchange);
            currencyTableView.getSelectionModel().select(null);

            showCurrencyExchange(currencyExchange);

            LOGGER.info("[REMOVED]: {}", currency);
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Callback fired when a user wishes to buy a selected number of a selected {@link Currency}.
     * After the transaction is completed the {@link TextField} is cleared.
     */
    @FXML
    private void handleBuyCurrency() {
        final Currency currency = currencyTableView.getSelectionModel().getSelectedItem();

        if(nonNull(currency)) {
            final double originalRate = currency.getCurrentRate();
            final int numberOfAsset = Integer.parseInt(numberOfCurrenciesTextField.getText());
            simulation.buySelectedResource(currency, numberOfAsset, originalRate, simulation.getPlayer());

            showPlayerDetails(simulation.getPlayer());
            showCurrency(currency);
            numberOfCurrenciesTextField.clear();
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
     * Shows characteristics of a selected {@link CommodityExchange}.
     * @param commodityExchange {@code CommodityExchange} from {@link Simulation}.
     */
    public void showCommodityExchange(final CommodityExchange commodityExchange) {
        if(nonNull(commodityExchange)) {
            commodityExchangeNameLabel.setText(commodityExchange.getName());
            commodityExchangeCountryLabel.setText(commodityExchange.getCountry());
            commodityExchangeCityLabel.setText(commodityExchange.getCity());
            commodityExchangeAddressLabel.setText(commodityExchange.getAddress());
            commodityExchangeCurrencyLabel.setText(commodityExchange.getCurrency());
            commodityExchangeMarginLabel.setText(Double.toString(commodityExchange.getMargin()));

            commodityTableView.setItems(FXCollections.observableArrayList(commodityExchange.getCommodities()));
        } else {
            commodityExchangeNameLabel.setText("");
            commodityExchangeCountryLabel.setText("");
            commodityExchangeCityLabel.setText("");
            commodityExchangeAddressLabel.setText("");
            commodityExchangeCurrencyLabel.setText("");
            commodityExchangeMarginLabel.setText("");

            commodityTableView.setItems(null);
        }
    }

    /**
     * Callback fired when a user wishes to add a new {@link CommodityExchange} to {@link Simulation}.
     */
    @FXML
    private void handleAddCommodityExchange() {
        final CommodityExchange commodityExchange = simulation.addCommodityExchange();
        commodityExchangeTableView.getSelectionModel().select(commodityExchange);

        LOGGER.info("[ADDED]: {}", commodityExchange);
    }

    /**
     * Callback fired when a user wishes to remove a selected {@link CommodityExchange} from {@link Simulation}.
     */
    @FXML
    private void handleRemoveCommodityExchange() {
        final CommodityExchange commodityExchange = commodityExchangeTableView.getSelectionModel().getSelectedItem();

        if(nonNull(commodityExchange)) {
            simulation.removeCommodityExchange(commodityExchange);
            commodityExchangeTableView.getSelectionModel().select(null);

            LOGGER.info("[REMOVED]: {}", commodityExchange);
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Shows characteristics of a selected {@link Commodity}.
     * @param commodity {@code Commodity} from a selected {@link CommodityExchange}.
     */
    public void showCommodity(Commodity commodity) {
        if(nonNull(commodity)) {
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
     * Callback fired when a user wishes to add a new {@link Commodity} to a selected {@link CommodityExchange}.
     */
    @FXML
    private void handleAddCommodity() {
        final CommodityExchange commodityExchange = commodityExchangeTableView.getSelectionModel().getSelectedItem();

        if(nonNull(commodityExchange)) {
            Optional<Commodity> optionalCommodity = simulation.addCommodity(commodityExchange);

            if (optionalCommodity.isPresent()) {
                final Commodity commodity = optionalCommodity.get();
                commodityTableView.getSelectionModel().select(commodity);

                showCommodityExchange(commodityExchange);

                simulation.issueEntities();

                LOGGER.info("[ADDED]: {}", commodity);
            } else {
                showAddingObjectWarning();
            }
        } else {
            showAddingWithoutMarkingWarning();
        }
    }

    /**
     * Callback fired when a user wishes to remove a selected {@link Commodity} from a selected {@link CommodityExchange}.
     */
    @FXML
    private void handleRemoveCommodity() {
        final CommodityExchange commodityExchange = commodityExchangeTableView.getSelectionModel().getSelectedItem();
        final Commodity commodity = commodityTableView.getSelectionModel().getSelectedItem();

        if (nonNull(commodityExchange) && nonNull(commodity)) {
            simulation.removeCommodity(commodity, commodityExchange);
            commodityTableView.getSelectionModel().select(null);

            showCommodityExchange(commodityExchange);

            LOGGER.info("[REMOVED]: {}", commodity);
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Callback fired when a user wishes to buy a selected number of a selected {@link Commodity}.
     * After the transaction is completed the {@link TextField} is cleared.
     */
    @FXML
    private void handleBuyCommodity() {
        final Commodity commodity = commodityTableView.getSelectionModel().getSelectedItem();

        if (nonNull(commodity)) {
            final double originalRate = commodity.getCurrentRate();
            final int numberOfAsset = Integer.parseInt(numberOfCommoditiesTextField.getText());
            simulation.buySelectedResource(commodity, numberOfAsset, originalRate, simulation.getPlayer());

            showPlayerDetails(simulation.getPlayer());
            showCommodity(commodity);
            numberOfCommoditiesTextField.clear();
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
     * Callback fired when a user wishes to remove a selected {@link Investor} from {@link Simulation}.
     */
    @FXML
    private void handleRemoveInvestor() {
        final Investor investor = investorTableView.getSelectionModel().getSelectedItem();

        if(nonNull(investor)) {
            simulation.removeInvestor(investor);
            investorTableView.getSelectionModel().select(null);

            LOGGER.info("[REMOVED]: {}", investor);
        } else {
            showActionWithoutSelection();
        }
    }
    
    /**
     * Shows characteristics a selected {@link Investor}.
     * @param investor {@code Investor} from a {@link Simulation}.
     */
    private void showInvestor(final Investor investor) {
        if(nonNull(investor)) {
            investorFirstNameLabel.setText(investor.getFirstName());
            investorLastNameLabel.setText(investor.getLastName());
            investorPESELLabel.setText(investor.getPESEL());
            investorBudgetLabel.setText(Double.toString(investor.getBudget()));

            final double summarizedValueOfAssets = investor.getBriefcase().getAssets()
                    .stream()
                    .map(Asset::getCurrentRate)
                    .mapToDouble(x -> x)
                    .sum();
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
     * Callback fired when a user wishes to remove a selected {@link InvestmentFund} from {@link Simulation}.
     */
    @FXML
    private void handleRemoveInvestmentFund() {
        final InvestmentFund investmentFund = investmentFundTableView.getSelectionModel().getSelectedItem();

        if(nonNull(investmentFund)) {
            simulation.removeInvestmentFund(investmentFund);
            investmentFundTableView.getSelectionModel().select(null);

            LOGGER.info("[REMOVED]: {}", investmentFund);
        } else {
            showActionWithoutSelection();
        }
    } 
    
    /**
     * Shows characteristics of a selected {@link InvestmentFund}.
     * @param investmentFund {@code InvestmentFund} from a {@link Simulation}.
     */
    private void showInvestmentFund(final InvestmentFund investmentFund) {
        if(nonNull(investmentFund)) {
            investmentFundNameLabel.setText(investmentFund.getName());
            investmentFundCurrentRateLabel.setText(Double.toString(investmentFund.getCurrentRate()));
            investmentFundMinRateLabel.setText(Double.toString(investmentFund.getMinRate()));
            investmentFundMaxRateLabel.setText(Double.toString(investmentFund.getMaxRate()));
            investmentFundBudgetLabel.setText(Double.toString(investmentFund.getBudget()));
            investmentUnitNumberLabel.setText(Integer.toString(investmentFund.getNumberOfAssets()));
        } else {
            investmentFundNameLabel.setText("");
            investmentFundCurrentRateLabel.setText("");
            investmentFundMinRateLabel.setText("");
            investmentFundMaxRateLabel.setText("");
            investmentFundBudgetLabel.setText("");
            investmentUnitNumberLabel.setText("");
        }
    }
    
    /**
     * Callback fired when a user wishes to buy a selected number of a selected {@link InvestmentFund}.
     * After the transaction is completed the {@link TextField} is cleared.
     */
    @FXML
    private void handleBuyInvestmentUnits() {
        final InvestmentFund investmentFund = investmentFundTableView.getSelectionModel().getSelectedItem();

        if(nonNull(investmentFund)) {
            final double originalRate = investmentFund.getCurrentRate();
            final int numberOfAsset =  Integer.parseInt(numberOfInvestmentUnitsTextField.getText());
            simulation.buySelectedResource(investmentFund, numberOfAsset, originalRate, simulation.getPlayer());

            showPlayerDetails(simulation.getPlayer());
            showInvestmentFund(investmentFund);
            numberOfInvestmentUnitsTextField.clear();
        } else {
            showActionWithoutSelection();
        }
    }

    /**
     * Shows a {@link LineChart} that displays rate changes of a selected {@link Asset} from the player's briefcase.
     * Graph can display up to 2 assets at once.
     */
    private void showAssetLineChart() {
        try {
           assetLineChart.getData().clear();
           assetLineChart.setCreateSymbols(false);
           assetLineChart.getXAxis().setAutoRanging(true);
           assetLineChart.getYAxis().setAutoRanging(true);
           
           if (briefcaseAssetsTableView.getSelectionModel().getSelectedItems().size() < 2) {
                Asset abstractAsset = briefcaseAssetsTableView.getSelectionModel().getSelectedItem();

                xAxisTime = new NumberAxis(0,1,1);
                xAxisTime.setLabel("Time");

                yAxisValue = new NumberAxis(abstractAsset.getMinRate(), abstractAsset.getMaxRate(),10);
                yAxisValue.setLabel("Value");

                final XYChart.Series<Integer, Double> series = new XYChart.Series<>();
                int iterator = 0;
                for (double rate : abstractAsset.getRateChanges()) {
                    series.getData().add(new XYChart.Data<>(iterator, rate));
                    iterator++;
                }

                series.setName(abstractAsset.getName());
                assetLineChart.getData().add(series);
           } else {
               for (final Asset abstractAsset : briefcaseAssetsTableView.getSelectionModel().getSelectedItems()) {
                   xAxisTime =  new NumberAxis(0, 1, 1);
                   yAxisValue = new NumberAxis(-100, 100, 0);

                   final XYChart.Series<Integer, Double> series = new XYChart.Series<>();
                   int iterator = 0;
                   for (double rate : abstractAsset.getRateChanges()) {
                       double ratio = rate / abstractAsset.getRateChanges().get(0);

                       series.getData().add(new XYChart.Data<>(iterator, ratio));
                       iterator++;
                   }

                   series.setName(abstractAsset.getName());
                   assetLineChart.getData().add(series);
               }
           }
        } catch (IndexOutOfBoundsException exception){
            exception.printStackTrace();
        }
    }
}
