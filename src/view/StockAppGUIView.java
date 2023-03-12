package view;

import controller.Feature;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import util.ApiPeriod;
import util.ValidDateChecker;


/**
 * Represents an implementation of {@link IView} that has all the possible features for this
 * application.
 */
public class StockAppGUIView extends JFrame implements IView {

  private static final DecimalFormat df = new DecimalFormat("0.00");
  private final ValidDateChecker validDateChecker;
  private final JPanel mainPanel;
  private JPanel menuPanel;
  private JPanel portfolioPanel;
  private JPanel portfolioOptions;
  private JPanel buyByAmountPanel;
  private JPanel strategyPanel;
  private JButton addPortfolioButton;
  private JButton changeCommissionButton;
  private JButton backBtn;
  private JButton buyByQuantityBtn;
  private JButton buyByAmountBtn;
  private JButton doBuyByAmountBtn;
  private JButton implementStrategyBtn;
  private JButton addStocksBtn;
  private JButton addStocksInStrategyBtn;
  private JButton sellMenuBtn;
  private JButton sellBtn;
  private JButton buyBtn;
  private JButton compositionMenuBtn;
  private JButton valueMenuBtn;
  private JButton costBasisMenuBtn;
  private JButton costBasisBtn;
  private JButton performanceMenuBtn;
  private JButton performanceBtn;
  private JButton strategyMenuBtn;
  private JButton valueBtn;
  private JButton compositionBtn;
  private JTextField dateForValue;
  private JTextField dateForCostBasis;
  private JTextField dateForBuyByQuantity;
  private JTextField dateForSell;
  private JTextField dateToBuyByAmount;
  private JTextField strategyToDate;
  private JTextField strategyFromDate;
  private JTextField performanceToDate;
  private JTextField performanceFromDate;
  private JTextField amountText;
  private JTextField strategyAmountText;
  private JTextField dateForComposition;
  private JTextField bbqTickerText;
  private JTextField sellTickerText;
  private JSpinner bbqSpinner;
  private JSpinner sellSpinner;
  private JSpinner numberOfStocksSpinner;
  private JSpinner strategyNumberOfStocksSpinner;
  private JLabel valueOfPortfolio;
  private JLabel costBasisOfPortfolio;
  private JLabel compositionDate;
  private JLabel buyByQuantityDate;
  private JLabel sellDate;
  private JComboBox<String> combobox;
  private JComboBox<String> periodCombobox;
  private JSplitPane splitPane;
  private JSplitPane compositionSplitPane;
  private List<JTextField> stocksToBuy;
  private List<JTextField> proportionsToBuy;
  private List<JTextField> stocksToBuyInStrategy;
  private List<JTextField> proportionsToBuyInStrategy;
  private String currentPortfolio = "select portfolio";


