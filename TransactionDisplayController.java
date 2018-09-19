// **********************************************************
// Title: Transaction Display Controller abstract class
// File: MainDisplayController.java
// Author: Matt Lochman
// Description: Abstract class for the display stage.
// **********************************************************

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public abstract class TransactionDisplayController {
   private MainDisplayController mainController;
   
   @FXML
   private Label descLabel;

   public Label getDescLabel() {
      return descLabel;
   }
   public MainDisplayController getMainController() {
      return mainController;
   }
   public void setMainController(MainDisplayController mdc) {
      mainController = mdc;
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
//Methods for this dialog 

   @FXML
   public abstract void cancelButtonListener(ActionEvent event);
}