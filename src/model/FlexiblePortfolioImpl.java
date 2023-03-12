package model;

import static java.time.temporal.TemporalAdjusters.firstDayOfNextYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import util.ApiPeriod;
import util.FileUtil;
import util.ParamNullChecker;
import util.TradeType;
import util.ValidDateChecker;

/**
 * Represents a version of portfolio that is flexible in nature and possesses the features of a
 * portfolio and its flexible version that let the user add trades after its creation.
 */
final class FlexiblePortfolioImpl extends AbstractPortfolio implements FlexiblePortfolio {

  private final String portfolioName;
  private final Consumer<Object[]> nullChecker;
  private final List<Trade> trades;

  /**
   * Constructs an instance of FlexiblePortfolioImpl that represents a flexible portfolio.
   *
   * @param portfolioName name of portfolio
   */
  public FlexiblePortfolioImpl(String portfolioName) {
    nullChecker = new ParamNullChecker();
    nullChecker.accept(new Object[]{portfolioName});
    this.portfolioName = portfolioName;
    trades = new ArrayList<>();
  }

  private static boolean isValidDate(LocalDate date) {
    LocalDate minYear = LocalDate.of(1995, 01, 01);

    return !date.isBefore(minYear) && !date.isAfter(LocalDate.now());
  }

  @Override
  public void addTrade(String ticker, Double quantity, String date)
      throws IllegalArgumentException {
    nullChecker.accept(new Object[]{portfolioName, ticker, quantity, date});
    checkTradeConsistency(ticker, quantity);
    this.trades.add(new TradeImpl(new StockImpl(ticker, quantity), date));
  }

  private void checkTradeConsistency(String ticker, Double quantity)
      throws IllegalArgumentException {
    this.trades.sort(Comparator.comparing(Trade::getDateOfTrade));
    if (quantity < 0.0) {
      Double qtySoFar = 0.0;
      for (Trade trade : this.trades) {
        if (trade.getName().equals(ticker)) {
          qtySoFar += trade.getQuantity();
        }
      }
      qtySoFar += quantity;
      if (qtySoFar < 0.0) {
        throw new IllegalArgumentException(
            "You cannot sell more than you have bought so far for " + ticker);
      }
    }
  }

  @Override
  public Double getCostBasis(String date, Double commission,
      Map<String, Map<String, Double>> stockPriceMap) {
    Double costBasis = 0.0;
    if (trades.isEmpty()) {
      return costBasis;
    }
    this.trades.sort(Comparator.comparing(Trade::getDateOfTrade));
    for (Trade trade : this.trades) {
      if (trade.getDateOfTrade().compareTo(date) > 0) {
        break;
      }
      if (trade.getQuantity() > 0.0) {
        Double price = getStockPriceOnDate(trade.getName(), trade.getDateOfTrade(), stockPriceMap);
        costBasis += (price * trade.getQuantity());
      }
      costBasis += commission;
    }
    return costBasis;
  }

  @Override
  public void getPerformance(String from, String to,
      Map<String, Map<String, Double>> stockPriceMap,
      BiConsumer<Map<String, Double>, ApiPeriod> performanceScaler)
      throws IllegalArgumentException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromDate = LocalDate.parse(from, formatter);
    LocalDate toDate = LocalDate.parse(to, formatter);
    long diff = ChronoUnit.DAYS.between(fromDate, toDate);
    Map<String, Double> performance;
    ApiPeriod timeScale;

    if (diff <= 0) {
      throw new IllegalArgumentException("From Date is after To Date");
    } else if (diff < 30) {
      //Daily scale
      performance = getDailyPerformance(stockPriceMap, formatter, fromDate, toDate);
      timeScale = ApiPeriod.DAILY;
    } else if (diff < 210) {
      //Weekly scale
      performance = getWeeklyPerformance(stockPriceMap, formatter, fromDate, toDate);
      timeScale = ApiPeriod.WEEKLY;
    } else if (diff < 900) {
      //Monthly scale
      performance = getMonthlyPerformance(stockPriceMap, formatter, fromDate, toDate);
      timeScale = ApiPeriod.MONTHLY;
    } else if (diff > 900) {
      //Yearly Scale
      performance = getYearlyPerformance(stockPriceMap, formatter, fromDate, toDate);
      timeScale = ApiPeriod.YEARLY;
    } else {
      throw new IllegalArgumentException("Dates not supported to view performance");
    }

