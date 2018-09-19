// **********************************************************
// Title: Data Input/Output Class
// File: DataIO.java
// Author: Matt Lochman
// Description: Class contaiing file read/write methods.
// **********************************************************

import java.io.File;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.net.URISyntaxException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataIO {
//File Contains static methods related to file reading and writing used by the main program.

   private MainDisplayController mainController;
   
   public DataIO(MainDisplayController mdc) { // Constructor requires reference to main controller.
      mainController = mdc;
   }

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Dialogue for when unsaved data is about to be erased.

   //Pop-up to ask if you want to save un-saved data before it's deleted.
   public String checkDataSaveState() {
      Alert alert = new Alert(AlertType.CONFIRMATION, "You have unsaved changes. Do you want to save the changes you made first?", 
         ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
      alert.setTitle("Save Changes?");
      alert.showAndWait();
      return alert.getResult().getText();
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Main method to create a save/open file dialog

   //Creates an save or open file dialogue window
   public boolean createFileDialog(String s) {
      if (s.equals("Save...") && mainController.getCurrentFile() != null) { // saves in current file
         writeDataFile(mainController.getCurrentFile());
         return true;
      }
      else { 
         if (s.equals("Save...")) // switches save to save as if no current file
            s = "Save";
         FileChooser fileChooser = new FileChooser(); // sets up the file chooser dialog
         fileChooser.setTitle(s + " Data File");
         fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Transaction Data File (*.trd)", "*.trd"));
         try {
            //sets the fhile chooser to be in the Data subfolder of the current directory.  If it doesn't exists, creates it.
            File directory = new File(
               MainDisplayController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()+ "\\Data\\");
            if (directory.isDirectory())
               fileChooser.setInitialDirectory(directory);
            else {
               directory.mkdirs();
               fileChooser.setInitialDirectory(directory);
            }   
         }
         catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Failed to find current file name.");
         }
         
         if (s.equals("Open")) {
            File file = fileChooser.showOpenDialog(mainController.getPrimaryStage());
            if (file != null) {
               readDataFile(file);
               mainController.setCurrentFile(file); // sets the current file to the opened one.
               return true;
            }
         } else if (s.equals("Save")) {
            File file = fileChooser.showSaveDialog(mainController.getPrimaryStage());
            if (file != null) {
               writeDataFile(file);
               mainController.setCurrentFile(file); // sets the current file to the opened one.
               return true;
            }
         } else {
            System.out.println("No Save or Open command sent.");
            return false;
         }
      }
      return false;
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Read data method
   
   //Reads data from file
   private void readDataFile(File file) {      
      final String COMMA_DELIMITER = ",";
      BufferedReader br;
      mainController.purgeData(); // Remove current data to be replaced with new data.
      
      try {
         br = new BufferedReader(new FileReader(file));
         mainController.setCurrentFile(file);
         
         String line;
         while ((line = br.readLine()) != null) {
            String[] fields = line.split(COMMA_DELIMITER, -1); //splits the read line
            String transaction = fields[0];
            switch(transaction) { // The first entry tells the reader what type of item is coming next
               case "Credit":
               case "Debit": // pulls apart the entries and creates the appropriate object.
                  boolean visible = (fields[1].equals("true"));           
                  String description = fields[2];
                  int month = Integer.parseInt(fields[3]);
                  int day = Integer.parseInt(fields[4]);
                  int year = Integer.parseInt(fields[5]);
                  double amount = Double.parseDouble(fields[6]);
                  String category = fields[7];
                  String type = fields[8]; 
                  if (transaction.equals("Credit")) 
                     mainController.addTransactionData(
                           new Credit(visible, description, month, day, year, amount, category, type));
                  else if (transaction.equals("Debit"))
                     mainController.addTransactionData(
                           new Debit(visible, description, month, day, year, amount, category, type));
                  break;
               
               case "Credit Categories": // pulls the list of credit categories
                  Credit.clearCreditCategories();
                  for (int i = 1; i < fields.length; i++)
                     Credit.addCreditCategory(fields[i]);
                  break;
                  
               case "Credit Types": // pulls the list of credit types
                  Credit.clearCreditTypes();
                  for (int i = 1; i < fields.length; i++)
                     Credit.addCreditType(fields[i]);
                  break;
               
               case "Debit Categories": // pulls the list of debit categories
                  Debit.clearDebitCategories();
                  for (int i = 1; i < fields.length; i++)
                     Debit.addDebitCategory(fields[i]);
                  break;
                  
               case "Debit Types": // pulls the list of debit types
                  Debit.clearDebitTypes();
                  for (int i = 1; i < fields.length; i++)
                     Debit.addDebitType(fields[i]);
                  break;
               default:
            }//end switch
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
         System.out.println("Could not find file: " + file); // thrown if the file is missing
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("IOException thrown while trying to read " + file); // thrown if there is a read error
      }
   }

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Write data method

   //Writes the data to file
   private void writeDataFile(File file) {
      final String COMMA_DELIMITER = ",";
      final String NEW_LINE = "\n";
      FileWriter fileWriter = null;
      try {
         fileWriter = new FileWriter(file);
         mainController.setCurrentFile(file);
         
         for (int i = 0; i < mainController.getCreditData().size(); i++) { // writes the credit data
            fileWriter.append("Credit");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(mainController.getCreditData().get(i).getWriteString());
            fileWriter.append(NEW_LINE);
         }
         for (int i = 0; i < mainController.getDebitData().size(); i++) { // writes the debit data
            fileWriter.append("Debit");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(mainController.getDebitData().get(i).getWriteString());
            fileWriter.append(NEW_LINE);
         }
         fileWriter.append("Credit Categories"); // writes the credit categories
         for (int i = 0; i < Credit.getCreditCategorySize(); i++) {
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Credit.getCreditCategory(i));
         }
         fileWriter.append(NEW_LINE);
         fileWriter.append("Credit Types"); // writes the credit types
         for (int i = 0; i < Credit.getCreditTypeSize(); i++) {  
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Credit.getCreditType(i));
         }
         fileWriter.append(NEW_LINE);
         fileWriter.append("Debit Categories"); // writes the debit categories
         for (int i = 0; i < Debit.getDebitCategorySize(); i++) {
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Debit.getDebitCategory(i));
         }
         fileWriter.append(NEW_LINE);
         fileWriter.append("Debit Types"); // writes the debit types
         for (int i = 0; i < Debit.getDebitTypeSize(); i++) {
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Debit.getDebitType(i));
         }
         fileWriter.append(NEW_LINE);
         System.out.println("File Correctly Written.");
      } catch(Exception e) {
         System.out.println("Error in file writing.");
         e.printStackTrace();
      } finally {
         try {
            fileWriter.flush();
            fileWriter.close();
         } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter.");
            e.printStackTrace();
         }
      }
   }
}