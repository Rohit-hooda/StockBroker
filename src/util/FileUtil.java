package util;

import java.util.List;
import java.util.Map;

/**
 * This interface represents the various operations that can be performed with file.
 */
public interface FileUtil {

  /**
   * This method reads a given file and returns the content of that file.
   *
   * @param filePath is the path of the file
   * @return a List of strings
   * @throws IllegalArgumentException when the file is not found
   */
  List<String[]> readFile(String filePath)
      throws IllegalArgumentException;

  /**
   * This method reads all the files present in a particular directory.
   *
   * @param filePath is the path of the file
   * @return a map where key represents a file name and the value is the content of the file
   */
  Map<String, List<String[]>> readAllFiles(String filePath);

  /**
   * This method saves a created portfolio to the file.
   *
   * @param stocks            is the stock list in that portfolio
   * @param portfolioName     is the name of the portfolio
   * @param resourceDirectory is the directory where the file will be stored
   */
  void savePortfolio(List<String[]> stocks,
      String portfolioName, String resourceDirectory);
}