    performanceScaler.accept(performance, timeScale);
  }

  @Override
  public void addFractionalTrades(Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String date) throws IllegalArgumentException {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    double sum = 0.0;
    for (Map.Entry<String, Double> tickerRatio : tickerRatios.entrySet()) {
      if (tickerRatio.getValue() > 0) {
        sum += tickerRatio.getValue();
      } else {
        throw new IllegalArgumentException("Proportion of a stock cannot be negative or zero");
      }
    }
    if (sum != 100.00) {
      throw new IllegalArgumentException(
          "Sum of proportions while buying fractional shares is not equal to 100!");
    }
    if (new ValidDateChecker().apply(date)) {
      for (Map.Entry<String, Double> tickerRatio : tickerRatios.entrySet()) {
        String ticker = tickerRatio.getKey();
        Double ratio = tickerRatio.getValue();
        if (stockPriceMap.containsKey(ticker + "_DAILY")) {
          Map<String, Double> datePrices = stockPriceMap.get(ticker + "_DAILY");
          if (datePrices.containsKey(date)) {
            Double stockPrice = stockPriceMap.get(ticker + "_DAILY").get(date);
            Double qty = ((ratio / 100) * amount) / stockPrice;
            this.addTrade(ticker, qty, date);
          } else {
            throw new IllegalArgumentException("Stock price not present for the day " + date);
          }
        } else {
          throw new IllegalArgumentException("Stock price for " + ticker + " not available!");
        }
      }
    } else {
      throw new IllegalArgumentException("Date string is invalid!");
    }

  }

  @Override
  public void addStrategy(Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String from, String to, ApiPeriod period)
      throws IllegalArgumentException {
    Set<String> dates = new TreeSet<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    if (to == null || to.isBlank() || to.isEmpty()) {
      to = formatter.format(LocalDate.now());
    }
    Function<String, Boolean> validDateChecker = new ValidDateChecker();
    if (!(validDateChecker.apply(from) && validDateChecker.apply(to))) {
      throw new IllegalArgumentException("Invalid dates!");
    }
    LocalDate fromDate;
    LocalDate toDate;
    try {
      fromDate = LocalDate.parse(from, formatter);
      toDate = LocalDate.parse(to, formatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid dates!");
    }

    if (fromDate.isAfter(toDate)) {
      throw new IllegalArgumentException("Invalid dates!");
    }
    String ticker = String.valueOf(tickerRatios.keySet().toArray()[0]);
    if (period.equals(ApiPeriod.WEEKLY)) {
      int fromDay = 0;
      while (fromDate.isBefore(toDate)) {
        String current = formatter.format(fromDate);
        if (stockPriceMap.get(ticker + "_DAILY").containsKey(current)) {
          dates.add(current);
          fromDate = fromDate.plusDays(7 - fromDay);
          fromDay = 0;
        } else {
          fromDate = fromDate.plusDays(1);
          fromDay++;
        }
      }
    } else if (period.equals(ApiPeriod.MONTHLY)) {
      LocalDate initialDate = fromDate;
      int monthCount = 0;
      while (fromDate.isBefore(toDate)) {
        String current = formatter.format(fromDate);
        if (stockPriceMap.get(ticker + "_DAILY").containsKey(current)) {
          dates.add(current);
          monthCount++;
          fromDate = initialDate.plusMonths(monthCount);
        } else {
          fromDate = fromDate.plusDays(1);
        }
      }
    } else {
      throw new IllegalArgumentException("Cannot implement strategy for period");
    }

    try {
      for (String date : dates) {
        this.addFractionalTrades(stockPriceMap, amount, tickerRatios, date);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot implement strategy for period");
    }
  }

  private Map<String, Double> getMonthlyPerformance(Map<String, Map<String, Double>> stockPriceMap,
      DateTimeFormatter formatter, LocalDate fromDate, LocalDate toDate) {
    Map<String, Double> performance = new TreeMap<>();
    LocalDate current = fromDate;
    current = current.with(lastDayOfMonth());
    while (current != null && !current.isAfter(toDate)) {
      String today = current.format(formatter);
      Double todayPerformance;
      try {
        todayPerformance = getValue(stockPriceMap, today);
      } catch (IllegalArgumentException ie) {
        performance.put(today, 0.0);
        current = current.plusMonths(1);
        current = current.with(lastDayOfMonth());
        continue;
      }
      performance.put(today, todayPerformance);
      current = current.plusMonths(1);
      current = current.with(lastDayOfMonth());
    }
    return performance;
  }

  private Map<String, Double> getYearlyPerformance(Map<String, Map<String, Double>> stockPriceMap,
      DateTimeFormatter formatter, LocalDate fromDate, LocalDate toDate) {
    Map<String, Double> performance = new TreeMap<>();
    LocalDate current = fromDate;

    if (!isValidDate(fromDate) || !isValidDate(toDate)) {
      throw new IllegalArgumentException("Date cannot be less than 1995 and greater than 2022");
    }

    while (current != null && !current.isAfter(toDate)) {
      current = current.with(lastDayOfYear());
      String today = current.format(formatter);
      Double yearPerformance;
      try {
        yearPerformance = getValue(stockPriceMap, today);
      } catch (IllegalArgumentException ie) {
        throw new IllegalArgumentException("Please insert dates after 1995");
      }
      performance.put(today, yearPerformance);

      current = current.with(firstDayOfNextYear());
    }
    return performance;
  }

  private Map<String, Double> getWeeklyPerformance(Map<String, Map<String, Double>> stockPriceMap,
      DateTimeFormatter formatter, LocalDate fromDate, LocalDate toDate) {
    Map<String, Double> performance = new TreeMap<>();
    LocalDate current = fromDate;
    while (!current.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
      current = current.plusDays(1);
    }
    while (!current.isAfter(toDate)) {
      String today = current.format(formatter);
      Double todayPerformance;
      try {
        todayPerformance = getValue(stockPriceMap, today);
      } catch (IllegalArgumentException ie) {
        performance.put(today, 0.0);
        current = current.plusDays(7);
        continue;
      }
      performance.put(today, todayPerformance);
      current = current.plusDays(7);

    }
    return performance;
  }


  private Map<String, Double> getDailyPerformance(Map<String, Map<String, Double>> stockPriceMap,
      DateTimeFormatter formatter, LocalDate fromDate, LocalDate toDate) {
    Map<String, Double> performance = new TreeMap<>();
    LocalDate current = fromDate;
    while (!current.isAfter(toDate)) {
      String today = current.format(formatter);
      Double todayPerformance;
      try {
        todayPerformance = getValue(stockPriceMap, today);
      } catch (IllegalArgumentException ie) {
        performance.put(today, 0.0);
        current = current.plusDays(1);
        continue;
      }
      performance.put(today, todayPerformance);
      current = current.plusDays(1);
    }
    return performance;
  }


  @Override
  public String getPortfolioName() {
    return this.portfolioName;
  }

  @Override
  public Map<String, Double> getComposition(String date) {
    Map<String, Double> composition = new HashMap<>();
    this.trades.sort(Comparator.comparing(Trade::getDateOfTrade));
    for (Trade trade : this.trades) {
      if (trade.getDateOfTrade().compareTo(date) > 0) {
        break;
      }
      composition.put(trade.getName(),
          composition.getOrDefault(trade.getName(), 0.0) + trade.getQuantity());
    }
    return composition;
  }

  @Override
  public void savePortfolio(String filePath, FileUtil fileUtil) {
    List<String[]> itemList = new LinkedList<>();
    itemList.add(new String[]{"Stock Name", "Quantity", "Date", "Type of Trade"});
    for (Trade trade : this.trades) {
      Double qty = trade.getQuantity();
      String typeOfTrade = qty < 0.0 ? TradeType.SELL.getTradeType() : TradeType.BUY.getTradeType();
      itemList.add(
          new String[]{trade.getName(), Math.abs(qty) + "", trade.getDateOfTrade(), typeOfTrade});
    }
    fileUtil.savePortfolio(itemList, portfolioName, filePath);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlexiblePortfolioImpl that = (FlexiblePortfolioImpl) o;
    return portfolioName.equals(that.portfolioName) && trades.equals(that.trades);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portfolioName, trades);
  }
}
