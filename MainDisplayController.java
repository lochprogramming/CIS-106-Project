// **********************************************************
// Title: Main Display Controller
// File: MainDisplayController.java
// Author: Matt Lochman
// Description: Controller for MainDisplay stage.
// **********************************************************

////////////////////////////////////////////////////////////////
//Things to implement still.
//7. Allow these reports to be saved as a pdf or printed.
//10. Undo/Redo features.
//11. Double check all methods for passing original objects instead of copies.
//12. Confirm security of private objects.
//13. Consider when fields should be static.
////////////////////////////////////////////////////////////////

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.CheckBox;
import javafx.util.Callback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableMap;
import javafx.application.Platform;



public class MainDisplayController 
{
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields used by the main controller

   private MainDisplayController mainController;//Need to call this controllers public methods.
   private Stage primaryStage;//For referencing the main window
   private File currentFile = null; //For storing the current file
   private boolean needsSaved = false; //Flag for unsaved changes.
        
   //Main Data fields.
   private ObservableList<Credit> creditData = FXCollections.observableArrayList(Credit.creditExtractor);
   private ObservableList<Debit> debitData = FXCollections.observableArrayList(Debit.debitExtractor);
   
   //Subsets of main data fields with visibility attribute true.
   //Table views and calculations are only made on visible data.
   private FilteredList<Credit> creditVisible = new FilteredList<Credit> (creditData, Credit.creditVisibilityFilter());
   private FilteredList<Debit> debitVisible = new FilteredList<Debit> (debitData, Debit.debitVisibilityFilter());
   
   //Second parallel lists to the above FilteredLists. 
   //TableView doesn't allow column sorting with FilteredLists, so we create an ObservableList version.
   private ObservableList<Credit> creditDisplayed = FXCollections.observableArrayList(creditVisible);
   private ObservableList<Debit> debitDisplayed = FXCollections.observableArrayList(debitVisible);
   
   //Use for the reports
   private ObservableList<Double> creditOutliers = FXCollections.observableArrayList();
   private ObservableList<Double> debitOutliers = FXCollections.observableArrayList();

   //Date maps for the date tree.  Maps year -> Month list.
   private ObservableMap<String, ObservableList<String>> creditDateMap = FXCollections.observableHashMap();
   private ObservableMap<String, ObservableList<String>> debitDateMap = FXCollections.observableHashMap();
   
   //The root nodes for the dateTree
   //Transactions is hidden so it looks like two trees in the display
   private TreeItem<String> treeRoot = new TreeItem<String> ("Transactions");
   private TreeItem<String> creditRoot = new TreeItem<String> ("Credits");
   private TreeItem<String> debitRoot = new TreeItem<String> ("Debits");
   
   //Create the objects needed for the main scene.  
   //Layed out in the FXML file from Scene Builder
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
   private TreeView<String> dateTree;
   
   @FXML
   private Label totalCreditLabel;

   @FXML
   private Label totalDebitLabel;

   @FXML
   private Label totalNetLabel;
   
   @FXML
   private TextArea creditTextArea;

   @FXML
   private TextArea debitTextArea;
   
   @FXML
   private MenuItem saveFileMenuItem;

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Public accessor and mutator methods used by child dialogs   

   //Mutator methods
   public void setStage(Stage stage) { // For passing stage information to the controller.
      primaryStage = stage;
   }
   public void setController(MainDisplayController controller) {
      mainController = controller;
   }
   public void setCurrentFile(File file) {
      currentFile = file;
      if (currentFile != null)
         saveFileMenuItem.setDisable(false);
      else
         saveFileMenuItem.setDisable(true);
      setNeedsSaved(false);
   }
   public void setNeedsSaved(boolean b) {
      needsSaved = b;
   }
   
   //Accessor Methods
   public Stage getPrimaryStage() {
      return primaryStage;
   }
   public Scene getMainScene() {
      return creditTable.getScene();
   }
   public File getCurrentFile() {
      return currentFile;
   }
   public boolean getNeedsSaved() {
      return needsSaved;
   }
   
   //accessor methods for text summaries
   public Label getTotalCreditLabel() {
      return totalCreditLabel;
   }
   public Label getTotalDebitLabel() {
      return totalDebitLabel;
   }
   public Label getTotalNetLabel() {
      return totalNetLabel;
   }
   public TextArea getCreditTextArea() {
      return creditTextArea;
   }
   public TextArea getDebitTextArea() {
      return debitTextArea;
   }
 