  /**
   * Initializes the view by setting the size of the frame and adding a main panel to the frame.
   *
   * @throws HeadlessException when the frame does not get initialized as intended
   */
  public StockAppGUIView() throws HeadlessException {
    super();
    setTitle("Stocks App");
    setSize(1200, 500);

    validDateChecker = new ValidDateChecker();
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    createFirstPanel();
    mainPanel.add(menuPanel);
    add(mainPanel);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  private void createFirstPanel() {
    menuPanel = new JPanel();
    addPortfolioButton = new JButton("Add Portfolio");
    menuPanel.add(addPortfolioButton);
    combobox = new JComboBox<>();
    combobox.addItem("Select Existing Portfolio");
    menuPanel.add(combobox);
    changeCommissionButton = new JButton("Change Commission");
    menuPanel.add(changeCommissionButton);
    backBtn = new JButton("Go Back");
    portfolioOptions = new JPanel();
    portfolioOptions.setLayout(new BoxLayout(portfolioOptions, BoxLayout.Y_AXIS));
    portfolioOptions.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    addButton(portfolioOptions);
    valueBtn = new JButton("View Value");
    compositionBtn = new JButton("View Composition");
    buyBtn = new JButton("Buy");
    sellBtn = new JButton("Sell");
    costBasisBtn = new JButton("View Cost Basis");
    addStocksBtn = new JButton("Add Stocks");
    doBuyByAmountBtn = new JButton("Buy Stocks");
    addStocksInStrategyBtn = new JButton("Add Stocks");
    implementStrategyBtn = new JButton("Apply Strategy");
    performanceBtn = new JButton("Show Performance");
  }

  @Override
  public void addFeatures(Feature feature) {
    addPortfolioButton.addActionListener(evt -> feature.handleCreateNewPortfolio());
    changeCommissionButton.addActionListener(evt -> feature.handleChangeCommissionFee());
    combobox.addActionListener(evt -> feature.handleWorkWithPortfolio());
    backBtn.addActionListener(evt -> feature.handleGoBack());
    buyByQuantityBtn.addActionListener(evt -> feature.handleBuyByQuantity());
    buyBtn.addActionListener(evt -> feature.performBuyByQty());
    buyByAmountBtn.addActionListener(evt -> feature.handleBuyByAmount());
    doBuyByAmountBtn.addActionListener(evt -> feature.performBuyByAmount());
    addStocksBtn.addActionListener(evt -> feature.handleAddStocksToBuy());
    sellMenuBtn.addActionListener(evt -> feature.handleSell());
    sellBtn.addActionListener(evt -> feature.performSell());
    valueMenuBtn.addActionListener(evt -> feature.handlePortfolioValue());
    costBasisMenuBtn.addActionListener(evt -> feature.handleCostBasis());
    costBasisBtn.addActionListener(evt -> feature.showCostBasis());
    performanceMenuBtn.addActionListener(evt -> feature.handlePerformance());
    performanceBtn.addActionListener(evt -> feature.showPerformance());
    strategyMenuBtn.addActionListener(evt -> feature.handleStrategy());
    addStocksInStrategyBtn.addActionListener(evt -> feature.handleAddStocksInStrategy());
    implementStrategyBtn.addActionListener(evt -> feature.implementStrategy());
    valueBtn.addActionListener(evt -> feature.showPortfolioValue());
    compositionMenuBtn.addActionListener(evt -> feature.handleCompositionOfPortfolio());
    compositionBtn.addActionListener(evt -> feature.showPortfolioComposition());
  }

  @Override
  public void populatePortfolioMenu(List<String> portfolioNames) {
    for (String option : portfolioNames) {
      combobox.addItem(option);
    }
  }

  @Override
  public void addNewPortfolioToMenu(String newPortfolioName) {
    combobox.addItem(newPortfolioName);
  }

  @Override
  public String getNewPortfolioName() {
    String newPortfolio = JOptionPane.showInputDialog(mainPanel,
        "Please enter new PortfolioName",
        "Add Portfolio", JOptionPane.QUESTION_MESSAGE);
    Set<String> portfolios = new HashSet<>();
    for (int i = 0; i < combobox.getItemCount(); i++) {
      portfolios.add(combobox.getItemAt(i));
    }
    if (newPortfolio == null || newPortfolio.isBlank()
        || newPortfolio.isEmpty()) {
      return "";
    } else if (newPortfolio != null && portfolios.contains(newPortfolio)) {
      JOptionPane.showMessageDialog(mainPanel.getParent(),
          "Input Portfolio Name already exists!",
          "Invalid Portfolio Name", JOptionPane.WARNING_MESSAGE);
      return "";
    }

    return newPortfolio;
  }

  @Override
  public void showPortfolioOptions() {
    if (!String.valueOf(combobox.getSelectedItem()).equals("Select Existing Portfolio")) {
      addPortfolioPanel(String.valueOf(combobox.getSelectedItem()));
    }
  }

  @Override
  public void goBackToMainMenu() {
    portfolioPanel.setVisible(false);
    menuPanel.setVisible(true);
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  private void addPortfolioPanel(String currentPortfolio) {
    this.currentPortfolio = currentPortfolio;
    menuPanel.setVisible(false);
    portfolioPanel = new JPanel();
    portfolioPanel.setLayout(new BoxLayout(portfolioPanel, BoxLayout.Y_AXIS));
    mainPanel.add(portfolioPanel);

    portfolioPanel.add(backBtn);
    portfolioPanel.setBorder(BorderFactory.createTitledBorder(currentPortfolio));

    splitPane = new JSplitPane();
    splitPane.setTopComponent(portfolioOptions);

    JLabel defaultLabel = new JLabel();
    splitPane.setBottomComponent(defaultLabel);
    portfolioPanel.add(splitPane);

    portfolioPanel.setVisible(true);
    mainPanel.repaint();
  }

  private void addButton(JPanel portfolioOptions) {
    buyByQuantityBtn = new JButton("Buy by Quantity");
    portfolioOptions.add(buyByQuantityBtn);
    buyByAmountBtn = new JButton("Buy by Amount");
    portfolioOptions.add(buyByAmountBtn);
    sellMenuBtn = new JButton("Sell");
    portfolioOptions.add(sellMenuBtn);
    compositionMenuBtn = new JButton("Composition");
    portfolioOptions.add(compositionMenuBtn);
    valueMenuBtn = new JButton("Value");
    portfolioOptions.add(valueMenuBtn);
    performanceMenuBtn = new JButton("Performance");
    portfolioOptions.add(performanceMenuBtn);
    costBasisMenuBtn = new JButton("Cost Basis");
    portfolioOptions.add(costBasisMenuBtn);
    strategyMenuBtn = new JButton("Strategy");
    portfolioOptions.add(strategyMenuBtn);
  }

  @Override
  public void showInputFormForPortfolioValue() {
    JPanel valuePanel = new JPanel();
    JPanel datePanel = getValueDatePanel();
    valuePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    splitPane.setBottomComponent(valuePanel);
    valuePanel.add(datePanel);
  }

  private JPanel getValueDatePanel() {
    JPanel datePanel = new JPanel();
    dateForValue = new JTextField(10);
    dateForValue.setText("yyyy-MM-dd");
    dateForValue.setForeground(new Color(153, 153, 153));
    JLabel pDisplay = new JLabel("Enter Date:", JLabel.CENTER);
    valueOfPortfolio = new JLabel("", JLabel.CENTER);
    datePanel.add(pDisplay);
    dateForValue.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (dateForValue.getText().equals("yyyy-MM-dd")) {
          dateForValue.setText("");
          dateForValue.setForeground(new Color(0, 0, 0));
          valueOfPortfolio.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        dateForValue.setForeground(new Color(153, 153, 153));
      }
    });
    datePanel.add(dateForValue);
    datePanel.add(valueBtn);
    datePanel.add(valueOfPortfolio);
    return datePanel;
  }

