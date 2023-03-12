## Design Discussion

We have implemented this application using `Model-View-Controller` Pattern. It helps to separate
responsibilities of showing data on `text-based` or any `interface`, handle data using `Model` layer
and handle delegation using `Controller` layer. This also keeps our application scalable in terms
of let's say `view` is going to change to some other interface then there will be minimum changes
required in the controller to achieve this.

### Model

#### Overview of design changes as part of assignment 6 in model:

1. We created another method to buy fractional shares by specifying the amount and a set of stocks
   with their proportions and a trade date and to utilise existing functionality of adding a trade
   in flexible portfolio, we just calculated the quantities with the help of proportion and stock
   prices on the date and used the add trade feature.
2. To implement strategy for a range of dates providing a set of stock names and their proportions
   to be bought on provided frequency within the date range, we interpret it as performing
   fractional trades on multiple dates in the date range. We utilised the same feature that we
   developed to add fractional trade onto the portfolio and call it multiple time on a range of
   dates.
3. Rest of the features of the model were readily usable for us to integrate with new UI GUI with
   the help of another controller.

#### Overview of design changes as part of assignment 5 in model:

1. To utilise existing model for Stock and use it for buy/sell operation, we created another model '
   Trade' wherein positive quantity represents buy and negative quantity represents sell. Earlier,
   stock could not be initialised with a negative quantity but then to support Trade as well we
   removed this validation from model. This does not mean that user will be able to input negative
   quantity, as it is validated before only in controller layer.
2. Earlier there was only one kind of portfolio that too was fixed. So, earlier implementation was
   done in such a way that PortfolioImpl had only getters for list of stocks and portfolio name,
   while the features to show value and composition were being handled by the main model User. We
   changed this design and brought down the responsibility of showing composition and value to
   Portfolio model itself. This helped in making InflexiblePortfolio implementation relevant to its
   model name.
3. Now with this better modularised model design, we were able to pick the common features present
   in both kinds of portfolios such that they could somehow be an implementation of same interface.
   In this case, flexible portfolio supports some additional features other than the previous
   Portfolio, so we decided to extend another interface from Portfolio named FlexiblePortfolio that
   has methods to view cost basis, performance, buy and sell stock.
4. We carefully observed the implementation of getting value of flexible and inflexible portfolios
   which could be generalised to calculate the value of portfolio on a given date. This led us to
   create an abstract class called AbstractPortfolio and we saved code duplication and reused entire
   existing implementation. This way we kept our code still scalable to support any other
   implementation of portfolio that may come up in future.
5. Earlier user was handling Portfolio and Stock models, taking data out, processing and enabling
   the features of the application which meant User was responsible to take out data from both of
   those models. Now, in the newer design, user only delegates the portfolio-specific tasks to
   portfolio objects and maintains only collections of flexible and inflexible portfolios to query
   rather than performing any logic here itself.

#### Features of Model:

1. We have taken `Stock` as `Model` which enables its client to get the name of the stock
   and a quantity. It is a `package private interface` because for this application a stock will at
   minimum need to provide these methods to its clients, and we do not want to have its
   implementation or usage out of the model package.
2. We have taken `Portfolio` as `Model` which enables its client to get the name of the portfolio
   and
   details of stocks in it. Since it shows details of stocks, its implementation will
   result in `Composition` of Stock implementation classes. It is a `package private interface`
   because
   for this application, a Portfolio will at minimum need to provide these methods to its clients,
   and we do not want to have its implementation or usage out of the model package. It provides
   methods to save and retrieve Portfolio from files.
3. We have taken `User` as `Main model` for the application which will provide actual features of
   the whole application like creating multiple portfolios and viewing their compositions and
   values. It
   is `public interface` because it keeps the main model scalable for future enhancements like to
   support different kinds of Users of the application like stockbroker. Since User handles all the
   data related to the portfolios and stocks, User implementations are a `composition` of Stock and
   Portfolio implementations and make sure that these internal models are not directly exposed out
   of the model package. It provides methods to create both kinds of portfolios, view their
   composition and values. It is enhanced to show cost basis and performance of flexible portfolios.