   //accessor methods for credit/debit 
   public ObservableList<Credit> getCreditData() {
      return creditData;
   } 
   public ObservableList<Debit> getDebitData() {
      return debitData;
   } 
   public ObservableList<Credit> getDisplayedCreditData() {
      return creditDisplayed;
   }
   public ObservableList<Debit> getDisplayedDebitData() {
      return debitDisplayed;
   }
   public TreeItem<String> getCreditRoot() {
      return creditRoot;
   }
   public TreeItem<String> getDebitRoot() {
      return debitRoot;
   }
   public ObservableList<Double> getCreditOutliers() {
      return creditOutliers;
   }
   public ObservableList<Double> getDebitOutliers() {
      return debitOutliers;
   }

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Section containing the different methods used in the menus

   @FXML
   public void closeMenuAction(ActionEvent event) { //Closes the program if the exit option is selected from the menu.
      DataIO dataIO = new DataIO(mainController);
      boolean okToDoAction = false;
          
      if (getNeedsSaved()) {
         String s = dataIO.checkDataSaveState();
         
         if (s.equals("Yes"))
            okToDoAction = dataIO.createFileDialog("Save...");
         else if (s.equals("No"))
            okToDoAction = true;
      }
      else
         okToDoAction = true;   
            
      if (okToDoAction)
         Platform.exit();
         //primaryStage.close();
   }
   
   @FXML
   private void newMenuAction(ActionEvent event) {
      DataIO dataIO = new DataIO(mainController);
      boolean okToDoAction = false;
      if (getNeedsSaved()) {
         String s = dataIO.checkDataSaveState();
         
         if (s.equals("Yes"))
            okToDoAction = dataIO.createFileDialog("Save...");
         else if (s.equals("No"))
            okToDoAction = true;
      }
      else
         okToDoAction = true;   
            
      if (okToDoAction)
         purgeData();
   } 
   
   @FXML
   private void openFileMenuAction(ActionEvent event) {
      DataIO dataIO = new DataIO(mainController);
      boolean okToDoAction = false;
      if (getNeedsSaved()) {
         String s = dataIO.checkDataSaveState();
         
         if (s.equals("Yes"))
            okToDoAction = dataIO.createFileDialog("Save...");
         else if (s.equals("No"))
            okToDoAction = true;
      }
      else
         okToDoAction = true;   
            
      if (okToDoAction)
         System.out.println("Open successfully completed? " + dataIO.createFileDialog("Open") + ".");
   }
   
   @FXML
   private void saveAsFileMenuAction(ActionEvent event) {
      DataIO dataIO = new DataIO(mainController);
      System.out.println("Save As successfully completed? " + dataIO.createFileDialog("Save") + ".");
   }
   
   @FXML
   private void saveFileMenuAction(ActionEvent event) {
      DataIO dataIO = new DataIO(mainController);
      System.out.println("Save... successfully completed? " + dataIO.createFileDialog("Save...") + ".");
   }
   
   @FXML
   private void manageCategoryMenuAction(ActionEvent event) { //Creates a Credit entry dialog 
      try {             
         FXMLLoader categoryManagementLoader = new FXMLLoader(getClass().getResource("CategoryManagement.fxml"));
         Scene categoryManagementDisplay = new Scene(categoryManagementLoader.load());//create a new scene from the TransactionInput fxml
         makeNewDialog("Category Management", categoryManagementDisplay);//Creates a new window to hold the transaction input dialog
         setNeedsSaved(true);
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Failed to load: CategoryManagement.fxml");
      }
   }
   
   @FXML
   private void manageTypeMenuAction(ActionEvent event) { //Creates a Credit entry dialog  
      try {             
         FXMLLoader typeManagementLoader = new FXMLLoader(getClass().getResource("TypeManagement.fxml"));
         Scene typeManagementDisplay = new Scene(typeManagementLoader.load());//create a new scene from the TransactionInput fxml
         makeNewDialog("Type Management", typeManagementDisplay);//Creates a new window to hold the transaction input dialog
         setNeedsSaved(true);
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Failed to load: TypeManagement.fxml");
      }
   }
   
