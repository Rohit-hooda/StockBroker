package controller;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import util.ApiPeriod;
import view.StockAppView;

/**
 * Represents an intermediate processing class that scales the portfolio performance data for view
 * to show conveniently.
 */
public class PerformanceScalerCommand implements BiConsumer<Map<String, Double>, ApiPeriod> {

  private final StockAppView view;

  /**
   * Constructs an instance of {@link PerformanceScalerCommand} that scales the performance data of
   * flexible portfolio.
   *
   * @param view text-based interface of the application
   */
  public PerformanceScalerCommand(StockAppView view) {
    this.view = view;
  }


  @Override
  public void accept(Map<String, Double> performances, ApiPeriod timeScale) {
    double max = Integer.MIN_VALUE;
    for (Map.Entry<String, Double> performance : performances.entrySet()) {
      max = Math.max(max, performance.getValue());
    }
    double valueScale = (max / 30);
    Map<String, Integer> scaledPerformances = new TreeMap<>();
    for (Map.Entry<String, Double> performance : performances.entrySet()) {
      int stars = (int) (performance.getValue() / valueScale);
      scaledPerformances.put(performance.getKey(), stars);
    }
    view.showPerformance(scaledPerformances, timeScale, valueScale);
  }

}