4. We have taken `Flexible Portfolio` as another `model` to support the mutable version of
   portfolios that enables buying and selling of stocks. This extends the `Portfolio` interface
   hence apart from existing features of creating a portfolio, viewing its composition and value it
   has additional features to determine cost basis and show performance of the portfolio on a range
   of dates.
5. We have created an abstract class called `AbstractPortfolio` that contains common features
   required
   to serve both kinds of Portfolios like getting their value.
6. We have created `Trade` as a `Model`. In internal terminology of the application, we consider
   a `buy or sell` as a Trade that has stock details like a name and quantity with addition of a
   date on
   which that trade is performed. So, Trade is a `composition of Stock` and a date. A positive
   quantity in a trade corresponds to a buy of stock whereas a negative quantity in a trade
   represents selling of a stock internally.

### View

#### Text-based View

1. We have taken `StockAppView` as `View` which enables printing various messages as text on the
   print
   stream(like System console for text-based interface) configured.
2. Since this is `View` layer, we have not implemented any filtering or logical operations in this
   layer. Its responsibility is just to print whatever it gets delegated from controller.
3. Enhanced view with additional methods to show performance of flexible portfolios as a bar graph
   of store with respective scales annotated.

#### GUI-based View

1. We created an interface called `IView` that has necessary features for any kind of view interface
   to be supported by this application and is not bound by a printstream as before.
2. We implemented this interface `StockAppGUIView` that implements a Java Swing based User interface
   to support the features of the application.
3. Usual flow for any feature to be invoked is whenever an action happens on the UI, listener method
   stays with the controller which interprets that action and tells the view what to do next. Every
   flow in the UI works the similar way. There are no action Listeners in this class, and we made
   sure to forward the invocations as it is to the controller to decide and tell the view what to do
   next.
4. We used external library `JFreeChart` to show a line chart of the performance of the portfolio
   over a range of time.

### Controller

#### Overview of design changes as part of assignment 6 in Controller:

1. Since previously developed controller worked closely with a Text-based interface, we had to
   implement another controller that could interact with our GUI. Although, we tried as much as
   possible to keep this version of controller to be free from a particular interface based view and
   could in future be generalised for another kind of interface to support.
2. To work with GUI we created a `Feature` interface that contains all the features of the
   application. An implementation of this interface comes out to be `GUIController` that takes an
   object of type `IView` which is again an interface representing the features of the view not tied
   to any kind of interface.
3. We designed this `GUIController` to function as the definition of controller ie, tells view and
   model what to do when. All the method implementations align to the features of the application
   and coordinate among the view and user model to run the application.

#### Overview of design changes as part of assignment 5 in Controller:

1. As we implemented Command Design Pattern for controller to delegate each menu option previously
   to fulfil features of inflexible portfolios. We extended the similar design but now main
   controller
   has a main menu that enables to go inside two sub-menus each serving a flexible and inflexible
   portfolio.
2. We created AppInputCommand interface that is responsible to take various kinds of inputs from
   user like date, quantity and portfolioname which were being asked in multiple menu options. So,
   to avoid code duplication we created a separate interface itself that lets mutiple commands to
   get inputs from user without repeating the same code everywhere.
3. We separated out File read and write operations from controller layer and moved them to utilities
   and generalised them to read and write files as much as possible without tying them to context of
   the application. We wrote it in such a way that if in future we have to support different file
   formats then we just need to write another implementation for FileUtil class and we are good.
4. Earlier while initialisation, we had a fixed set of ticker symbols that we supported and we
   provided it through a static file, now this has been changed to directly depend upon the API
   calls. And also for API calls we have made the design generic enough that to support another API
   provider one new implementation will be required that makes the API call and saves the response
   to a file that can later be processed by our generic file reader.
5. Earlier for data of stock prices, we provided few static files to enable viewing value of
   portfolio. Now, our app makes an API call to fetch the stock prices data at one go whenever it
   starts and a new ticker symbol as introduced into the application. This way we are not bound by a
   static file anymore and can query a big range of dates supported by the API provider. To minimise
   the number of API calls, we cache this data of stock prices in run time such that if same stock
   is used in multiple portfolios then data will be brought in only once into the application
   through API and then you can perform any number of view value, performance queries on it.