  @Override
  public String getValueInputs() {
    if (validDateChecker.apply(dateForValue.getText())) {
      return dateForValue.getText();
    } else {
      valueOfPortfolio.setText("Invalid Date!");
      dateForValue.setText("yyyy-MM-dd");
      return "";
    }
  }

  @Override
  public String getCurrentPortfolioName() {
    return this.currentPortfolio;
  }

  @Override
  public void showValueOfPortfolio(Double value) {
    valueOfPortfolio.setText(df.format(value));
    dateForValue.setText("yyyy-MM-dd");
  }

  @Override
  public void showValueStatus(String message) {
    valueOfPortfolio.setText(message);
    dateForValue.setText("yyyy-MM-dd");
  }


  @Override
  public void showCompositionInputForm() {
    compositionSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JPanel datePanel = getCompositionDatePanel();
    compositionSplitPane.setTopComponent(datePanel);
    splitPane.setBottomComponent(compositionSplitPane);
  }

  private JPanel getCompositionDatePanel() {
    JPanel datePanel = new JPanel();
    dateForComposition = new JTextField(10);
    dateForComposition.setText("yyyy-MM-dd");
    dateForComposition.setForeground(new Color(153, 153, 153));
    JLabel pDisplay = new JLabel("Enter Date:", JLabel.CENTER);
    compositionDate = new JLabel("", JLabel.CENTER);
    datePanel.add(pDisplay);
    dateForComposition.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (dateForComposition.getText().equals("yyyy-MM-dd")) {
          dateForComposition.setText("");
          dateForComposition.setForeground(new Color(0, 0, 0));
          compositionDate.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        dateForComposition.setForeground(new Color(153, 153, 153));
      }
    });
    datePanel.add(dateForComposition);
    datePanel.add(compositionBtn);
    datePanel.add(compositionDate);
    return datePanel;
  }

  @Override
  public String getInputForComposition() {
    String date = dateForComposition.getText();
    if (!validDateChecker.apply(date)) {
      compositionDate.setText("Invalid Date!");
      dateForComposition.setText("yyyy-MM-dd");
      return "";
    } else {
      dateForComposition.setText("yyyy-MM-dd");
      return date;
    }
  }

  @Override
  public void showCompositionOfPortfolio(Map<String, Double> composition) {
    int numberOfStocks = composition.size();
    String[][] comp = new String[numberOfStocks][2];
    int i = 0;
    for (Map.Entry<String, Double> stockData : composition.entrySet()) {
      comp[i][0] = stockData.getKey();
      comp[i++][1] = df.format(stockData.getValue());
    }
    String[] columnNames = {"Stock Name", "Quantity"};
    JTable table = new JTable(comp, columnNames);
    JScrollPane sp = new JScrollPane(table);
    compositionSplitPane.setBottomComponent(sp);
  }

  @Override
  public void showInputFormForBuyByQty() {
    SpinnerModel model = new SpinnerNumberModel(1, 1, 10000, 1);
    bbqSpinner = new JSpinner(model);
    JPanel buyQtyPanel = new JPanel();
    buyQtyPanel.setLayout(new BoxLayout(buyQtyPanel, BoxLayout.PAGE_AXIS));

    JPanel tickerPanel = new JPanel();
    JLabel tickerLabel = new JLabel("Name of Stock", JLabel.CENTER);
    tickerPanel.add(tickerLabel);
    bbqTickerText = new JTextField(10);
    tickerPanel.add(bbqTickerText);
    buyQtyPanel.add(tickerPanel);

    JPanel quantityPanel = new JPanel();
    JLabel qtyLabel = new JLabel("Quantity", JLabel.CENTER);
    quantityPanel.add(qtyLabel);
    quantityPanel.add(bbqSpinner);
    buyQtyPanel.add(quantityPanel);

    JPanel datePanel = new JPanel();
    dateForBuyByQuantity = new JTextField(10);
    dateForBuyByQuantity.setText("yyyy-MM-dd");
    dateForBuyByQuantity.setForeground(new Color(153, 153, 153));
    JLabel dateLabel = new JLabel("Date", JLabel.CENTER);
    datePanel.add(dateLabel);
    datePanel.add(dateForBuyByQuantity);
    buyByQuantityDate = new JLabel("", JLabel.CENTER);
    dateForBuyByQuantity.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (dateForBuyByQuantity.getText().equals("yyyy-MM-dd")) {
          dateForBuyByQuantity.setText("");
          dateForBuyByQuantity.setForeground(new Color(0, 0, 0));
          buyByQuantityDate.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        dateForBuyByQuantity.setForeground(new Color(153, 153, 153));
      }
    });
    datePanel.add(buyByQuantityDate);
    buyQtyPanel.add(datePanel);
    buyQtyPanel.add(buyBtn);
    splitPane.setRightComponent(buyQtyPanel);
  }

  @Override
  public String[] getInputForBuyByQuantity() {
    if (validDateChecker.apply(dateForBuyByQuantity.getText())) {
      return new String[]{bbqTickerText.getText(), String.valueOf(bbqSpinner.getValue()),
          dateForBuyByQuantity.getText()};
    } else {
      dateForBuyByQuantity.setText("yyyy-MM-dd");
      buyByQuantityDate.setText("Invalid Date!");
      return null;
    }
  }

  @Override
  public void showBuyStatus(String message) {
    bbqTickerText.setText("");
    bbqSpinner.setValue(1);
    dateForBuyByQuantity.setText("");
    buyByQuantityDate.setText(message);
    dateForBuyByQuantity.setText("yyyy-MM-dd");
    bbqTickerText.setText("");
  }

  @Override
  public void showSellStatus(String message) {
    sellTickerText.setText("");
    sellSpinner.setValue(1);
    dateForSell.setText("");
    sellDate.setText(message);
    dateForSell.setText("yyyy-MM-dd");
    sellTickerText.setText("");
  }

  @Override
  public Double getCommissionCharge(Double commissionCharge) {
    String newCommission = JOptionPane.showInputDialog(
        "Current Commission Charge is " + df.format(commissionCharge)
            + ". Please enter new commission");
    if (newCommission == null || newCommission.isBlank()
        || newCommission.isEmpty()) {
      //cancel button pressed
      return -1.0;
    } else if (!isValidDouble(newCommission)) {
      JOptionPane.showMessageDialog(mainPanel.getParent(),
          "Invalid value for commission charge! Try Again!",
          "Invalid Commission", JOptionPane.WARNING_MESSAGE);
      return -1.0;
    }
    return Double.parseDouble(newCommission);
  }

  @Override
  public void showInputFormForSell() {
    SpinnerModel model = new SpinnerNumberModel(1, 1, 10000, 1);
    sellSpinner = new JSpinner(model);
    JPanel sellPanel = new JPanel();
    sellPanel.setLayout(new BoxLayout(sellPanel, BoxLayout.PAGE_AXIS));

    JPanel tickerPanel = new JPanel();
    JLabel tickerLabel = new JLabel("Name of Stock", JLabel.CENTER);
    tickerPanel.add(tickerLabel);
    sellTickerText = new JTextField(10);
    tickerPanel.add(sellTickerText);
    sellPanel.add(tickerPanel);

    JPanel quantityPanel = new JPanel();
    JLabel qtyLabel = new JLabel("Quantity", JLabel.CENTER);
    quantityPanel.add(qtyLabel);
    quantityPanel.add(sellSpinner);
    sellPanel.add(quantityPanel);

    JPanel datePanel = new JPanel();
    dateForSell = new JTextField(10);
    dateForSell.setText("yyyy-MM-dd");
    dateForSell.setForeground(new Color(153, 153, 153));
    JLabel dateLabel = new JLabel("Date", JLabel.CENTER);
    datePanel.add(dateLabel);
    datePanel.add(dateForSell);
    sellDate = new JLabel("", JLabel.CENTER);
    dateForSell.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (dateForSell.getText().equals("yyyy-MM-dd")) {
          dateForSell.setText("");
          dateForSell.setForeground(new Color(0, 0, 0));
          sellDate.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        dateForSell.setForeground(new Color(153, 153, 153));
      }
    });
    datePanel.add(sellDate);
    sellPanel.add(datePanel);
    sellPanel.add(sellBtn);
    splitPane.setRightComponent(sellPanel);
  }

  @Override
  public String[] getInputForSell() {
    if (validDateChecker.apply(dateForSell.getText())) {
      return new String[]{sellTickerText.getText(), String.valueOf(sellSpinner.getValue()),
          dateForSell.getText()};
    } else {
      dateForSell.setText("yyyy-MM-dd");
      sellDate.setText("Invalid Date!");
      return null;
    }
  }

  @Override
  public void showInputFormForPortfolioCostBasis() {
    JPanel costBasisPanel = new JPanel();
    JPanel datePanel = getCostBasisDatePanel();
    costBasisPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    splitPane.setBottomComponent(costBasisPanel);
    costBasisPanel.add(datePanel);
  }

  private JPanel getCostBasisDatePanel() {
    JPanel datePanel = new JPanel();
    dateForCostBasis = new JTextField(10);
    dateForCostBasis.setText("yyyy-MM-dd");
    dateForCostBasis.setForeground(new Color(153, 153, 153));
    JLabel pDisplay = new JLabel("Enter Date:", JLabel.CENTER);
    costBasisOfPortfolio = new JLabel("", JLabel.CENTER);
    datePanel.add(pDisplay);
    dateForCostBasis.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (dateForCostBasis.getText().equals("yyyy-MM-dd")) {
          dateForCostBasis.setText("");
          dateForCostBasis.setForeground(new Color(0, 0, 0));
          costBasisOfPortfolio.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        dateForCostBasis.setForeground(new Color(153, 153, 153));
      }
    });
    datePanel.add(dateForCostBasis);
    datePanel.add(costBasisBtn);
    datePanel.add(costBasisOfPortfolio);
    return datePanel;
  }

  @Override
  public void showCostBasisOfPortfolio(Double costBasisForFlexiblePortfolio) {
    costBasisOfPortfolio.setText(df.format(costBasisForFlexiblePortfolio));
    dateForCostBasis.setText("yyyy-MM-dd");
  }

  @Override
  public String getCostBasisInputs() {
    if (validDateChecker.apply(dateForCostBasis.getText())) {
      return dateForCostBasis.getText();
    } else {
      costBasisOfPortfolio.setText("Invalid Date!");
      dateForCostBasis.setText("yyyy-MM-dd");
      return "";
    }
  }

  @Override
  public void showCostBasisStatus(String message) {
    costBasisOfPortfolio.setText(message);
    dateForCostBasis.setText("yyyy-MM-dd");
  }

  @Override
  public void showBuyByAmount() {
    buyByAmountPanel = new JPanel();
    buyByAmountPanel.setLayout(new BoxLayout(buyByAmountPanel, BoxLayout.PAGE_AXIS));
    addStocksBtn.setVisible(true);
    doBuyByAmountBtn.setVisible(false);

    JPanel amountPanel = new JPanel();
    JLabel amountLabel = new JLabel("Amount");
    amountText = new JTextField(10);
    amountPanel.add(amountLabel);
    amountPanel.add(amountText);
    buyByAmountPanel.add(amountPanel);

    JPanel nosPanel = new JPanel();
    JLabel nosLabel = new JLabel("Number of Stocks");
    SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
    numberOfStocksSpinner = new JSpinner(model);
    nosPanel.add(nosLabel);
    nosPanel.add(numberOfStocksSpinner);
    buyByAmountPanel.add(nosPanel);

    JPanel datePanel = new JPanel();
    dateToBuyByAmount = new JTextField(10);
    dateToBuyByAmount.setText("yyyy-MM-dd");
    dateToBuyByAmount.setForeground(new Color(153, 153, 153));
    JLabel pDisplay = new JLabel("Enter Date:", JLabel.CENTER);
    datePanel.add(pDisplay);
    dateToBuyByAmount.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (dateToBuyByAmount.getText().equals("yyyy-MM-dd")) {
          dateToBuyByAmount.setText("");
          dateToBuyByAmount.setForeground(new Color(0, 0, 0));
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        //do nothing
      }
    });
    datePanel.add(dateToBuyByAmount);
    datePanel.add(addStocksBtn);
    stocksToBuy = new LinkedList<>();
    proportionsToBuy = new LinkedList<>();

    buyByAmountPanel.add(datePanel);

    JScrollPane scrollPane = new JScrollPane(buyByAmountPanel);
    splitPane.setBottomComponent(scrollPane);
  }

  @Override
  public String getDateToBuyByAmount() {
    String dateStr = dateToBuyByAmount.getText();
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate date = LocalDate.parse(dateStr, formatter);
      if (date.isAfter(LocalDate.now())) {
        JOptionPane.showMessageDialog(splitPane.getParent(),
            "Date cannot be a future date!",
            "Invalid Date", JOptionPane.WARNING_MESSAGE);
        return "";
      }
    } catch (DateTimeParseException dpe) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Date must be in yyyy-MM-dd format!",
          "Invalid Date", JOptionPane.WARNING_MESSAGE);
      return "";
    }
    return dateStr;
  }

  @Override
  public Double getAmountToBuyByAmount() {
    String amountStr = amountText.getText();
    double amount;
    try {
      amount = Double.parseDouble(amountStr);
      if (amount < 0.0) {
        throw new IllegalArgumentException("Amount cannot be negative");
      }
    } catch (IllegalArgumentException nfe) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Amount must be a valid positive number!",
          "Invalid Amount", JOptionPane.WARNING_MESSAGE);
      return null;
    }
    return amount;
  }

  @Override
  public Integer getNumberOfStocksToBuy() {
    return Integer.parseInt(String.valueOf(numberOfStocksSpinner.getValue()));
  }

  @Override
  public void showInputForStocksWithProportion(Integer numberOfStocks) {
    JPanel stocksPanel = new JPanel();
    stocksPanel.setLayout(new BoxLayout(stocksPanel, BoxLayout.PAGE_AXIS));
    for (int i = 0; i < numberOfStocks; i++) {
      JPanel stockPanel = new JPanel();
      JLabel stockLabel = new JLabel("Ticker");
      JTextField stockText = new JTextField(10);
      stocksToBuy.add(stockText);
      JLabel proportionLabel = new JLabel("Proportion");
      JTextField proportionText = new JTextField(10);
      proportionsToBuy.add(proportionText);
      stockPanel.add(stockLabel);
      stockPanel.add(stockText);
      stockPanel.add(proportionLabel);
      stockPanel.add(proportionText);
      stocksPanel.add(stockPanel);
    }
    buyByAmountPanel.add(stocksPanel);
    stocksPanel.add(doBuyByAmountBtn);
    addStocksBtn.setVisible(false);
    doBuyByAmountBtn.setVisible(true);
    dateToBuyByAmount.setEnabled(false);
    amountText.setEnabled(false);
    numberOfStocksSpinner.setEnabled(false);
  }

  @Override
  public Map<String, Double> getTickerProportions() {
    for (JTextField proportion : proportionsToBuy) {
      if (!isValidDouble(proportion.getText())) {
        JOptionPane.showMessageDialog(splitPane.getParent(),
            "Proportion can be positive number within 100!",
            "Invalid Proportions", JOptionPane.WARNING_MESSAGE);
        return null;
      }
    }
    try {
      Optional<Double> sum = proportionsToBuy.stream().map(s -> Double.parseDouble(s.getText()))
          .reduce(Double::sum);
      if (sum.isPresent()) {
        if (sum.get() != 100.00) {
          throw new IllegalArgumentException();
        }
      }
    } catch (IllegalArgumentException ie) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Sum of proportions should be 100!",
          "Invalid Proportions", JOptionPane.WARNING_MESSAGE);
      return null;
    }

    Map<String, Double> tickerProportions = new HashMap<>();
    for (int i = 0; i < stocksToBuy.size(); i++) {
      tickerProportions.put(stocksToBuy.get(i).getText(),
          Double.parseDouble(proportionsToBuy.get(i).getText()));
    }

    return tickerProportions;
  }

  @Override
  public void showBuyByAmountFailed() {
    JPanel failPanel = new JPanel();
    JLabel failLabel = new JLabel("Buy By Amount Failed! Click Buy By Amount to try again!",
        JLabel.CENTER);
    failPanel.add(failLabel);
    splitPane.setBottomComponent(failPanel);
  }

  @Override
  public void showBuyByAmountSucceeded() {
    JPanel successPanel = new JPanel();
    JLabel successLabel = new JLabel("Buy By Amount Succeeded!", JLabel.CENTER);
    successPanel.add(successLabel);
    splitPane.setBottomComponent(successPanel);
  }

  @Override
  public void showStrategyInputs() {
    strategyPanel = new JPanel();
    strategyPanel.setLayout(new BoxLayout(strategyPanel, BoxLayout.PAGE_AXIS));
    addStocksInStrategyBtn.setVisible(true);
    implementStrategyBtn.setVisible(false);

    JPanel amountPanel = new JPanel();
    JLabel amountLabel = new JLabel("Amount");
    strategyAmountText = new JTextField(10);
    amountPanel.add(amountLabel);
    amountPanel.add(strategyAmountText);
    strategyPanel.add(amountPanel);

    JPanel nosPanel = new JPanel();
    JLabel nosLabel = new JLabel("Number of Stocks");
    SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
    strategyNumberOfStocksSpinner = new JSpinner(model);
    nosPanel.add(nosLabel);
    nosPanel.add(strategyNumberOfStocksSpinner);
    strategyPanel.add(nosPanel);

    JPanel periodPanel = new JPanel();
    JLabel periodLabel = new JLabel("Frequency");
    periodCombobox = new JComboBox<>();
    periodCombobox.addItem(ApiPeriod.WEEKLY.getApiPeriod());
    periodCombobox.addItem(ApiPeriod.MONTHLY.getApiPeriod());
    periodPanel.add(periodLabel);
    periodPanel.add(periodCombobox);
    strategyPanel.add(periodPanel);

    JPanel fromDatePanel = new JPanel();
    strategyFromDate = new JTextField(10);
    strategyFromDate.setText("yyyy-MM-dd");
    strategyFromDate.setForeground(new Color(153, 153, 153));
    JLabel from = new JLabel("From:", JLabel.CENTER);
    fromDatePanel.add(from);
    strategyFromDate.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (strategyFromDate.getText().equals("yyyy-MM-dd")) {
          strategyFromDate.setText("");
          strategyFromDate.setForeground(new Color(0, 0, 0));
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        // do nothing
      }
    });
    fromDatePanel.add(strategyFromDate);

    JPanel toDatePanel = new JPanel();
    strategyToDate = new JTextField(10);
    strategyToDate.setText("yyyy-MM-dd");
    strategyToDate.setForeground(new Color(153, 153, 153));
    JLabel to = new JLabel("To:", JLabel.CENTER);
    toDatePanel.add(to);
    strategyToDate.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (strategyToDate.getText().equals("yyyy-MM-dd")) {
          strategyToDate.setText("");
          strategyToDate.setForeground(new Color(0, 0, 0));
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        // do nothing
      }
    });
    toDatePanel.add(strategyToDate);

    JPanel datePanel = new JPanel();
    datePanel.add(fromDatePanel);
    datePanel.add(toDatePanel);
    datePanel.add(addStocksInStrategyBtn);
    stocksToBuyInStrategy = new LinkedList<>();
    proportionsToBuyInStrategy = new LinkedList<>();

    strategyPanel.add(datePanel);
    JScrollPane scrollPane = new JScrollPane(strategyPanel);
    splitPane.setBottomComponent(scrollPane);
  }

  @Override
  public String getFromDateForStrategy() {
    String dateStr = strategyFromDate.getText();
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate date = LocalDate.parse(dateStr, formatter);
      if (date.isAfter(LocalDate.now())) {
        JOptionPane.showMessageDialog(splitPane.getParent(),
            "Date cannot be a future date!",
            "Invalid Date", JOptionPane.WARNING_MESSAGE);
        return "";
      }
    } catch (DateTimeParseException dpe) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Date must be in yyyy-MM-dd format!",
          "Invalid Date", JOptionPane.WARNING_MESSAGE);
      return "";
    }
    return dateStr;
  }

  @Override
  public String getToDateForStrategy() {
    String dateStr = strategyToDate.getText();
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate date = LocalDate.parse(dateStr, formatter);
      if (!date.isAfter(LocalDate.now())) {
        return dateStr;
      }
    } catch (DateTimeParseException dpe) {
      //do nothing
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate yesterday = LocalDate.now();
    yesterday = yesterday.minusDays(1);
    dateStr = formatter.format(yesterday);
    strategyToDate.setText(dateStr);
    return dateStr;
  }

  @Override
  public Double getAmountForStrategy() {
    String amountStr = strategyAmountText.getText();
    double amount;
    try {
      amount = Double.parseDouble(amountStr);
      if (amount < 0.0) {
        throw new IllegalArgumentException("Amount cannot be negative");
      }
    } catch (IllegalArgumentException nfe) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Amount must be a valid positive number!",
          "Invalid Amount", JOptionPane.WARNING_MESSAGE);
      return null;
    }
    return amount;
  }

  @Override
  public Integer getNumberOfStocksForStrategy() {
    return Integer.parseInt(String.valueOf(strategyNumberOfStocksSpinner.getValue()));
  }

  @Override
  public void showInputForStocksWithProportionForStrategy(Integer numberOfStocks) {
    JPanel stocksPanel = new JPanel();
    stocksPanel.setLayout(new BoxLayout(stocksPanel, BoxLayout.PAGE_AXIS));
    for (int i = 0; i < numberOfStocks; i++) {
      JPanel stockPanel = new JPanel();
      JLabel stockLabel = new JLabel("Ticker");
      JTextField stockText = new JTextField(10);
      stocksToBuyInStrategy.add(stockText);
      JLabel proportionLabel = new JLabel("Proportion");
      JTextField proportionText = new JTextField(10);
      proportionsToBuyInStrategy.add(proportionText);
      stockPanel.add(stockLabel);
      stockPanel.add(stockText);
      stockPanel.add(proportionLabel);
      stockPanel.add(proportionText);
      stocksPanel.add(stockPanel);
    }
    strategyPanel.add(stocksPanel);
    stocksPanel.add(implementStrategyBtn);
    addStocksInStrategyBtn.setVisible(false);
    implementStrategyBtn.setVisible(true);
    strategyFromDate.setEnabled(false);
    strategyToDate.setEnabled(false);
    strategyAmountText.setEnabled(false);
    strategyNumberOfStocksSpinner.setEnabled(false);
    periodCombobox.setEnabled(false);
  }

  @Override
  public Map<String, Double> getTickerProportionsForStrategy() {
    for (JTextField proportion : proportionsToBuyInStrategy) {
      if (!isValidDouble(proportion.getText())) {
        JOptionPane.showMessageDialog(splitPane.getParent(),
            "Proportion can be positive number within 100!",
            "Invalid Proportions", JOptionPane.WARNING_MESSAGE);
        return null;
      }
    }
    try {
      Optional<Double> sum = proportionsToBuyInStrategy.stream()
          .map(s -> Double.parseDouble(s.getText()))
          .reduce(Double::sum);
      if (sum.isPresent()) {
        if (sum.get() != 100.00) {
          throw new IllegalArgumentException();
        }
      }
    } catch (IllegalArgumentException ie) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Sum of proportions should be 100!",
          "Invalid Proportions", JOptionPane.WARNING_MESSAGE);
      return null;
    }

    Map<String, Double> tickerProportions = new HashMap<>();
    for (int i = 0; i < stocksToBuyInStrategy.size(); i++) {
      tickerProportions.put(stocksToBuyInStrategy.get(i).getText(),
          Double.parseDouble(proportionsToBuyInStrategy.get(i).getText()));
    }

    return tickerProportions;
  }

  @Override
  public void showStrategyFailed() {
    strategyPanel.removeAll();
    JPanel failPanel = new JPanel();
    JLabel failLabel = new JLabel("Strategy Failed! Click Strategy to try again!",
        JLabel.CENTER);
    failPanel.add(failLabel);
    splitPane.setBottomComponent(failPanel);
  }

  @Override
  public void showStrategySucceeded() {
    strategyPanel.removeAll();
    JPanel successPanel = new JPanel();
    JLabel successLabel = new JLabel("Strategy applied Successfully!", JLabel.CENTER);
    successPanel.add(successLabel);
    splitPane.setBottomComponent(successPanel);
  }

  @Override
  public void showInvalidFromToDates() {
    JOptionPane.showMessageDialog(splitPane.getParent(),
        "From Date cannot be After To Date!",
        "Invalid Dates", JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public String getPeriodForStrategy() {
    return String.valueOf(periodCombobox.getSelectedItem());
  }

  @Override
  public void showPerformanceInputView() {
    JPanel fromDatePanel = new JPanel();
    performanceFromDate = new JTextField(10);
    performanceFromDate.setText("yyyy-MM-dd");
    performanceFromDate.setForeground(new Color(153, 153, 153));
    JLabel from = new JLabel("From:", JLabel.CENTER);
    fromDatePanel.add(from);
    performanceFromDate.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (performanceFromDate.getText().equals("yyyy-MM-dd")) {
          performanceFromDate.setText("");
          performanceFromDate.setForeground(new Color(0, 0, 0));
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        // do nothing
      }
    });
    fromDatePanel.add(performanceFromDate);

    JPanel toDatePanel = new JPanel();
    performanceToDate = new JTextField(10);
    performanceToDate.setText("yyyy-MM-dd");
    performanceToDate.setForeground(new Color(153, 153, 153));
    JLabel to = new JLabel("To:", JLabel.CENTER);
    toDatePanel.add(to);
    performanceToDate.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (performanceToDate.getText().equals("yyyy-MM-dd")) {
          performanceToDate.setText("");
          performanceToDate.setForeground(new Color(0, 0, 0));
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        // do nothing
      }
    });
    toDatePanel.add(performanceToDate);

    JPanel datePanel = new JPanel();
    datePanel.add(fromDatePanel);
    datePanel.add(toDatePanel);
    datePanel.add(performanceBtn);
    splitPane.setRightComponent(datePanel);
  }

  @Override
  public String getFromDateForPerformance() {
    String dateStr = performanceFromDate.getText();
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate date = LocalDate.parse(dateStr, formatter);
      if (date.isAfter(LocalDate.now())) {
        JOptionPane.showMessageDialog(splitPane.getParent(),
            "Date cannot be a future date!",
            "Invalid Date", JOptionPane.WARNING_MESSAGE);
        return "";
      }
    } catch (DateTimeParseException dpe) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Date must be in yyyy-MM-dd format!",
          "Invalid Date", JOptionPane.WARNING_MESSAGE);
      return "";
    }
    return dateStr;
  }

  @Override
  public String getToDateForPerformance() {
    String dateStr = performanceToDate.getText();
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate date = LocalDate.parse(dateStr, formatter);
      if (date.isAfter(LocalDate.now())) {
        JOptionPane.showMessageDialog(splitPane.getParent(),
            "Date cannot be a future date!",
            "Invalid Date", JOptionPane.WARNING_MESSAGE);
        return "";
      }
    } catch (DateTimeParseException dpe) {
      JOptionPane.showMessageDialog(splitPane.getParent(),
          "Date must be in yyyy-MM-dd format!",
          "Invalid Date", JOptionPane.WARNING_MESSAGE);
      return "";
    }
    return dateStr;
  }

  @Override
  public void showPerformanceView(Map<String, Double> performances, ApiPeriod period) {
    DefaultCategoryDataset performanceDataset = createDataset(performances, period);
    JFreeChart performanceChart = ChartFactory.createLineChart(
        "Performance of portfolio " + this.currentPortfolio + " from "
            + performanceFromDate.getText() + " to " + performanceToDate.getText(),
        "Time",
        "Amount ($)",
        performanceDataset
    );
    CategoryPlot plot = performanceChart.getCategoryPlot();
    plot.setBackgroundPaint(Color.WHITE);
    CategoryAxis axis = plot.getDomainAxis();
    ValueAxis valueAxis = plot.getRangeAxis();
    int sizeOfTimeRange = 7;
    if (performances.size() < 12) {
      sizeOfTimeRange = 11;
    }
    axis.setTickLabelFont(new Font(null, Font.PLAIN, sizeOfTimeRange));
    valueAxis.setTickLabelFont(new Font(null, Font.PLAIN, 10));

    ChartPanel panel = new ChartPanel(performanceChart);
    JScrollPane scrollPane = new JScrollPane(panel);
    splitPane.setRightComponent(scrollPane);
  }

  private boolean isValidDouble(String value) {
    try {
      Double.parseDouble(value);
      return value.charAt(0) != '-';
    } catch (NumberFormatException | NullPointerException e) {
      return false;
    }
  }

  private DefaultCategoryDataset createDataset(Map<String, Double> performances, ApiPeriod period) {

    String series = "Performance";

    DefaultCategoryDataset performanceDataset = new DefaultCategoryDataset();

    for (Map.Entry<String, Double> performance : performances.entrySet()) {
      if (period.equals(ApiPeriod.MONTHLY)) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(performance.getKey(), formatter);
        int year = date.getYear();
        String nameOfMonth = Month.from(date).name().substring(0, 3);
        String time = nameOfMonth + " " + year;
        performanceDataset.addValue(performance.getValue(), series, time);
      } else if (period.equals(ApiPeriod.DAILY)) {
        performanceDataset.addValue(performance.getValue(), series,
            performance.getKey().substring(5));
      } else {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(performance.getKey(), formatter);
        int year = date.getYear();
        String time = String.valueOf(year);
        performanceDataset.addValue(performance.getValue(), series, time);
      }
    }

    return performanceDataset;
  }

}