   @FXML
   private void newCreditMenuAction(ActionEvent event) { //Creates a Credit entry dialog  
      try {             
         FXMLLoader transactionInputLoader = new FXMLLoader(getClass().getResource("CreditInput.fxml"));
         Scene transactionScreenDisplay = new Scene(transactionInputLoader.load());//create a new scene from the TransactionInput fxml
         makeNewDialog("Input New Credit Data", transactionScreenDisplay);//Creates a new window to hold the transaction input dialog
         CreditInputController controller = transactionInputLoader.getController();
         controller.setMainController(mainController);
         setNeedsSaved(true);
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Failed to load: CreditInput.fxml");
      }
   }
   
   @FXML
   private void newDebitMenuAction(ActionEvent event) { //Creates a Debit entry dialog
      try {
         FXMLLoader transactionInputLoader = new FXMLLoader(getClass().getResource("DebitInput.fxml"));
         Scene transactionScreenDisplay = new Scene(transactionInputLoader.load());//create a new scene from the TransactionInput fxml
         makeNewDialog("Input New Debit Data", transactionScreenDisplay);//Creates a new window to hold the transaction input dialog
         DebitInputController controller = transactionInputLoader.getController();
         controller.setMainController(mainController);
         setNeedsSaved(true);
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Failed to load: DebitInput.fxml");
      }
   }
      
   @FXML
   private void deleteCreditMenuAction(ActionEvent event) {
      removeTransactionData(creditTable.getSelectionModel().getSelectedItem());
      creditTable.getSelectionModel().clearSelection();
      setNeedsSaved(true);
   }
   
   @FXML
   private void deleteDebitMenuAction(ActionEvent event) {
      removeTransactionData(debitTable.getSelectionModel().getSelectedItem());
      debitTable.getSelectionModel().clearSelection();
      setNeedsSaved(true);
   }
   
   @FXML    
   public void aboutMenuAction(ActionEvent event) {  
      try {        
         FXMLLoader splashScreenLoader = new FXMLLoader(getClass().getResource("SplashScreen.fxml"));
         Scene splashScreenDisplay = new Scene(splashScreenLoader.load());//create a new scene from the SplashScreen.fxml
         makeNewDialog("Splash Screen Stage", splashScreenDisplay);//Creates a new window to hold the transaction input dialog
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Failed to load: SplashScreen");
      }
   }//aboutMenuAction
   
   @FXML
   private void generateReportMenuAction(ActionEvent event) {
      try {        
         FXMLLoader reportScreenLoader = new FXMLLoader(getClass().getResource("ReportDisplay.fxml"));
         Scene reportScreenDisplay = new Scene(reportScreenLoader.load());//create a new scene from the ReportDisplay.fxml
         
         Stage newWindow = new Stage(); // create a new window
         newWindow.setTitle("Data Report"); // sets the window title
         newWindow.setScene(reportScreenDisplay); //sets the scene diplayed in the window
         newWindow.setMaximized(true); // maximize new window
         newWindow.show(); //make window visible
         
         ReportDisplayController reportController = reportScreenLoader.getController();
         reportController.setMainController(mainController);
         reportController.setPrimaryStage(primaryStage);
         
         
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Failed to load: Report Display");
      }
   }
         
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Helper methods used by other parts of the program    

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
   
