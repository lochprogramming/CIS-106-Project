// **********************************************************
// Title: Assignment5 Primary Program
// File: Assignment5.java
// Author: Matt Lochman
// Description: Contains main method for program.
// **********************************************************

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
 
public class Assignment5 extends Application {
   @Override
   public void start(final Stage primaryStage) {
      try {
         /*creates an instance of the appropriate scene loader
         creates a parent of for that scene
         gets the current stage
         changes the scene to the new one*/
         
         FXMLLoader mainDisplayLoader = new FXMLLoader(getClass().getResource("MainDisplay.fxml"));
         Scene mainDisplay = new Scene(mainDisplayLoader.load());
         primaryStage.setTitle("Finance Program"); //Set the title for our program window
         primaryStage.setScene(mainDisplay); //Display SplashScreen window, using the scene graph
         primaryStage.setMaximized(true);
         primaryStage.show();
            
         MainDisplayController mainController = mainDisplayLoader.getController();//gets the mainDisplay controller reference
         mainController.setStage(primaryStage);//pass the stage reference to the mainController
         mainController.setController(mainController);//pass the controller reference to the controller.
         mainController.aboutMenuAction(new ActionEvent());//displays the splash screen
         
         //Set up a handler to deal with someone trying to close the window from non-menu means.
         primaryStage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            mainController.closeMenuAction(new ActionEvent());
            event.consume();
         });
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Error Loading MainDisplay");
      }
   }
   public static void main(String[] args) {
      //Launch the application
      launch(args);
   }
}