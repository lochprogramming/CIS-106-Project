// **********************************************************
// Title: Management Controller
// File: ManagementController.java
// Author: Matt Lochman
// Description: Abstract Controller for Management stage.
// **********************************************************

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public abstract class ManagementController {
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields

   //Set up the string lists.
   private String itemType = null;
   private ObservableList<String> creditItems = FXCollections.observableArrayList();
   private ObservableList<String> debitItems = FXCollections.observableArrayList();
   
   @FXML
   private ListView<String> creditListView;

   @FXML
   private ListView<String> debitListView;
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Field Methods   

   //Accessor methods for the fields
   public ObservableList<String> getCreditItems() {
      return creditItems;
   }
   public ObservableList<String> getDebitItems() {
      return debitItems;
   }
   public ListView<String> getCreditListView() {
      return creditListView;
   }
   public ListView<String> getDebitListView() {
      return debitListView;
   }
   public String getItemType() {
      return itemType;
   }
   
   //setter method for item type
   public void setItemType(String s) {
      itemType = s;
   }

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Simple Dialog Methods

   //dialog creations.  Only called by the above public methods, not on their own.
   private String addDialog () {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Create New" + itemType);
      dialog.setHeaderText(itemType);
      //dialog.setContentText("Please enter your name:");
      
      // Traditional way to get the response value.
      Optional<String> result = dialog.showAndWait();
      if (result.isPresent())
          return result.get();
      else
         return null;
      
      // The Java 8 way to get the response value (with lambda expression).
      //result.ifPresent(name -> System.out.println("Your name: " + name));
   }
   private String editDialog(String s) {
      TextInputDialog dialog = new TextInputDialog(s);
      dialog.setTitle("Edit" + itemType);
      dialog.setHeaderText(itemType);
      //dialog.setContentText("Please enter your name:");
      
      // Traditional way to get the response value.
      Optional<String> result = dialog.showAndWait();
      if (result.isPresent())
          return result.get();
      else
         return null;
      
      // The Java 8 way to get the response value (with lambda expression).
      //result.ifPresent(name -> System.out.println("Your name: " + name));
   }
 

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//FXML Button Action Methods   


   //Credit Button action methods   
   @FXML
   public void addCreditButtonAction(ActionEvent e) {
      String newItem = addDialog();
      if (newItem != null)
         creditItems.add(newItem);
   }
   @FXML
   public void editCreditButtonAction(ActionEvent e) {
      String newItem = editDialog(creditListView.getSelectionModel().getSelectedItem());
      if (newItem != null) {
         creditItems.remove(creditListView.getSelectionModel().getSelectedItem());
         creditItems.add(newItem);
      }
   }
   @FXML
   public void removeCreditButtonAction(ActionEvent e) {
      creditItems.remove(creditListView.getSelectionModel().getSelectedItem());
      creditListView.getSelectionModel().clearSelection();
   }
   
   //Debit Button action methods
   @FXML
   public void addDebitButtonAction(ActionEvent e) {
      String newItem = addDialog();
      if (newItem != null)
         debitItems.add(newItem);
   }
   @FXML
   public void editDebitButtonAction(ActionEvent e) {
      String newItem = editDialog(debitListView.getSelectionModel().getSelectedItem());
      if (newItem != null) {
         debitItems.remove(debitListView.getSelectionModel().getSelectedItem());
         debitItems.add(newItem);
      }
   }
   @FXML
   public void removeDebitButtonAction(ActionEvent e) {
      debitItems.remove(debitListView.getSelectionModel().getSelectedItem());
      debitListView.getSelectionModel().clearSelection();
   }
   
   //Other button action methods
   @FXML
   public abstract void okButtonAction(ActionEvent e); //Must be overwriten by subclass
   
   @FXML
   public void cancelButtonAction(ActionEvent e) {
      ((Stage) getCreditListView().getScene().getWindow()).close();
   }
}

