// **********************************************************
// Title: Debit Display Controller
// File: DebitDisplayController.java
// Author: Matt Lochman
// Description: Controller for DebitDisplay stage.
// **********************************************************

//basic import statements needed for the different parts of the scene
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//needed for the change of scenes
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class DebitDisplayController extends TransactionDisplayController
{
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields

   private Debit debit;
   
////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
//Methods for this dialog 

   @FXML
   public void cancelButtonListener(ActionEvent event) { //If the cancel button is pressed, loads the MainDisplay scene.      
      getMainController().addTransactionData(debit);
      ((Stage) getDescLabel().getScene().getWindow()).close(); //closes the window
   }
    
   //Method for passing Credit objects to this scene.
   //Sets the scenes label to display the toString text for the credit object just entered.
   public void setDebit(Debit d) {
      this.debit = d;
      getDescLabel().setText(debit.toString());    
   }
}