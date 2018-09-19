// **********************************************************
// Title: Splash Screen Controller
// File: SplashScreenController.java
// Author: Matt Lochman
// Description: Controller for SplashScreen stage.
// **********************************************************

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class SplashScreenController {
   //Button.  Used to grab stage in next method.
   @FXML
   private Button okButton;

   //
   @FXML
   public void okButtonAction(ActionEvent event) { //changes to the MainDisplay scene when the OK button is pressed.
     ((Stage) okButton.getScene().getWindow()).close(); //gets current window and closes it
   }
}