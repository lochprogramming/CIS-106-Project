// **********************************************************
// Title: Report Display Controller
// File: ReportDisplayController.java
// Author: Matt Lochman
// Description: Controller for the Report stage.
// **********************************************************

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.collections.ObservableMap;
import javafx.scene.chart.*;

public class ReportDisplayController {
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields used by the report controller

   private MainDisplayController mainController;//Need to call this controllers public methods.
   private Stage primaryStage;//For referencing the main window
   private ObservableList<Credit> creditDisplayed; // The displayed credits from the main controller
   private ObservableList<Debit> debitDisplayed; // The displayed debits from the main controller
   
   //Create the objects needed for the report scene.  
   //Layed out in the FXML file from Scene Builder
   
   @FXML
   private TextArea creditTextArea;

   @FXML
   private TextArea debitTextArea;
   
   @FXML
   private TableView<Credit> creditTable;

   @FXML
   private TableColumn<Credit,String> creditDescriptionColumn;

   @FXML
   private TableColumn<Credit,String> creditDateColumn;

   @FXML
   private TableColumn<Credit,String> creditAmountColumn;

   @FXML
   private TableColumn<Credit,String> creditCategoryColumn;

   @FXML
   private TableColumn<Credit,String> creditTypeColumn;

   @FXML
   private TableView<Debit> debitTable;

   @FXML
   private TableColumn<Debit, String> debitDescriptionColumn;

   @FXML
   private TableColumn<Debit, String> debitDateColumn;

   @FXML
   private TableColumn<Debit, String> debitAmountColumn;

   @FXML
   private TableColumn<Debit, String> debitCategoryColumn;

   @FXML
   private TableColumn<Debit, String> debitTypeColumn;   
   
   @FXML
   private Label totalCreditLabel;

   @FXML
   private Label totalDebitLabel;

   @FXML
   private Label totalNetLabel;

   @FXML
   private PieChart creditPieChart;

   @FXML
   private PieChart debitPieChart;

   @FXML
   private StackedBarChart<String, Double> creditStackedBarChart;

   @FXML
   private StackedBarChart<String, Double> debitStackedBarChart;
   
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Public accessor and mutator methods used by child dialogs 

