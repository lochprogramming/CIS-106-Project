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

public class DebitInputController extends TransactionInputController
{   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields

   private Debit debit; //create a new credit object for use later
   
////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
//Methods for this dialog    

   //Overwrites abstract method in superclass.  
   //Creates a new credit object and passes it along to the display window
   public void createEntry(String desc, int m, int d, int y, double a, String cat, String t) {
      //creates an instance of debit
      debit = new Debit(true, desc, m, d, y, a, cat, t);
               
      try { //Check if there's an error when moving to DebitDisplay scene
         //creates an instance of the debitDisplay loader
         FXMLLoader debitDisplayLoader = new FXMLLoader(getClass().getResource("DebitDisplay.fxml"));
         Scene debitScreenDisplay = new Scene(debitDisplayLoader.load());//create a new scene from the TransactionDisplay fxml
         ((Stage) getErrorLabel().getScene().getWindow()).setScene(debitScreenDisplay);// Change this window to the display scene           
         DebitDisplayController controller = debitDisplayLoader.getController(); 
         controller.setMainController(getMainController());       
         controller.setDebit(debit); //passes credit
      } catch (IOException e) {
         e.printStackTrace(); //prints this throwable and its backtrace to the standard error stream
         System.out.println("Failed to load: TransactionInput.fxml");
      }
   }


   public void initialize() {
      getCatChoiceBox().getItems().addAll(Debit.getAllDebitCategories()); //adds category options
      getTypeChoiceBox().getItems().addAll(Debit.getAllDebitTypes()); //adds type options
 
      getCatChoiceBox().getSelectionModel().select(0); //defaults to the first in the list
      getTypeChoiceBox().getSelectionModel().select(0); //defaults to the first in the list
   }
}