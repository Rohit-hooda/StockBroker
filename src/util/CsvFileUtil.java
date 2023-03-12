package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents an implementation of {@link FileUtil} that has functionalities to be performed.
 */

public final class CsvFileUtil implements FileUtil {

  private static final String COMMA_DELIMITER = ",";
  private static final String CSV = ".csv";

  @Override
  public List<String[]> readFile(String filePath)
      throws IllegalArgumentException {
    List<String[]> rows;
    try {
      rows = new ArrayList<>();
      Scanner fileScanner = new FileScannerFunction().apply(filePath);
      boolean isFirstLine = true;
      while (fileScanner.hasNextLine()) {
        if (isFirstLine) {
          fileScanner.nextLine();
          isFirstLine = false;
          continue;
        }
        String[] oneRow = fileScanner.nextLine().split(COMMA_DELIMITER);
        rows.add(oneRow);
      }
      fileScanner.close();
    } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Csv file provided is faulty!");
    }

    return rows;
  }

  @Override
  public Map<String, List<String[]>> readAllFiles(String filePath) {
    Map<String, List<String[]>> multiFileDataMap = new HashMap<>();
    File resourceDir = new File(filePath);
    if (resourceDir.isDirectory()) {
      for (File file : Objects.requireNonNull(resourceDir.listFiles())) {
        if (file.isFile() && file.getName().endsWith(CSV)) {
          String fileName = file.getName();
          String header = fileName.substring(0, fileName.length() - 4);
          List<String[]> stockDataFromFile = readFile(filePath + fileName);
          multiFileDataMap.put(header, stockDataFromFile);
        }
      }
    }
    return multiFileDataMap;
  }

  @Override
  public void savePortfolio(List<String[]> stocks,
      String portfolioName, String resourceDirectory) {
    String portfolioFilename = resourceDirectory + portfolioName + CSV;
    writePortfolioToFile(portfolioFilename, stocks);
  }

  private void writePortfolioToFile(String portfolioFilename, List<String[]> stocks) {
    String stockListAsString = getStockListAsString(stocks);
    try {
      FileWriter fileWriter = new FileWriter(portfolioFilename);
      fileWriter.write(stockListAsString);
      fileWriter.close();
    } catch (IOException e) {
      //do nothing
    }
  }

  private String getStockListAsString(List<String[]> stocks) {
    StringBuilder sb = new StringBuilder();
    for (String[] stock : stocks) {
      sb.append(new StringAppender().apply(stock, COMMA_DELIMITER));
      sb.append("\n");
    }
    return sb.toString();
  }
}