   //Creates Dialog windows owned by the main window.
   private void makeNewDialog(String title, Scene dialogScene) {
      Stage newWindow = new Stage(); // create a new window
      newWindow.setTitle(title); // sets the window title
      newWindow.setScene(dialogScene); //sets the scene diplayed in the window
      newWindow.initOwner(primaryStage); // set owner to main window
      newWindow.initModality(Modality.WINDOW_MODAL); // can't click on main window while dialog is open
      newWindow.show(); //make window visible
      newWindow.centerOnScreen(); //center window
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Main data mutator methods

   //Add Credit Data
   public void addTransactionData(Credit credit) {
      creditData.add(credit);
      
      if (creditDateMap.containsKey(credit.getYearString())) { // Year node already exists
         if (!creditDateMap.get(credit.getYearString()).contains(credit.getMonthString())) { // Month node doesn't exist
            creditDateMap.get(credit.getYearString()).add(credit.getMonthString()); // Add month to datemap
            addMonthNode(-1, creditRoot, credit.getYearString(), credit.getMonthString()); // add month node
         }
      } else { // Year node didn't exist
         ObservableList<String> newMonthList = FXCollections.observableArrayList(credit.getMonthString()); //create month list for year
         creditDateMap.put(credit.getYearString(), newMonthList); // create new entry in datemap
         addYearNode(creditRoot, credit.getYearString(), credit.getMonthString()); //create new year and month node.
      }  
   }
   
   //Overloaded method to add Debit Data
   public void addTransactionData(Debit debit) {
      debitData.add(debit);
      
      if (debitDateMap.containsKey(debit.getYearString())) { // Year node already exists
         if (!debitDateMap.get(debit.getYearString()).contains(debit.getMonthString())) { // Month node doesn't exist
            debitDateMap.get(debit.getYearString()).add(debit.getMonthString()); // Add month to datemap
            addMonthNode(-1, debitRoot, debit.getYearString(), debit.getMonthString()); // add month node
         }
      } else { // Year node didn't exist
         ObservableList<String> newMonthList = FXCollections.observableArrayList(debit.getMonthString()); //create month list for year
         debitDateMap.put(debit.getYearString(), newMonthList); // create new entry in datemap
         addYearNode(debitRoot, debit.getYearString(), debit.getMonthString()); //create new year and month node.
      }  
   }
   
   //Remove Credit Data
   public void removeTransactionData(Credit credit) {
      creditData.remove(credit);
      //Makes a list that's only the data in that current year and month
      FilteredList<Credit> yearCheck = 
            new FilteredList<Credit> (creditData, Credit.creditYearPredicateGenerator(credit.getYear()));
      FilteredList<Credit> monthCheck = 
            new FilteredList<Credit> (yearCheck, Credit.creditMonthPredicateGenerator(credit.getMonthString()));
      
      if (monthCheck.size() == 0) { // We just removed the last item in that year's month
         if (yearCheck.size() == 0) { // That was also the last item in that year; so remove year node
            creditDateMap.remove(credit.getYearString());
            removeYearNode(creditRoot, credit.getYearString());
         } else { // There are still things in this year; so just remove the month node
            creditDateMap.get(credit.getYearString()).remove(credit.getMonthString());
            removeMonthNode(creditRoot, credit.getYearString(), credit.getMonthString());
         }
      }
   }
   
   //Overloaded method to remove Credit Data
   public void removeTransactionData(Debit debit) {
      debitData.remove(debit);
      //Makes a list that's only the data in that current year and month
      FilteredList<Debit> yearCheck = 
            new FilteredList<Debit> (debitData, Debit.debitYearPredicateGenerator(debit.getYear()));
      FilteredList<Debit> monthCheck = 
            new FilteredList<Debit> (yearCheck, Debit.debitMonthPredicateGenerator(debit.getMonthString()));
      
      if (monthCheck.size() == 0) { // We just removed the last item in that year's month
         if (yearCheck.size() == 0) { // That was also the last item in that year; so remove year node
            debitDateMap.remove(debit.getYearString());
            removeYearNode(debitRoot, debit.getYearString());
         }else { // There are still things in this year; so just remove the month node
            debitDateMap.get(debit.getYearString()).remove(debit.getMonthString());
            removeMonthNode(debitRoot, debit.getYearString(), debit.getMonthString());
         }
      }
   }
   
   //Updates the credit summary text
   private void generateCreditSummary() {
      // Resets relevant text areas 
      creditTextArea.setText("");
      totalCreditLabel.setText("Total Credits: $0.00");
      totalNetLabel.setText("Net Amount: $0.00");
      totalNetLabel.setStyle("-fx-text-fill: black;");
      
      //Parses the value of the total debits
      double currentDebitSum = Double.parseDouble(totalDebitLabel.getText().substring(15).replaceAll(",",""));
      
      //Only calculate statistics when there is data displayed
      if (creditDisplayed.size() > 0) {
         ObservableList<Double> creditAmounts = FXCollections.observableArrayList();
      
         //pulls out the amount values from each data set.
         for (int i = 0; i < creditVisible.size(); i++)
            creditAmounts.add(creditVisible.get(i).getAmount());
         
         StatCalculator creditStats = new StatCalculator(creditAmounts); // generates the summary stats for the credits
         creditOutliers = creditStats.getOutliers();
         
         //Makes updates to the total credits and net total text fields
         totalCreditLabel.setText("Total Credits: " + String.format("$%,.2f", creditStats.getSum()));
         totalNetLabel.setText("Net Amount: " + String.format("$%,.2f", creditStats.getSum()-currentDebitSum));
         if (creditStats.getSum()-currentDebitSum < 0)
            totalNetLabel.setStyle("-fx-text-fill: red;");
         
         //updates the credit summary statistics in the credit text area.
         String summaryText = "Credit Summary Statistics\n" + "--------------------------\n";
         summaryText += creditStats.toMoneyString();
         creditTextArea.setText(summaryText);
      }
   }

   //Updates the credit summary text
   private void generateDebitSummary() {
      // Resets relevant text areas 
      debitTextArea.setText("");
      totalDebitLabel.setText("Total Debits: $0.00");
      totalNetLabel.setText("Net Amount: $0.00");
      totalNetLabel.setStyle("-fx-text-fill: black;");
      
      //Parses the value of the total debits
      double currentCreditSum = Double.parseDouble(totalCreditLabel.getText().substring(16).replaceAll(",",""));
      
      //Only calculate statistics when there is data displayed
      if (debitDisplayed.size() > 0) {
         ObservableList<Double> debitAmounts = FXCollections.observableArrayList();
      
         //pulls out the amount values from each data set.
         for (int i = 0; i < debitVisible.size(); i++)
            debitAmounts.add(debitVisible.get(i).getAmount());
         
         StatCalculator debitStats = new StatCalculator(debitAmounts);// generates the summary stats for the credits
         debitOutliers = debitStats.getOutliers();
         
         //Makes updates to the total credits and net total text fields
         totalDebitLabel.setText("Total Debits: " + String.format("$%,.2f", debitStats.getSum()));
         totalNetLabel.setText("Net Amount: " + String.format("$%,.2f", currentCreditSum - debitStats.getSum()));
         if (currentCreditSum - debitStats.getSum() < 0)
            totalNetLabel.setStyle("-fx-text-fill: red;");
         
         //updates the credit summary statistics in the credit text area.
         String summaryText = "Debit Summary Statistics\n" + "--------------------------\n";
         summaryText += debitStats.toMoneyString();
         debitTextArea.setText(summaryText);
      }
   }
      
   
   //Erases all stored data and resets all summary text areas
   //Returns program to a new-file state
   public void purgeData() {
      setCurrentFile(null);
      creditData.clear();
      debitData.clear();
      creditDateMap.clear();
      debitDateMap.clear();
      creditRoot.getChildren().clear();
      debitRoot.getChildren().clear();
      Credit.setDefaultCreditCategories();
      Credit.setDefaultCreditTypes();
      Debit.setDefaultDebitCategories();
      Debit.setDefaultDebitCategories();
      creditTextArea.setText("");
      debitTextArea.setText("");
      totalCreditLabel.setText("Total Credits: $0.00");
      totalDebitLabel.setText("Total Debits: $0.00");
      totalNetLabel.setText("Net Amount: $0.00");
      totalNetLabel.setStyle("-fx-text-fill: black;");
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Tree Manipulation methods

   //method to add a month node in the dateTree
   private void addMonthNode(int yearLocation, TreeItem<String> treeRoot, String year, String month) {
      if (yearLocation < 0) {
         yearLocation = 0; // going to cycle through until we find the correct year node
         while ((!treeRoot.getChildren().get(yearLocation).getValue().equals(year)) 
                     && (yearLocation < treeRoot.getChildren().size())) {
            yearLocation++;
         }
      }
      CheckBoxTreeItem<String> newMonthNode = new CheckBoxTreeItem<String> (month);
      newMonthNode.setIndependent(false);
      newMonthNode.setSelected(true);
      newMonthNode.selectedProperty().addListener((obs, oldVal, newVal) -> {
         //This determines which node has been altered and toggles visibility for the appropriate transactions    
         if (treeRoot == creditRoot) {
            FilteredList<Credit> yearCheck = new FilteredList<Credit> (creditData, 
                     Credit.creditYearPredicateGenerator(Integer.parseInt(year)));
            FilteredList<Credit> monthCheck = new FilteredList<Credit> (yearCheck, 
                     Credit.creditMonthPredicateGenerator(month));
            for (int i = 0; i < monthCheck.size(); i++) 
               monthCheck.get(i).setVisibility(newVal);
         }
         else if (treeRoot == debitRoot) {
            FilteredList<Debit> yearCheck = new FilteredList<Debit> (debitData, 
                     Debit.debitYearPredicateGenerator(Integer.parseInt(year)));
            FilteredList<Debit> monthCheck = new FilteredList<Debit> (yearCheck, 
                     Debit.debitMonthPredicateGenerator(month));
            for (int i = 0; i < monthCheck.size(); i++) 
               monthCheck.get(i).setVisibility(newVal);
            
         }
      });
      treeRoot.getChildren().get(yearLocation).getChildren().add(newMonthNode);
   }
   
   //method to add a year node to the dateTree
   private void addYearNode(TreeItem<String> treeRoot, String year, String month) {
      CheckBoxTreeItem<String> newYearNode = new CheckBoxTreeItem<String> (year);
      newYearNode.setSelected(true);
      newYearNode.setExpanded(true);
      treeRoot.getChildren().add(newYearNode);
      addMonthNode(treeRoot.getChildren().size() - 1, treeRoot, year, month); // adds a month node to this year node.
   }
   
   //method to remove a year node from the dateTree
   private void removeYearNode(TreeItem<String> treeRoot, String year) {
      int yearLocation = 0;//Find the location of the year node in the tree
      while((!treeRoot.getChildren().get(yearLocation).getValue().equals(year)) 
                  && (yearLocation < treeRoot.getChildren().size())) {
         yearLocation++;
      }
      treeRoot.getChildren().remove(treeRoot.getChildren().get(yearLocation));
   }
   
   //method to remove a month node from the dateTree
   private void removeMonthNode(TreeItem<String> treeRoot, String year, String month) {
      int yearLocation = 0;//Find the location of the year node in the tree
      while((!treeRoot.getChildren().get(yearLocation).getValue().equals(year)) 
                  && (yearLocation < treeRoot.getChildren().size())) {
         yearLocation++;
      }
      int monthLocation = 0;//Find the location of the month node in the tree
      while((!treeRoot.getChildren().get(yearLocation).getChildren().get(monthLocation).getValue().equals(month)) 
               && (yearLocation < treeRoot.getChildren().get(yearLocation).getChildren().size())) {
         monthLocation++;
      }
      treeRoot.getChildren().get(yearLocation).getChildren().remove(
            treeRoot.getChildren().get(yearLocation).getChildren().get(monthLocation));
   }
   

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Initializer method
   
   //Method is run when the MainDisplay is created for the first time.  
   //Sets up the columns as well as creates the table
   //Sets up the Date tree

   public void initialize() {
      setDefaultColumnSize(); // Create the column spacing in the tables.
      purgeData();
      
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
      
      
      //Initializes the two TableViews
      //Sets selection type to multiple so multiple things can be deleted at once.
      creditTable.setItems(creditDisplayed);
      debitTable.setItems(debitDisplayed);
      creditTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
      debitTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
      
      //Creates the Transaction date tree
      //First creates a root node and the nodes for credits and debits.
      treeRoot.setExpanded(true);
      creditRoot.setExpanded(true);
      debitRoot.setExpanded(true);
      
      //Adds the nodes and hides the top node since we only have transactions.
      treeRoot.getChildren().add(creditRoot);
      treeRoot.getChildren().add(debitRoot);
      dateTree.setRoot(treeRoot);
      dateTree.setShowRoot(false);
      
      //Add a Listener to the two data sets that checks to see if the visibility property has been changed.
      //If so, it will update the tables.
      creditData.addListener(new ListChangeListener<Credit>() {
         @Override
         public void onChanged(Change<? extends Credit> c) {
            creditVisible.setPredicate(Credit.creditVisibilityFilter()); //forces refresh of filtered list
            creditDisplayed.clear(); // clears the displayed list
            creditDisplayed.addAll(creditVisible); // adds the new visible list
            generateCreditSummary();
            //setTable(); // recalculates all of the statistics     
         }
      });
      debitData.addListener(new ListChangeListener<Debit>() {
         @Override
         public void onChanged(Change<? extends Debit> d) {
            debitVisible.setPredicate(Debit.debitVisibilityFilter());//forces refresh of filtered list
            debitDisplayed.clear();// clears the displayed list
            debitDisplayed.addAll(debitVisible);// adds the new visible list
            generateDebitSummary();
            //setTable();     // recalculates all of the statistics
         }
      });
      
      //Use a custom callback to determine the style of the tree item
      //Taken from https://blog.idrsolutions.com/2014/08/mixed-treeview-nodes-javafx/
      dateTree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
         @Override
         public TreeCell<String> call(TreeView<String> param) {
            return new CheckBoxTreeCell<String>() {
               @Override
               public void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  // If there is no information for the Cell, make it empty
                  if(empty) {
                     setGraphic(null);
                     setText(null);
                     // Otherwise if it's representation as an item of the tree
                     // is not a CheckBoxTreeItem, remove the checkbox item
                  } else if (!(getTreeItem() instanceof CheckBoxTreeItem)) {
                     setGraphic(null);
                  }
               }
            };
         }
      });
   }
}//class