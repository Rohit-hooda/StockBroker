# Setup steps to run the Stocks-app Application

1. Open `Intellij Idea` and create a new java project let's say `Stock app`
2. Download and extract the `stocks-app` zip file and copy the `src`, `test`, `res` folder into the
   newly created intellij java project
3. Then mark the `src` directory as the 'sources root directory' by right-clicking on the `src`
   and then selecting `Sources Root` from the mark directory option
4. Similarly, mark the `test` directory as the test sources root directory by right-clicking on
   the `test` and selecting `Test Sources Root` from the mark directory option
5. After completing the above steps 3 and 4 the color `src` folder will be 'blue' and color
   of `test`
   directory will be 'green'
6. Click on `File` on top left corner of the screen and click `Project Structure`
7. Click on `Libraries` and then click on `+` and select 'Java'
8. Navigate to the current intellij project folder from the dialog box and open `res` folder that is
   present in the current intellij project
9. Add `jcommon-1.0.23.jar` and hit `enter`
10. Follow the steps 7 through 9 and add `jfreechart-1.0.19.jar`
11. After adding both the libraries your `Libraries` in the project structure should look like
    this: <br/>![Library Structure](screenshots/Library%20Structure.png)
12. After this open `test` folder and navigate to 'controller package' and
    open `controllerTest.java`
13. Thereafter, add `junit4` to the class path by clicking on add `junit4` to the class path error
14. Now to run the application, create a new run configuration by clicking on 'Edit Configuration'
    dropdown menu option on top right corner of intellij
15. Add a new configuration by clicking `+` and selecting `Application`
16. Enter the name of the main class in `Build and Run` and provide arguments like
    this: `PBBOCARQLUUHKQBA GUI` and click `OK` : where first string is our api key and other
    specifies the interface to run that can be either `TEXT` or `GUI`. Here, this configuration will
    run the graphical user interface for the application
    [`PBBOCARQLUUHKQBA` is our API key, if it does not work then this can be replaced while creating the Build Configuration and passing another API Key]
17. The configuration file would look
    like: <br/>![Build Config](screenshots/Build%20Config.png)
18. Now run the application by clicking the `play` button on top right corner of the intellij window
19. To run the `text` based version of the application follow the same step 16 and 17 but, in the
    program arguments specify `PBBOCARQLUUHKQBA TEXT`

#### `NOTE:` Application will not run if there is no res folder in the project directory and the jar files are not imported in the Project Modules

## Setup steps to create a portfolio and view its value, performance for text based Stocks-app Application

1. To create a new `Flexible Portfolio`, input `2` and hit `enter`
2. To create a new flexible portfolio by providing name, input `1` and hit `enter`
3. Give a non-blank name to the portfolio, let's say `Retirement` and hit `enter`. The portfolio
   will be created
4. To buy a stock and add it to the created portfolio, input `2` and hit `enter`
5. Give a valid `ticker name` let's say `AAPL` and hit enter. It will prompt to enter `quantity`
   let's say `20` and `date` as `2022-10-01` and hit enter. The buy operation will be successfully
   completed. To buy more stocks to the portfolio follow the steps 8 and 9 again
6. To sell a stock from the portfolio input `3` and hit `enter`
7. Give a valid `ticker name` let's say `AAPL` and hit enter. It will prompt to enter `quantity`
   let's say `5` and `date` as `2022-10-11` and hit enter. The sell operation will be successfully
   completed. To sell more stocks from the portfolio follow the steps as 10 and 11 again
8. To view the amount of money invested in a portfolio i.e. the cost basis, input `4` and
   hit `enter`
9. You will see a list of portfolio names that are available in the app. Give a
   valid `portfolio name` let's say `Retirement` and a date as `2022-10-09` and hit `enter`. To view
   the cost basis of the portfolio on different dates follow the same process as above
10. To view the value of the portfolio input `5` and hit `enter`
11. You will see a list of portfolio names that are available in the app. Give a
    valid `portfolio name` let's say `Retirement` and a date as `2022-10-12` and hit `enter`. To
    view the value of the portfolio on different dates follow the same process as above
12. To view the performance of the portfolio input `6` and hit `enter`
13. You will see a list of portfolio names that are available in the app. Give a
    valid `portfolio name` let's say `Retirement` and hit `enter`. You will be asked to enter date
    in `yyyy-MM-dd` format. Enter the `date` from which you want to view the performance of the
    portfolio let's say `2022-09-30`. Enter a `date`
    up to which you want to view the performance say `2022-10-30` and hit `enter` and you should be
    able to see a bar graph showing performance and the scale for the graph annotated.
14. To view the composition of the portfolio input `7` and hit `enter`
15. You will see a list of portfolio names that are available in the app. Give a
    valid `portfolio name` let's say `Retirement` and give a `date` let's say `2022-10-13`
16. To goto the main menu of the application input `back` and hit `enter`
17. Input `exit` the application type `exit` and hit `enter`

# Setup steps to create a portfolio and view its composition, performance from Graphical User Interface

1. Run the application by creating the build configuration as specified in the 15-18 steps in the
   section :`Setup steps to run the Stocks-app Application`
2. Click on the `Add Portfolio` button. You will be prompted to enter a portfolio name. Enter let'
   say `Retirement` and click `OK`
3. Select the `Retirement` portfolio from the drop down
4. Thereafter, to buy stocks to the portfolio by specifying the amount click on `Buy By Amount`
   button
5. Enter the amount of money let's say `20000`. Increment the number of stocks let's say `2` by
   clicking the up arrow besides the `Number of stock` text field
6. Enter a valid date let's say `2022-10-10`. Then you will be prompted to enter `Ticker` name let's
   say `GOOG` and the proportion of that stock let's say `40`. Add another stock let's say `AAPL`
   and enter the proportion `60` and click `Buy Stocks` to complete purchase of the stocks.
7. To invest as a dollar-cost averaging strategy over a period of time, click on `Strategy`. Enter
   the amount to invest periodically, enter the number of stocks to include in the strategy by
   incrementing the spinner in the UI, select the frequency of investment which can be either weekly
   or monthly and specify a from and to date. For example amount is `2000`, number of stocks is `2`,
   frequency is selected as `MONTHLY`, from date is `2021-11-11` and to date is `2022-11-11`. Now you will
   be able to see a number of textfields to enter the stock names and their respective proportions
   in the strategy. Let's say you enter `AAPL` and `40` in first and `VZ` and `60` in second. Now, you can
   click on `Apply Strategy` to implement the strategy and perform purchases monthly on 11th of
   every month from 2021-11-11 to 2022-11-11.
7. Click on `Compositon` button to view the composition of the portfolio. Enter a valid date let's
   say `2022-11-10` and click on `View Composition` button.
8. Click on `Value` button to view the value of the portfolio. Enter a valid date let's
   say `2022-11-10` and click on `View Value` button.
9. Click on `Cost Basis` button to view the money invested upto a certain date in the portfolio.
   Enter a valid date let's say `2022-11-10` and click on `View Cost Basis` button.
8. To view the `Performance` click on `Performance` button. Enter a `from` date let's
   say `2022-10-11` and `to` date let's
   say `2022-11-30` and click on `Show Performance` button.


