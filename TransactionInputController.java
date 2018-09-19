// **********************************************************
// Title: Transaction Input Controller
// File: TransactionInputController.java
// Author: Matt Lochman
// Description: Controller for Transaction Input 
// **********************************************************

//basic import statements needed for the different parts of the scene
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

//needed for the change value listener on choicebox
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

//needed for the change of scenes
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;

//To deal with the try/catch statements
import java.lang.NumberFormatException;
import java.io.IOException;

public abstract class TransactionInputController{      
   private MainDisplayController mainController;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Fields used in the fxml file

   @FXML
   private TextField descTextBox;

   @FXML
   private TextField monthTextBox;

   @FXML
   private TextField dayTextBox;

   @FXML
   private TextField yearTextBox;

   @FXML
   private TextField amountTextBox;

   @FXML
   private ChoiceBox<String> catChoiceBox;

   @FXML
   private ChoiceBox<String> typeChoiceBox;

   @FXML
   private Label errorLabel;
   
   public ChoiceBox<String> getCatChoiceBox() {
      return catChoiceBox;
   }
   public ChoiceBox<String> getTypeChoiceBox() {
      return typeChoiceBox;
   }
   
   public Label getErrorLabel() {
      return errorLabel;
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
//Methods for this dialog    

   //Method for setting the main controller field.
   public void setMainController(MainDisplayController mdc) {
      mainController = mdc;
   }
   public MainDisplayController getMainController() {
      return mainController;
   }
   
   //Method called when the enter button is pressed by the user.  
   //Performs data validation and creates Credit object.
   //Then changes the scene to the CreditDisplay Scene after passing it the Credit object.
   @FXML
   public void entryButtonListener(ActionEvent event) throws Exception {
      //Create some temporary values for the credit attributes.
      String desc = "";
      int m = 0, d = 0, y = 0;
      double a = 0.0;
      String cat = "", t = ""; 
      boolean flag = true; // flag if error-free
      
      //Check to make sure text fields have entries
      if (descTextBox.getText().equals(""))
         errorLabel.setText("Please enter a description.");
      else if (monthTextBox.getText().equals(""))
         errorLabel.setText("Please enter a month.");
      else if (dayTextBox.getText().equals(""))
         errorLabel.setText("Please enter a day.");
      else if (yearTextBox.getText().equals(""))
         errorLabel.setText("Please enter a year.");
      else if (amountTextBox.getText().equals(""))
         errorLabel.setText("Please enter an amount.");
      else {
         desc = descTextBox.getText(); //pulls the description string
         cat = catChoiceBox.getValue();//pulls the category string
         t = typeChoiceBox.getValue();//pulls the type string
         
                  
         //determine if the amount is a valid double
         try {
            a = Double.parseDouble(amountTextBox.getText());
         } catch (NumberFormatException e) {
            //Entry wasn't a double. 
            errorLabel.setText("The amount entry isn't an valid number."); 
            flag = false;
         } 
         
         //determine if the year is a valid int    
         try {
            y = Integer.parseInt(yearTextBox.getText());
         } catch (NumberFormatException e) {
            //Entry wasn't an integer. 
            errorLabel.setText("The year entry isn't an integer."); 
            flag = false;
         }
         
         //determine if the day is a valid int
         try {
            d = Integer.parseInt(dayTextBox.getText());
         } catch (NumberFormatException e) {
            //Entry wasn't an integer. 
            errorLabel.setText("The day entry isn't an integer.");
            flag = false; 
         }
         
         //determine if the month is a valid int
         try {
            m = Integer.parseInt(monthTextBox.getText());
         } catch (NumberFormatException e) {
            //Entry wasn't an integer. 
            errorLabel.setText("The month entry isn't an integer.");
            flag = false; 
         }
         
         //if statement that handles checking if the numerical entries are in valid ranges.
         //Only checks if an error hasn't been previously thrown (i.e. if the entries are already valid numbers). 
         if (flag) {  
            if ((m < 1) || (m > 12))
               errorLabel.setText("The month entry must be between 1 and 12.");  
            else if ((d < 1) || (d > 31))
               errorLabel.setText("The day entry must be between 1 and 31.");
            else if ((y < 1900) || (y > java.time.Year.now().getValue()))
               errorLabel.setText("The year entry must be between 1900 and " + java.time.Year.now() + ".");
            else if (a != Double.valueOf(String.format("%.2f",a)))
               errorLabel.setText("The amount entry must be a positive number rounded to the nearest cent.");
            else if (a <= 0)
               errorLabel.setText("The amount entry must be a positive number rounded to the nearest cent.");
            else {
               errorLabel.setText("No problems.");
               
               createEntry(desc, m, d, y, a, cat, t); // Calls the abstract createEntry method if all input is valid.
               
            }//end else for creation of transaction object and changing to display scene  
         }//End if statement for checking numerical ranges.
      }//end else after checking for data type errors
   }//end enterButtonListener
       
   @FXML
   public void cancelButtonListener(ActionEvent event) { //If the cancel button is pressed, loads the MainDisplay scene.
      ((Stage) errorLabel.getScene().getWindow()).close(); //closes this dialog
   }
   
   //Create entry method to be overwritten by subclass.
   public abstract void createEntry(String desc, int m, int d, int y, double a, String cat, String t);
               
}//end controller class