#### Features of Controller:

1. We have taken `StockAppController` as `Controller` interface that helps in running the
   application by
   co-ordinating with and delegating tasks to other layers of `Model` and `View`.
2. `StockAppControllerImpl` is the implementation of `StockAppController` that acts as the main
   controller by implementing `Command Design Pattern` by delegating pieces of tasks to different
   `smaller-purpose feature-specific commands` that can be further reused in the future. It takes
   care of initial operations like asking model to load existing portfolios and initial data from
   cache in the application. It also takes care of closing operations like saving of created
   portfolios and cleanup of unnecessary data.
    1. We have created `AppCommand` as an interface that enables a controller to delegate tasks to
       various commands and enable the features of the application by passing them reference to
       model and view.
    2. `InflexiblePortfolioCommand` handles all the features related to Inflexible or fixed
       portfolios. It delegates tasks further to various commands to enable features of portfolio
       creation, viewing composition and values all are accomplished from a separate command.
    2. `FlexiblePortfolioCommand` handles all the features related to Flexible or mutable
       portfolios. It delegates tasks further to various commands to enable features of a flexible
       portfolio
       creation, viewing cost basis, composition, values and performance all are accomplished from a
       separate command.
    2. `PortfolioCreationCommand` is used to create portfolio by taking inputs from user, calling
       model methods to create data and delegates to view to show appropriate
       messages to the view
    2. `StockCompositionCommand` is used to view the composition of portfolio by fetching the
       composition details from the user model and passing it on to view to show it to the user. It
       can be fetched for a particular date for flexible portfolio while it does not matter in case
       of an inflexible portfolio as its composition will never change once created.
    3. `StockValueCommand` is used to view the value of flexible and inflexible portfolios by
       passing the required price
       data to the model in order to fetch the value of the portfolio and then show it to the user.
    4. `CostBasisCommand` is used to fetch and show the money invested in a flexible portfolio so
       far by a given
       date which includes all the purchases of stocks along with commission charge incurred per
       trade.
    5. `PortfolioPerformanceCommand` is used to fetch the values of a flexible portfolio over a
       given range of dates and show it to the user as a bar graph. It internally
       implements `Visitor Design Pattern` by passing an instance of `PortfolioScalerCommand` which
       takes in raw data of values of portfolio over a range and scales it suitably in order to
       conveniently show them through view as a bar graph.
    6. `TradeCommand` is used to add a trade into a flexible portfolio by taking inputs from user.
    7. CommissionChargeCommand is used to set a commission charge in the app which can be used while
       calculating the cost basis of a flexible portfolio. By default, commission charge is
       initialised as `$10` which can be changed at any point in the application.
    7. `AppInputCommand` is another interface we have created to improve code reuse for the places
       where taking input from user is required in multiple commands. So, it contains methods to
       take input for a date, portfolio name and valid quantity that are being used in multiple
       features of the application. It is implemented by `StockAppInputCommand` which is tied to a
       text-based user interface.
4. We have kept all these implementations of commands as `package private` so that no one
   gets access to these internal controllers out of the controller package and only
   StockAppControllerImpl is allowed to use them by `composition`. Also, we have restricted these
   classes from inheritance by adding `final` keyword as these are very implementation and logic
   specific classes.
5. `StockAppControllerImpl` is kept as `public class` as it is going to be used outside the
   controller package by the Driver class which will invoke the start of the application.
6. We enhanced the design of our controller to further modularise the code and avoid duplication by
   breaking them into multiple commands and generalising the commands wherever possible to reuse the
   code like `StockCompositionCommand` and `StockValueCommand` both serve both kinds of portfolios (
   Flexible and Inflexible) in the application.

### Utility Functions

1. We have created utilities like `ParamNullChecker`, `FileScannerFunction`, `FileUtil`
   , `StockDataHydrator`, `StockPriceUtil`, `ValidDateChecker` and `StringAppender` in
   order to utilise existing standard functional interfaces and use them to avoid `code duplication`
   and better `modularity` in overall code.
2. We have created enum classes like `ApiPeriod`, `TradeType`, `TypeOfPortfolio` to represent a
   fixed set of values in some cases.
