import controller.Feature;
import controller.GUIController;
import controller.StockAppController;
import controller.StockAppControllerImpl;
import model.User;
import model.UserImpl;
import view.IView;
import view.StockAppGUIView;
import view.StockAppView;
import view.StockAppViewImpl;

/**
 * Represents the driver of the stock application working with a model, view and controller. It is
 * responsible to create the objects for model, view and controller. It also specifies the input
 * stream to take the inputs from an interface, output stream to show the results of tasks happening
 * in the application. It also specifies the base location of required resources for the application
 * to run. It can bring up a text-based or a GUI based interface according to the arguments
 * provided.
 */
public class Driver {

  private static final String RESOURCES_DIRECTORY = "res/";
  private static final String STOCK_PRICE_DATA_DIRECTORY = "stock_value_data/";

  /**
   * Main method to set all the necessary objects and information and invoke the running of the
   * application by calling the controller.
   *
   * @param args params to be passed to main method including api-key and kind of interface gui or
   *             text
   */
  public static void main(String[] args) {

    if (args.length < 2) {
      throw new IllegalArgumentException("usage: java -cp stocks-app.jar Driver <api-key> "
          + "<interface-type>   - interface-type can be 'gui' or 'text'");
    }
    String apiKey = args[0];
    String userInterface = args[1];
    User user = new UserImpl();
    if (userInterface.equalsIgnoreCase("GUI")) {
      Feature controller = new GUIController(user, RESOURCES_DIRECTORY, apiKey);
      IView view = new StockAppGUIView();
      controller.setView(view);
    } else if (userInterface.equalsIgnoreCase("text")) {
      StockAppView view = new StockAppViewImpl(System.out);
      StockAppController controller = new StockAppControllerImpl(user, view, System.in,
          RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, apiKey);
      controller.run();
    } else {
      throw new IllegalArgumentException(
          "usage: java -cp stocks-app.jar Driver <api-key> <interface-type>   - interface-type "
              + "can be 'gui' or 'text'");
    }
  }
}
