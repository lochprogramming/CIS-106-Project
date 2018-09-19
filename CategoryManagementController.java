// **********************************************************
// Title: Category Management Controller
// File: CategoryManagementController.java
// Author: Matt Lochman
// Description: Controller for Category Management stage.
// **********************************************************

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CategoryManagementController extends ManagementController {

   @FXML
   public void okButtonAction(ActionEvent event) {
      Credit.clearCreditCategories();//removes all current categories
      Debit.clearDebitCategories();//removes all current categorires
      for (int i = 0; i < getCreditItems().size(); i++)
         Credit.addCreditCategory(getCreditItems().get(i));//replaces the categories with the ones from the window
      for (int i = 0; i < getDebitItems().size(); i++)
         Debit.addDebitCategory(getDebitItems().get(i));//replaces the categories with the ones from the window
      cancelButtonAction(event);
   }
   
   //initialize the lists
   //Pulls in the curernt lists from the credit and debit classes
   //Sets the type to category for the abstract controller. This fills in the correct headings.
   public void initialize() {
      setItemType("Category");
      getCreditItems().addAll(Credit.getAllCreditCategories());
      getDebitItems().addAll(Debit.getAllDebitCategories());
      getCreditListView().setItems(getCreditItems());
      getDebitListView().setItems(getDebitItems());
   }  
}