   public void setMainController(MainDisplayController mdc) {
      mainController = mdc;
      generateContent(); // generate the summary tab
      generateCreditPieChart(); // generate the credit pie chart
      generateDebitPieChart(); // generate the debit pie chart
      generateCreditStackedBarChart(); // generate the credit stacked bar chart
      generateDebitStackedBarChart(); // generate the debit stacked bar chart
   }
   public void setPrimaryStage(Stage stage) {
      primaryStage = stage;
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Section containing the different methods used in the menus

   @FXML
   private void closeMenuAction(ActionEvent event) { //Closes report and returns to the main display
      ((Stage) creditTable.getScene().getWindow()).close(); //closes the window
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Major methods

   private void generateContent() {
      //Grabs the displayed data from the main scene
      creditDisplayed = FXCollections.observableArrayList(mainController.getDisplayedCreditData());
      debitDisplayed = FXCollections.observableArrayList(mainController.getDisplayedDebitData());
      
      //copies all of the summary statistics from the main scene
      totalCreditLabel.setText(mainController.getTotalCreditLabel().getText());
      totalDebitLabel.setText(mainController.getTotalDebitLabel().getText());
      totalNetLabel.setText(mainController.getTotalNetLabel().getText());
      totalNetLabel.setStyle(mainController.getTotalNetLabel().getStyle());
      creditTextArea.setText(mainController.getCreditTextArea().getText());
      debitTextArea.setText(mainController.getDebitTextArea().getText());
      
      //Grab the outlier lists from the main scene
      ObservableList<Double> creditOutliers = mainController.getCreditOutliers();
      ObservableList<Double> debitOutliers = mainController.getDebitOutliers();
      
      //Extracts the Credit outliers.
      if (creditOutliers.size() > 0) {
         for (int i = 0; i < creditOutliers.size(); i++)
            for (int j = 0; j < creditDisplayed.size(); j++)
               if (creditDisplayed.get(j).getAmount() == creditOutliers.get(i))
                  creditTable.getItems().add(creditDisplayed.get(j));
      }
      
      //Extracts the Debit outliers.
      if (debitOutliers.size() > 0) {
         for (int i = 0; i < debitOutliers.size(); i++)
            for (int j = 0; j < debitDisplayed.size(); j++)
               if (debitDisplayed.get(j).getAmount() == debitOutliers.get(i))
                  debitTable.getItems().add(debitDisplayed.get(j));
      }
   }
   
   private void generateCreditPieChart() {
      //initialize the list for the pie chart slices and the category + amount combinations
      ObservableList<PieChart.Data> creditPieChartData = FXCollections.observableArrayList();
      ObservableMap<String, Double> categoryAmounts = FXCollections.observableHashMap();
      
      //Count up all the amounts for each category in the displayed credits.
      for (int i = 0; i < creditDisplayed.size(); i++) {
         if (categoryAmounts.containsKey(creditDisplayed.get(i).getCategory())) { // if it's already in the list
            double amount = categoryAmounts.get(creditDisplayed.get(i).getCategory()); // grab the current value
            amount += creditDisplayed.get(i).getAmount(); // add this to the new amount
            categoryAmounts.put(creditDisplayed.get(i).getCategory(),amount); // replace the old amount in the map
         } else
            categoryAmounts.put(creditDisplayed.get(i).getCategory(),creditDisplayed.get(i).getAmount()); // create a map entry
      }
      
      //grab the current total of all credits
      double currentCreditSum = Double.parseDouble(totalCreditLabel.getText().substring(16).replaceAll(",",""));
      
      for (int i = 0; i < Credit.getCreditCategorySize(); i++)
         if (categoryAmounts.containsKey(Credit.getCreditCategory(i))) {
            double sliceProportion = categoryAmounts.get(Credit.getCreditCategory(i)) / currentCreditSum; // calculate proportion
            String sliceLabel = String.format("%.2f%%",sliceProportion * 100); // create a label with the percent
            creditPieChartData.add(
                  new PieChart.Data(Credit.getCreditCategory(i) + " (" + sliceLabel + ")"
                           , categoryAmounts.get(Credit.getCreditCategory(i))));  //Add a slice with the category and percent
         }
         
      creditPieChart.setData(creditPieChartData); // create the pie chart
      creditPieChart.setTitle("Credits by Category"); // add its title
   }
   
   private void generateDebitPieChart() {
      //initialize the list for the pie chart slices and the category + amount combinations
      ObservableList<PieChart.Data> debitPieChartData = FXCollections.observableArrayList();
      ObservableMap<String, Double> categoryAmounts = FXCollections.observableHashMap();
      
      //Count up all the amounts for each category in the displayed credits.
      for (int i = 0; i < debitDisplayed.size(); i++) {
         if (categoryAmounts.containsKey(debitDisplayed.get(i).getCategory())) {// if it's already in the list
            double amount = categoryAmounts.get(debitDisplayed.get(i).getCategory());// grab the current value
            amount += debitDisplayed.get(i).getAmount();// add this to the new amount
            categoryAmounts.put(debitDisplayed.get(i).getCategory(),amount);// replace the old amount in the map
         } else
            categoryAmounts.put(debitDisplayed.get(i).getCategory(),debitDisplayed.get(i).getAmount());// create a map entry
      }
      
      //grab the current total of all debits
      double currentDebitSum = Double.parseDouble(totalDebitLabel.getText().substring(15).replaceAll(",",""));
      
      for (int i = 0; i < Debit.getDebitCategorySize(); i++)
         if (categoryAmounts.containsKey(Debit.getDebitCategory(i))) {
            double sliceProportion = categoryAmounts.get(Debit.getDebitCategory(i)) / currentDebitSum;// calculate proportion
            String sliceLabel = String.format("%.2f%%",sliceProportion * 100);// create a label with the percent
            debitPieChartData.add(
                  new PieChart.Data(Debit.getDebitCategory(i) + " (" + sliceLabel + ")"
                           , categoryAmounts.get(Debit.getDebitCategory(i))));  //Add a slice with the category and percent
         }
         
      debitPieChart.setData(debitPieChartData);// create the pie chart
      debitPieChart.setTitle("Debits by Category");// add its title
   }
   
   private void generateCreditStackedBarChart() {
      // create a list and maps to hold the information needed to build the charts
      ObservableList<String> creditDateStrings = FXCollections.observableArrayList();
      ObservableMap<String, ObservableMap<String, Double>> creditBarChartData = FXCollections.observableHashMap();
      
      //We're going to loop through all of the credits and collect information as follows: 
      //For each category, we want the amount spent in that category for each (Year, Month) pair.
      for (int i = 0; i < creditDisplayed.size(); i++) { // iterate through all credit entries
         if (creditBarChartData.containsKey(creditDisplayed.get(i).getCategory())) { // see if that category has already been added
            // grab the (year, Month) pair from the current credit entry
            String dateString = creditDisplayed.get(i).getYear() + ", " + creditDisplayed.get(i).getMonthString();
            //Check if that pair has been registered for this category yet
            if (creditBarChartData.get(creditDisplayed.get(i).getCategory()).containsKey(dateString)) {
               // if it has, add this amount to the current amount.
               double amount = creditBarChartData.get(creditDisplayed.get(i).getCategory()).get(dateString);
               amount += creditDisplayed.get(i).getAmount();
               creditBarChartData.get(creditDisplayed.get(i).getCategory()).put(dateString, amount);
            } else {
               // if it hasn't been registered, create an entry for that (year, month) pair.
               creditDateStrings.add(dateString);
               creditBarChartData.get(creditDisplayed.get(i).getCategory()).put(dateString, creditDisplayed.get(i).getAmount());
            }
         } else { 
            // If the category wasn't already in the mapping, add it along with the current (yaer, month) pair and amount
            ObservableMap<String, Double> newEntry = FXCollections.observableHashMap();
            String dateString = creditDisplayed.get(i).getYear() + ", " + creditDisplayed.get(i).getMonthString();
            newEntry.put(dateString, creditDisplayed.get(i).getAmount());
            creditDateStrings.add(dateString);
            creditBarChartData.put(creditDisplayed.get(i).getCategory(), newEntry);
         }
      }
      
      // iterate through all the possible categories. 
      for (int i = 0; i < Credit.getCreditCategorySize(); i++) {
         // check if there was spending in that category.
         if (creditBarChartData.containsKey(Credit.getCreditCategory(i))) {
            //if there was spending, we'll create a series for the category.
            XYChart.Series<String, Double> dataSeries = new XYChart.Series<String, Double>();
            dataSeries.setName(Credit.getCreditCategory(i));
            
            // iterate through each of the (year, month) pairs that were encountered)
            for (int j = 0; j < creditDateStrings.size(); j++) {
               // check if the (year, month) pair was actually part of this category
               if (creditBarChartData.get(Credit.getCreditCategory(i)).containsKey(creditDateStrings.get(j))) {
                  //if so, add an entry in that categories series
                  dataSeries.getData().add(new XYChart.Data<String, Double>(creditDateStrings.get(j), 
                        creditBarChartData.get(Credit.getCreditCategory(i)).get(creditDateStrings.get(j))));
               }
            }
            // add that series to the chart
            creditStackedBarChart.getData().add(dataSeries);
         }
      }
   }
   
   private void generateDebitStackedBarChart() {
      // create a list and maps to hold the information needed to build the charts
      ObservableList<String> debitDateStrings = FXCollections.observableArrayList();
      ObservableMap<String, ObservableMap<String, Double>> debitBarChartData = FXCollections.observableHashMap();
      
      //We're going to loop through all of the debits and collect information as follows: 
      //For each category, we want the amount spent in that category for each (Year, Month) pair.
      for (int i = 0; i < debitDisplayed.size(); i++) { // iterate through all debit entries
         if (debitBarChartData.containsKey(debitDisplayed.get(i).getCategory())) { // see if that category has already been added
            // grab the (year, Month) pair from the current debit entry
            String dateString = debitDisplayed.get(i).getYear() + ", " + debitDisplayed.get(i).getMonthString();
            //Check if that pair has been registered for this category yet
            if (debitBarChartData.get(debitDisplayed.get(i).getCategory()).containsKey(dateString)) {
               // if it has, add this amount to the current amount.
               double amount = debitBarChartData.get(debitDisplayed.get(i).getCategory()).get(dateString);
               amount += debitDisplayed.get(i).getAmount();
               debitBarChartData.get(debitDisplayed.get(i).getCategory()).put(dateString, amount);
            } else {
               // if it hasn't been registered, create an entry for that (year, month) pair.
               debitDateStrings.add(dateString);
               debitBarChartData.get(debitDisplayed.get(i).getCategory()).put(dateString, debitDisplayed.get(i).getAmount());
            }
         } else { 
            // If the category wasn't already in the mapping, add it along with the current (yaer, month) pair and amount
            ObservableMap<String, Double> newEntry = FXCollections.observableHashMap();
            String dateString = debitDisplayed.get(i).getYear() + ", " + debitDisplayed.get(i).getMonthString();
            newEntry.put(dateString, debitDisplayed.get(i).getAmount());
            debitDateStrings.add(dateString);
            debitBarChartData.put(debitDisplayed.get(i).getCategory(), newEntry);
         }
      }
      
      // iterate through all the possible categories. 
      for (int i = 0; i < Debit.getDebitCategorySize(); i++) {
         // check if there was spending in that category.
         if (debitBarChartData.containsKey(Debit.getDebitCategory(i))) {
            //if there was spending, we'll create a series for the category.
            XYChart.Series<String, Double> dataSeries = new XYChart.Series<String, Double>();
            dataSeries.setName(Debit.getDebitCategory(i));
            
            // iterate through each of the (year, month) pairs that were encountered)
            for (int j = 0; j < debitDateStrings.size(); j++) {
               // check if the (year, month) pair was actually part of this category
               if (debitBarChartData.get(Debit.getDebitCategory(i)).containsKey(debitDateStrings.get(j))) {
                  //if so, add an entry in that categories series
                  dataSeries.getData().add(new XYChart.Data<String, Double>(debitDateStrings.get(j), 
                        debitBarChartData.get(Debit.getDebitCategory(i)).get(debitDateStrings.get(j))));
               }
            }
            // add that series to the chart
            debitStackedBarChart.getData().add(dataSeries);
         }
      }
   }
   
   private void setDefaultColumnSize() { // Used by the Initializer to fix the columns
      double[] widths = {10, 50, 20, 10, 10};//define the width of the columns

      //calculate the sum of the width
      double sum = 0;
      for (double i : widths) 
         sum += i;

      //set the width to the columns
      for (int i = 0; i < widths.length; i++) {
         creditTable.getColumns().get(i).prefWidthProperty().bind(
            creditTable.widthProperty().multiply(widths[i] / sum));
            //---------The exact width-----------^-------------^
         debitTable.getColumns().get(i).prefWidthProperty().bind(
            debitTable.widthProperty().multiply(widths[i] / sum));
            //---------The exact width----------^-------------^
      }
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Initializer method
   
   //Method is run when the MainDisplay is created for the first time.  
   //Sets up the columns as well as creates the table
   //Sets up the Date tree

   public void initialize() {
      setDefaultColumnSize(); // Create the column spacing in the tables.
      
      //Sets up the columns in the credit table to dynamically change based on the ObservableList creditData
      creditDescriptionColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("description"));
      creditDateColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("date"));
      creditAmountColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("amountString"));
      creditCategoryColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("category"));
      creditTypeColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("type"));
      
      //Sets up the columns in the debit table to dynamically change based on the ObservableList debitData
      debitDescriptionColumn.setCellValueFactory(new PropertyValueFactory<Debit, String>("description"));
      debitDateColumn.setCellValueFactory(new PropertyValueFactory<Debit, String>("date"));
      debitAmountColumn.setCellValueFactory(new PropertyValueFactory<Debit, String>("amountString"));
      debitCategoryColumn.setCellValueFactory(new PropertyValueFactory<Debit, String>("category"));
      debitTypeColumn.setCellValueFactory(new PropertyValueFactory<Debit, String>("type"));
   }
}